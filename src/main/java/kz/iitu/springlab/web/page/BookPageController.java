package kz.iitu.springlab.web.page;

import jakarta.validation.Valid;
import kz.iitu.springlab.catalog.Book;
import kz.iitu.springlab.catalog.BookService;
import kz.iitu.springlab.catalog.DuplicateTitleException;
import kz.iitu.springlab.web.Genre;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

/** HTML pages of the book catalogue (Thymeleaf). The JSON API lives at /api/books. */
@Controller
@RequestMapping("/books")
public class BookPageController {

    private static final DateTimeFormatter CREATED_FORMAT =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").withZone(ZoneId.systemDefault());

    private final BookService service;

    public BookPageController(BookService service) {
        this.service = service;
    }

    @ModelAttribute("genres")
    public Genre[] genres() {
        return Genre.values();
    }

    @ModelAttribute("activePage")
    public String activePage() {
        return "books";
    }

    @GetMapping
    public String list(@RequestParam(required = false) String author, Model model) {
        List<Book> books = service.findAll(author).stream()
                .sorted(Comparator.comparing(Book::id))
                .toList();
        model.addAttribute("books", books);
        model.addAttribute("author", author);
        model.addAttribute("total", service.findAll(null).size());
        return "books/list";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable long id, Model model) {
        Book book = load(id);
        model.addAttribute("book", book);
        model.addAttribute("createdAt", CREATED_FORMAT.format(book.createdAt()));
        return "books/details";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("form", new BookForm());
        model.addAttribute("editing", false);
        model.addAttribute("activePage", "new");
        return "books/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") BookForm form,
                         BindingResult binding,
                         Model model,
                         RedirectAttributes redirect) {
        if (!binding.hasErrors()) {
            try {
                Book saved = service.create(form.toBook());
                redirect.addFlashAttribute("flashKey", "flash.created");
                redirect.addFlashAttribute("flashTitle", saved.title());
                return "redirect:/books";
            } catch (DuplicateTitleException ex) {
                binding.rejectValue("title", "book.duplicate");
            }
        }
        model.addAttribute("editing", false);
        model.addAttribute("activePage", "new");
        return "books/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable long id, Model model) {
        model.addAttribute("form", BookForm.from(load(id)));
        model.addAttribute("bookId", id);
        model.addAttribute("editing", true);
        return "books/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable long id,
                         @Valid @ModelAttribute("form") BookForm form,
                         BindingResult binding,
                         Model model,
                         RedirectAttributes redirect) {
        load(id);
        if (binding.hasErrors()) {
            model.addAttribute("bookId", id);
            model.addAttribute("editing", true);
            return "books/form";
        }
        Book saved = service.update(id, form.toBook());
        redirect.addFlashAttribute("flashKey", "flash.updated");
        redirect.addFlashAttribute("flashTitle", saved.title());
        return "redirect:/books/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable long id, RedirectAttributes redirect) {
        Book book = load(id);
        service.delete(id);
        redirect.addFlashAttribute("flashKey", "flash.deleted");
        redirect.addFlashAttribute("flashTitle", book.title());
        return "redirect:/books";
    }

    private Book load(long id) {
        return service.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No book with id " + id));
    }
}
