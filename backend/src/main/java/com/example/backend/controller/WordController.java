package com.example.backend.controller;

import com.example.backend.dto.WordResponse;
import com.example.backend.dto.WordSubmission;
import com.example.backend.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class WordController {
    @Autowired
    private WordService wordService;

    @PostMapping("/submitWord")
    public WordResponse submitWord(@RequestBody WordSubmission submission) {
        // Process the submitted word
        List<Integer> result = new ArrayList<>();
        try{
            String word = submission.getWord();
            System.out.println("Received word: " + word);

            String randomWord = wordService.getRandomWord();
            System.out.println("Random word: " + randomWord);
            // compare...

            if (!wordService.isValidWord(word)){

                result.add(-1);
                for (int i = 0; i < 5; ++i){
                    result.add(0);
                }
                return new WordResponse("success", "word not in wordlist", result);
            }
            else {
                result.add(-2);
                for (int i = 0; i < 5; ++i){
                    if (word.charAt(i) == randomWord.charAt(i)){
                        result.add(2);
                    }
                    else if (randomWord.contains(String.valueOf(word.charAt(i)))){
                        result.add(1);
                    }
                    else {
                        result.add(0);
                    }
                }
                return new WordResponse("success", "word in wordlist", result);
            }
        } catch (Exception e) {
            return new WordResponse("fail", "Error processing word: " + e.getMessage(), result);
        }
    }
}
