package zesp03.common.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestRandomUtil {
    private RandomUtil ru;

    @Before
    public void before() {
        ru = new RandomUtil();
    }

    @Test
    public void decide_0_false() {
        assertEquals(ru.decide(0.0), false);
    }

    @Test
    public void decide_1_true() {
        assertEquals(ru.decide(1.0), true);
    }

    @Test
    public void decide_minus_false() {
        assertEquals(ru.decide(-1.0), false);
    }

    @Test
    public void decide_moreThan1_true() {
        assertEquals(ru.decide(5.0), true);
    }

    @Test
    public void decide_0_100_false() {
        assertEquals(ru.decide(0, 100), false);
    }

    @Test
    public void decide_100_100_true() {
        assertEquals(ru.decide(100, 100), true);
    }

    @Test
    public void decide_minus_100_false() {
        assertEquals(ru.decide(-100, 100), false);
    }

    @Test
    public void decide_500_100_true() {
        assertEquals(ru.decide(500, 100), true);
    }

    @Test
    public void choose_1_1_1() {
        assertEquals(ru.choose(1, 1), 1);
    }

    @Test
    public void choose_minus99_minus99_minus99() {
        assertEquals(ru.choose(-99, -99), -99);
    }

    @Test
    public void choose_minus5_plus5_inRange() {
        int x = ru.choose(-5, 5);
        assertTrue(x  >= -5);
        assertTrue(x <= 5);
    }

    @Test
    public void choose_big_bigger_inRange() {
        int x = ru.choose(444, 999);
        assertTrue(x  >= 444);
        assertTrue(x <= 999);
    }
}
