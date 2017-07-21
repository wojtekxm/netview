/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.common.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestValidator {
    private Validator validator;
    @Before
    public void before() {
        validator = new Validator();
    }
    @Test
    public void checkIP_0_0_0_0_true() {
        assertTrue(validator.checkIP("0.0.0.0"));
    }

    @Test
    public void checkIP_255_255_255_255_true() {
        assertTrue(validator.checkIP("255.255.255.255"));
    }

    @Test
    public void checkIP_00_0_0_0_false() {
        assertFalse(validator.checkIP("00.0.0.0"));
    }

    @Test
    public void checkIP_1_2_3_false() {
        assertFalse(validator.checkIP("1.2.3"));
    }

    @Test
    public void checkIP_1_2_3_4_commas_false() {
        assertFalse(validator.checkIP("1,2,3,4"));
    }

    @Test
    public void checkIP_999_999_999_999_false() {
        assertFalse(validator.checkIP("999.999.999.999"));
    }

    @Test
    public void checkIP_a_b_c_d_false() {
        assertFalse(validator.checkIP("a.b.c.d"));
    }

    @Test
    public void checkIP_1_2_3_4_5_false() {
        assertFalse(validator.checkIP("1.2.3.4.5"));
    }

    @Test
    public void checkIP_empty_false() {
        assertFalse(validator.checkIP(""));
    }

    @Test
    public void checkEmail_dot_false() {
        assertFalse(validator.checkEmail("."));
    }

    @Test
    public void checkEmail_abc_false() {
        assertFalse(validator.checkEmail("abc"));
    }

    @Test
    public void checkEmail_at3_false() {
        assertFalse(validator.checkEmail("@@@"));
    }

    @Test
    public void checkEmail_trash_false() {
        assertFalse(validator.checkEmail("@#%^!*&("));
    }

    @Test
    public void checkEmail_good_true() {
        assertTrue(validator.checkEmail("g@c.com"));
    }

    @Test
    public void checkEmail_noname_false() {
        assertFalse(validator.checkEmail("@k.pl"));
    }

    @Test
    public void checkEmail_doubleAt_false() {
        assertFalse(validator.checkEmail("test@@x.net"));
    }
}
