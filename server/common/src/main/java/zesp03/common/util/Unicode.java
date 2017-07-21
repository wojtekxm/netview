/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.common.util;

/**
 * Klasa Unicode zawiera same statyczne metody które służą do sprawdzenia
 * czy dany znak lub string posiada pewne właściwości.
 */
public class Unicode {
    /**
     * @param unicode wartość znaku w kodowaniu Unicode.
     * @return true jeśli argument unicode jest małą literą angielską [a-z].
     */
    public static boolean isLower(int unicode) {
        return (unicode >= 'a') && (unicode <= 'z');
    }

    /**
     * @param unicode wartość znaku w kodowaniu Unicode.
     * @return true jeśli argument unicode jest dużą literą angielską [A-Z].
     */
    public static boolean isUpper(int unicode) {
        return (unicode >= 'A') && (unicode <= 'Z');
    }

    /**
     * @param unicode wartość znaku w kodowaniu Unicode.
     * @return true jeśli argument unicode jest cyfrą [0-9].
     */
    public static boolean isDigit(int unicode) {
        return (unicode >= '0') && (unicode <= '9');
    }

    /**
     * @param unicode wartość znaku w kodowaniu Unicode.
     * @return true jeśli argument unicode jest angielską literą (małą lub dużą) [a-zA-Z].
     */
    public static boolean isAlpha(int unicode) {
        return isLower(unicode) || isUpper(unicode);
    }

    /**
     * @param unicode wartość znaku w kodowaniu Unicode.
     * @return true jeśli argument unicode jest znakiem alfanumerycznym [a-zA-Z0-9].
     */
    public static boolean isAlphaNum(int unicode) {
        return isAlpha(unicode) || isDigit(unicode);
    }

    public static boolean onlyLower(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!isLower(s.charAt(i))) return false;
        }
        return true;
    }

    public static boolean onlyDigit(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!isDigit(s.charAt(i))) return false;
        }
        return true;
    }

    public static boolean onlyAlpha(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!isAlpha(s.charAt(i))) return false;
        }
        return true;
    }

    public static boolean onlyAlphaNum(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!isAlphaNum(s.charAt(i))) return false;
        }
        return true;
    }

    public static boolean onlyUpper(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!isUpper(s.charAt(i))) return false;
        }
        return true;
    }

    /**
     * Sprawdza czy argument text ma taką właściwość,
     * że każdy znak (Unicode code point) jest reprezentowany przez tylko jeden char (Unicode code unit).
     *
     * @param text tekst do sprawdzenia.
     * @return true jeśli argument text nie zawiera surrogate.
     */
    public static boolean noSurrogates(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (Character.isSurrogate(text.charAt(i))) return false;
        }
        return true;
    }
}
