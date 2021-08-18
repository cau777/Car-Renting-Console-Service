package carsharing.dbservices;

import carsharing.DatabaseManager;
import carsharing.models.Car;

import java.sql.SQLException;
import java.util.List;

public class CarsService extends DatabaseService<Car> {
    public CarsService(DatabaseManager dbManager) throws NoSuchMethodException {
        super(dbManager, Car.class.getConstructor(int.class, String.class, int.class));
        
        try {
            dbManager.createTable("CAR", "ID INT PRIMARY KEY AUTO_INCREMENT", "NAME VARCHAR(50) UNIQUE NOT NULL", "COMPANY_ID INT NOT NULL", "FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID)");
        } catch (SQLException ignored) {
        }
    }
    
    @Override
    public List<Car> getAll() throws SQLException {
        return dbManager.constructObjectsFromSelect("SELECT ID, NAME, COMPANY_ID FROM CAR", constructor);
    }
    
    @Override
    public List<Car> getAllOrdered() throws SQLException {
        return dbManager.constructObjectsFromSelect("SELECT ID, NAME, COMPANY_ID FROM CAR ORDER BY ID", constructor);
    }
    
    @Override
    public void add(Car obj) throws SQLException {
        dbManager.executeUpdateStatement("INSERT INTO CAR(NAME, COMPANY_ID) VALUES ('{0}', {1})", obj.getName(), obj.getCompanyId());
    }
}
