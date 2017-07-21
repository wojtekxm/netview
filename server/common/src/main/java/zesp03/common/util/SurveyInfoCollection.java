/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.common.util;

import zesp03.common.data.SurveyInfo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class SurveyInfoCollection implements Iterable<SurveyInfo> {
    private final HashMap<Key, SurveyInfo> map;

    public SurveyInfoCollection(Collection<SurveyInfo> surveys) {
        map = new HashMap<>(surveys.size());
        for(final SurveyInfo si : surveys) {
            final Key k = new Key(si.getName(), si.getFrequencyMhz());
            if( ! map.containsKey(k) ) {
                map.put(k, si);
            }
        }
    }

    @Override
    public Iterator<SurveyInfo> iterator() {
        return map.values().iterator();
    }

    public SurveyInfo find(String name, int frequencyMhz) {
        return map.get( new Key(name, frequencyMhz) );
    }

    private static class Key {
        private final String name;
        private final int frequencyMhz;
        private final int hash;

        private Key(String name, int frequencyMhz) {
            this.name = name;
            this.frequencyMhz = frequencyMhz;
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + frequencyMhz;
            this.hash = result;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Key key = (Key) o;

            if (frequencyMhz != key.frequencyMhz) return false;
            return name != null ? name.equals(key.name) : key.name == null;
        }

        @Override
        public int hashCode() {
            return hash;
        }
    }
}
