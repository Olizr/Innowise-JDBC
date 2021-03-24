package access;

import models.Role;
import models.User;
import models.UserRole;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TableInit {
    public static List<User> users;
    public static Role roleAdmin;
    public static Role roleUser;

    private static final String clearTableRoles = "DELETE FROM roles";
    private static final String clearTableUser = "DELETE FROM users";
    private static final String clearTableUserRoles = "DELETE FROM user_roles";

    private static final String insertUserInTable = "INSERT INTO users (username, password, mail) VALUES (?, ?, ?)";
    private static final String insertRoleInTable = "INSERT INTO roles (name, description) VALUES (?, ?)";
    private static final String insertUserRole = "INSERT INTO user_roles (user_Id, role_Id) VALUES (?, ?)";

    public static void cleatDatabase(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(clearTableRoles);
        statement.execute();
        statement = connection.prepareStatement(clearTableUser);
        statement.execute();
        statement = connection.prepareStatement(clearTableUserRoles);
        statement.execute();
    }

    public static void fillDatabase(Connection connection) throws SQLException {
        users = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            User user = new User();
            user.setUsername("Username" + i);
            user.setPassword("Password" + i);
            user.setMail("Mail" + i);
            users.add(user);
        }

        for (User u: users) {
            insertUser(connection, u);
        }

        roleAdmin = new Role();
        roleAdmin.setName("Admin");
        roleAdmin.setDescription("Admin");

        roleAdmin = insertRole(connection, roleAdmin);

        roleUser = new Role();
        roleUser.setName("User");
        roleUser.setDescription("User");

        roleUser = insertRole(connection, roleUser);

        //Set two roles to 1-st user
        UserRole userRole = new UserRole();
        userRole.setUserId(users.get(0).getId());
        userRole.setRoleId(roleAdmin.getId());
        insertUserRole(connection, userRole);

        userRole.setRoleId(roleUser.getId());
        insertUserRole(connection, userRole);

        //Set role to 2-nd user
        userRole.setUserId(users.get(1).getId());
        insertUserRole(connection, userRole);
    }

    private static void insertUser(Connection connection, User user) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(insertUserInTable ,
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
    }

    private static Role insertRole(Connection connection, Role role) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(insertRoleInTable,
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

    public static UserRole insertUserRole(Connection connection, UserRole userRole) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(insertUserRole);
        statement.setInt(1, userRole.getUserId());
        statement.setInt(2, userRole.getRoleId());

        int affectedRows = statement.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Creating role failed, no rows affected.");
        }

        return userRole;
    }
}
