//package ru.sfchick.libraryapp.Dao;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.BatchPreparedStatementSetter;
//import org.springframework.jdbc.core.BeanPropertyRowMapper;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;
//import ru.sfchick.libraryapp.Model.Book;
//import ru.sfchick.libraryapp.Model.Person;
//
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Component
//public class PersonDao {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    public PersonDao(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    public List<Person> index() throws SQLException {
//        return jdbcTemplate.query("SELECT * FROM person",
//                new BeanPropertyRowMapper<>(Person.class));
//    }
//
//    public Person show(int id) {
//        return jdbcTemplate.query("SELECT * FROM person WHERE id=?",
//                new Object[]{id}, new BeanPropertyRowMapper<>(Person.class))
//                .stream().findAny().orElse(null);
//    }
//
//    public Optional<Person> show(String fullName) {
//        return jdbcTemplate.query("SELECT * FROM Person WHERE full_name=?",
//                new Object[]{fullName}, new BeanPropertyRowMapper<>(Person.class))
//                .stream().findAny();
//    }
//
//    public void save(Person person) throws SQLException {
//        jdbcTemplate.update("INSERT INTO person (full_name, age) VALUES(?, ?)",
//                person.getFullName(), person.getAge());
//    }
//
//    public void update(int id, Person updatedPerson) {
//        jdbcTemplate.update("UPDATE person SET full_name=?, age=? WHERE id=?",
//                updatedPerson.getFullName(),
//                updatedPerson.getAge(), id);
//
//    }
//
//    public void delete(int id) {
//        jdbcTemplate.update("DELETE FROM person WHERE id=?", id);
//    }
//
//    public Object getBooksByPerson(int id) {
//        return jdbcTemplate.query("SELECT * FROM Book WHERE person_id=?",
//                new Object[]{id}, new BeanPropertyRowMapper<>(Book.class));
//    }
//}
