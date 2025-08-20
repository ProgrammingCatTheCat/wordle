package com.example.backend.controller;

import com.example.backend.dto.WordResponse;
import com.example.backend.dto.WordSubmission;
import com.example.backend.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class WordController {
    @Autowired
    private WordService wordService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping(value = "/submitWord",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public WordResponse submitWord(@RequestBody WordSubmission submission) {
        // Process the submitted word
        List<Integer> result = new ArrayList<>();
        try{
            String word = submission.getWord();
            System.out.println("Received word: " + word);

            String redisKey = "wordle:currentWord";

            String randomWord = redisTemplate.opsForValue().get(redisKey);
            if (randomWord == null) {
                randomWord = wordService.getRandomWord();
                // 存储到Redis，设置过期时间（例如30分钟）
                redisTemplate.opsForValue().set(redisKey, randomWord, 30, TimeUnit.MINUTES);
            }
            // compare...
            System.out.println("Random word: " + randomWord);
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

                if(word.equals(randomWord)){
                    redisTemplate.delete(redisKey);
                    return new WordResponse("success", "hit", result);
                }
                return new WordResponse("success", "word in wordlist", result);
            }
        } catch (Exception e) {
            System.err.println("Null pointer exception: " + e.getMessage());
            e.printStackTrace();
            return new WordResponse("fail", "Error processing word: " + e.getMessage(), result);
        }
    }
}
