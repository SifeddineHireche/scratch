package com.example.scratchgame.models;

import java.util.List;

public class WinCombination {
    private String name;
    private double rewardMultiplier;
    private String when;
    private int count;
    private String group;
    private List<List<String>> coveredAreas;

    public WinCombination(String name, double rewardMultiplier, String when, int count, String group, List<List<String>> coveredAreas) {
        this.name = name;
        this.rewardMultiplier = rewardMultiplier;
        this.when = when;
        this.count = count;
        this.group = group;
        this.coveredAreas = coveredAreas;
    }

    public String getName() {
        return name;
    }

    public double getRewardMultiplier() {
        return rewardMultiplier;
    }

    public String getWhen() {
        return when;
    }

    public int getCount() {
        return count;
    }

    public String getGroup() {
        return group;
    }

    public List<List<String>> getCoveredAreas() {
        return coveredAreas;
    }
}