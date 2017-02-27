package zesp03.util;

public class IPv4 {
    public static boolean isValid(String ip) {
        String[] split = ip.split("\\.");
        if (split.length != 4) return false;
        int[] a = new int[4];
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            try {
                a[i] = Integer.parseInt(s);
                if (a[i] < 0 || a[i] > 255) return false;
            } catch (NumberFormatException exc) {
                return false;
            }
        }

        String efect = a[0] + "." + a[1] + "." + a[2] + "." + a[3];
        return efect.equals(ip);
    }
}
