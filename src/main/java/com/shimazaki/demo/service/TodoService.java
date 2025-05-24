package com.shimazaki.demo.service;

import com.shimazaki.demo.model.Todo;
import com.shimazaki.demo.model.TodoCreateRequest;
import com.shimazaki.demo.model.TodoUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Slf4j
public class TodoService {

    private final Map<Long, Todo> todoStorage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public TodoService() {
        // 初期データを追加
        createInitialData();
    }

    private void createInitialData() {
        Todo todo1 = new Todo();
        todo1.setId(idGenerator.getAndIncrement());
        todo1.setTitle("Spring Bootの勉強");
        todo1.setDescription("OpenAPI 3.0との連携を学ぶ");
        todo1.setCompleted(false);
        todo1.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        todoStorage.put(todo1.getId(), todo1);

        Todo todo2 = new Todo();
        todo2.setId(idGenerator.getAndIncrement());
        todo2.setTitle("デモアプリの作成");
        todo2.setDescription("REST APIのサンプルを作る");
        todo2.setCompleted(false);
        todo2.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        todoStorage.put(todo2.getId(), todo2);
    }

    public List<Todo> getAllTodos() {
        return new ArrayList<>(todoStorage.values());
    }

    public Optional<Todo> getTodoById(Long id) {
        return Optional.ofNullable(todoStorage.get(id));
    }

    public Todo createTodo(TodoCreateRequest request) {
        Todo todo = new Todo();
        todo.setId(idGenerator.getAndIncrement());
        todo.setTitle(request.getTitle());
        todo.setDescription(request.getDescription());
        todo.setCompleted(false);
        todo.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        
        todoStorage.put(todo.getId(), todo);
        log.info("Created new TODO: {}", todo);
        return todo;
    }

    public Optional<Todo> updateTodo(Long id, TodoUpdateRequest request) {
        return getTodoById(id).map(todo -> {
            if (request.getTitle() != null) {
                todo.setTitle(request.getTitle());
            }
            if (request.getDescription() != null) {
                todo.setDescription(request.getDescription());
            }
            if (request.getCompleted() != null) {
                todo.setCompleted(request.getCompleted());
            }
            todo.setUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));
            
            log.info("Updated TODO: {}", todo);
            return todo;
        });
    }

    public boolean deleteTodo(Long id) {
        Todo removed = todoStorage.remove(id);
        if (removed != null) {
            log.info("Deleted TODO: {}", removed);
            return true;
        }
        return false;
    }
} 