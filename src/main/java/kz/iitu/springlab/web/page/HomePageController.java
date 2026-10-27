package kz.iitu.springlab.web.page;

import kz.iitu.springlab.catalog.Book;
import kz.iitu.springlab.catalog.BookService;
import kz.iitu.springlab.web.HelloController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;

/** Home page, the "tools" page (hello + word count) and the "about" page. */
@Controller
public class HomePageController {

    private final BookService books;
    private final HelloController hello;

    public HomePageController(BookService books, HelloController hello) {
        this.books = books;
        this.hello = hello;
    }

    @GetMapping({"/", "/index", "/home"})
    public String home(Model model) {
        var all = books.findAll(null);
        model.addAttribute("activePage", "home");
        model.addAttribute("bookCount", all.size());
        model.addAttribute("authorCount", all.stream().map(Book::author).distinct().count());
        model.addAttribute("genreCount", all.stream().map(Book::genre).distinct().count());
        model.addAttribute("latest", all.stream()
                .sorted(Comparator.comparing(Book::id).reversed())
                .limit(3).toList());
        return "index";
    }

    @GetMapping("/tools")
    public String tools(@RequestParam(required = false) String name,
                        @RequestParam(required = false) String text,
                        Model model) {
        model.addAttribute("activePage", "tools");
        model.addAttribute("name", name);
        model.addAttribute("text", text);
        if (name != null) {
            model.addAttribute("greeting", hello.hello(name.isBlank() ? "world" : name));
        }
        if (text != null) {
            model.addAttribute("wordCount", hello.wordCount(text));
        }
        return "tools";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("activePage", "about");
        model.addAttribute("info", hello.info());
        return "about";
    }
}
