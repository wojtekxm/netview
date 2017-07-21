/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.common.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestSecret {
    @Test
    public void constructor_123456789() {
        int iterations = 0x04030201;
        byte[] salt = new byte[Secret.SALT_BYTES];
        for (int i = 0; i < salt.length; i++) {
            salt[i] = (byte) (i + Secret.ITERATIONS_BYTES + 1);
        }
        byte[] key = new byte[Secret.KEY_BYTES];
        for (int i = 0; i < key.length; i++) {
            key[i] = (byte) (i + Secret.ITERATIONS_BYTES + Secret.SALT_BYTES + 1);
        }
        Secret s = new Secret(iterations, salt, key);
        byte[] expected = new byte[Secret.ALL_BYTES];
        for (int i = 0; i < expected.length; i++) {
            expected[i] = (byte) (i + 1);
        }
        assertArrayEquals(expected, s.getData());
        assertEquals(iterations, s.getIterations());
        assertArrayEquals(salt, s.getSalt());
        assertArrayEquals(key, s.getKey());
    }

    @Test
    public void check_valid() {
        char[] pass = "a".toCharArray();
        char[] input = "a".toCharArray();
        Secret s1 = Secret.create(pass, 1);
        Secret s2 = Secret.create(pass, 2);
        assertTrue(s1.check(input));
        assertTrue(s2.check(input));
        assertTrue(s1.check(pass));
        assertTrue(s1.check(pass));
    }

    @Test
    public void check_invalid() {
        char[] pass = "a".toCharArray();
        char[] input = "b".toCharArray();
        Secret s1 = Secret.create(pass, 1);
        Secret s2 = Secret.create(pass, 2);
        assertFalse(s1.check(input));
        assertFalse(s2.check(input));
    }

    @Test
    public void readData_theSame() {
        char[] pass = "abc".toCharArray();
        Secret s1 = Secret.create(pass, 5);
        Secret s2 = Secret.readData(s1.getData());
        assertArrayEquals(s1.getData(), s2.getData());
    }
}
