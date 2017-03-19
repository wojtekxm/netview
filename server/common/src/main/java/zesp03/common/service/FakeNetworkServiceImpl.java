package zesp03.common.service;

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
public class FakeNetworkServiceImpl implements NetworkService {
    /**
     * mapuje adres IP kontrolera do listy nazw urządzeń które są zarządzane przez ten kontroler
     */
    private final HashMap<String, ArrayList<String>> map = new HashMap<>();

    /**
     * wczytuje dane z plików konfiguracyjnych FakeSNMP_***.txt
     * pierwsza linia takiego pliku to adres IP kontrolera
     * każda pozostała linia pliku zawiera nazwę urządzenia które jest zarządzane przez ten kontroler
     *
     * @throws IOException błąd przy czytaniu plików konfiguracyjnych
     */
    public FakeNetworkServiceImpl() throws IOException {
        parse("FakeNetworkServiceImpl_wifi02.txt");
        parse("FakeNetworkServiceImpl_wifi03.txt");
        parse("FakeNetworkServiceImpl_wifi05.txt");
        parse("FakeNetworkServiceImpl_wifi06.txt");
        parse("FakeNetworkServiceImpl_wifi07.txt");
        parse("FakeNetworkServiceImpl_wifi10.txt");
        parse("FakeNetworkServiceImpl_wifi11.txt");
        parse("FakeNetworkServiceImpl_wifi99.txt");
        parse("FakeNetworkServiceImpl_wifi441.txt");
        parse("FakeNetworkServiceImpl_wifi442.txt");
    }

    private void parse(String resourceName) throws IOException {
        InputStream stream = null;
        try {
            stream = FakeNetworkServiceImpl.class.getResourceAsStream(resourceName);
            if (stream == null)
                throw new IllegalStateException("resource \"" + resourceName + "\" for FakeNetworkServiceImpl is not available");
            try (InputStreamReader isr = new InputStreamReader(stream, "utf-8");
                 BufferedReader reader = new BufferedReader(isr)) {
                String controllerIP = reader.readLine();
                if (controllerIP == null)
                    throw new IllegalStateException("invalid resource for FakeNetworkServiceImpl, IP is missing");
                controllerIP = controllerIP.trim().intern();
                final ArrayList<String> names = new ArrayList<>();
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim().intern();
                    if (line.length() > 0)
                        names.add(line);
                }
                map.put(controllerIP, names);
            }
        } finally {
            if (stream != null) stream.close();
        }
    }

    @Override
    public List<SurveyInfo> queryDevices(String controllerIP) throws SNMPException {
        if (!map.containsKey(controllerIP))
            throw new SNMPException("unable to connect with specified controller");
        final ArrayList<String> names = map.get(controllerIP);
        final Random random = new Random();
        final ArrayList<SurveyInfo> result = new ArrayList<>();
        for (String name : names) {
            SurveyInfo ds = new SurveyInfo();
            ds.setName(name);
            // 80% szansy że będzie włączony
            ds.setEnabled(random.nextInt(5) < 4);
            // losowa liczba klientów z przedziału 0 .. 49
            ds.setClientsSum(random.nextInt(50));
            result.add(ds);
        }
        return result;
    }
}
