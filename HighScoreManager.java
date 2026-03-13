package com.cardgame.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighScoreManager {
    private static final String FILE_NAME = "highscores.dat";
    private List<HighScore> scores;

    public HighScoreManager() {
        scores = new ArrayList<>();
        loadScores();
    }

    public void addScore(String name, int score) {
        scores.add(new HighScore(name, score));
        Collections.sort(scores);
        if (scores.size() > 10) { // Keep top 10
            scores.remove(scores.size() - 1);
        }
        saveScores();
    }

    public List<HighScore> getScores() {
        return scores;
    }

    @SuppressWarnings("unchecked")
    private void loadScores() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            scores = (List<HighScore>) ois.readObject();
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, ignore
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveScores() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(scores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class HighScore implements Serializable, Comparable<HighScore> {
        private String name;
        private int score;
        private String date;

        public HighScore(String name, int score) {
            this.name = name;
            this.score = score;
            this.date = new java.util.Date().toString();
        }

        public String toString() {
            return name + " - " + score + " (" + date + ")";
        }

        @Override
        public int compareTo(HighScore o) {
            return Integer.compare(o.score, this.score); // Descending order
        }
    }
}
