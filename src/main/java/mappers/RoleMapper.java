package mappers;

import models.Role;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleMapper implements Mapper<Role>{
    public Role get(ResultSet resultSet) throws SQLException {
        Role role = new Role();
        role.setId(resultSet.getInt("id"));
        role.setName(resultSet.getString("name"));
        role.setDescription(resultSet.getString("description"));

        return role;
    }
}
