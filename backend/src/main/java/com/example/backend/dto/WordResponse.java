package com.example.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class WordResponse {

    @Getter
    @Setter
    private String status;
    @Getter
    @Setter
    private String message;

    @Getter
    @Setter
    private List<Integer> result;

    public WordResponse() {
    }

    public WordResponse(String status, String message, List<Integer> result) {
        this.status = status;
        this.message = message;
        this.result = result;
    }
}
