package com.ck.todoapp.persistence.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.ck.todoapp.model.TodoStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@Entity
public class Todo {

    @Id
    @GeneratedValue
    private Long id;

    private String description;
    private TodoStatus status;
    private LocalDateTime creation;
    private LocalDateTime dueDateTime;
    private LocalDateTime completedDateTime;
}
