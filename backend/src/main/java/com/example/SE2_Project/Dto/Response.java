package com.example.SE2_Project.Dto;

import lombok.Data;

@Data
public class Response <T> {
    private int code;
    private String message;
    private T data;
}
