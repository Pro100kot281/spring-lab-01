package kz.iitu.springlab.catalog;

import kz.iitu.springlab.web.Genre;
public record GenreReport(Genre genre, Long count, Double avgYear) { }