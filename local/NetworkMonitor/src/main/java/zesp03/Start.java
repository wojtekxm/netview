package zesp03;

import java.io.IOException;
import java.util.List;

public class Start {
    public static void main(String[] args) throws IOException, SNMPException {
        SNMPHandler s = new FakeSNMP();
        List<DeviceInfo> list = s.queryDevices("106.106.106.106");
        for(DeviceInfo info : list) {
            System.out.println("name=" + info.getName() +
                    " enabled=" + info.isEnabled() +
                    " clients=" + info.getClients() );
        }
    }
}
