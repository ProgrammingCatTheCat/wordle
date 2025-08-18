package com.example.backend.dto;

public class WordSubmission {
    private String word;
    public WordSubmission(){};

    public WordSubmission(String word) {
        this.word = word;
    }
    public String getWord() {
        return word;
    }
    public void setWord(String word) {
        this.word = word;
    }
}
