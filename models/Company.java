package carsharing.models;

public class Company implements HasId{
    private final int id;
    private final String name;

    public Company(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public Company(String name) {
        this.id = 0;
        this.name = name;
    }
    
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
