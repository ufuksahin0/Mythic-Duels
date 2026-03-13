package com.cardgame.util;

public class GameConfig {
    public enum Difficulty { EASY, NORMAL, HARD }
    
    private static Difficulty difficulty = Difficulty.NORMAL;
    private static boolean animationsEnabled = true;

    public static Difficulty getDifficulty() {
        return difficulty;
    }

    public static void setDifficulty(Difficulty difficulty) {
        GameConfig.difficulty = difficulty;
    }

    public static boolean isAnimationsEnabled() {
        return animationsEnabled;
    }

    public static void setAnimationsEnabled(boolean animationsEnabled) {
        GameConfig.animationsEnabled = animationsEnabled;
    }
}
