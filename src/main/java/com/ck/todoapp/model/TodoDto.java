package com.ck.todoapp.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoDto {
    private Long id;
    private String description;
    private TodoStatus status;
    private LocalDateTime dueDateTime;
    private LocalDateTime completedDateTime;

}
