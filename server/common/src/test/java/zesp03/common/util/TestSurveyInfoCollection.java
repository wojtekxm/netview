/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.common.util;

import org.junit.Before;
import org.junit.Test;
import zesp03.common.data.SurveyInfo;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestSurveyInfoCollection {
    SurveyInfo si;
    SurveyInfo si2;
    SurveyInfoCollection col;

    @Before
    public void before() {
        ArrayList<SurveyInfo> l = new ArrayList<>();
        si = new SurveyInfo("a", 1, 6);
        si2 = new SurveyInfo("kkk", 322, 88);
        l.add(si);
        l.add(si2);
        col = new SurveyInfoCollection(l);
    }

    @Test
    public void theSame_matches() {
        assertEquals(col.find("a", 1), si);
    }

    @Test
    public void theSame_null_mismatch() {
        assertNotEquals(col.find("a", 1), null);
    }

    @Test
    public void otherName_mismatch() {
        assertNotEquals(col.find("b", 1), si);
    }

    @Test
    public void otherFreq_mismatch() {
        assertNotEquals(col.find("a", 2), si);
    }

    @Test
    public void otherBoth_mismatch() {
        assertNotEquals(col.find("z", 3), si);
    }

    @Test
    public void iterate() {
        boolean b1 = false;
        boolean b2 = false;
        for(SurveyInfo elem : col) {
            if(elem.equals(si))b1 = true;
            if(elem.equals(si2))b2 = true;
        }
        assertTrue(b1);
        assertTrue(b2);
    }
}
