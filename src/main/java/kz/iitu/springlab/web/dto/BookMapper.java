package kz.iitu.springlab.web.dto;

import kz.iitu.springlab.catalog.Book;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.List;

@Component
public class BookMapper {

    public Book toDomain(BookRequest request) {
        return new Book(null, request.title(), request.author(),
                request.year(), request.genre(), Instant.now());
    }

    public BookResponse toResponse(Book book) {
        return new BookResponse(book.id(), book.title(), book.author(),
                book.year(), book.genre(), book.createdAt());
    }

    public List<BookResponse> toResponseList(List<Book> books) {
        return books.stream().map(this::toResponse).toList();
    }

    public Book applyPatch(Book existing, BookPatchRequest patch) {
        return new Book(existing.id(),
                patch.title() != null ? patch.title() : existing.title(),
                patch.author() != null ? patch.author() : existing.author(),
                patch.year() != null ? patch.year() : existing.year(),
                patch.genre() != null ? patch.genre() : existing.genre(),
                existing.createdAt());
    }
}