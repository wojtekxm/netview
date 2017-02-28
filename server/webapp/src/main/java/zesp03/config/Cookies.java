package zesp03.config;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class Cookies {
    /**
     * Zwraca ciasteczko o nazwie cookieName.
     *
     * @param request    zapytanie HTTP w którym ma być wyszukane ciasteczko
     * @param cookieName nazwa poszukiwanego ciasteczka
     * @return Szukane ciasteczko. Zwraca null jeśli nie znajdzie ciastka.
     */
    public static Cookie find(HttpServletRequest request, String cookieName) {
        Cookie[] arr = request.getCookies();
        if (arr == null) return null;
        for (Cookie c : arr) {
            if (c.getName().equals(cookieName)) return c;
        }
        return null;
    }

    /**
     * Zwraca wartość przechowywaną w ciasteczku o nazwie cookieName.
     *
     * @param request    zapytanie HTTP w którym ma być wyszukane ciasteczko
     * @param cookieName nazwa poszukiwanego ciasteczka
     * @return Wartość w poszukiwanym ciasteczku. Zwraca null jeśli nie znajdzie ciastka.
     */
    public static String get(HttpServletRequest request, String cookieName) {
        Cookie c = find(request, cookieName);
        if (c == null) return null;
        return c.getValue();
    }
}
