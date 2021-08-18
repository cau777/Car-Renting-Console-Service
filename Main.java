package carsharing;

import carsharing.dbservices.CarsService;
import carsharing.dbservices.CompaniesService;
import carsharing.dbservices.CustomerService;

public class Main {
    public static void main(String[] args) {
        String databaseName = "database.db";
        if (args.length >= 2 && "-databaseFileName".equals(args[0])) {
            databaseName = args[1];
        }

        try (DatabaseManager databaseManager = new DatabaseManager(databaseName)) {
            
            CompaniesService companiesManager = new CompaniesService(databaseManager);
            CarsService carsService = new CarsService(databaseManager);
            CustomerService customerService = new CustomerService(databaseManager);

            UserInterface userInterface = new UserInterface(companiesManager, carsService, customerService);
            userInterface.showMainUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}