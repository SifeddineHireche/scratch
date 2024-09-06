package com.example.scratchgame.models;

public class Symbol {
    private String name;
    private double rewardMultiplier;
    private String type;
    private String impact;
    private double extra;

    public Symbol(String name, double rewardMultiplier, String type) {
        this(name, rewardMultiplier, type, null, 0);
    }

    public Symbol(String name, double rewardMultiplier, String type, String impact) {
        this(name, rewardMultiplier, type, impact, 0);
    }

    public Symbol(String name, double rewardMultiplier, String type, String impact, double extra) {
        this.name = name;
        this.rewardMultiplier = rewardMultiplier;
        this.type = type;
        this.impact = impact;
        this.extra = extra;
    }

    public double getRewardMultiplier() {
        return rewardMultiplier;
    }

    public String getType() {
        return type;
    }

    public String getImpact() {
        return impact;
    }

    public double getExtra() {
        return extra;
    }
}