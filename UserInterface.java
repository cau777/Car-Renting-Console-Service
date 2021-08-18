package carsharing;

import carsharing.dbservices.CarsService;
import carsharing.dbservices.CompaniesService;
import carsharing.dbservices.CustomerService;
import carsharing.models.Car;
import carsharing.models.Company;
import carsharing.models.Customer;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class UserInterface {
    private final CompaniesService companiesService;
    private final CarsService carsService;
    private final CustomerService customerService;
    private final Scanner scanner;
    
    public UserInterface(CompaniesService companiesService, CarsService carsService, CustomerService customerService) {
        this.companiesService = companiesService;
        this.carsService = carsService;
        this.customerService = customerService;
        
        scanner = new Scanner(System.in);
    }
    
    public void showMainUI() {
        int option;
        
        do {
            System.out.println("1. Log in as a manager");
            System.out.println("2. Log in as a customer");
            System.out.println("3. Create a customer");
            System.out.println("0. Exit");
            
            option = readNum();
            
            if (option == 1) {
                showManagerMenu();
            } else if (option == 2) {
                showCostumerList();
            } else if (option == 3) {
                showCreateCostumer();
            }
        } while (option != 0);
    }
    
    private void showCostumerList() {
        try {
            List<Customer> customers = customerService.getAll();
            int totalCompanies = customers.size();
            
            if (totalCompanies == 0) {
                System.out.println("The customer list is empty!");
            } else {
                int option;
                
                do {
                    System.out.println("Customer list:");
                    for (int i = 0; i < totalCompanies; i++) {
                        Customer company = customers.get(i);
                        System.out.printf("%d. %s%n", i + 1, company.getName());
                    }
                    System.out.println("0. Back");
                    
                    option = readNum();
                    if (option >= 1 && option <= totalCompanies) {
                        showCostumerMenu(customers.get(option - 1));
                        break;
                    }
                } while (option != 0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println();
    }
    
    private void showCostumerMenu(Customer customer) {
        int option;
        
        do {
            System.out.println("1. Rent a car");
            System.out.println("2. Return a rented car");
            System.out.println("3. My rented car");
            System.out.println("0. Back");
            
            option = readNum();
            if (option == 1) {
                showCustomerRentCar(customer);
            } else if (option == 2) {
                showCustomerReturnCar(customer);
            } else if (option == 3) {
                showCustomerRentedCar(customer);
            }
            
        } while (option != 0);
    }
    
    private void showCustomerRentedCar(Customer customer) {
        Integer rentedCarId = customer.getRentedCarId();
        if (rentedCarId == null) System.out.println("You didn't rent a car!");
        else {
            try {
                Optional<Car> car = carsService.findById(rentedCarId);
                if (car.isPresent()) {
                    Optional<Company> carCompany = companiesService.findById(car.get().getCompanyId());
                    if (carCompany.isEmpty()) return;
                    System.out.printf("You rented car:%n%s%nCompany:%n%s%n%n", car.get().getName(), carCompany.get().getName());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void showCustomerReturnCar(Customer customer) {
        if (customer.getRentedCarId() == null) {
            System.out.println("You didn't rent a car!");
        } else {
            try {
                customer.setRentedCarId(null);
                customerService.update(customer.getId(), customer);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("You've returned a rented car!");
        }
    }
    
    private void showCustomerRentCar(Customer customer) {
        if (customer.getRentedCarId() == null) {
            Company company = chooseCompany();
            
            if (company != null) {
                Car car = chooseCar(company);
                if (car != null) {
                    try {
                        customer.setRentedCarId(car.getId());
                        customerService.update(customer.getId(), customer);
                        System.out.printf("You rented '%s'%n", car.getName());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            System.out.println("You've already rented a car!");
        }
    }
    
    private void showCreateCostumer() {
        System.out.println("Enter the customer name:");
        String name = readLine();
        try {
            customerService.add(new Customer(name));
            System.out.println("The customer was added!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println();
    }
    
    private void showManagerMenu() {
        int option;
        
        do {
            System.out.println("1. Company list");
            System.out.println("2. Create a company");
            System.out.println("0. Back");
            
            option = readNum();
            
            if (option == 1) {
                Company company = chooseCompany();
                if (company != null) showCompanyMenu(company);
            } else if (option == 2) {
                showCreateCompany();
            }
        } while (option != 0);
    }
    
    private void showCreateCompany() {
        System.out.println("Enter the company name:");
        
        String name = readLine();
        try {
            companiesService.add(new Company(name));
            System.out.println("The company was created!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private Company chooseCompany() {
        try {
            List<Company> companies = companiesService.getAll();
            int totalCompanies = companies.size();
            
            if (totalCompanies == 0) {
                System.out.println("The company list is empty!");
            } else {
                int option;
                
                do {
                    System.out.println("Choose the company:");
                    for (int i = 0; i < totalCompanies; i++) {
                        Company company = companies.get(i);
                        System.out.printf("%d. %s%n", i + 1, company.getName());
                    }
                    System.out.println("0. Back");
                    
                    option = readNum();
                    if (option >= 1 && option <= totalCompanies) {
                        return companies.get(option - 1);
                    }
                } while (option != 0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println();
        return null;
    }
    
    private void showCompanyMenu(Company company) {
        int option;
        
        do {
            System.out.printf("'%s' company%n", company.getName());
            System.out.println("1. Car list");
            System.out.println("2. Create a car");
            System.out.println("0. Back");
            
            option = readNum();
            
            if (option == 1) {
                showCarList(company.getId());
            } else if (option == 2) {
                showCreateCar(company.getId());
            }
        } while (option != 0);
    }
    
    private void showCreateCar(int id) {
        System.out.println("Enter the car name:");
        String name = readLine();
        try {
            carsService.add(new Car(name, id));
            System.out.println("The car was added!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println();
    }
    
    private Car chooseCar(Company company) {
        try {
            Set<Integer> rentedCars = customerService.getAllRentedCars();
            List<Car> cars = carsService.getAllOrdered().stream().filter(o -> o.getCompanyId() == company.getId())
                    .filter(o -> !rentedCars.contains(o.getId())).collect(Collectors.toList());
            
            int totalCars = cars.size();
            
            if (totalCars == 0) {
                System.out.printf("No available cars in the '%s' company%n", company.getName());
            } else {
                int option;
                
                do {
                    System.out.println("Car list:");
                    for (int i = 0; i < totalCars; i++) {
                        Car car = cars.get(i);
                        System.out.printf("%d. %s%n", i + 1, car.getName());
                    }
                    
                    System.out.println("0. Back");
                    
                    option = readNum();
                    if (option >= 1 && option <= cars.size()) {
                        return cars.get(option - 1);
                    }
                } while (option != 0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        System.out.println();
        return null;
    }
    
    private void showCarList(int id) {
        try {
            List<Car> cars = carsService.getAll().stream().filter(o -> o.getCompanyId() == id).collect(Collectors.toList());
            int totalCars = cars.size();
            
            if (totalCars == 0) {
                System.out.println("The car list is empty!");
            } else {
                System.out.println("Car list:");
                for (int i = 0; i < totalCars; i++) {
                    Car car = cars.get(i);
                    System.out.printf("%d. %s%n", i + 1, car.getName());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        System.out.println();
    }
    
    private int readNum() {
        int num = Integer.parseInt(scanner.nextLine());
        System.out.println();
        return num;
    }
    
    private String readLine() {
        String line = scanner.nextLine();
        System.out.println();
        return line;
    }
}
