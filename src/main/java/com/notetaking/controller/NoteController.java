package com.notetaking.controller;

import com.notetaking.dto.NoteForm;
import com.notetaking.service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @GetMapping
    public String list(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("notes", q != null && !q.isBlank() ? noteService.search(q) : noteService.findAll());
        model.addAttribute("q", q);
        return "notes/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("noteForm", new NoteForm());
        return "notes/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("noteForm") NoteForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "notes/form";
        }
        noteService.save(form.getTitle(), form.getContent());
        return "redirect:/notes";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        var note = noteService.findById(id);
        var form = new NoteForm();
        form.setTitle(note.getTitle());
        form.setContent(note.getContent());
        model.addAttribute("noteForm", form);
        model.addAttribute("noteId", id);
        return "notes/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("noteForm") NoteForm form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("noteId", id);
            return "notes/form";
        }
        noteService.update(id, form.getTitle(), form.getContent());
        return "redirect:/notes";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        noteService.delete(id);
        return "redirect:/notes";
    }
}
