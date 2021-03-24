package access;

import connection.ConnectionManager;
import mappers.UserMapper;
import mappers.UserRolesMapper;
import models.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class UserAccessIntegrationTest {
    private Connection connection;
    private UserAccess userAccess;
    private UserRolesAccess userRolesAccess;

    @Before
    public void setConnection() throws SQLException {
        connection = ConnectionManager.getConnection();
        userAccess = new UserAccess(connection, new UserMapper());
        userRolesAccess = new UserRolesAccess(connection, new UserRolesMapper());
        TableInit.cleatDatabase(connection);
    }

    @Test
    public void get() throws SQLException{
        TableInit.fillDatabase(connection);
        Assert.assertEquals(TableInit.users.get(0), userAccess.get(TableInit.users.get(0).getId()));
        Assert.assertNull(userAccess.get(-1));
    }

    @Test
    public void getAll() throws SQLException{
        TableInit.fillDatabase(connection);
        Assert.assertEquals(3, userAccess.getAll().size());
    }

    @Test
    public void getAllByUserId() throws SQLException{
        TableInit.fillDatabase(connection);
        //Testing for role with 2 users
        Assert.assertEquals(2, userAccess.getAllByRoleId(TableInit.roleUser.getId()).size());
        //Testing for role with 1 user
        Assert.assertEquals(1, userAccess.getAllByRoleId(TableInit.roleAdmin.getId()).size());
        //Testing for role with none users
        Assert.assertEquals(0, userAccess.getAllByRoleId(-1).size());
    }

    @Test
    public void getAllByUserName() throws SQLException{
        TableInit.fillDatabase(connection);
        //Testing for role with 2 users
        Assert.assertEquals(2, userAccess.getAllByRoleName(TableInit.roleUser.getName()).size());
        //Testing for role with 1 user
        Assert.assertEquals(1, userAccess.getAllByRoleName(TableInit.roleAdmin.getName()).size());
        //Testing for role with none users
        Assert.assertEquals(0, userAccess.getAllByRoleName("name").size());
    }

    @Test
    public void save() throws SQLException{
        TableInit.fillDatabase(connection);
        int before = userAccess.getAll().size();
        User user = new User();
        user.setUsername("name");
        user.setPassword("password");
        user.setMail("mail");

        userAccess.save(user);

        Assert.assertNotEquals(before, userAccess.getAll().size());
    }

    @Test
    public void update() throws SQLException{
        String newUserName = "name";
        TableInit.fillDatabase(connection);
        User user = TableInit.users.get(0);
        user.setUsername(newUserName);

        userAccess.update(user);

        Assert.assertEquals(newUserName, userAccess.get(user.getId()).getUsername());
    }

    @Test
    public void delete() throws SQLException{
        TableInit.fillDatabase(connection);
        int before = userAccess.getAll().size();
        int userRolesBefore = userRolesAccess.getAll().size();

        userAccess.delete(TableInit.users.get(0));

        Assert.assertNotEquals(before, userAccess.getAll().size());
        //Cascade deletion test
        Assert.assertNotEquals(userRolesBefore, userRolesAccess.getAll().size());
    }
}
