package com.kr.media.amazing.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @Author: shenchengyu
 * @Date: 2020/9/14
 * @Description: 基于JDK LocalDate 开发工具类
 */
public class LocalDateUtils {

    public static Date localDate2Date(LocalDate localDate) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDate.atStartOfDay(zoneId);
        Date date = Date.from(zdt.toInstant());
        return date;
    }

    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        Date date = Date.from(zdt.toInstant());
        return date;
    }

    public static LocalDateTime date2LocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        return localDateTime;
    }

    public static LocalDate date2LocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();
        return localDate;
    }

    public static Date parseDateTime(String datetime) {
        LocalDateTime localDateTime = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return localDateTime2Date(localDateTime);
    }

    public static Date getDateTime(Date dateOfDay, Date timeOfDay) {
        LocalDateTime localDateTime1 = date2LocalDateTime(dateOfDay);
        LocalDateTime localDateTime2 = date2LocalDateTime(timeOfDay);

        LocalDateTime localDateTime = localDateTime1.plusHours(localDateTime2.getHour())
                .plusMinutes(localDateTime2.getMinute())
                .plusSeconds(localDateTime2.getSecond());

        return localDateTime2Date(localDateTime);
    }

    public static Date currDateTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime2Date(localDateTime);
    }

    public static Date currDate() {
        LocalDate localDate = LocalDate.now();
        return localDate2Date(localDate);
    }

    public static Date prevDate() {
        LocalDate localDate = LocalDate.now().plusDays(-1);
        return localDate2Date(localDate);
    }

    public static Date nextDate() {
        LocalDate localDate = LocalDate.now().plusDays(1);
        return localDate2Date(localDate);
    }

    public static Date getDate(Date date) {
        LocalDate localDate = date2LocalDate(date);
        return localDate2Date(localDate);
    }

    public static Date getDate(int year, int month, int dayOfMonth) {
        LocalDate localDate = LocalDate.of(year, month, dayOfMonth);
        return localDate2Date(localDate);
    }

    public static Date plusDays(Date date, long daysToAdd) {
        LocalDate localDate = date2LocalDate(date);
        localDate = localDate.plusDays(daysToAdd);
        return localDate2Date(localDate);
    }

    public static Date plusHours(Date date, long hoursToAdd) {
        LocalDateTime localDateTime = date2LocalDateTime(date);
        localDateTime = localDateTime.plusHours(hoursToAdd);
        return localDateTime2Date(localDateTime);
    }

    public static Date plusMonths(Date date, long monthsToAdd) {
        LocalDateTime localDateTime = date2LocalDateTime(date);
        localDateTime = localDateTime.plusMonths(monthsToAdd);
        return localDateTime2Date(localDateTime);
    }

    public static Date getStartDateTimeOfDay(Date date) {
        LocalDate localDate = date2LocalDate(date);
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.MIN);
        return localDateTime2Date(localDateTime);
    }

    public static Date getEndDateTimeOfDay(Date date) {
        LocalDate localDate = date2LocalDate(date);
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.MAX);
        return localDateTime2Date(localDateTime);
    }

    public static int currDayOfWeek() {
        return LocalDate.now().getDayOfWeek().getValue();
    }

    public static int prevDayOfWeek() {
        return LocalDate.now().plusDays(-1).getDayOfWeek().getValue();
    }

    public static int getDayOfWeek(Date date) {
        LocalDate localDate = date2LocalDate(date);
        return localDate.getDayOfWeek().getValue();
    }

    /**
     * 比较时间 （不比较日期）
     *
     * @param sourceTime
     * @param targetTime
     * @return
     */
    public static boolean isBefore(Date sourceTime, Date targetTime) {
        LocalDateTime ldt1 = date2LocalDateTime(sourceTime);
        LocalDateTime ldt2 = date2LocalDateTime(targetTime);

        return (ldt1.getHour() <= ldt2.getHour()) && ldt1.getMinute() <= ldt2.getMinute() && (ldt1.getSecond() <= ldt2.getSecond());
    }

    public static String format(Date date) {
        return date2LocalDateTime(date).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}