package com.example.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


public class WordResponse {

    private String status;
    private String message;

    private List<Integer> result;

    public WordResponse() {
    }

    public WordResponse(String status, String message, List<Integer> result) {
        this.status = status;
        this.message = message;
        this.result = result;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<Integer> getResult() { return result; }
    public void setResult(List<Integer> result) { this.result = result; }

}
