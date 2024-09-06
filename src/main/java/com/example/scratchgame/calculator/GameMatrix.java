package com.example.scratchgame.calculator;

import java.util.List;
import java.util.Map;
import java.util.Random;

import com.example.scratchgame.configuration.ConfigLoader;
import com.example.scratchgame.models.Probability;

public class GameMatrix {
    private ConfigLoader configLoader;

    public GameMatrix(ConfigLoader configLoader) {
        this.configLoader = configLoader;
    }

    public String[][] generateMatrix() {
        String[][] matrix = new String[configLoader.getRows()][configLoader.getColumns()];
        Random random = new Random();

        for (int row = 0; row < configLoader.getRows(); row++) {
            for (int col = 0; col < configLoader.getColumns(); col++) {
                matrix[row][col] = generateSymbolWithBonusChance(col, row);
            }
        }

        return matrix;
    }

    private String generateSymbolWithBonusChance(int col, int row) {
        Random random = new Random();
        int bonusChance = 10;
        int rand = random.nextInt(100) + 1;

        if (rand <= bonusChance) {
            return getRandomBonusSymbol();
        } else {
            return getRandomStandardSymbol(configLoader.getStandardProbabilities(), col, row);
        }
    }

    private String getRandomStandardSymbol(List<Probability> probabilities, int col, int row) {
        for (Probability prob : probabilities) {
            if (prob.getColumn() == col && prob.getRow() == row) {
                int total = prob.getTotalProbability();
                int rand = new Random().nextInt(total) + 1;
                int cumulative = 0;
                for (Map.Entry<String, Integer> entry : prob.getSymbolProbabilities().entrySet()) {
                    cumulative += entry.getValue();
                    if (rand <= cumulative) {
                        return entry.getKey();
                    }
                }
            }
        }
        return "MISS";
    }

    private String getRandomBonusSymbol() {
        Map<String, Integer> bonusProbabilities = configLoader.getBonusProbabilities();
        int total = bonusProbabilities.values().stream().mapToInt(Integer::intValue).sum();
        int rand = new Random().nextInt(total) + 1;
        int cumulative = 0;

        for (Map.Entry<String, Integer> entry : bonusProbabilities.entrySet()) {
            cumulative += entry.getValue();
            if (rand <= cumulative) {
                return entry.getKey();
            }
        }
        return "MISS";
    }
}
