package main;

import entities.Customer;
import entities.Produs;
import exception.InexistentItemException;
import exception.InsufficientStockException;
import model.WorkInProgress;
import service.ShopStore;
import service.customers.HibernateCustomersService;
import service.producators.HibernateProducatorsService;
import service.producators.ProducatorsService;
import service.produs.HibernateInventoryService;
import service.produs.InventoryService;
import service.reports.ReportsService;
import service.shopping_cart.HibernateShoppingCartService;
import service.shopping_cart.ShoppingCartService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.Scanner;

public class Application {

    public static void main(String[] args) {


        ShopStore shopStore = new ShopStore(new HibernateInventoryService(), new HibernateShoppingCartService(),
                new HibernateProducatorsService(), new HibernateCustomersService(), new ReportsService());
        Scanner scanner = new Scanner(System.in);

        do {
            try {
                System.out.println("***********MAIN MENU***********");
                System.out.println("0. Exit");
                System.out.println("1. Admin");
                System.out.println("2. Client");
                System.out.println("******************************");
                int option = Integer.parseInt(scanner.nextLine());

                switch (option) {
                    case 0:
                        System.exit(0);
                    case 1:
                        boolean backToMainMenu;
                        //admin menu
                        do {
                            displayAdminMenu();

                                    backToMainMenu = processAdminOption(scanner, shopStore.getInventoryService(),shopStore.getReportsService(),shopStore.getProducatorsService());
                            shopStore.getProducatorsService();
                        } while (!backToMainMenu);
                        break;

                    case 2:
                        //client menu
                        System.out.println("What is your customer email?");
                        String email = scanner.nextLine();

                        Optional<Customer> optionalCustomer = shopStore.getCustomersService().get(email);

                        if (!optionalCustomer.isPresent()) {
                            System.out.println("Customer does not exist!!");
                        } else {

                            do {
                                displayClientMenu();
                                backToMainMenu = processClientOption(scanner, optionalCustomer.get(), shopStore.getInventoryService(), shopStore.getShoppingCartService());
                            } while (!backToMainMenu);
                        }
                        break;
                    default:
                        System.out.println("Invalid option. Choose 0-2");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid value for option. Should be a number");
            }

        } while (true);


    }

    private static boolean processClientOption(Scanner scanner, Customer customer, InventoryService produsInventory, ShoppingCartService shoppingCartsService) {
        String title;
        String isbn;

        System.out.println("Input option: ");
        int option = Integer.parseInt(scanner.nextLine());

        try {
            switch (option) {
                case 0:
                    return true;
                case 1:
                    //add to shopping cart
                    System.out.println("Input isbn: ");
                    isbn = scanner.nextLine();
                    //this throws exception does not exist
                    Produs produs = produsInventory.searchByIsbn(isbn);
                    //see if item exists in cart and get its quantity
                    int quantity = shoppingCartsService.getQuantity(customer, produs);
                    //check if there is enough stock
                    produs.checkStock(quantity != 0 ? -(quantity + 1) : -1);

                    //add to cart
                    shoppingCartsService.addToCart(customer, produs);

                    System.out.println("Item successfully added!");

                    break;
                case 2:
                    //remove item
                    System.out.println("Which item?");
                    System.out.println("Input isbn for book you wish to delete from cart.");
                    isbn = scanner.nextLine();

                    //search by isbn
                    //remove

                    System.out.println("Item successfully removed!");

                    break;

                case 3:
                    //update quantity
                    System.out.println("Which item?");
                    shoppingCartsService.displayAll(customer);

                    System.out.println("Input isbn for product you wish to delete from cart.");
                    isbn = scanner.nextLine();

                    System.out.println("Input new quantity");
                    quantity = Integer.parseInt(scanner.nextLine());

                    //search by isbn
                    //check if there is enough stock
                    //update
                    break;

                case 4:
                    //search by title
                    System.out.println("Input title: ");
                    title = scanner.nextLine();

                    System.out.println(produsInventory.searchByTitle(title));
                    break;
                case 5:
                    //display all
                    produsInventory.displayAll();
                    break;
                case 6:
                    //display items in shopping cart
                    shoppingCartsService.displayAll(customer);
                    break;

            }

        } catch (InexistentItemException e) {
            System.out.println("Warning: " + e.getMessage());
            System.out.println("Choose from: ");

        } catch (InsufficientStockException e) {
            System.out.println("Warning: " + e.getMessage());
        }

        return false;
    }


    private static boolean processAdminOption(Scanner scanner, InventoryService produsInventory, ReportsService reportsService, ProducatorsService producatorsService) {
        String title;
        int stock;
        double price;
        try {
            System.out.println("Input option: ");
            int option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 0:
                    return true;
                case 1:
                    //add book
                    System.out.println("Input isbn:");
                    String isbn = scanner.nextLine();

                    System.out.println("Input title: ");
                    title = scanner.nextLine();

                    System.out.println("Input stock:");
                    stock = Integer.parseInt(scanner.nextLine());

                    System.out.println("Input price: ");
                    price = Double.parseDouble(scanner.nextLine());

                    System.out.println("Input publishing date (dd-MM-yyyy)");
                    String dateStr = scanner.nextLine();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    LocalDate localDate = LocalDate.parse(dateStr, formatter);

                    break;

                case 2:
                    //search by title
                    System.out.println("Input title: ");
                    title = scanner.nextLine();

                    System.out.println("Found: " + produsInventory.searchByTitle(title));
                    break;
                case 3:
                    //remove by title
                    System.out.println("Input title: ");
                    title = scanner.nextLine();

                    //search by title
                    //remove it
                    break;

                case 4:
                    System.out.println("Input title: ");
                    title = scanner.nextLine();
                    System.out.println("Input new stock: ");
                    stock = Integer.parseInt(scanner.nextLine());

                    //this also checks if book exists
                    //search by title
                    //set new stock
                    //update it
                    break;
                case 5:
                    System.out.println("Input title: ");
                    title = scanner.nextLine();

                    System.out.println("Input price: ");
                    price = Double.parseDouble(scanner.nextLine());

                    //search by title
                    //set new price
                    //update it
                    break;
                case 6:
                case 7:
                    produsInventory.displayAll();
                    break;

                case 8:
                    //add
                    System.out.println("Input produs isbn: ");
                    isbn = scanner.nextLine();

                    //search by title

                    //ask user to input comma separated author ids

                    //split the string by comma

                    //iterate over ids and build a new list of authors by calling authorsService.get on each id

                    //add all authors to author list of the book

                    //update the book


                    break;
                default:
                    System.out.println("Invalid option");

            }

        } catch (InexistentItemException e) {
            System.out.println("Warning: " + e.getMessage());
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date.");
        }
        return false;
    }

    private static void displayAdminMenu() {

        System.out.println("********ADMIN MENU***********");
        System.out.println("0. Back to main menu");
        System.out.println("1. Add product");
        System.out.println("2. Remove product");
        System.out.println("3. Search by title");
        System.out.println("4. Search by isbn");
        System.out.println("5. Update quantity");
        System.out.println("6. Update price");
        System.out.println("7. Display all");
        System.out.println("8. Generate alphabetical by title report");
        System.out.println("*****************************");
    }


    private static void displayClientMenu() {
        System.out.println("********CLIENT MENU***********");
        System.out.println("0. Back to main menu");
        System.out.println("1. Add to cart");
        System.out.println("2. Remove to cart");
        System.out.println("3. Update quantity");
        System.out.println("4. Display shopping cart items");
        System.out.println("5. Display product from inventory");
        System.out.println("6. Search product by title");
        System.out.println("*****************************");
    }
}
