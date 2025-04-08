package com.cuongnguyen.cse441.enums;

public enum Month {
    JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER;

    public static Month fromInt(int month) {
        return values()[month - 1];
    }
}