package kz.iitu.springlab.catalog;

import kz.iitu.springlab.web.Genre;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Order(1)
@Component
public class DataSeeder implements CommandLineRunner {

    private final AuthorRepository authors;
    private final TagRepository tags;

    public DataSeeder(AuthorRepository authors, TagRepository tags) {
        this.authors = authors;
        this.tags = tags;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (authors.count() > 0) return;

        Tag classic = new Tag("classic");
        Tag bestseller = new Tag("bestseller");
        Tag programming = new Tag("programming");
        Tag kazakh = new Tag("kazakh-literature");
        tags.saveAll(List.of(classic, bestseller, programming, kazakh));

        Author bloch = new Author("Joshua Bloch", "USA");
        Author martin = new Author("Robert Martin", "USA");
        Author auezov = new Author("Mukhtar Auezov", "Kazakhstan");

        Book effective = new Book("Effective Java", Genre.NON_FICTION, 2018);
        effective.addTag(programming);
        effective.addTag(bestseller);
        Book puzzlers = new Book("Java Puzzlers", Genre.NON_FICTION, 2005);
        puzzlers.addTag(programming);
        Book concurrency = new Book("Java Concurrency in Practice", Genre.NON_FICTION, 2006);
        concurrency.addTag(programming);
        concurrency.addTag(classic);
        bloch.addBook(effective);
        bloch.addBook(puzzlers);
        bloch.addBook(concurrency);

        Book clean = new Book("Clean Code", Genre.NON_FICTION, 2008);
        clean.addTag(programming);
        clean.addTag(bestseller);
        Book architecture = new Book("Clean Architecture", Genre.NON_FICTION, 2017);
        architecture.addTag(programming);
        martin.addBook(clean);
        martin.addBook(architecture);

        Book abai = new Book("The Path of Abai", Genre.FICTION, 1942);
        abai.addTag(classic);
        abai.addTag(kazakh);
        Book kobylandy = new Book("Kara-Kypshak Kobylandy", Genre.FICTION, 1944);
        kobylandy.addTag(kazakh);
        Book enlik = new Book("Enlik-Kebek", Genre.FICTION, 1917);
        enlik.addTag(kazakh);
        enlik.addTag(classic);
        auezov.addBook(abai);
        auezov.addBook(kobylandy);
        auezov.addBook(enlik);

        // cascade = ALL: авторды сақтағанда кітаптары да сақталады
        authors.saveAll(List.of(bloch, martin, auezov));
    }
}