package zesp03.util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestIPv4 {
    @Test
    public void isValid_0_0_0_0_true() {
        assertTrue(IPv4.isValid("0.0.0.0"));
    }

    @Test
    public void isValid_255_255_255_255_true() {
        assertTrue(IPv4.isValid("255.255.255.255"));
    }

    @Test
    public void isValid_00_0_0_0_false() {
        assertFalse(IPv4.isValid("00.0.0.0"));
    }

    @Test
    public void isValid_1_2_3_false() {
        assertFalse(IPv4.isValid("1.2.3"));
    }

    @Test
    public void isValid_1_2_3_4_commas_false() {
        assertFalse(IPv4.isValid("1,2,3,4"));
    }

    @Test
    public void isValid_999_999_999_999_false() {
        assertFalse(IPv4.isValid("999.999.999.999"));
    }

    @Test
    public void isValid_a_b_c_d_false() {
        assertFalse(IPv4.isValid("a.b.c.d"));
    }

    @Test
    public void isValid_1_2_3_4_5_false() {
        assertFalse(IPv4.isValid("1.2.3.4.5"));
    }

    @Test
    public void isValid_empty_false() {
        assertFalse(IPv4.isValid(""));
    }
}
