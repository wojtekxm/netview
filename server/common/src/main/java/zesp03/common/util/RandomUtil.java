/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.common.util;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class RandomUtil {
    public boolean decide(double chance) {
        if(chance > 1.0)chance = 1.0;
        else if(chance < 0.0)chance = 0.0;
        final int BIG = 1000_000_000;
        final int positiveOptions = (int)Math.round(chance * BIG);
        return decide(positiveOptions, BIG);
    }

    public boolean decide(int positiveOptions, int allOptions) {
        return ThreadLocalRandom.current().nextInt(allOptions) < positiveOptions;
    }

    public int choose(int min, int max) {
        if(max < min) {
            throw new IllegalArgumentException("max < min");
        }
        int diff = max - min;
        if(diff == 0)return min;
        return min + ThreadLocalRandom.current().nextInt(diff + 1);
    }
}
