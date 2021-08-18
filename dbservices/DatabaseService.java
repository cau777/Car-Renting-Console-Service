package carsharing.dbservices;

import carsharing.DatabaseManager;
import carsharing.models.HasId;

import java.lang.reflect.Constructor;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public abstract class DatabaseService<T extends HasId> {
    protected final DatabaseManager dbManager;
    protected final Constructor<T> constructor;
    
    public DatabaseService(DatabaseManager dbManager, Constructor<T> constructor) {
        this.dbManager = dbManager;
        this.constructor = constructor;
    }
    
    public Optional<T> findById(int id) throws SQLException {
        return getAll().stream().filter(o -> o.getId() == id).findFirst();
    }
    
    public abstract List<T> getAll() throws SQLException;
    
    public abstract List<T> getAllOrdered() throws SQLException;
    
    public abstract void add(T obj) throws SQLException;
}
