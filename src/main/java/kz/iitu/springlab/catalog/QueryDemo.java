package kz.iitu.springlab.catalog;

import kz.iitu.springlab.web.Genre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;
import java.util.List;
import java.util.function.Supplier;
@Component
@Order(2)
@ConditionalOnProperty(name = "app.demo.queries", havingValue = "true")
public class QueryDemo implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(QueryDemo.class);

    private final BookRepository books;
    private final TransactionTemplate tx;

    public QueryDemo(BookRepository books, TransactionTemplate tx) {
        this.books = books;
        this.tx = tx;
    }

    @Override
    public void run(String... args) {
        step("findByGenre(NON_FICTION)",
                () -> books.findByGenre(Genre.NON_FICTION).size());
        step("findByTitleContainingIgnoreCase(\"java\")",
                () -> books.findByTitleContainingIgnoreCase("java").size());
        step("findByYearBetweenOrderByYearDesc(2000, 2010)",
                () -> books.findByYearBetweenOrderByYearDesc(2000, 2010).size());
        step("findTop3ByGenreOrderByYearDesc(NON_FICTION)",
                () -> books.findTop3ByGenreOrderByYearDesc(Genre.NON_FICTION).size());
        step("countByAuthorName(\"Joshua Bloch\")",
                () -> books.countByAuthorName("Joshua Bloch"));
        step("search(NON_FICTION, 2006) [JPQL]",
                () -> books.search(Genre.NON_FICTION, 2006).size());


        step("N+1 BEFORE: findAll() + getAuthor().getName()", () -> {
            List<Book> all = books.findAll();
            all.forEach(b -> log.info("   {} — {}", b.getTitle(), b.getAuthor().getName()));
            return all.size();
        });


        step("N+1 AFTER: findAllWithAuthor() [join fetch]", () -> {
            List<Book> all = books.findAllWithAuthor();
            all.forEach(b -> log.info("   {} — {}", b.getTitle(), b.getAuthor().getName()));
            return all.size();
        });

        step("archiveOlderThan(1950) [bulk update]",
                () -> books.archiveOlderThan(1950));
    }

    private void step(String name, Supplier<Number> action) {
        log.info("==================== START: {} ====================", name);
        Number rows = tx.execute(status -> action.get());
        log.info("==================== END: {} -> rows = {} ====================", name, rows);
    }
}