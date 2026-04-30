package com.notetaking.controller;

import com.notetaking.domain.Note;
import com.notetaking.exception.NoteNotFoundException;
import com.notetaking.service.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NoteController.class)
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NoteService noteService;

    private Note note;

    @BeforeEach
    void setUp() {
        note = Note.builder()
                .id(1L)
                .title("Test note")
                .content("Some content")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void list_returnsListView() throws Exception {
        when(noteService.findAll()).thenReturn(List.of(note));

        mockMvc.perform(get("/notes"))
                .andExpect(status().isOk())
                .andExpect(view().name("notes/list"))
                .andExpect(model().attributeExists("notes"));
    }

    @Test
    void list_withQuery_callsSearch() throws Exception {
        when(noteService.search("test")).thenReturn(List.of(note));

        mockMvc.perform(get("/notes").param("q", "test"))
                .andExpect(status().isOk())
                .andExpect(view().name("notes/list"));

        verify(noteService).search("test");
    }

    @Test
    void newForm_returnsFormView() throws Exception {
        mockMvc.perform(get("/notes/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("notes/form"))
                .andExpect(model().attributeExists("noteForm"));
    }

    @Test
    void create_redirectsOnSuccess() throws Exception {
        when(noteService.save(anyString(), anyString())).thenReturn(note);

        mockMvc.perform(post("/notes")
                        .param("title", "My note")
                        .param("content", "Content"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notes"));
    }

    @Test
    void create_returnsFormOnValidationError() throws Exception {
        mockMvc.perform(post("/notes")
                        .param("title", "")
                        .param("content", "Content"))
                .andExpect(status().isOk())
                .andExpect(view().name("notes/form"));
    }

    @Test
    void editForm_returnsFormWithNote() throws Exception {
        when(noteService.findById(1L)).thenReturn(note);

        mockMvc.perform(get("/notes/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("notes/form"))
                .andExpect(model().attributeExists("noteForm", "noteId"));
    }

    @Test
    void editForm_returns404_whenNotFound() throws Exception {
        when(noteService.findById(99L)).thenThrow(new NoteNotFoundException(99L));

        mockMvc.perform(get("/notes/99/edit"))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_redirectsOnSuccess() throws Exception {
        when(noteService.update(anyLong(), anyString(), anyString())).thenReturn(note);

        mockMvc.perform(post("/notes/1")
                        .param("title", "Updated")
                        .param("content", "Updated content"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notes"));
    }

    @Test
    void update_returnsFormOnValidationError() throws Exception {
        mockMvc.perform(post("/notes/1")
                        .param("title", "")
                        .param("content", "Content"))
                .andExpect(status().isOk())
                .andExpect(view().name("notes/form"));
    }

    @Test
    void delete_redirectsAfterDeletion() throws Exception {
        mockMvc.perform(post("/notes/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notes"));

        verify(noteService).delete(1L);
    }
}
