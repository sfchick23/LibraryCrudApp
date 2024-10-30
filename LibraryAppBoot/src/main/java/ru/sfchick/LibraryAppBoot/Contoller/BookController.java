package ru.sfchick.LibraryAppBoot.Contoller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.sfchick.LibraryAppBoot.Model.Book;
import ru.sfchick.LibraryAppBoot.Model.Person;
import ru.sfchick.LibraryAppBoot.Services.BookService;
import ru.sfchick.LibraryAppBoot.Services.PeopleService;


import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final PeopleService peopleService;

    @Autowired
    public BookController(BookService bookService, PeopleService peopleService) {
        this.bookService = bookService;
        this.peopleService = peopleService;
    }


    @GetMapping()
    public String books(@RequestParam(required = false, defaultValue = "false") String sort_by_year,
                        @RequestParam(required = false, defaultValue = "0") int page,
                        @RequestParam(required = false, defaultValue = "100") int books_per_page,
                        Model model) throws SQLException {
        List<Book> books;

        //sorted by year
        if ("true".equals(sort_by_year)) {
            books = bookService.findAllSortedByYear();
        } else {
            books = bookService.findAll();
        }
        //pageable
        Pageable pageable = PageRequest.of(page, books_per_page);

        if ("true".equals(sort_by_year)) {
            books = bookService.findAllSortedByYear(pageable);
        } else {
            books = bookService.findAll(pageable);
        }

        model.addAttribute("books", books);
        return "books/index";
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String query, Model model) {
        if (query == null || query.trim().isEmpty()) {
            model.addAttribute("message", "Пожалуйста, введите название книги.");
            return "books/search";
        }

        List<Book> books = bookService.searchByTitle(query);

        if (books.isEmpty()) {
            model.addAttribute("message", "Книг не найдено");
        } else {
            for (Book book : books) {
                Optional<Person> owner = bookService.findOwnerByBookId(book.getId());
                owner.ifPresent(book::setOwner);
            }
            model.addAttribute("books", books);
        }

        return "books/search";
    }


    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model,
                       @ModelAttribute("person") Person person) throws SQLException {
        model.addAttribute("book", bookService.findById(id));

        Optional<Person> bookOwner = bookService.findOwnerByBookId(id);

        if (bookOwner.isPresent()) {
            model.addAttribute("owner", bookOwner.get());
        }else
            model.addAttribute("people", peopleService.findAll());


        return "books/show";
    }


    @GetMapping("/new")
    public String newBook(Model model) {
        model.addAttribute("book", new Book());
        return "books/new";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookService.findById(id));

        return "books/edit";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) throws SQLException {

        if (bindingResult.hasErrors()) {
            return "books/new";
        }

        bookService.save(book);
        return "redirect:/books";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult , @PathVariable("id") int id) {

        if (bindingResult.hasErrors()) {
            return "books/edit";
        }

        bookService.update(id, book);
        return "redirect:/books";
    }


    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        bookService.release(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id,
                         @ModelAttribute("person") Person selectedPerson) {

        bookService.assign(id, selectedPerson);
        return "redirect:/books/" + id;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/books";
    }

}
