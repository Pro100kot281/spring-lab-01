package kz.iitu.springlab.catalog;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<Book> findAll(String author) {
        if (author == null || author.isBlank()) {
            return repository.findAll();
        }
        return repository.findAll().stream()
                .filter(b -> b.author().toLowerCase().contains(author.toLowerCase()))
                .toList();
    }

    public Optional<Book> findById(long id) {
        return repository.findById(id);
    }

    public Book create(Book book) {
        boolean duplicate = repository.findAll().stream()
                .anyMatch(b -> b.title().equalsIgnoreCase(book.title())
                        && b.author().equalsIgnoreCase(book.author()));
        if (duplicate) {
            throw new DuplicateTitleException(book.title());
        }
        return repository.save(book);
    }

    public Book update(long id, Book book) {
        Book existing = repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        return repository.save(new Book(id, book.title(), book.author(),
                book.year(), book.genre(), existing.createdAt()));
    }

    public boolean delete(long id) {
        return repository.deleteById(id);
    }
}