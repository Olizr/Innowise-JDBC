package access;

import models.Entity;
import mappers.Mapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class Access<T extends Entity> {
    protected final Connection connection;
    protected final Mapper<T> mapper;

    public Access(Connection connection, Mapper<T> mapper) {
        this.connection = connection;
        this.mapper = mapper;
    }

    public abstract T get(int id) throws SQLException;
    public abstract List<T> getAll() throws SQLException;
    public abstract T save(T user) throws SQLException;
    public abstract boolean update(T user) throws SQLException;
    public abstract boolean delete(T user) throws SQLException;

    /**
     * Searching for entity with id
     * @param id Entity id to find
     * @param sql String sql query to specify table
     * @return Entity with giving id
     * @throws SQLException
     */
    protected T getById(int id, String sql) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        statement.execute();
        ResultSet resultSet = statement.getResultSet();
        if (resultSet.next()) {
            return mapper.get(resultSet);
        }
        else {
            return null;
        }
    }

    /**
     * Takes all entities from table
     * @param sql String sql query to specify table
     * @return List of all entities in table
     * @throws SQLException
     */
    public List<T> getAll(String sql) throws SQLException {
        List<T> users = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.execute();
        ResultSet resultSet = statement.getResultSet();

        while (resultSet.next()) {
            users.add(mapper.get(resultSet));
        }

        return users;
    }

    /**
     * Deleting entity form database in specific table
     * @param entity Entity to delete
     * @param sql String sql query to specify table
     * @return True if succeed, false - otherwise
     * @throws SQLException
     */
    public boolean delete(T entity, String sql) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, entity.getId());

        int affectedRows = statement.executeUpdate();

        return affectedRows > 0;
    }
}
