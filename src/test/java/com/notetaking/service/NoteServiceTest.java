package com.notetaking.service;

import com.notetaking.domain.Note;
import com.notetaking.exception.NoteNotFoundException;
import com.notetaking.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteServiceImpl noteService;

    private Note note;

    @BeforeEach
    void setUp() {
        note = Note.builder().id(1L).title("Test").content("Content").build();
    }

    @Test
    void findAll_returnsAllNotes() {
        when(noteRepository.findAll()).thenReturn(List.of(note));

        var result = noteService.findAll();

        assertThat(result).hasSize(1).contains(note);
    }

    @Test
    void search_returnsMatchingNotes() {
        when(noteRepository.findByTitleContainingIgnoreCase("test")).thenReturn(List.of(note));

        var result = noteService.search("test");

        assertThat(result).hasSize(1).contains(note);
    }

    @Test
    void findById_returnsNote_whenExists() {
        when(noteRepository.findById(1L)).thenReturn(Optional.of(note));

        var result = noteService.findById(1L);

        assertThat(result).isEqualTo(note);
    }

    @Test
    void findById_throwsException_whenNotFound() {
        when(noteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> noteService.findById(99L))
                .isInstanceOf(NoteNotFoundException.class);
    }

    @Test
    void save_createsAndReturnsNote() {
        when(noteRepository.save(any(Note.class))).thenReturn(note);

        var result = noteService.save("Test", "Content");

        assertThat(result).isEqualTo(note);
        verify(noteRepository).save(any(Note.class));
    }

    @Test
    void update_updatesAndReturnsNote() {
        when(noteRepository.findById(1L)).thenReturn(Optional.of(note));
        when(noteRepository.save(note)).thenReturn(note);

        var result = noteService.update(1L, "Updated", "New content");

        assertThat(result.getTitle()).isEqualTo("Updated");
        assertThat(result.getContent()).isEqualTo("New content");
    }

    @Test
    void update_throwsException_whenNotFound() {
        when(noteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> noteService.update(99L, "Title", "Content"))
                .isInstanceOf(NoteNotFoundException.class);
    }

    @Test
    void delete_callsRepository() {
        noteService.delete(1L);

        verify(noteRepository).deleteById(1L);
    }

    @Test
    void sync_updatesAndReturnsNote() {
        when(noteRepository.findById(1L)).thenReturn(Optional.of(note));
        when(noteRepository.save(note)).thenReturn(note);

        var result = noteService.sync(1L, "Synced", "Synced content");

        assertThat(result.getTitle()).isEqualTo("Synced");
        assertThat(result.getContent()).isEqualTo("Synced content");
    }
}
