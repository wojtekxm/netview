package zesp03.common.util;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class Validator {
    private final Pattern emailPattern;

    public Validator() {
        emailPattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
    }

    public boolean checkIP(String ip) {
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

    public boolean checkEmail(String email) {
        return emailPattern.matcher(email).matches();
    }
}
