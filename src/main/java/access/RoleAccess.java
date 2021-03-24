package access;

import models.Role;
import mappers.Mapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for accessing data using JDBC
 */
public class RoleAccess extends Access<Role> {
    private final String SQL_INSERT = "INSERT INTO roles (name, description) VALUES (?, ?)";
    private final String SQL_GET = "SELECT * FROM roles WHERE id = ?";
    private final String SQL_GET_ALL = "SELECT * FROM roles";
    private final String SQL_DELETE = "DELETE FROM roles WHERE id = ?";
    private final String SQL_UPDATE = "UPDATE roles SET name = ?, description = ? WHERE id = ?";
    private final String SQL_GET_BY_USER_ID = "SELECT * FROM roles WHERE id IN (SELECT role_id FROM user_roles WHERE user_id = ?)";
    private final String SQL_GET_BY_USER_NAME = "SELECT * FROM roles WHERE id IN (SELECT role_id FROM user_roles WHERE user_id = (SELECT id FROM users WHERE username = ?))";

    public RoleAccess(Connection connection, Mapper<Role> mapper) {
        super(connection, mapper);
    }

    /**
     * Searching for role with id
     * @param id Role id to find
     * @return Role with giving id
     * @throws SQLException
     */
    public Role get(int id) throws SQLException {
        return getById(id, SQL_GET);
    }

    /**
     * Takes all role from table
     * @return List of all roles in table
     * @throws SQLException
     */
    public List<Role> getAll() throws SQLException {
        return getAll(SQL_GET_ALL);
    }

    /**
     * Takes all role from table that's have role with
     * specific id
     * @param id Role id to search
     * @return List of roles
     * @throws SQLException
     */
    public List<Role> getAllByUserId(int id) throws SQLException {
        List<Role> roles = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(SQL_GET_BY_USER_ID);
        statement.setInt(1, id);
        statement.execute();
        ResultSet resultSet = statement.getResultSet();

        while (resultSet.next()) {
            roles.add(mapper.get(resultSet));
        }

        return roles;
    }

    /**
     * Takes all role from table that's have role with
     * specific name
     * @param userName Name of role to search
     * @return List of roles
     * @throws SQLException
     */
    public List<Role> getAllByUserName(String userName) throws SQLException {
        List<Role> roles = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(SQL_GET_BY_USER_NAME);
        statement.setString(1, userName);
        statement.execute();
        ResultSet resultSet = statement.getResultSet();

        while (resultSet.next()) {
            roles.add(mapper.get(resultSet));
        }

        return roles;
    }

    /**
     * Saving role in database
     * @param role Role to save
     * @return Role with id
     * @throws SQLException
     */
    public Role save(Role role) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_INSERT,
                Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, role.getName());
        statement.setString(2, role.getDescription());

        int affectedRows = statement.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Creating role failed, no rows affected.");
        }

        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                role.setId(generatedKeys.getInt(1));
            }
            else {
                throw new SQLException("Creating role failed, no ID obtained.");
            }
        }

        return role;
    }

    /**
     * Updating role id database
     * @param role Role to update
     * @return True if succeed, false - otherwise
     * @throws SQLException
     */
    public boolean update(Role role) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_UPDATE);
        statement.setString(1, role.getName());
        statement.setString(2, role.getDescription());
        statement.setInt(3, role.getId());

        int affectedRows = statement.executeUpdate();

        return affectedRows > 0;
    }

    /**
     * Deleting role form database
     * @param role Role to delete
     * @return  True if succeed, false - otherwise
     * @throws SQLException
     */
    public boolean delete(Role role) throws SQLException {
        return delete(role, SQL_DELETE);
    }
}
