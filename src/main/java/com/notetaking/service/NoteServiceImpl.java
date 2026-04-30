package com.notetaking.service;

import com.notetaking.domain.Note;
import com.notetaking.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    @Override
    public List<Note> findAll() {
        return noteRepository.findAll();
    }

    @Override
    public List<Note> search(String title) {
        return noteRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public Note findById(Long id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found: " + id));
    }

    @Override
    public Note save(String title, String content) {
        Note note = Note.builder()
                .title(title)
                .content(content)
                .build();
        return noteRepository.save(note);
    }

    @Override
    public Note update(Long id, String title, String content) {
        Note note = findById(id);
        note.setTitle(title);
        note.setContent(content);
        return noteRepository.save(note);
    }

    @Override
    public void delete(Long id) {
        noteRepository.deleteById(id);
    }

    @Override
    public Note sync(Long id, String title, String content) {
        Note note = findById(id);
        note.setTitle(title);
        note.setContent(content);
        return noteRepository.save(note);
    }
}
