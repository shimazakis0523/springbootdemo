package com.shimazaki.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shimazaki.demo.model.TodoCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllTodos_Success() throws Exception {
        // When & Then - 初期状態はサンプルデータがある
        mockMvc.perform(get("/todos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void createTodo_Success() throws Exception {
        // Given
        TodoCreateRequest request = new TodoCreateRequest();
        request.setTitle("New Test Task");
        request.setDescription("New Test Description");

        // When & Then
        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("New Test Task"))
                .andExpect(jsonPath("$.description").value("New Test Description"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void getTodoById_ExistingId_Success() throws Exception {
        // Given - 初期データのID=1を使用
        Long existingId = 1L;

        // When & Then
        mockMvc.perform(get("/todos/" + existingId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(existingId));
    }

    @Test
    void getTodoById_NonExistingId_NotFound() throws Exception {
        // Given
        Long nonExistingId = 999L;

        // When & Then
        mockMvc.perform(get("/todos/" + nonExistingId))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTodo_ExistingId_Success() throws Exception {
        // Given - 初期データのID=1を使用
        Long existingId = 1L;

        // When & Then
        mockMvc.perform(delete("/todos/" + existingId))
                .andExpect(status().isNoContent());

        // 削除後は取得できない
        mockMvc.perform(get("/todos/" + existingId))
                .andExpect(status().isNotFound());
    }
} 