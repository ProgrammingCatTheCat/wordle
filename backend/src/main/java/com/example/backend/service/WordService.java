package com.example.backend.service;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class WordService {
    private List<String> wordList = new ArrayList<>();

    @PostConstruct
    public void loadWords(){
        try {
            ClassPathResource resource = new ClassPathResource("words.txt");
            BufferedReader reader = new BufferedReader(new java.io.FileReader(resource.getFile()));
            String line;
            while((line = reader.readLine()) != null){
                wordList.add(line.trim().toUpperCase());
            }
            reader.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getRandomWord(){
        int index = (int)(Math.random() * wordList.size());
        return wordList.get(index);
    }

    public boolean isValidWord(String word){
        return wordList.contains(word);
    }
}
