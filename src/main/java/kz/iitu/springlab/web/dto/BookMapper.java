package kz.iitu.springlab.web.dto;

import kz.iitu.springlab.catalog.Book;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class BookMapper {

    public Book toDomain(BookRequest request) {
        return new Book(request.title(), request.genre(), request.year());
    }

    public BookResponse toResponse(Book book) {
        return new BookResponse(book.getId(), book.getTitle(),
                book.getAuthor() == null ? null : book.getAuthor().getName(),
                book.getYear(), book.getGenre(), book.getCreatedAt());
    }

    public List<BookResponse> toResponseList(List<Book> books) {
        return books.stream().map(this::toResponse).toList();
    }

    public Book applyPatch(Book existing, BookPatchRequest patch) {
        return new Book(
                patch.title() != null ? patch.title() : existing.getTitle(),
                patch.genre() != null ? patch.genre() : existing.getGenre(),
                patch.year() != null ? patch.year() : existing.getYear());
    }
}