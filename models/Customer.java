package carsharing.models;

public class Customer implements HasId {
    private final int id;
    private final String name;
    
    private Integer rentedCarId;
    
    public Customer(int id, String name, Integer rentedCarId) {
        this.id = id;
        this.name = name;
        this.rentedCarId = rentedCarId;
    }
    
    public Customer(String name) {
        this.id = 0;
        this.name = name;
        this.rentedCarId = null;
    }
    
    public Customer(String name, Integer rentedCarId) {
        this.id = 0;
        this.name = name;
        this.rentedCarId = rentedCarId;
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public Integer getRentedCarId() {
        return rentedCarId;
    }
    
    public void setRentedCarId(Integer rentedCarId) {
        this.rentedCarId = rentedCarId;
    }
}
