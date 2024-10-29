package ru.sfchick.libraryapp.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.sfchick.libraryapp.Model.Book;
import ru.sfchick.libraryapp.Model.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByOwner(Person owner);

    @Query("SELECT b.owner FROM Book b WHERE b.id = :id")
    Optional<Person> findOwnerByBookId(@Param("id") int id);
}
