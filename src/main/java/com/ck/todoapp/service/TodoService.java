package com.ck.todoapp.service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ck.todoapp.error.InvalidArgumentException;
import com.ck.todoapp.model.TodoDto;
import com.ck.todoapp.model.TodoModifyRequest;
import com.ck.todoapp.model.TodoStatus;
import com.ck.todoapp.persistence.entity.Todo;
import com.ck.todoapp.persistence.repository.TodoRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoService.class);

    private final Clock clock;
    private final ModelMapper mapper;
    private final TodoRepository todoRepository;

    public void updateTodo(final TodoModifyRequest todoRequest) {
        LOGGER.info("updating todo with id: {} and with description: {} and with status: {}",
                    todoRequest.getId(),
                    todoRequest.getDescription(),
                    todoRequest.getStatus());

        Optional<Todo> todoEntity = todoRepository.findById(todoRequest.getId());

        if (todoEntity.isEmpty()) {
            throw new InvalidArgumentException(HttpStatus.NOT_FOUND, "Todo entity could not be found. TodoId: " + todoRequest.getId());
        }

        if (TodoStatus.PAST_DUE == todoEntity.get().getStatus()) {
            throw new InvalidArgumentException(HttpStatus.BAD_REQUEST,
                                               "PAST_DUE entities can not be modified. TodoId: " + todoRequest.getId());
        }

        final Todo todo = todoEntity.get();

        todo.setStatus(todoRequest.getStatus());
        todo.setDescription(todoRequest.getDescription());
        todoRepository.save(todo);
    }

    public List<Todo> updateTodos() {
        LOGGER.info("updating todos to PAST_DUE");

        final List<Todo> todos = todoRepository.findByDueDateTime(LocalDateTime.now(clock));
        if (todos == null) {
            LOGGER.warn("no todos found to update via scheduler.");
            return Collections.emptyList();
        }

        todos.forEach(c -> c.setStatus(TodoStatus.PAST_DUE));

        return todoRepository.saveAll(todos);
    }

    public Long addTodo(final TodoDto request) {
        LOGGER.info("adding new todo with description: {} and with status: {}",
                    request.getDescription(),
                    request.getStatus());

        if (request.getDueDateTime().isBefore(LocalDateTime.now())) {
            throw new InvalidArgumentException(HttpStatus.BAD_REQUEST, "Due date must be in the future.");
        }

        Todo todo = mapper.map(request, Todo.class);
        todo.setCreation(LocalDateTime.now());

        Todo savedTodo = todoRepository.save(todo);
        return savedTodo.getId();
    }

    public List<TodoDto> getNotCompletedTodos() {
        LOGGER.info("retrieving uncompleted todos");

        final List<Todo> todos = todoRepository.findByStatus(TodoStatus.NOT_DONE);

        return todos.stream()
                    .map(c -> mapper.map(c, TodoDto.class))
                    .collect(Collectors.toUnmodifiableList());
    }

    public List<TodoDto> getAll() {
        LOGGER.info("retrieving all todos");

        return todoRepository.findAll().stream()
                             .map(c -> mapper.map(c, TodoDto.class))
                             .collect(Collectors.toUnmodifiableList());
    }

    public TodoDto getBy(Long id) {
        LOGGER.info("retrieving todo");
        return mapper.map(todoRepository.findById(id), TodoDto.class);
    }
}
