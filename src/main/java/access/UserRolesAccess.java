package access;

import models.UserRole;
import mappers.Mapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRolesAccess {
    protected final Connection connection;
    protected final Mapper<UserRole> mapper;

    private final String SQL_INSERT = "INSERT INTO user_roles (userId, roleId) VALUES (?, ?)";
    private final String SQL_GET_BY_USER_AND_ROLE_IDS = "SELECT * FROM user_roles WHERE userId = ? and roleId = ?";
    private final String SQL_GET_ALL = "SELECT * FROM user_roles";
    private final String SQL_GET_ALL_BY_USER_ID = "SELECT * FROM user_roles WHERE userId = ?";
    private final String SQL_GET_ALL_BY_ROLE_ID = "SELECT * FROM user_roles WHERE roleId = ?";
    private final String SQL_DELETE = "DELETE FROM user_roles WHERE roleId = ? and userId = ?";

    public UserRolesAccess(Connection connection, Mapper<UserRole> mapper) {
        this.connection = connection;
        this.mapper = mapper;
    }

    /**
     * Searching for user role using specific user and role
     * @param userId User id to find
     * @param roleId Role id to find
     * @return User role with giving id's
     * @throws SQLException
     */
    protected UserRole getByUserIdAndRoleId(int userId, int roleId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_GET_BY_USER_AND_ROLE_IDS);
        statement.setInt(1, userId);
        statement.setInt(2, roleId);
        statement.execute();
        ResultSet resultSet = statement.getResultSet();
        resultSet.next();
        return mapper.get(resultSet);
    }

    /**
     * Saving role in database
     * @param userRole user role to save
     * @return user role
     * @throws SQLException
     */
    public UserRole save(UserRole userRole) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_INSERT);
        statement.setInt(1, userRole.getUserId());
        statement.setInt(2, userRole.getRoleId());

        int affectedRows = statement.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Creating role failed, no rows affected.");
        }

        return userRole;
    }

    /**
     * Take all user roles from table
     * @return List of all user role in table
     * @throws SQLException
     */
    public List<UserRole> getAll() throws SQLException {
        List<UserRole> users = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(SQL_GET_ALL);
        statement.execute();
        ResultSet resultSet = statement.getResultSet();

        while (resultSet.next()) {
            users.add(mapper.get(resultSet));
        }

        return users;
    }

    /**
     * Take all user roles from table that's matches giving userId
     * @param userId User id to find
     * @return List of user roles in table
     * @throws SQLException
     */
    public List<UserRole> getAllByUserId(int userId) throws SQLException {
        List<UserRole> users = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(SQL_GET_ALL_BY_USER_ID);
        statement.setInt(1, userId);
        statement.execute();
        ResultSet resultSet = statement.getResultSet();

        while (resultSet.next()) {
            users.add(mapper.get(resultSet));
        }

        return users;
    }

    /**
     * Take all user roles from table that's matches giving roleId
     * @param roleId Role id to find
     * @return List of user roles in table
     * @throws SQLException
     */
    public List<UserRole> getAllByRoleId(int roleId) throws SQLException {
        List<UserRole> users = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(SQL_GET_ALL_BY_ROLE_ID);
        statement.setInt(1, roleId);
        statement.execute();
        ResultSet resultSet = statement.getResultSet();

        while (resultSet.next()) {
            users.add(mapper.get(resultSet));
        }

        return users;
    }

    /**
     * Deleting userRole form database
     * @param userRole Entity to delete
     * @return True if succeed, false - otherwise
     * @throws SQLException
     */
    public boolean delete(UserRole userRole) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_DELETE);
        statement.setInt(1, userRole.getUserId());
        statement.setInt(2, userRole.getRoleId());

        int affectedRows = statement.executeUpdate();

        return affectedRows > 0;
    }
}
