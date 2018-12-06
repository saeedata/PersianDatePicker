package com.PersianCalendar.Component;

import java.util.Calendar;

public class PersianCalendar {

    private int day;
    private int month;
    private int year;

    private int jY;
    private int jM;
    private int jD;

    private int gY;
    private int gM;
    private int gD;

    private int leap;
    private int march;
    private int day_of_week;
    private String monthName;
    private String dayName;
    public static final String[] SHAMSI_MONTHS = {"فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور", "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"};
    public static final String[] SHAMSI_DAYS = {"یکشنبه", "دوشنبه", "سه شنبه", "چهارشنبه", "پنج شنبه", "جمعه", "شنبه"};
    private static Calendar cal = Calendar.getInstance();

    public PersianCalendar() {
        this(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
    }

    public PersianCalendar(int year, int month, int day) {
        int jd = JG2JD(year, month, day, 0);
        JD2Jal(jd);
        this.year = this.jY;
        this.month = this.jM;
        this.day = this.jD;
        this.monthName = SHAMSI_MONTHS[this.jM - 1];
        cal.set(year, month - 1, day);
        day_of_week = cal.get(Calendar.DAY_OF_WEEK);
        this.dayName = SHAMSI_DAYS[day_of_week - 1];

    }

    public int getDay() {
        return this.day;
    }

    public int getMonth() {
        return this.month;
    }

    public int getYear() {
        return this.year;
    }

    public String getDayName() {
        return this.dayName;
    }

    public String getMonthName() {
        return this.monthName;
    }

    public int getStartDay() {

        int k = day % 7;
        int t = (k == 0) ? 1 : 8 - k;
        int l = ((t + day_of_week) % 7);
        return l % 8 - 1;

    }

    public int getNumberOfMonthDays() {

        if (month <= 6 && month > 0) {
            return 31;
        } else if (month < 12 && month > 6) {
            return 30;
        }

        if (month == 12) {
            return isLeapYear() ? 30 : 29;
        }

        return 0;
    }

    public boolean isLeapYear() {
        return isLeapYear(year);
    }

    public static int getStartDay(int year, int month) {
        // Return the start day
        return (getTotalNumberOfDays(year, month) + 1) % 7;

    }

    private static int getTotalNumberOfDays(int year, int month) {

        int total = 0;
        // Get the total days from 1800 to year - 1
        for (int i = 1300; i < year; i++) {
            total += isLeapYear(i) ? 366 : 365;
        }
        // Add days from Jan to the month prior to the calendar month
        for (int i = 1; i < month; i++) {
            total += getNumberOfMonthDays(year, i);
        }
        return total;

    }

    public static int getNumberOfMonthDays(int year, int month) {

        if (month <= 6 && month > 0) {
            return 31;
        } else if (month < 12 && month > 6) {
            return 30;
        }

        if (month == 12) {
            return isLeapYear(year) ? 30 : 29;
        }

        return 0; // If month is incorrect
    }

    public static boolean isLeapYear(int year) {

        switch (year % 33) {
            case 1:
                return true;

            case 5:
                return true;

            case 9:
                return true;

            case 13:
                return true;

            case 17:
                return true;

            case 22:
                return true;

            case 26:
                return true;

            case 30:
                return true;

            default:
                return false;

        }

    }

    @Override
    public String toString() {
        return String.format("%04d/%02d/%02d", year, month, day);
    }

    private int JG2JD(int year, int month, int day, int J1G0) {
        int jd = 1461 * (year + 4800 + (month - 14) / 12) / 4 + 367 * (month - 2 - 12 * ((month - 14) / 12)) / 12 - 3 * ((year + 4900 + (month - 14) / 12) / 100) / 4 + day - 32075;
        if (J1G0 == 0) {
            jd = jd - (year + 100100 + (month - 8) / 6) / 100 * 3 / 4 + 752;
        }
        return jd;
    }

    private void JD2JG(int JD, int J1G0) {
        int j = 4 * JD + 139361631;
        if (J1G0 == 0) {
            j = j + (4 * JD + 183187720) / 146097 * 3 / 4 * 4 - 3908;
        }
        int i = j % 1461 / 4 * 5 + 308;
        this.gD = (i % 153 / 5 + 1);
        this.gM = (i / 153 % 12 + 1);
        this.gY = (j / 1461 - 100100 + (8 - this.gM) / 6);
    }

    private void JD2Jal(int JDN) {
        JD2JG(JDN, 0);
        this.jY = (this.gY - 621);
        JalCal(this.jY);
        int JDN1F = JG2JD(this.gY, 3, this.march, 0);
        int k = JDN - JDN1F;
        if (k >= 0) {
            if (k <= 185) {
                this.jM = (1 + k / 31);
                this.jD = (k % 31 + 1);
                return;
            }
            k -= 186;
        } else {
            this.jY -= 1;
            k += 179;
            if (this.leap == 1) {
                k += 1;
            }
        }
        this.jM = (7 + k / 30);
        this.jD = (k % 30 + 1);
    }

    private void JalCal(int jY) {
        this.march = 0;
        this.leap = 0;
        int[] breaks = {-61, 9, 38, 199, 426, 686, 756, 818, 1111, 1181, 1210, 1635, 2060, 2097, 2192, 2262, 2324, 2394, 2456, 3178};
        this.gY = (jY + 621);
        int leapJ = -14;
        int jp = breaks[0];
        int jump = 0;
        for (int j = 1; j <= 19; j++) {
            int jm = breaks[j];
            jump = jm - jp;
            if (jY < jm) {
                int N = jY - jp;
                leapJ = leapJ + N / 33 * 8 + (N % 33 + 3) / 4;
                if ((jump % 33 == 4) && (jump - N == 4)) {
                    leapJ += 1;
                }
                int leapG = this.gY / 4 - (this.gY / 100 + 1) * 3 / 4 - 150;
                this.march = (20 + leapJ - leapG);
                if (jump - N < 6) {
                    N = N - jump + (jump + 4) / 33 * 33;
                }
                this.leap = (((N + 1) % 33 - 1) % 4);
                if (this.leap != -1) {
                    break;
                }
                this.leap = 4;
                break;
            }
            leapJ = leapJ + jump / 33 * 8 + jump % 33 / 4;
            jp = jm;
        }
    }

}
