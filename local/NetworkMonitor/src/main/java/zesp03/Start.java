package zesp03;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.*;
import java.time.Instant;
import java.util.List;

public class Start {
    public static void main(String[] args) throws IOException, SNMPException, SQLException, AdminException, InterruptedException {
        examine();
    }

    public static void examine() throws IOException, SQLException, SNMPException {
        Management m = new Management();
        System.out.println("start");
        long t0 = System.currentTimeMillis();

        m.examineAll();

        long t1 = System.currentTimeMillis();
        System.out.println("czas trwania " + (t1-t0) + "ms");
    }
}
