//package ru.sfchick.libraryapp.Dao;
//
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.BeanPropertyRowMapper;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.stereotype.Component;
//import ru.sfchick.libraryapp.Model.Book;
//import ru.sfchick.libraryapp.Model.Person;
//
//import java.sql.SQLException;
//import java.util.List;
//import java.util.Optional;
//
//@Component
//public class BookDao {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    public BookDao(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//
//    public List<Book> index() throws SQLException {
//        return jdbcTemplate.query("SELECT * FROM Book",
//                new BeanPropertyRowMapper<Book>(Book.class));
//    }
//
//    public void save(Book book) {
//        jdbcTemplate.update("INSERT INTO Book (title, author, year) VALUES (?, ?, ?)",
//                book.getTitle(), book.getAuthor(), book.getYear());
//    }
//
//    public Book show(int id) {
//        return jdbcTemplate.query("SELECT * FROM Book WHERE id =?",
//                new Object[] {id}, new BeanPropertyRowMapper<>(Book.class))
//                .stream().findAny().orElse(null);
//    }
//
//    public void update(int id, Book updateBook) {
//        jdbcTemplate.update("UPDATE Book SET title=?, author=?, year=? where id=?",
//                updateBook.getTitle(), updateBook.getAuthor(), updateBook.getYear(), id);
//    }
//
//    public void delete(int id) {
//        jdbcTemplate.update("DELETE FROM Book WHERE id=?", id);
//    }
//
//
//    public Optional<Person> getBookOwner(int id) {
//        return jdbcTemplate.query("SELECT Person.* FROM Book JOIN Person ON Book.person_id = Person.id WHERE Book.id=?",
//                new Object[] {id}, new BeanPropertyRowMapper<>(Person.class))
//                .stream().findAny();
//    }
//
//    public void release(int id) {
//        jdbcTemplate.update("UPDATE Book SET person_id=NULL WHERE id=?", id);
//    }
//
//    public void assign(int id, Person selectedPerson) {
//        jdbcTemplate.update("UPDATE Book SET person_id=? WHERE id=?",
//                selectedPerson.getId(), id);
//    }
//
//
//}
