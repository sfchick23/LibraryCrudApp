package ru.sfchick.libraryapp.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sfchick.libraryapp.Model.Book;
import ru.sfchick.libraryapp.Model.Person;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    Optional<Person> findByOwner(int id);
}
