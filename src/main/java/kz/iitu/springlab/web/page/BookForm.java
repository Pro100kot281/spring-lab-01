package kz.iitu.springlab.web.page;

import jakarta.validation.constraints.*;
import kz.iitu.springlab.catalog.Book;
import kz.iitu.springlab.web.Genre;
import kz.iitu.springlab.web.NoDigits;

import java.time.Instant;

/** Form-backing object for the "new book" and "edit book" HTML forms. */
public class BookForm {

    @NotBlank(message = "{book.title.required}")
    @Size(max = 120, message = "{book.title.size}")
    private String title;

    @NotBlank(message = "{book.author.required}")
    @NoDigits
    private String author;

    @NotNull(message = "{book.year.range}")
    @Min(value = 1450, message = "{book.year.range}")
    @Max(value = 2100, message = "{book.year.range}")
    private Integer year;

    @NotNull(message = "{book.genre.required}")
    private Genre genre;

    public static BookForm from(Book book) {
        BookForm form = new BookForm();
        form.setTitle(book.title());
        form.setAuthor(book.author());
        form.setYear(book.year());
        form.setGenre(book.genre());
        return form;
    }

    public Book toBook() {
        return new Book(null, title.trim(), author.trim(), year, genre, Instant.now());
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    public Genre getGenre() { return genre; }
    public void setGenre(Genre genre) { this.genre = genre; }
}
