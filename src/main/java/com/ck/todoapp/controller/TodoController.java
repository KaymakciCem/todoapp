package com.ck.todoapp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ck.todoapp.error.InvalidArgumentException;
import com.ck.todoapp.model.TodoDto;
import com.ck.todoapp.model.TodoModifyRequest;
import com.ck.todoapp.service.TodoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @GetMapping("/todos")
    public ResponseEntity<List<TodoDto>> getAllTodos() {
        final List<TodoDto> allTodos = todoService.getAll();
        return ResponseEntity.ok(allTodos);
    }

    @GetMapping("/todos/notcompleted")
    public ResponseEntity<List<TodoDto>> getNotCompletedTodos() {
        final List<TodoDto> allTodos = todoService.getNotCompletedTodos();
        return ResponseEntity.ok(allTodos);
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<TodoDto> getTodo(@PathVariable Long id){
        return ResponseEntity.ok(todoService.getBy(id));
    }

    @PutMapping("/todos/{id}")
    public ResponseEntity<Void> updateTodo(@RequestBody TodoModifyRequest todo){
        todoService.updateTodo(todo);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/todo/creation")
    public ResponseEntity<Long> createTodo(@RequestBody TodoDto todo) throws InvalidArgumentException {
        Long todoId = todoService.addTodo(todo);
        return ResponseEntity.ok(todoId);
    }

}
