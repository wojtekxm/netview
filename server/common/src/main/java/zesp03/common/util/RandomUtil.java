package zesp03.common.util;

import java.util.Random;

// thread safe
public class RandomUtil {
    private final Random r = new Random();

    public synchronized boolean decide(double chance) {
        final int BIG = 1000_000_000;
        final int positiveOptions = (int)Math.round(chance * BIG);
        return decide(positiveOptions, BIG);
    }

    public synchronized boolean decide(int positiveOptions, int allOptions) {
        return r.nextInt(allOptions) < positiveOptions;
    }

    public synchronized int choose(int min, int max) {
        if(max < min) {
            throw new IllegalArgumentException("max < min");
        }
        int diff = max - min;
        if(diff == 0)return min;
        return min + r.nextInt(diff + 1);
    }
}
