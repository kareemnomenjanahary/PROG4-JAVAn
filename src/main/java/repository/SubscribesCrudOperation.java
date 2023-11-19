package repository;

import model.User;
import repository.interfacegenerique.CrudOperations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SubscribesCrudOperation implements CrudOperations<User> {
    private Connection connection;

    public SubscribesCrudOperation(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<User> findAll() {
        List<User> userList = new ArrayList<>();
        try {
            String query = "SELECT * FROM users";
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    User user = mapResultSetToUser(resultSet);
                    userList.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    @Override
    public List<User> saveAll(List<User> toSave) {
        try {
            connection.setAutoCommit(false);
            String query = "INSERT INTO users (id, firstName, lastName, email, ref, status, phone, birthDate, entranceDatetime, sex, address) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                for (User user : toSave) {
                    setParametersForUser(statement, user);
                    statement.addBatch();
                }
                statement.executeBatch();
                connection.commit();
            }
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
        }
        return findAll();
    }

    @Override
    public User save(User toSave) {
        try {
            String query = "INSERT INTO users (id, firstName, lastName, email, ref, status, phone, birthDate, entranceDatetime, sex, address) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                setParametersForUser(statement, toSave);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toSave;
    }

    @Override
    public User delete(User toDelete) {
        try {
            String query = "DELETE FROM users WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, toDelete.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toDelete;
    }

    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getString("id"));
        user.setFirstName(resultSet.getString("firstName"));
        user.setLastName(resultSet.getString("lastName"));
        user.setEmail(resultSet.getString("email"));
        user.setAddress(resultSet.getString("address"));
        user.setPhone(resultSet.getString("phone"));
        user.setSex(User.Sex.valueOf(resultSet.getString("sex")));
        user.setRole(User.Role.valueOf(resultSet.getString("Role")));
        user.setStatus(User.Status.valueOf(resultSet.getString("status")));
        user.setRef(resultSet.getString("ref"));
        user.setBirthDate(resultSet.getDate("birthDate").toLocalDate());
        user.setEntranceDatetime(resultSet.getTimestamp("entranceDatetime").toInstant());
        return user;
    }

    private void setParametersForUser(PreparedStatement statement, User user) throws SQLException {
        statement.setString(1, user.getId());
        statement.setString(2, user.getFirstName());
        statement.setString(3, user.getLastName());
        statement.setString(4, user.getEmail());
        statement.setString(5, user.getRef());
        statement.setString(6, user.getStatus().name());
        statement.setString(7, user.getPhone());
        statement.setDate(8, java.sql.Date.valueOf(user.getBirthDate()));
        statement.setTimestamp(9, java.sql.Timestamp.from(user.getEntranceDatetime()));
        statement.setString(10, user.getSex().name());
        statement.setString(11, user.getAddress());
    }
}
