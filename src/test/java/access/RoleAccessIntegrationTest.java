package access;

import connection.ConnectionManager;
import mappers.RoleMapper;
import mappers.UserRolesMapper;
import models.Role;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class RoleAccessIntegrationTest {
    private Connection connection;
    private RoleAccess roleAccess;
    private UserRolesAccess userRolesAccess;

    @Before
    public void setConnection() throws SQLException {
        connection = ConnectionManager.getConnection();
        roleAccess = new RoleAccess(connection, new RoleMapper());
        userRolesAccess = new UserRolesAccess(connection, new UserRolesMapper());
        TableInit.cleatDatabase(connection);
    }

    @Test
    public void get() throws SQLException{
        TableInit.fillDatabase(connection);
        //Testing get for existing user
        Assert.assertEquals(TableInit.roleUser, roleAccess.get(TableInit.roleUser.getId()));
        //Testing get for not existing user
        Assert.assertNull(roleAccess.get(-1));
    }

    @Test
    public void getAll() throws SQLException{
        TableInit.fillDatabase(connection);
        Assert.assertEquals(2, roleAccess.getAll().size());
    }

    @Test
    public void getAllByUserId() throws SQLException{
        TableInit.fillDatabase(connection);
        //Testing for user with 2 roles
        Assert.assertEquals(2, roleAccess.getAllByUserId(TableInit.users.get(0).getId()).size());
        //Testing for user with 1 role
        Assert.assertEquals(1, roleAccess.getAllByUserId(TableInit.users.get(1).getId()).size());
        //Testing for user with none roles
        Assert.assertEquals(0, roleAccess.getAllByUserId(TableInit.users.get(2).getId()).size());
    }

    @Test
    public void getAllByUserName() throws SQLException{
        TableInit.fillDatabase(connection);
        //Testing for user with 2 roles
        Assert.assertEquals(2, roleAccess.getAllByUserName(TableInit.users.get(0).getUsername()).size());
        //Testing for user with 1 role
        Assert.assertEquals(1, roleAccess.getAllByUserName(TableInit.users.get(1).getUsername()).size());
        //Testing for user with none roles
        Assert.assertEquals(0, roleAccess.getAllByUserName(TableInit.users.get(2).getUsername()).size());
    }

    @Test
    public void save() throws SQLException{
        TableInit.fillDatabase(connection);
        int before = roleAccess.getAll().size();
        Role role = new Role();
        role.setName("name");
        role.setDescription("desc");

        roleAccess.save(role);

        Assert.assertNotEquals(before, roleAccess.getAll().size());
    }

    @Test
    public void update() throws SQLException{
        String newRoleName = "name";
        TableInit.fillDatabase(connection);
        Role role = TableInit.roleUser;
        role.setName(newRoleName);

        roleAccess.update(role);

        Assert.assertEquals(newRoleName, roleAccess.get(role.getId()).getName());
    }

    @Test
    public void delete() throws SQLException{
        TableInit.fillDatabase(connection);
        int before = roleAccess.getAll().size();
        int userRolesBefore = userRolesAccess.getAll().size();

        roleAccess.delete(TableInit.roleUser);

        Assert.assertNotEquals(before, roleAccess.getAll().size());
        //Cascade deletion test
        Assert.assertNotEquals(userRolesBefore, userRolesAccess.getAll().size());
    }
}