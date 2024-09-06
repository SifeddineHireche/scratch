package com.scratchgame;

import java.util.Arrays;
import java.util.Scanner;

import com.example.scratchgame.calculator.GameMatrix;
import com.example.scratchgame.calculator.RewardCalculator;
import com.example.scratchgame.configuration.ConfigLoader;
import com.example.scratchgame.models.Result;


public class ScratchGameRun {

    public static void main(String[] args) {
        ConfigLoader configLoader = new ConfigLoader("/config.json");
        configLoader.loadConfig();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the betting-amount: ");
        double betAmount = scanner.nextDouble();

        GameMatrix gameMatrix = new GameMatrix(configLoader);
        String[][] matrix = gameMatrix.generateMatrix();

        RewardCalculator rewardCalculator = new RewardCalculator(configLoader);
        Result result = rewardCalculator.calculateReward(matrix, betAmount);

        displayResult(result);
    }

    private static void displayResult(Result result) {
        for (String[] row : result.getMatrix()) {
            System.out.println(Arrays.toString(row));
        }

        System.out.println("Total reward: " + result.getReward());
        System.out.println("Winning combinations applied: " + result.getAppliedWinningCombinations());
        System.out.println("Bonus symbols applied: " + (result.getBonusSymbolsApplied().isEmpty() ? "0" : result.getBonusSymbolsApplied()));

        System.out.println("Reward breakdown:");
        for (String breakdown : result.getRewardBreakdown()) {
            System.out.println(breakdown);
        }
    }
}

  



    

    

   

   
