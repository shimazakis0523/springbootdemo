package com.shimazaki.demo.service;

import com.shimazaki.demo.model.Todo;
import com.shimazaki.demo.model.TodoCreateRequest;
import com.shimazaki.demo.model.TodoUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TodoServiceTest {

    @Autowired
    private TodoService todoService;

    @Test
    void getAllTodos_WithSampleData_ReturnsNonEmptyList() {
        // When
        List<Todo> todos = todoService.getAllTodos();

        // Then - サンプルデータが存在することを確認
        assertThat(todos).isNotEmpty();
        assertThat(todos.get(0).getTitle()).isNotBlank();
    }

    @Test
    void createTodo_ValidRequest_Success() {
        // Given
        TodoCreateRequest request = new TodoCreateRequest();
        request.setTitle("New Task");
        request.setDescription("New Description");

        int initialSize = todoService.getAllTodos().size();

        // When
        Todo createdTodo = todoService.createTodo(request);

        // Then
        assertThat(createdTodo.getId()).isNotNull();
        assertThat(createdTodo.getTitle()).isEqualTo("New Task");
        assertThat(createdTodo.getDescription()).isEqualTo("New Description");
        assertThat(createdTodo.getCompleted()).isFalse();
        assertThat(createdTodo.getCreatedAt()).isNotNull();

        // サイズも増加していることを確認
        assertThat(todoService.getAllTodos()).hasSize(initialSize + 1);
    }

    @Test
    void getTodoById_ExistingId_Success() {
        // Given - まずTODOを作成
        TodoCreateRequest request = new TodoCreateRequest();
        request.setTitle("Test Task");
        request.setDescription("Test Description");
        Todo createdTodo = todoService.createTodo(request);

        // When
        Optional<Todo> found = todoService.getTodoById(createdTodo.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(createdTodo.getId());
        assertThat(found.get().getTitle()).isEqualTo("Test Task");
    }

    @Test
    void getTodoById_NonExistingId_NotFound() {
        // Given
        Long nonExistingId = 9999L;

        // When
        Optional<Todo> found = todoService.getTodoById(nonExistingId);

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    void updateTodo_ExistingId_Success() {
        // Given - まずTODOを作成
        TodoCreateRequest createRequest = new TodoCreateRequest();
        createRequest.setTitle("Original Task");
        createRequest.setDescription("Original Description");
        Todo createdTodo = todoService.createTodo(createRequest);
        
        TodoUpdateRequest updateRequest = new TodoUpdateRequest();
        updateRequest.setTitle("Updated Task");
        updateRequest.setDescription("Updated Description");
        updateRequest.setCompleted(true);

        // When
        Optional<Todo> updated = todoService.updateTodo(createdTodo.getId(), updateRequest);

        // Then
        assertThat(updated).isPresent();
        assertThat(updated.get().getTitle()).isEqualTo("Updated Task");
        assertThat(updated.get().getDescription()).isEqualTo("Updated Description");
        assertThat(updated.get().getCompleted()).isTrue();
        assertThat(updated.get().getUpdatedAt()).isNotNull();
    }

    @Test
    void deleteTodo_ExistingId_Success() {
        // Given - まずTODOを作成
        TodoCreateRequest request = new TodoCreateRequest();
        request.setTitle("To be deleted");
        request.setDescription("This will be deleted");
        Todo createdTodo = todoService.createTodo(request);
        int initialSize = todoService.getAllTodos().size();

        // When
        boolean deleted = todoService.deleteTodo(createdTodo.getId());

        // Then
        assertThat(deleted).isTrue();
        assertThat(todoService.getAllTodos()).hasSize(initialSize - 1);
        assertThat(todoService.getTodoById(createdTodo.getId())).isEmpty();
    }

    @Test
    void deleteTodo_NonExistingId_ReturnsFalse() {
        // Given
        Long nonExistingId = 9999L;

        // When
        boolean deleted = todoService.deleteTodo(nonExistingId);

        // Then
        assertThat(deleted).isFalse();
    }
} 