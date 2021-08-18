package carsharing.models;

public class Car implements HasId{
    private final int id;
    private final String name;
    private final int companyId;
    
    public Car(int id, String name, int companyId) {
        this.id = id;
        this.name = name;
        this.companyId = companyId;
    }
    
    public Car(String name, int companyId) {
        this.id = 0;
        this.name = name;
        this.companyId = companyId;
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public int getCompanyId() {
        return companyId;
    }
}
