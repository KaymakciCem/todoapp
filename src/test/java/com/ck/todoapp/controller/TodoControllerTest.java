package com.ck.todoapp.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.ck.todoapp.error.InvalidArgumentException;
import com.ck.todoapp.model.TodoDto;
import com.ck.todoapp.model.TodoModifyRequest;
import com.ck.todoapp.service.TodoService;

@ExtendWith(MockitoExtension.class)
class TodoControllerTest {

    private static final Long TODO_ID = 1L;

    @Mock
    private TodoService todoService;

    @InjectMocks
    private TodoController todoController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getAllTodos() {
        when(todoService.getAll()).thenReturn(List.of(getTodoDto()));
        ResponseEntity<List<TodoDto>> allTodos = todoController.getAllTodos();
        assertThat(allTodos.getBody().get(0).getId()).isEqualTo(1L);
    }

    @Test
    void getNotCompletedTodos() {
        when(todoService.getNotCompletedTodos()).thenReturn(List.of(getTodoDto()));
        ResponseEntity<List<TodoDto>> allTodos = todoController.getNotCompletedTodos();
        assertThat(allTodos.getBody().get(0).getId()).isEqualTo(1L);
    }

    @Test
    void getTodo() {
        when(todoService.getBy(1L)).thenReturn(getTodoDto());
        ResponseEntity<TodoDto> todo = todoController.getTodo(1L);
        assertThat(todo.getBody().getId()).isEqualTo(1L);
    }

    @Test
    void updateTodo() {
        TodoModifyRequest todoModifyRequest = TodoModifyRequest.builder()
                                                   .id(1L)
                                                   .build();

//        when(todoService.updateTodo(todoModifyRequest)).thenReturn(true);
        todoController.updateTodo(todoModifyRequest);

        verify(todoService).updateTodo(todoModifyRequest);
    }

    @Test
    void createTodo() throws InvalidArgumentException {
        when(todoService.addTodo(getTodoDto())).thenReturn(1L);
        ResponseEntity<Long> todo = todoController.createTodo(getTodoDto());
        assertThat(todo.getBody()).isEqualTo(1L);
    }

    private TodoDto getTodoDto() {
        return TodoDto.builder()
                .id(TODO_ID).build();
    }
}