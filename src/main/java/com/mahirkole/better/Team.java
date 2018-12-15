package com.mahirkole.better;

public class Team {
    private Long iddaaId;
    private String shortName;
    private String name;

    public Team(Long iddaaId, String shortName, String name) {
        this.iddaaId = iddaaId;
        this.shortName = shortName;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                '}';
    }
}
