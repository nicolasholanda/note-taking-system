package com.notetaking.controller;

import com.notetaking.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SyncController {

    private final NoteService noteService;

    @PostMapping("/notes/{id}/sync")
    public ResponseEntity<Void> sync(@PathVariable Long id, @RequestBody Map<String, String> body) {
        noteService.sync(id, body.get("title"), body.get("content"));
        return ResponseEntity.ok().build();
    }
}
