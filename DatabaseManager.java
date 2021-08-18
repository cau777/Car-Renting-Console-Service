package carsharing;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager implements AutoCloseable {
    public static final String DATABASE_DIRECTORY_PATH = "src/carsharing/db/";
    public static final String DATABASE_URL_START = "jdbc:h2:";
    
    private final Connection connection;
    
    public DatabaseManager(String name) throws IOException, SQLException {
        String filePath = DATABASE_DIRECTORY_PATH + name;
        
        File databaseDirectory = new File(DATABASE_DIRECTORY_PATH);
        boolean directoryCreated = databaseDirectory.mkdirs();
        
        File databaseFile = new File(filePath);
        boolean fileCreated = databaseFile.createNewFile();
        
        connection = DriverManager.getConnection(DATABASE_URL_START + "./" + filePath);
        connection.setAutoCommit(true);
    }
    
    public void createTable(String name, String... cols) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder("CREATE TABLE ").append(name).append("(");
        for (int i = 0; i < cols.length; i++) {
            sqlBuilder.append(cols[i]);
            if (i != cols.length - 1) sqlBuilder.append(", ");
        }
        sqlBuilder.append(")");
        
        executeUpdateStatement(sqlBuilder.toString());
    }
    
    // Wanted to try java reflection
    public <T> List<T> constructObjectsFromSelect(String sql, Constructor<T> constructor) throws SQLException {
        List<T> results = new ArrayList<>();
        
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            int parametersCount = constructor.getParameterCount();
            
            while (resultSet.next()) {
                Object[] parameters = new Object[parametersCount];
                
                for (int i = 0; i < parametersCount; i++) {
                    Class<?> type = parameterTypes[i];
                    
                    if (type.equals(int.class)) parameters[i] = resultSet.getInt(i + 1);
                    else if (type.equals(byte.class)) parameters[i] = resultSet.getByte(i + 1);
                    else if (type.equals(short.class)) parameters[i] = resultSet.getShort(i + 1);
                    else if (type.equals(long.class)) parameters[i] = resultSet.getLong(i + 1);
                    else if (type.equals(float.class)) parameters[i] = resultSet.getFloat(i + 1);
                    else if (type.equals(double.class)) parameters[i] = resultSet.getDouble(i + 1);
                    else if (type.equals(boolean.class)) parameters[i] = resultSet.getBoolean(i + 1);
                    else parameters[i] = resultSet.getObject(i + 1, type);
                }
                
                results.add(constructor.newInstance(parameters));
            }
            
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        
        return results;
    }
    
    public void executeUpdateStatement(String sql) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }
    
    public void executeUpdateStatement(String sql, Object... args) throws SQLException {
        for (int i = 0; i < args.length; i++) {
            sql = sql.replace("{" + i + "}", args[i] == null ? "NULL" : args[i].toString());
        }
        
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }
    
    @Override
    public void close() throws SQLException {
        connection.close();
    }
}
