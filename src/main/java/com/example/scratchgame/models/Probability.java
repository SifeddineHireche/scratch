package com.example.scratchgame.models;

import java.util.Map;

public class Probability {
    private int column;
    private int row;
    private Map<String, Integer> symbolProbabilities;

    public Probability(int column, int row, Map<String, Integer> symbolProbabilities) {
        this.column = column;
        this.row = row;
        this.symbolProbabilities = symbolProbabilities;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public Map<String, Integer> getSymbolProbabilities() {
        return symbolProbabilities;
    }

    public int getTotalProbability() {
        return symbolProbabilities.values().stream().mapToInt(Integer::intValue).sum();
    }
}
