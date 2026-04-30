package com.notetaking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NoteForm {

    @NotBlank
    @Size(max = 255)
    private String title;

    private String content;
}
