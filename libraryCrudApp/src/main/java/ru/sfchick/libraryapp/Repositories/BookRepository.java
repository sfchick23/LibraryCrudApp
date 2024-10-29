package ru.sfchick.libraryapp.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    //пагинация
    Page<Book> findAll(Pageable pageable);
    Page<Book> findAllByOrderByYearAsc(Pageable pageable);
    //поиск
    @Query("SELECT b FROM Book b WHERE b.title LIKE :title%")
    List<Book> findByTitleStartingWith(@Param("title") String title);
}
