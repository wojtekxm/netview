/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zesp03.common.data.CurrentDeviceState;
import zesp03.common.data.SampleRaw;
import zesp03.common.data.SurveyInfo;
import zesp03.common.entity.Controller;
import zesp03.common.entity.Device;
import zesp03.common.entity.DeviceFrequency;
import zesp03.common.entity.DeviceSurvey;
import zesp03.common.exception.NotFoundException;
import zesp03.common.repository.DeviceFrequencyRepository;
import zesp03.common.repository.DeviceRepository;
import zesp03.common.repository.DeviceSurveyRepository;
import zesp03.common.util.SurveyInfoCollection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class SurveyModifyingServiceImpl implements SurveyModifyingService {
    private static final Logger log = LoggerFactory.getLogger(SurveyModifyingServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private SurveyReadingService surveyReadingService;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceFrequencyRepository deviceFrequencyRepository;

    @Autowired
    private DeviceSurveyRepository deviceSurveyRepository;

    @Override
    public int update(Controller controller, SurveyInfoCollection collection) {
        final int timestamp = (int) Instant.now().getEpochSecond();
        makeDevices(controller, collection);
        final Map<Long, CurrentDeviceState> id2current = surveyReadingService
                .checkForControllerFetch( controller.getId() );
        final List<DeviceSurvey> ds2persist = new ArrayList<>();
        id2current.forEach( (deviceId, currentState) -> {
            for(Integer frequencyMhz : currentState.getFrequencies()) {
                final DeviceFrequency deviceFrequency = currentState.findFrequency(frequencyMhz);
                final DeviceSurvey previousSurvey = currentState.findSurvey(frequencyMhz);
                final SurveyInfo info = collection.find(
                        currentState.getDevice().getName(), frequencyMhz);
                final DeviceSurvey newSurvey = new DeviceSurvey();
                newSurvey.setTimestamp(timestamp);
                if(info != null) {
                    newSurvey.setEnabled(true);
                    newSurvey.setClientsSum(info.getClientsSum());
                }
                else {
                    newSurvey.setEnabled(false);
                    newSurvey.setClientsSum(0);
                }
                newSurvey.setFrequency(deviceFrequency);
                if(previousSurvey == null) {
                    ds2persist.add(newSurvey);
                }
                else {
                    final int prevTime = previousSurvey.getTimestamp();
                    if(prevTime > timestamp) {
                        log.warn("last survey is from the future, last timestamp = {}, " +
                                        "current timestamp = {}, DeviceFrequency.id = {}",
                                prevTime, timestamp, deviceFrequency.getId());
                    }
                    if(prevTime < timestamp) {
                        ds2persist.add(newSurvey);
                    }
                }
            }
        } );
        for(DeviceSurvey ds : ds2persist) {
            em.persist(ds);
        }
        return ds2persist.size();
    }

    @Override
    public void makeDevices(Controller controller, Iterable<SurveyInfo> surveys) {
        final HashMap<String, Device> existing = new HashMap<>();
        for(SurveyInfo si : surveys) {
            existing.put(si.getName(), null);
        }
        if(existing.isEmpty()) {
            return;
        }
        em.createQuery("SELECT d FROM Device d LEFT JOIN FETCH d.frequencyList WHERE " +
                "d.name IN (:names) AND d.deleted = 0", Device.class)
                .setParameter("names", existing.keySet())
                .getResultList()
                .forEach( device -> existing.put(device.getName(), device) );
        final HashMap<String, Device> result = new HashMap<>();
        existing.forEach( (name, device) -> {
            if(device == null) {
                Device d = new Device();
                d.setName(name);
                d.setController(controller);
                d.setBuilding(null);
                d.setDeleted(0L);
                em.persist(d);
                result.put(name, d);
            }
            else {
                device.setController(controller);
                result.put(name, device);
            }
        } );
        int createdFrequencies = 0;
        for(SurveyInfo si : surveys) {
            final Device d = result.get( si.getName() );
            final List<DeviceFrequency> freqList = d.getFrequencyList();
            DeviceFrequency freq = null;
            for(DeviceFrequency test : freqList) {
                if( (int)test.getFrequency() == si.getFrequencyMhz() &&
                        test.getDeleted() == 0L ) {
                    freq = test;
                    break;
                }
            }
            if(freq == null) {
                freq = new DeviceFrequency();
                freq.setDevice(d);
                freq.setFrequency(si.getFrequencyMhz());
                freq.setDeleted(0L);
                freqList.add(freq);
                em.persist(freq);
                createdFrequencies++;
            }
        }
        if(createdFrequencies > 0) {
            log.info("{} device frequencies created", createdFrequencies);
        }
    }

    @Override
    public void importSurveys(Long deviceId, Integer frequencyMhz, List<SampleRaw> data) {
        data.sort(Comparator.comparingInt(SampleRaw::getTimestamp));
        Optional<DeviceFrequency> opt = deviceFrequencyRepository.findByDeviceAndFrequencyNotDeleted(deviceId, frequencyMhz);
        final DeviceFrequency df = new DeviceFrequency();
        if(opt.isPresent()) {
            final DeviceFrequency old = opt.get();
            old.setDeleted(old.getId());
            deviceFrequencyRepository.save(old);
            em.flush();
            df.setDevice(old.getDevice());
        }
        else {
            final Optional<Device> optDevice = deviceRepository.findOneNotDeleted(deviceId);
            if(!opt.isPresent()) {
                throw new NotFoundException("device");
            }
            df.setDevice(optDevice.get());
        }
        df.setFrequency(frequencyMhz);
        df.setDeleted(0L);
        deviceFrequencyRepository.save(df);
        DeviceSurvey lastDS = null;
        for(SampleRaw sampleRaw : data) {
            if(sampleRaw.getTimestamp() < 0) {
                throw new IllegalArgumentException("survey time < 0");
            }
            if( (lastDS != null) &&
                    (sampleRaw.getTimestamp() <= lastDS.getTimestamp()) )continue;
            DeviceSurvey deviceSurvey = new DeviceSurvey();
            deviceSurvey.setFrequency(df);
            deviceSurvey.setClientsSum(sampleRaw.getClients());
            deviceSurvey.setTimestamp(sampleRaw.getTimestamp());
            deviceSurvey.setEnabled(sampleRaw.isEnabled());
            deviceSurveyRepository.save(deviceSurvey);
            lastDS = deviceSurvey;
        }
    }

    @Override
    public void deleteForAll() {
        //em.createQuery("DELETE FROM DeviceSurvey ds").executeUpdate();
        List<DeviceFrequency> list = deviceFrequencyRepository.findAllNotDeleted();
        for(DeviceFrequency df : list) {
            df.setDeleted(df.getId());
            deviceFrequencyRepository.save(df);
        }
        em.flush();
        for(DeviceFrequency df : list) {
            DeviceFrequency n = new DeviceFrequency();
            n.setDevice(df.getDevice());
            n.setFrequency(df.getFrequency());
            n.setDeleted(0L);
            deviceFrequencyRepository.save(n);
        }
    }

    @Override
    public void deleteForAll(int before) {
        em.createQuery("DELETE FROM DeviceSurvey ds " +
                "WHERE ds.timestamp < :b")
                .setParameter("b", before)
                .executeUpdate();
    }

    @Override
    public void deleteForOneDevice(Long deviceId) {
        Optional<Device> opt = deviceRepository.findOneNotDeleted(deviceId);
        if( ! opt.isPresent() ) {
            throw new NotFoundException("device");
        }
        Device dev = opt.get();
        Set<Long> ids = dev.getFrequencyList()
                .stream()
                .map(DeviceFrequency::getId)
                .collect(Collectors.toSet());
        if(! ids.isEmpty()) {
            //em.createQuery("UPDATE DeviceSurvey ds SET ds.deleted = TRUE, ds.timestamp = -ds.timestamp " +
            em.createQuery("DELETE FROM DeviceSurvey ds " +
                    "WHERE ds.frequency.id IN (:ids)")
                    .setParameter("ids", ids)
                    .executeUpdate();
        }
    }

    @Override
    public void deleteForOneDevice(Long deviceId, int before) {
        Optional<Device> opt = deviceRepository.findOneNotDeleted(deviceId);
        if( ! opt.isPresent() ) {
            throw new NotFoundException("device");
        }
        Device dev = opt.get();
        Set<Long> ids = dev.getFrequencyList()
                .stream()
                .map(DeviceFrequency::getId)
                .collect(Collectors.toSet());
        if(! ids.isEmpty()) {
            //em.createQuery("UPDATE DeviceSurvey ds SET ds.deleted = TRUE, ds.timestamp = -ds.timestamp " +
            em.createQuery("DELETE FROM DeviceSurvey ds " +
                    "WHERE ds.frequency.id IN (:ids) AND ds.timestamp < :b")
                    .setParameter("ids", ids)
                    .setParameter("b", before)
                    .executeUpdate();
        }
    }
}
