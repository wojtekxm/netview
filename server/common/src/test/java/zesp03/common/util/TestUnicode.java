/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.common.util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestUnicode {
    // znak "nietoperz" który nie mieści się w jednym char
    public static final int BAT_UNICODE = 0x1F987;
    // napis który zawiera znak "nietoperz"
    public static final String BAT_STRING;

    static {
        int[] arr = {BAT_UNICODE};
        BAT_STRING = new String(arr, 0, arr.length);
    }

    @Test
    public void isLower_a_true() {
        assertTrue(Unicode.isLower('a'));
    }

    @Test
    public void isLower_A_false() {
        assertFalse(Unicode.isLower('A'));
    }

    @Test
    public void isLower_hash_false() {
        assertFalse(Unicode.isLower('#'));
    }

    @Test
    public void isUpper_B_true() {
        assertTrue(Unicode.isUpper('B'));
    }

    @Test
    public void isUpper_b_false() {
        assertFalse(Unicode.isUpper('b'));
    }

    @Test
    public void isUpper_ampersand_false() {
        assertFalse(Unicode.isUpper('&'));
    }

    @Test
    public void isDigit_5_true() {
        assertTrue(Unicode.isDigit('5'));
    }

    @Test
    public void isDigit_z_false() {
        assertFalse(Unicode.isDigit('z'));
    }

    @Test
    public void isAlpha_x_true() {
        assertTrue(Unicode.isAlpha('x'));
    }

    @Test
    public void isAlpha_X_true() {
        assertTrue(Unicode.isAlpha('X'));
    }

    @Test
    public void isAlpha_8_false() {
        assertFalse(Unicode.isAlpha('8'));
    }

    @Test
    public void isAlphaNum_w_true() {
        assertTrue(Unicode.isAlphaNum('w'));
    }

    @Test
    public void isAlphaNum_W_true() {
        assertTrue(Unicode.isAlphaNum('W'));
    }

    @Test
    public void isAlphaNum_7_true() {
        assertTrue(Unicode.isAlphaNum('7'));
    }

    @Test
    public void isAlphaNum_dot_false() {
        assertFalse(Unicode.isAlphaNum('.'));
    }

    @Test
    public void onlyLower_lower_true() {
        assertTrue(Unicode.onlyLower("a"));
    }

    @Test
    public void onlyLower_bat_false() {
        assertFalse(Unicode.onlyLower(BAT_STRING));
    }

    @Test
    public void onlyUpper_upper_true() {
        assertTrue(Unicode.onlyUpper("A"));
    }

    @Test
    public void onlyUpper_bat_false() {
        assertFalse(Unicode.onlyUpper(BAT_STRING));
    }

    @Test
    public void onlyDigit_digit_true() {
        assertTrue(Unicode.onlyDigit("0"));
    }

    @Test
    public void onlyDigit_bat_false() {
        assertFalse(Unicode.onlyDigit(BAT_STRING));
    }

    @Test
    public void onlyAlpha_alpha_true() {
        assertTrue(Unicode.onlyAlpha("aA"));
    }

    @Test
    public void onlyAlpha_bat_false() {
        assertFalse(Unicode.onlyAlpha(BAT_STRING));
    }

    @Test
    public void onlyAlphaNum_alphanum_true() {
        assertTrue(Unicode.onlyAlphaNum("aA0"));
    }

    @Test
    public void onlyAlphaNum_bat_false() {
        assertFalse(Unicode.onlyAlphaNum(BAT_STRING));
    }

    @Test
    public void noSurrogates_empty_true() {
        assertTrue(Unicode.noSurrogates(""));
    }

    @Test
    public void noSurrogates_a_true() {
        assertTrue(Unicode.noSurrogates("a"));
    }

    @Test
    public void noSurrogates_highSurrogate_false() {
        String s = "" + Character.MIN_HIGH_SURROGATE;
        assertFalse(Unicode.noSurrogates(s));
    }

    @Test
    public void noSurrogates_lowSurrogate_false() {
        String s = "" + Character.MAX_LOW_SURROGATE;
        assertFalse(Unicode.noSurrogates(s));
    }

    @Test
    public void noSurrogates_supplementary_false() {
        assertFalse(Unicode.noSurrogates(BAT_STRING));
    }

    @Test
    public void noSurrogates_mixed_false() {
        String s = "x" + BAT_STRING + "d";
        assertFalse(Unicode.noSurrogates(s));
    }
}
