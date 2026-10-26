package kz.iitu.springlab.catalog;

import kz.iitu.springlab.web.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository repository;
    private final AuthorRepository authors;

    public BookService(BookRepository repository, AuthorRepository authors) {
        this.repository = repository;
        this.authors = authors;
    }

    public List<Book> findAll(String author) {
        if (author == null || author.isBlank()) {
            return repository.findAllWithAuthor();
        }
        return repository.findByAuthorNameContainingIgnoreCase(author);
    }


    public Page<Book> find(Genre genre, Pageable pageable) {
        return genre == null
                ? repository.findAll(pageable)
                : repository.findByGenre(genre, pageable);
    }


    public Slice<Book> findSlice(Pageable pageable) {
        return repository.findSliceBy(pageable);
    }

    public Optional<Book> findById(long id) {
        return repository.findById(id);
    }

    @Transactional
    public Book create(Book book, String authorName) {
        if (repository.existsByTitleIgnoreCaseAndAuthorNameIgnoreCase(book.getTitle(), authorName)) {
            throw new DuplicateTitleException(book.getTitle());
        }
        findOrCreateAuthor(authorName).addBook(book);
        return repository.save(book);
    }

    @Transactional
    public Book update(long id, Book changes, String authorName) {
        Book book = repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        book.setTitle(changes.getTitle());
        book.setYear(changes.getYear());
        book.setGenre(changes.getGenre());
        if (!book.getAuthor().getName().equalsIgnoreCase(authorName)) {
            book.getAuthor().removeBook(book);
            findOrCreateAuthor(authorName).addBook(book);
        }
        return book;
    }

    @Transactional
    public boolean delete(long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }


    @Transactional
    public int archiveOlderThan(int year) {
        return repository.archiveOlderThan(year);
    }

    private Author findOrCreateAuthor(String name) {
        return authors.findByNameIgnoreCase(name)
                .orElseGet(() -> authors.save(new Author(name, null)));
    }
}