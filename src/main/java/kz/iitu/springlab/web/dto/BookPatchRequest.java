package kz.iitu.springlab.web.dto;

import kz.iitu.springlab.web.Genre;
import jakarta.validation.constraints.*;

public record BookPatchRequest(
        @Size(min = 1, max = 120) String title,
        @Size(min = 1) String author,
        @Min(1450) @Max(2100) Integer year,
        Genre genre) { }