package mappers;

import models.UserRole;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRolesMapper implements Mapper<UserRole> {
    public UserRole get(ResultSet resultSet) throws SQLException {
        UserRole user = new UserRole();
        user.setUserId(resultSet.getInt("user_id"));
        user.setRoleId(resultSet.getInt("role_id"));

        return user;
    }
}
