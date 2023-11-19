package repository;

import lombok.AllArgsConstructor;
import model.Author;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import repository.interfacegenerique.CrudOperations;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@ComponentScan
public class AuthorCrudOperation implements CrudOperations<Author> {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Author> findAll() {
        List<Author> authors = new ArrayList<>();
        String query = "SELECT * FROM author";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                Author.Sex sex = Author.Sex.valueOf(resultSet.getString("sex"));
                authors.add(new Author(id, name, sex));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authors;
    }

    @Override
    public List<Author> saveAll(List<Author> toSave) {
        List<Author> savedAuthors = new ArrayList<>();
        for (Author book : toSave) {
            Author savedAuthor = save(book);
            if (savedAuthor != null) {
                savedAuthors.add(savedAuthor);
            }
        }
        return savedAuthors;
    }

    @Override
    public Author save(Author toSave) {
        String query = "INSERT INTO author (name, sex) VALUES (?, ?)";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, toSave.getName());
            statement.setString(2, toSave.getSex().name());
            statement.executeUpdate();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    toSave.setId(resultSet.getString(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toSave;
    }

    @Override
    public Author delete(Author toDelete) {
        String query = "DELETE FROM author WHERE id = ?";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, toDelete.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toDelete;
    }
}
