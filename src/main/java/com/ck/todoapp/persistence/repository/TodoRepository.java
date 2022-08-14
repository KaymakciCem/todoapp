package com.ck.todoapp.persistence.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ck.todoapp.model.TodoStatus;
import com.ck.todoapp.persistence.entity.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByStatus(final TodoStatus todoStatus);

    @Query(value = "from Todo t where t.creation <= :dueDateTime")
    List<Todo> findByDueDateTime(@Param("dueDateTime") LocalDateTime dueDateTime);

}
