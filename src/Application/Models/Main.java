//package Application.Models;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Scanner;
//
//public class Main {
//
//    private static final int EXIT = -1;
//    private static final String SUPER_FILE_NAME = "SuperMarket.bin";
//    private static final String COMPRESSED_SUPER_FILE_NAME = "SuperMarket_compress.bin";
//    private static final String CUSTOMER_FILE_NAME = "Customers.txt";
//
//    private enum MenuOption {
//        SHOW_SUPERMARKET(0, "Show SuperMarket"),
//        ADD_PRODUCT(1, "Add Application.Models.Product"),
//        ADD_CUSTOMER(2, "Add Application.Models.Customer"),
//        CUSTOMER_SHOPPING(3, "Application.Models.Customer Shopping"),
//        PRINT_CART(4, "Print Shopping Cart"),
//        CUSTOMER_PAY(5, "Application.Models.Customer Pay"),
//        SORT_CUSTOMERS(6, "Sort Customers"),
//        SEARCH_CUSTOMER(7, "Search a Application.Models.Customer"),
//        PRINT_PRODUCT_BY_TYPE(8, "Print Application.Models.Product By Type");
//
//        private final int value;
//        private final String description;
//
//        MenuOption(int value, String description) {
//            this.value = value;
//            this.description = description;
//        }
//
//        public static MenuOption fromInt(int value) {
//            for (MenuOption option : values()) {
//                if (option.value == value) {
//                    return option;
//                }
//            }
//            throw new IllegalArgumentException("Invalid menu option: " + value);
//        }
//    }
//
//    public static void main(String[] args) {
//        // Check if files exist, if not, create sample files
//        if (!new File(SUPER_FILE_NAME).exists()) {
//            System.out.println("Application.Models.Supermarket file not found. Creating a sample file.");
//            SuperFile.createSampleSupermarketFile(SUPER_FILE_NAME);
//        }
//
//        if (!new File(CUSTOMER_FILE_NAME).exists()) {
//            System.out.println("Customers file not found. Creating a sample file.");
//            try {
//                SuperFile.createSampleCustomersFile(CUSTOMER_FILE_NAME);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        Supermarket market = new Supermarket();
//        if (!market.initSuperMarket(SUPER_FILE_NAME, CUSTOMER_FILE_NAME)) {
//            System.out.println("Error initializing Super Market");
//            return;
//        }
//
//        System.out.println("Application.Models.Supermarket initialized successfully.");
//        market.printSuperMarket();
//
//        int option;
//        boolean stop = false;
//
//        do {
//            option = menu();
//            try {
//                MenuOption menuOption = MenuOption.fromInt(option);
//                switch (menuOption) {
//                    case SHOW_SUPERMARKET:
//                        market.printSuperMarket();
//                        break;
//                    case ADD_PRODUCT:
//                        if (!market.addProduct()) {
//                            System.out.println("Error adding product");
//                        }
//                        break;
//                    case ADD_CUSTOMER:
//                        if (!market.addCustomer()) {
//                            System.out.println("Error adding customer");
//                        }
//                        break;
//                    case CUSTOMER_SHOPPING:
//                        if (!market.doShopping()) {
//                            System.out.println("Error in shopping");
//                        }
//                        break;
//                    case PRINT_CART:
//                        market.doPrintCart();
//                        break;
//                    case CUSTOMER_PAY:
//                        if (!market.doPayment()) {
//                            System.out.println("Error in payment");
//                        }
//                        break;
//                    case SORT_CUSTOMERS:
//                        market.sortCustomers();
//                        break;
//                    case SEARCH_CUSTOMER:
//                        market.findCustomer();
//                        break;
//                    case PRINT_PRODUCT_BY_TYPE:
//                        market.printProductByType();
//                        break;
//                }
//            } catch (IllegalArgumentException e) {
//                if (option == EXIT) {
//                    General.printMessage("Thank", "You", "For", "Shopping", "With", "Us");
//                    stop = true;
//                } else {
//                    System.out.println("Wrong option");
//                }
//            }
//        } while (!stop);
//
//        market.handleCustomerStillShoppingAtExit();
//
//        if (!SuperFile.saveSuperMarketToFile(market, SUPER_FILE_NAME, CUSTOMER_FILE_NAME)) {
//            System.out.println("Error saving supermarket to file");
//        }
//
//        if (!Supermarket.saveSuperMarketToCompressedFile(COMPRESSED_SUPER_FILE_NAME, CUSTOMER_FILE_NAME)) {
//            System.out.println("Error saving supermarket to compressed file");
//        }
//
//        market.freeMarket();
//    }
//
//    private static int menu() {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("\n\nPlease choose one of the following options");
//        for (MenuOption option : MenuOption.values()) {
//            System.out.printf("%d - %s%n", option.value, option.description);
//        }
//        System.out.printf("%d - Quit%n", EXIT);
//        return scanner.nextInt();
//    }
//}