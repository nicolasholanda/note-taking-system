package com.notetaking.service;

import com.notetaking.domain.Note;

import java.util.List;

public interface NoteService {

    List<Note> findAll();

    List<Note> search(String title);

    Note findById(Long id);

    Note save(String title, String content);

    Note update(Long id, String title, String content);

    void delete(Long id);

    Note sync(Long id, String title, String content);
}
