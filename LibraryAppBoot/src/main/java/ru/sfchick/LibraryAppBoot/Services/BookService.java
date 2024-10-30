package ru.sfchick.LibraryAppBoot.Services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sfchick.LibraryAppBoot.Model.Book;
import ru.sfchick.LibraryAppBoot.Model.Person;
import ru.sfchick.LibraryAppBoot.Repositories.BookRepository;
import ru.sfchick.LibraryAppBoot.Repositories.PeopleRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private final PeopleRepository peopleRepository;

    @Autowired
    public BookService(BookRepository bookRepository, PeopleRepository peopleRepository) {
        this.bookRepository = bookRepository;
        this.peopleRepository = peopleRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public List<Book> findAllSortedByYear() {
        return bookRepository.findAll(Sort.by("year"));
    }

    public Book findById(int id) {
        return bookRepository.findById(id).orElse(null);
    }

    public List<Book> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).getContent();
    }

    public List<Book> findAllSortedByYear(Pageable pageable) {
        return bookRepository.findAllByOrderByYearAsc(pageable).getContent();
    }

    public Optional<Person> findOwnerByBookId(int id) {
        return bookRepository.findOwnerByBookId(id);
    }

    public List<Book> searchByTitle(String title) {
        return bookRepository.findByTitleStartingWith(title);
    }

    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updateBook) {
        updateBook.setId(id);
        bookRepository.save(updateBook);
    }

    @Transactional
    public void delete(int id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    public void release(int id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        Person person = book.getOwner();
        if (person != null) {
            person.setDateTakeBook(null); // Обнуляем дату при возврате
        }
        book.setOwner(null);

        bookRepository.save(book);
    }

    @Transactional
    public void assign(int id, Person selectedPerson) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        book.setOwner(selectedPerson);

        Person person = peopleRepository.findById(selectedPerson.getId())
                .orElseThrow(() -> new RuntimeException("Person not found"));

        person.setDateTakeBook(new Date());

        bookRepository.save(book);
    }




    public boolean isBookOverdue(Book book) {
        Person owner = book.getOwner();
        if (owner == null || owner.getDateTakeBook() == null) {
            return false;
        }
        long differenceInMillis = new Date().getTime() - owner.getDateTakeBook().getTime();
        long daysDifference = differenceInMillis / (1000 * 60 * 60 * 24);
        return daysDifference > 10;
    }
}
