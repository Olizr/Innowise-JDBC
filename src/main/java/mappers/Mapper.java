package mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Mapper<T> {
    T get(ResultSet resultSet) throws SQLException;
}
