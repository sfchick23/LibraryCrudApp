package ru.sfchick.libraryapp.Contoller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.sfchick.libraryapp.Model.Book;
import ru.sfchick.libraryapp.Model.Person;
import ru.sfchick.libraryapp.Services.BookService;
import ru.sfchick.libraryapp.Services.PeopleService;

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
    public String books(@RequestParam(required = false, defaultValue = "false") String sort_by_year, Model model) throws SQLException {
        List<Book> books;

        if ("true".equals(sort_by_year)) {
            books = bookService.findAllSortedByYear();
        } else {
            books = bookService.findAll();
        }

        model.addAttribute("books", books);
        return "books/index";
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
