package com.example.scratchgame.calculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.scratchgame.configuration.ConfigLoader;
import com.example.scratchgame.models.Result;
import com.example.scratchgame.models.Symbol;
import com.example.scratchgame.models.WinCombination;

public class RewardCalculator {
    private ConfigLoader configLoader;

    public RewardCalculator(ConfigLoader configLoader) {
        this.configLoader = configLoader;
    }

    public Result calculateReward(String[][] matrix, double betAmount) {
        double reward = 0;
        Map<String, Integer> symbolCount = new HashMap<>();
        Map<String, List<String>> appliedWinningCombinations = new HashMap<>();
        List<String> bonusSymbolsApplied = new ArrayList<>();
        List<String> rewardBreakdown = new ArrayList<>();

        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[0].length; col++) {
                symbolCount.put(matrix[row][col], symbolCount.getOrDefault(matrix[row][col], 0) + 1);
            }
        }

        for (Map.Entry<String, Integer> entry : symbolCount.entrySet()) {
            boolean combinationApplied = false;
            for (WinCombination combination : configLoader.getWinCombinations()) {
                if (entry.getValue() >= combination.getCount() && !combinationApplied) {
                    if (combination.getWhen().equals("same_symbols")) {
                        double symbolMultiplier = configLoader.getSymbols().get(entry.getKey()).getRewardMultiplier();
                        double combinationReward = betAmount * combination.getRewardMultiplier() * symbolMultiplier;
                        reward += combinationReward;
                        rewardBreakdown.add(String.format(
                            "Symbol: %s, Combination: %s, Reward: %.2f (Calculation: %s * %s * %s)",
                            entry.getKey(), combination.getName(), combinationReward, betAmount, combination.getRewardMultiplier(), symbolMultiplier
                        ));
                        appliedWinningCombinations.put(entry.getKey(), List.of(combination.getName()));
                        combinationApplied = true;
                    }
                }
            }
        }

        for (WinCombination combination : configLoader.getWinCombinations()) {
            if (combination.getWhen().equals("linear_symbols")) {
                for (List<String> area : combination.getCoveredAreas()) {
                    Set<String> uniqueSymbols = new HashSet<>();
                    for (String pos : area) {
                        String[] posArray = pos.split(":");
                        int r = Integer.parseInt(posArray[0]);
                        int c = Integer.parseInt(posArray[1]);
                        uniqueSymbols.add(matrix[r][c]);
                    }
                    if (uniqueSymbols.size() == 1) {
                        String symbol = uniqueSymbols.iterator().next();
                        double symbolMultiplier = configLoader.getSymbols().get(symbol).getRewardMultiplier();
                        double combinationReward = betAmount * combination.getRewardMultiplier() * symbolMultiplier;
                        reward += combinationReward;
                        rewardBreakdown.add(String.format(
                            "Symbol: %s, Linear Combination: %s, Reward: %.2f (Calculation: %s * %s * %s)",
                            symbol, combination.getName(), combinationReward, betAmount, combination.getRewardMultiplier(), symbolMultiplier
                        ));
                        appliedWinningCombinations.putIfAbsent(symbol, new ArrayList<>());
                        appliedWinningCombinations.get(symbol).add(combination.getName());
                    }
                }
            }
        }

        for (Map.Entry<String, Integer> entry : symbolCount.entrySet()) {
            if (configLoader.getSymbols().get(entry.getKey()).getType().equals("bonus")) {
                reward = applyBonus(reward, configLoader.getSymbols().get(entry.getKey()));
                bonusSymbolsApplied.add(entry.getKey());
                rewardBreakdown.add(String.format("Bonus symbol: %s applied", entry.getKey()));
            }
        }

        if (bonusSymbolsApplied.isEmpty()) {
            bonusSymbolsApplied.add("0");
        }

        return new Result(matrix, reward, appliedWinningCombinations, bonusSymbolsApplied, rewardBreakdown);
    }

    private double applyBonus(double reward, Symbol bonusSymbol) {
        switch (bonusSymbol.getImpact()) {
            case "multiply_reward":
                return reward * bonusSymbol.getRewardMultiplier();
            case "extra_bonus":
                return reward + bonusSymbol.getExtra();
            case "miss":
            default:
                return reward;
        }
    }
}