package com.mahirkole.better;

import java.util.Date;
import java.util.Map;

public class Event {

    private Long iddaaId;
    private EventType type;
    private Date date;
    private Integer matchCode;
    private Short mbs;
    private Team home;
    private Team away;
    private Map<OddType, Map<OddResult, Odd>> odds;

    public Event(Long iddaaId, EventType type, Date date, Integer matchCode, Short mbs, Team home, Team away, Map<OddType, Map<OddResult, Odd>> odds) {
        this.iddaaId = iddaaId;
        this.type = type;
        this.date = date;
        this.matchCode = matchCode;
        this.mbs = mbs;
        this.home = home;
        this.away = away;
        this.odds = odds;
    }

    public Event(Team home, Team away){
        this.home = home;
        this.away = away;
    }

    @Override
    public String toString() {
        return "Event{" +
                "iddaaId=" + iddaaId +
                ", type=" + type +
                ", date=" + date +
                ", matchCode=" + matchCode +
                ", mbs=" + mbs +
                ", home=" + home +
                ", away=" + away +
                ", odds=" + odds +
                '}';
    }
}
