package com.ck.todoapp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.tomcat.util.buf.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;

import com.ck.todoapp.persistence.entity.Todo;

@EnableAsync
public class ScheduledService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledService.class);

    private final TodoService todoService;

    public ScheduledService(final TodoService todoService) {
        this.todoService = todoService;
    }

    @Async
    @Scheduled(cron = "0 59 23 * * *") // every day midnight
    public void scheduledTodoStatusModificationAsync() {
        LOGGER.info("scheduled service for updating todos to PAST_DUE triggered.");
        List<Todo> todos = todoService.updateTodos();

        LOGGER.info("todos updated to PAST_DUE. ids " + todos.stream()
                                                             .map(c -> c.getId().toString())
                                                             .collect(Collectors.joining(",")));
    }
}
