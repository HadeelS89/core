package com.qpros.utils;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Month {

    Jan(1),
    Feb(2),
    Mar(3),
    Apr(4),
    May(5),
    Jun(6),
    Jul(7),
    Aug(8),
    Sep(9),
    Oct(10),
    Nov(11),
    Dec(12);

    private static final Map<Integer,Month> lookup
            = new HashMap<>();

    static {
        for(Month m : EnumSet.allOf(Month.class))
            lookup.put(m.getCode(), m);
    }

    private final int code;

     Month(int code) {
        this.code = code;
    }

    public int getCode() { return code; }

    public static Month get(int code) {
        return lookup.get(code);
    }

}