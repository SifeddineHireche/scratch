package com.example.scratchgame.models;

import java.util.List;
import java.util.Map;

public class Result {
    private String[][] matrix;
    private double reward;
    private Map<String, List<String>> appliedWinningCombinations;
    private List<String> bonusSymbolsApplied;
    private List<String> rewardBreakdown;

    public Result(String[][] matrix, double reward, Map<String, List<String>> appliedWinningCombinations, List<String> bonusSymbolsApplied, List<String> rewardBreakdown) {
        this.matrix = matrix;
        this.reward = reward;
        this.appliedWinningCombinations = appliedWinningCombinations;
        this.bonusSymbolsApplied = bonusSymbolsApplied;
        this.rewardBreakdown = rewardBreakdown;
    }

    public String[][] getMatrix() {
        return matrix;
    }

    public double getReward() {
        return reward;
    }

    public Map<String, List<String>> getAppliedWinningCombinations() {
        return appliedWinningCombinations;
    }

    public List<String> getBonusSymbolsApplied() {
        return bonusSymbolsApplied;
    }

    public List<String> getRewardBreakdown() {
        return rewardBreakdown;
    }
}
