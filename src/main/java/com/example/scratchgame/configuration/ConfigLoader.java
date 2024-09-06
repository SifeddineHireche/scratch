package com.example.scratchgame.configuration;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.scratchgame.models.Probability;
import com.example.scratchgame.models.Symbol;
import com.example.scratchgame.models.WinCombination;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConfigLoader {
    private String filePath;
    private int columns;
    private int rows;
    private Map<String, Symbol> symbols = new HashMap<>();
    private List<Probability> standardProbabilities = new ArrayList<>();
    private Map<String, Integer> bonusProbabilities = new HashMap<>();
    private List<WinCombination> winCombinations = new ArrayList<>();

    public ConfigLoader(String filePath) {
        this.filePath = filePath;
    }

    public void loadConfig() {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream inputStream = getClass().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found: " + filePath);
            }
            JsonNode config = mapper.readTree(inputStream);

            columns = config.get("columns").asInt();
            rows = config.get("rows").asInt();

            JsonNode symbolsJson = config.get("symbols");
            symbolsJson.fields().forEachRemaining(entry -> {
                String symbolName = entry.getKey();
                JsonNode symbolData = entry.getValue();
                double rewardMultiplier = symbolData.has("reward_multiplier") ? symbolData.get("reward_multiplier").asDouble() : 0;
                String type = symbolData.get("type").asText();
                String impact = symbolData.has("impact") ? symbolData.get("impact").asText() : null;
                double extra = symbolData.has("extra") ? symbolData.get("extra").asDouble() : 0;
                symbols.put(symbolName, new Symbol(symbolName, rewardMultiplier, type, impact, extra));
            });

            JsonNode standardSymbolsProb = config.get("probabilities").get("standard_symbols");
            standardSymbolsProb.forEach(probJson -> {
                int column = probJson.get("column").asInt();
                int row = probJson.get("row").asInt();
                Map<String, Integer> symbolProbs = new HashMap<>();
                probJson.get("symbols").fields().forEachRemaining(symEntry -> {
                    String symName = symEntry.getKey();
                    int probability = symEntry.getValue().asInt();
                    symbolProbs.put(symName, probability);
                });
                standardProbabilities.add(new Probability(column, row, symbolProbs));
            });

            JsonNode bonusSymbolsProb = config.get("probabilities").get("bonus_symbols").get("symbols");
            bonusSymbolsProb.fields().forEachRemaining(entry -> {
                String symbolName = entry.getKey();
                int probability = entry.getValue().asInt();
                bonusProbabilities.put(symbolName, probability);
            });

            JsonNode winCombJson = config.get("win_combinations");
            winCombJson.fields().forEachRemaining(entry -> {
                String combName = entry.getKey();
                JsonNode combData = entry.getValue();
                double rewardMultiplier = combData.get("reward_multiplier").asDouble();
                String when = combData.get("when").asText();
                int count = combData.has("count") ? combData.get("count").asInt() : 0;
                String group = combData.get("group").asText();
                List<List<String>> coveredAreas = new ArrayList<>();
                if (combData.has("covered_areas")) {
                    combData.get("covered_areas").forEach(areaNode -> {
                        List<String> positions = new ArrayList<>();
                        areaNode.forEach(posNode -> positions.add(posNode.asText()));
                        coveredAreas.add(positions);
                    });
                }
                winCombinations.add(new WinCombination(combName, rewardMultiplier, when, count, group, coveredAreas));
            });

            winCombinations.sort((a, b) -> Integer.compare(b.getCount(), a.getCount()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public Map<String, Symbol> getSymbols() {
        return symbols;
    }

    public List<Probability> getStandardProbabilities() {
        return standardProbabilities;
    }

    public Map<String, Integer> getBonusProbabilities() {
        return bonusProbabilities;
    }

    public List<WinCombination> getWinCombinations() {
        return winCombinations;
    }
}
