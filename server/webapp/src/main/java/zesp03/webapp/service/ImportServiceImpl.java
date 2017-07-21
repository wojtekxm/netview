/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.webapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zesp03.common.data.SampleRaw;
import zesp03.common.exception.ValidationException;
import zesp03.common.service.SurveyModifyingService;
import zesp03.common.util.RandomUtil;
import zesp03.webapp.dto.input.ImportFakeSurveysDto;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImportServiceImpl implements ImportService {
    private static final Logger log = LoggerFactory.getLogger(ImportServiceImpl.class);

    @Autowired
    private SurveyModifyingService surveyModifyingService;

    @Autowired
    private RandomUtil randomUtil;

    @Override
    public void fakeSurveys(ImportFakeSurveysDto dto) {
        if(dto.getMaxInterval() < dto.getMinInterval()) {
            throw new ValidationException("maxInterval", "less than minInterval");
        }
        if(dto.getMaxClients() < 0) {
            throw new ValidationException("maxClients", "less than zero");
        }
        if(dto.getTimeStart() < 0) {
            throw new ValidationException("timeStart", "less than zero");
        }
        final List<SampleRaw> list = new ArrayList<>();
        int lastTime = 0;
        int lastClients = randomUtil.choose(0, dto.getMaxClients());
        boolean lastEnabled = true;
        for(int i = 0; i < dto.getNumberOfSurveys(); i++) {
            SampleRaw ss = new SampleRaw();
            int time, clients, diff;
            boolean enabled;
            if(i == 0) {
                time = dto.getTimeStart();
            }
            else {
                time = lastTime + randomUtil.choose(dto.getMinInterval(), dto.getMaxInterval());
            }
            if(randomUtil.decide(0.9)) {
                diff = randomUtil.choose(-3, 3);
            }
            else {
                diff = randomUtil.choose(-8, 8);
            }
            if(lastEnabled) {
                enabled = randomUtil.decide(0.99);
            }
            else {
                enabled = randomUtil.decide(0.2);
            }
            if(enabled) {
                clients = lastClients + diff;
                if(clients < 0) {
                    clients = 0;
                }
                if(clients > dto.getMaxClients()) {
                    clients = dto.getMaxClients();
                }
            }
            else {
                clients = 0;
            }
            ss.setTimestamp(time);
            ss.setEnabled(enabled);
            ss.setClients(clients);
            if(ss.getTimestamp() < 0) {
                throw new ValidationException("survey time", "negative");
            }
            lastTime = ss.getTimestamp();
            lastEnabled = ss.isEnabled();
            lastClients = ss.getClients();
            list.add(ss);
        }
        surveyModifyingService.importSurveys(dto.getDeviceId(), dto.getFrequencyMhz(), list);
    }
}
