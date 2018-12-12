package com.mahirkole.better;

import java.util.Date;
import java.util.Map;

public class Event {

    private Long iddiaId;
    private EventType type;
    private Date date;
    private Integer matchCode;
    private Short mbs;
    private Team home;
    private Team away;
    private Map<OddType, Odd> odds;
}
