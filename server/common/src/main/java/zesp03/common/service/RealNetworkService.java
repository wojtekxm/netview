/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;
import org.springframework.stereotype.Service;
import zesp03.common.data.SurveyInfo;
import zesp03.common.exception.SNMPException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RealNetworkService implements NetworkService {
    private static final Logger log = LoggerFactory.getLogger(RealNetworkService.class);
    private Snmp snmp;

    public RealNetworkService() {
        try {
            DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping();
            transport.listen();
            snmp = new Snmp(transport);
        }
        catch(IOException exc) {
            throw new IllegalStateException(exc);
        }
    }

    @Override
    public List<SurveyInfo> queryDevices(String controllerIPv4, String community) {
        CommunityTarget target;
        try {
            target = makeTarget(controllerIPv4, community);
        }
        catch(UnknownHostException exc) {
            throw new SNMPException("unknown host", exc);
        }
        final TableUtils utils = new TableUtils(snmp, new DefaultPDUFactory());
        final OID oidFrequency = new OID("1.3.6.1.4.1.15983.1.1.3.1.1.1.3");
        final OID oidName = new OID("1.3.6.1.4.1.15983.1.1.3.1.1.1.4");
        final OID oidClients = new OID("1.3.6.1.4.1.15983.1.1.3.1.1.1.70");
        final OID[] oids = new OID[]{oidFrequency, oidName, oidClients};
        final List<TableEvent> list = utils.getTable(target, oids, null, null);
        if(list == null) {
            log.debug("TableUtils.getTable() returned null");
            return new ArrayList<>(0);
        }
        final List<SurveyInfo> result = new ArrayList<>();
        for(final TableEvent ev : list) {
            if(ev == null) {
                log.debug("TableEvent is null");
                continue;
            }
            if(ev.isError() || ev.getException() != null) {
                throw new SNMPException(ev.getErrorMessage(), ev.getException());
            }
            final VariableBinding[] bindings = ev.getColumns();
            if(bindings == null) {
                log.debug("TableEvent.getColumns() returned null");
                continue;
            }
            String name = null;
            Integer frequencyMhz = null;
            Integer clients = null;
            for(final VariableBinding b : bindings) {
                if(b == null) {
                    log.debug("VariableBinding is null");
                }
                else if(b.isException()) {
                    log.debug("VariableBinding.isException() returned true");
                }
                else {
                    final OID oid = b.getOid();
                    final Variable var = b.getVariable();
                    if(oid.startsWith(oidFrequency)) {
                        if(frequencyMhz == null) {
                            final Integer freqID = parseVariableAsIntOrNull(var);
                            if(freqID != null) {
                                if(freqID.equals(1)) {
                                    frequencyMhz = 2400;
                                }
                                else if(freqID.equals(2)){
                                    frequencyMhz = 5000;
                                }
                                else {
                                    log.debug("unexpected value for frequency ID: {}", freqID);
                                }
                            }
                            else {
                                log.debug("variable for frequency ID could not be parsed");
                            }
                        }
                        else {
                            log.debug("OID for device frequency has been encountered more than once");
                        }
                    }
                    else if(oid.startsWith(oidName)) {
                        if(name == null) {
                            name = var.toString();
                        }
                        else {
                            log.debug("OID for device name has been encountered more than once");
                        }
                    }
                    else if(oid.startsWith(oidClients)) {
                        if(clients == null) {
                            clients = parseVariableAsIntOrNull(var);
                            if(clients == null) {
                                log.debug("variable for clients sum could not be parsed");
                            }
                        }
                        else {
                            log.debug("OID for clients sum has been encountered more than once");
                        }
                    }
                    else {
                        log.debug("unexpected variable OID");
                    }
                }
            }
            if(name != null && frequencyMhz != null && clients != null) {
                final SurveyInfo si = new SurveyInfo(name, frequencyMhz, clients);
                result.add(si);
            }
        }
        return result;
    }

    private static CommunityTarget makeTarget(String IPv4, String community) throws UnknownHostException {
        final InetAddress inet = InetAddress.getByName(IPv4);
        final UdpAddress udp = new UdpAddress(inet, 161);
        final CommunityTarget ct = new CommunityTarget();
        ct.setAddress(udp);
        ct.setCommunity(new OctetString(community));
        ct.setVersion(SnmpConstants.version2c);
        ct.setTimeout(1500);
        ct.setRetries(2);
        return ct;
    }

    private static Integer parseVariableAsIntOrNull(Variable var) {
        try {
            return var.toInt();
        }
        catch(UnsupportedOperationException exc) {}
        try {
            final long lng = var.toLong();
            final int result = (int)lng;
            if((long)result == lng)return result;
        }
        catch(UnsupportedOperationException exc) {}
        return null;
    }
}
