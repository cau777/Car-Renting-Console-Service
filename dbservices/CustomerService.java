package carsharing.dbservices;

import carsharing.DatabaseManager;
import carsharing.models.Customer;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomerService extends DatabaseService<Customer> {
    public CustomerService(DatabaseManager dbManager) throws NoSuchMethodException {
        super(dbManager, Customer.class.getConstructor(int.class, String.class, Integer.class));
        
        try {
            dbManager.createTable("CUSTOMER", "ID INT PRIMARY KEY AUTO_INCREMENT", "NAME VARCHAR(50) UNIQUE NOT NULL", "RENTED_CAR_ID INT", "FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID)");
        } catch (SQLException ignored) {
        
        }
    }
    
    @Override
    public List<Customer> getAll() throws SQLException {
        return dbManager.constructObjectsFromSelect("SELECT ID, NAME, RENTED_CAR_ID FROM CUSTOMER", constructor);
    }
    
    @Override
    public List<Customer> getAllOrdered() throws SQLException {
        return dbManager.constructObjectsFromSelect("SELECT ID, NAME, RENTED_CAR_ID FROM CUSTOMER ORDER BY ID", constructor);
    }
    
    @Override
    public void add(Customer obj) throws SQLException {
        dbManager.executeUpdateStatement("INSERT INTO CUSTOMER(NAME, RENTED_CAR_ID) VALUES ('{0}', {1})", obj.getName(), obj.getRentedCarId());
    }
    
    public void update(int id, Customer newCustomer) throws SQLException {
        dbManager.executeUpdateStatement("UPDATE CUSTOMER SET NAME='{1}', RENTED_CAR_ID={2} WHERE ID={0}", id, newCustomer.getName(), newCustomer.getRentedCarId());
    }
    
    public Set<Integer> getAllRentedCars() {
        try {
            return getAll().stream().map(Customer::getRentedCarId).filter(Objects::nonNull).collect(Collectors.toSet());
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptySet();
        }
    }
}
