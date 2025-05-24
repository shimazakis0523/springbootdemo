package com.shimazaki.demo.controller;

import com.shimazaki.demo.api.TodosApi;
import com.shimazaki.demo.model.Todo;
import com.shimazaki.demo.model.TodoCreateRequest;
import com.shimazaki.demo.model.TodoUpdateRequest;
import com.shimazaki.demo.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TodoController implements TodosApi {

    private final TodoService todoService;

    @Override
    public ResponseEntity<List<Todo>> getTodos() {
        log.info("Getting all todos");
        List<Todo> todos = todoService.getAllTodos();
        return ResponseEntity.ok(todos);
    }

    @Override
    public ResponseEntity<Todo> createTodo(@Valid TodoCreateRequest todoCreateRequest) {
        log.info("Creating new todo with title: {}", todoCreateRequest.getTitle());
        Todo created = todoService.createTodo(todoCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    public ResponseEntity<Todo> getTodoById(Long todoId) {
        log.info("Getting todo by id: {}", todoId);
        return todoService.getTodoById(todoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Todo> updateTodo(Long todoId, @Valid TodoUpdateRequest todoUpdateRequest) {
        log.info("Updating todo with id: {}", todoId);
        return todoService.updateTodo(todoId, todoUpdateRequest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Void> deleteTodo(Long todoId) {
        log.info("Deleting todo with id: {}", todoId);
        if (todoService.deleteTodo(todoId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
} 