package com.example.SE2_Project.Dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponsePage <T>{
    Integer currentPage;
    Integer pageSize;
    Integer totalPages;
    Long totalElements;
    List<T> content;
}