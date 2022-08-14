package com.ck.todoapp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.ck.todoapp.model.TodoDto;
import com.ck.todoapp.model.TodoModifyRequest;
import com.ck.todoapp.model.TodoStatus;
import com.ck.todoapp.persistence.entity.Todo;
import com.ck.todoapp.persistence.repository.TodoRepository;
import com.ck.todoapp.util.TestClock;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    private static final Long TODO_ID = 1L;

    private final TestClock clock = new TestClock(Clock.fixed(Instant.ofEpochMilli(0), ZoneId.systemDefault()));

    private final ModelMapper mapper = new ModelMapper();

    @Mock
    private TodoRepository todoRepository;

//    @InjectMocks
    private TodoService todoService;

    private Todo todo1;
    private Todo todo2;

    @BeforeEach
    void setUp() {

        todoService = new TodoService(clock, new ModelMapper(), todoRepository);

        todo1 = getTodo(TODO_ID,
                             "desc",
                             TodoStatus.NOT_DONE,
                             LocalDateTime.now().minusDays(10),
                             LocalDateTime.now().plusDays(10),
                             LocalDateTime.now());

        todo2 = getTodo(TODO_ID + 1,
                             "desc1",
                             TodoStatus.NOT_DONE,
                             LocalDateTime.now().minusDays(15),
                             LocalDateTime.now().plusDays(100),
                             LocalDateTime.now());
    }

    @Test
    void updateTodo_fail_entity_not_found() {
        when(todoRepository.findById(TODO_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () ->
                todoService.updateTodo(TodoModifyRequest.builder().id(TODO_ID).build()));

        assertThat(exception.getMessage()).isEqualTo("Todo entity could not be found. TodoId: 1");
    }

    @Test
    void updateTodo_fail_status_past_due() {
        Todo todo = new Todo();
        todo.setStatus(TodoStatus.PAST_DUE);
        when(todoRepository.findById(TODO_ID)).thenReturn(Optional.of(todo));

        Exception exception = assertThrows(Exception.class, () ->
                todoService.updateTodo(TodoModifyRequest.builder().id(TODO_ID).status(TodoStatus.PAST_DUE).build()));

        assertThat(exception.getMessage()).isEqualTo("PAST_DUE entities can not be modified. TodoId: 1");
    }

    @ParameterizedTest
    @EnumSource(value = TodoStatus.class,
                names = "PAST_DUE",
                mode = EnumSource.Mode.EXCLUDE)
    void updateTodo_success(final TodoStatus todoStatus) {
        TodoModifyRequest todoModifyRequest = getTodoRequest(todoStatus);

        Todo todo = new Todo();
        todo.setId(TODO_ID);
        when(todoRepository.findById(TODO_ID)).thenReturn(Optional.of(todo));

        todoService.updateTodo(todoModifyRequest);
        verify(todoRepository).save(todo);
    }

    @Test
    void updateTodos_emptyList() {
        when(todoRepository.findByDueDateTime(LocalDateTime.now(clock))).thenReturn(Collections.emptyList());
        List<Todo> todos = todoService.updateTodos();
        assertThat(todos).isEmpty();
    }

    @Test
    void updateTodos_success() {
        when(todoRepository.findByDueDateTime(LocalDateTime.now(clock))).thenReturn(List.of(todo1, todo2));

        todoService.updateTodos();

        assertThat(todo1.getStatus()).isEqualTo(TodoStatus.PAST_DUE);
        assertThat(todo2.getStatus()).isEqualTo(TodoStatus.PAST_DUE);
    }

    @Test
    void addTodo_fails_due_date_in_the_past() {
        Exception exception = assertThrows(Exception.class, () ->
                todoService.addTodo(TodoDto.builder().id(TODO_ID).dueDateTime(LocalDateTime.now().minusDays(2)).build()));

        assertThat(exception.getMessage()).isEqualTo("Due date must be in the future.");
    }

    @Test
    void addTodo_success() {
        Todo response = new Todo();
        response.setId(TODO_ID);
        response.setCreation(LocalDateTime.now(clock));
        when(todoRepository.save(any())).thenReturn(response);
        long createdId = todoService.addTodo(TodoDto.builder().id(TODO_ID).dueDateTime(LocalDateTime.now().plusDays(2)).build());

        assertThat(createdId).isEqualTo(TODO_ID);
    }

    @Test
    void getNotCompletedTodos_success() {
        when(todoRepository.findByStatus(TodoStatus.NOT_DONE)).thenReturn(List.of(todo1, todo2));
        List<TodoDto> result = todoService.getNotCompletedTodos();
        assertThat(result).containsExactlyInAnyOrder(mapper.map(todo1, TodoDto.class), mapper.map(todo2, TodoDto.class));
    }

    @Test
    void getNotCompletedTodos_empty() {
        when(todoRepository.findByStatus(TodoStatus.NOT_DONE)).thenReturn(Collections.emptyList());
        List<TodoDto> result = todoService.getNotCompletedTodos();
        assertThat(result).isEmpty();
    }

    @Test
    void getAll_empty_list() {
        when(todoRepository.findAll()).thenReturn(Collections.emptyList());
        List<TodoDto> result = todoService.getAll();
        assertThat(result).isEmpty();
    }

    @Test
    void getAll_success() {
        when(todoRepository.findAll()).thenReturn(List.of(todo1, todo2));
        List<TodoDto> result = todoService.getAll();
        assertThat(result).containsExactlyInAnyOrder(mapper.map(todo1, TodoDto.class), mapper.map(todo2, TodoDto.class));
    }

    private Todo getTodo(final Long id,
                         final String description,
                         final TodoStatus status,
                         final LocalDateTime creation,
                         final LocalDateTime dueDateTime,
                         final LocalDateTime expiredDateTime) {
        Todo todo = new Todo();
        todo.setId(id);
        todo.setDescription(description);
        todo.setStatus(status);
        todo.setCreation(creation);
        todo.setDueDateTime(dueDateTime);
        todo.setCompletedDateTime(expiredDateTime);

        return todo;
    }

    private TodoModifyRequest getTodoRequest(final TodoStatus todoStatus) {
        return TodoModifyRequest.builder()
                                .id(TODO_ID)
                                .description("desc")
                                .status(todoStatus)
                                .build();
    }

}