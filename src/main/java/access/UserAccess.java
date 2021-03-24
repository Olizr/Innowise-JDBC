package access;

import models.User;
import mappers.Mapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for accessing data using JDBC
 */
public class UserAccess extends Access<User> {
    private final String SQL_INSERT = "INSERT INTO users (username, password, mail) VALUES (?, ?, ?)";
    private final String SQL_GET = "SELECT * FROM users WHERE id = ?";
    private final String SQL_GET_ALL = "SELECT * FROM users";
    private final String SQL_DELETE = "DELETE FROM users WHERE id = ?";
    private final String SQL_UPDATE = "UPDATE users SET username = ?, password = ?, mail = ? WHERE id = ?";
    private final String SQL_GET_BY_ROLE_ID = "SELECT * FROM users WHERE id IN (SELECT user_id FROM user_roles WHERE role_id = ?)";
    private final String SQL_GET_BY_ROLE_NAME = "SELECT * FROM users WHERE id IN (SELECT user_id FROM user_roles WHERE role_id = (SELECT id FROM roles WHERE name = ?))";

    public UserAccess(Connection connection, Mapper<User> mapper) {
        super(connection, mapper);
    }

    /**
     * Searching for user with id
     * @param id User id to find
     * @return User with giving id
     * @throws SQLException
     */
    public User get(int id) throws SQLException {
        return getById(id, SQL_GET);
    }

    /**
     * Takes all user from table
     * @return List of all users in table
     * @throws SQLException
     */
    public List<User> getAll() throws SQLException {
        return getAll(SQL_GET_ALL);
    }

    /**
     * Takes all user from table that's have role with
     * specific id
     * @param id Role id to search
     * @return List of users
     * @throws SQLException
     */
    public List<User> getAllByRoleId(int id) throws SQLException {
        List<User> users = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(SQL_GET_BY_ROLE_ID);
        statement.setInt(1, id);
        statement.execute();
        ResultSet resultSet = statement.getResultSet();

        while (resultSet.next()) {
            users.add(mapper.get(resultSet));
        }

        return users;
    }

    /**
     * Takes all user from table that's have role with
     * specific name
     * @param roleName Name of role to search
     * @return List of users
     * @throws SQLException
     */
    public List<User> getAllByRoleName(String roleName) throws SQLException {
        List<User> users = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(SQL_GET_BY_ROLE_NAME);
        statement.setString(1, roleName);
        statement.execute();
        ResultSet resultSet = statement.getResultSet();

        while (resultSet.next()) {
            users.add(mapper.get(resultSet));
        }

        return users;
    }

    /**
     * Saving user in database
     * @param user User to save
     * @return User with id
     * @throws SQLException
     */
    public User save(User user) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_INSERT,
                Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getMail());

        int affectedRows = statement.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Creating user failed, no rows affected.");
        }

        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt(1));
            }
            else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        }

        return user;
    }

    /**
     * Updating user id database
     * @param user User to update
     * @return True if succeed, false - otherwise
     * @throws SQLException
     */
    public boolean update(User user) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_UPDATE);
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getMail());
        statement.setInt(4, user.getId());

        int affectedRows = statement.executeUpdate();

        return affectedRows > 0;
    }

    /**
     * Deleting user form database
     * @param user User to delete
     * @return  True if succeed, false - otherwise
     * @throws SQLException
     */
    public boolean delete(User user) throws SQLException {
        return delete(user, SQL_DELETE);
    }
}
