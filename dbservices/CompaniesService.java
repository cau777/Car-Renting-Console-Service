package carsharing.dbservices;

import carsharing.DatabaseManager;
import carsharing.models.Company;

import java.sql.SQLException;
import java.util.List;

public class CompaniesService extends DatabaseService<Company> {
    public CompaniesService(DatabaseManager dbManager) throws NoSuchMethodException {
        super(dbManager, Company.class.getConstructor(int.class, String.class));
        
        try {
            dbManager.createTable("COMPANY", "ID INT PRIMARY KEY AUTO_INCREMENT", "NAME VARCHAR(50) UNIQUE NOT NULL");
        } catch (SQLException ignored) {
        }
    }
    
    public List<Company> getAll() throws SQLException {
        return dbManager.constructObjectsFromSelect("SELECT ID, NAME FROM COMPANY", constructor);
    }
    
    public List<Company> getAllOrdered() throws SQLException {
        return dbManager.constructObjectsFromSelect("SELECT ID, NAME FROM COMPANY ORDER BY ID", constructor);
    }
    
    public void add(Company obj) throws SQLException {
        dbManager.executeUpdateStatement("INSERT INTO COMPANY (NAME) VALUES ('{0}')", obj.getName());
    }
}
