/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import zesp03.common.data.SurveyInfo;
import zesp03.common.exception.SNMPException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Ta klasa służy do generowania sztucznych odpowiedzi protokołu SNMP.
 * W ogóle nie używa SNMP, nie łączy się z żadnym kontrolerem, nie wykonuje żadnych operacji sieciowych.
 * Lista "znanych" kontrolerów i urządzeń może być modyfikowana przez pliki konfiguracyjne (patrz konstruktor).
 */
@Service
public class FakeNetworkService implements NetworkService {
    private static final Logger log = LoggerFactory.getLogger(FakeNetworkService.class);
    /**
     * mapuje adres IP kontrolera do listy urządzeń które są zarządzane przez ten kontroler
     */
    private final HashMap<String, ArrayList<VirtualDevice>> map = new HashMap<>();
    private final Random random;

    /**
     * wczytuje dane z plików konfiguracyjnych FakeSNMP_***.txt
     * pierwsza linia takiego pliku to adres IP kontrolera
     * każda pozostała linia pliku zawiera nazwę urządzenia które jest zarządzane przez ten kontroler
     *
     * @throws IOException błąd przy czytaniu plików konfiguracyjnych
     */
    public FakeNetworkService() throws IOException {
        random = new Random();
        parse("FakeNetworkServiceImpl_wifi99.txt");
        parse("FakeNetworkServiceImpl_wifi441.txt");
        parse("FakeNetworkServiceImpl_wifi442.txt");
    }

    private void parse(String resourceName) throws IOException {
        InputStream stream = null;
        try {
            stream = FakeNetworkService.class.getResourceAsStream(resourceName);
            if (stream == null)
                throw new IllegalStateException("resource \"" + resourceName + "\" for FakeNetworkServiceImpl is not available");
            try (InputStreamReader isr = new InputStreamReader(stream, "utf-8");
                 BufferedReader reader = new BufferedReader(isr)) {
                String controllerIP = reader.readLine();
                if (controllerIP == null)
                    throw new IllegalStateException("invalid resource for FakeNetworkServiceImpl, IP is missing");
                controllerIP = controllerIP.trim().intern();
                final ArrayList<VirtualDevice> devices = new ArrayList<>();
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim().intern();
                    if (line.length() > 0) {
                        String[] split = line.split(" ");
                        if(split.length < 2) {
                            throw new IllegalStateException("invalid input line, less than 2 tokens");
                        }
                        final String deviceName = split[0].trim().intern();
                        if(deviceName.isEmpty()) {
                            throw new IllegalStateException("empty device name");
                        }
                        for(int i = 1; i < split.length; i++) {
                            VirtualDevice vd = new VirtualDevice();
                            vd.name = deviceName;
                            vd.frequencyMhz = Integer.parseInt(split[i]);
                            vd.lastClients = random.nextInt(50);
                            vd.lastEnabled = choose(0.8);
                            devices.add(vd);
                        }
                    }
                }
                map.put(controllerIP, devices);
            }
        } finally {
            if (stream != null) stream.close();
        }
    }

    @Override
    public List<SurveyInfo> queryDevices(String controllerIPv4, String community) throws SNMPException {
        if (!map.containsKey(controllerIPv4))
            throw new SNMPException("controller with IP \"" + controllerIPv4 +"\" is undefined");
        final ArrayList<VirtualDevice> devices = map.get(controllerIPv4);
        final Random random = new Random();
        final ArrayList<SurveyInfo> result = new ArrayList<>();
        for (VirtualDevice vd : devices) {
            boolean enabled;
            int clients;
            int diff;
            if(choose(0.9)) {
                diff = random.nextInt(7) - 3;
            }
            else {
                diff = random.nextInt(17) - 8;
            }
            if(vd.lastEnabled) {
                enabled = choose(0.99);
            }
            else {
                enabled = choose(0.2);
            }
            if(enabled) {
                clients = vd.lastClients + diff;
                if(clients < 0)clients = 0;
                if(clients > 70)clients = 65;
            }
            else {
                clients = 0;
            }

            if(enabled) {
                result.add( new SurveyInfo(vd.name, vd.frequencyMhz, clients) );
            }
            vd.lastEnabled = enabled;
            vd.lastClients = clients;
        }
        return result;
    }

    private boolean choose(double chance) {
        int limit = (int)Math.round(chance * 1000_000_000);
        return random.nextInt(1000_000_000) < limit;
    }

    private static class VirtualDevice {
        String name;
        int frequencyMhz;
        int lastClients;
        boolean lastEnabled;
    }
}
