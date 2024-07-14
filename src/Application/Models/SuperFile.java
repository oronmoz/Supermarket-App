package Application.Models;

import java.io.*;

public class SuperFile {

    public static void createSampleSupermarketFile(String fileName) {
        try (PrintWriter out = new PrintWriter(new FileWriter(fileName))) {
            // Write supermarket name
            out.println("Sample Application.Models.Supermarket");

            // Write address
            out.println("123"); // house number
            out.println("Application.Models.Main Street");
            out.println("Metropolis");

            // Write number of products
            out.println("5");

            // Write products
            writeProduct(out, "Milk", "A12C34B", 1, 6.7f, 15);
            writeProduct(out, "Bread", "F2AD91G", 3, 3.5f, 10);
            writeProduct(out, "Tomato", "G997G3H", 0, 4.5f, 30);
            writeProduct(out, "Ice Cream", "YU43G9H", 2, 17.9f, 5);
            writeProduct(out, "Shampoo", "E3R562Z", 3, 22.3f, 17);

        } catch (IOException e) {
            System.out.println("Error creating sample supermarket file: " + e.getMessage());
        }
    }

    private static void writeProduct(PrintWriter out, String name, String barcode, int type, float price, int count) {
        out.println(name);
        out.println(barcode);
        out.println(type);
        out.println(price);
        out.println(count);
    }

    // In Application.Models.SuperFile.java
    public static boolean saveSuperMarketToFile(Supermarket market, String fileName, String customersFileName) {
        try (PrintWriter out = new PrintWriter(new FileWriter(fileName))) {
            market.saveToFile(out);
            return true;
        } catch (IOException e) {
            System.out.println("Error saving supermarket to file: " + e.getMessage());
            return false;
        }
    }

    public static boolean loadSuperMarketFromFile(Supermarket market, String fileName, String customersFileName) {
        try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
            Supermarket loadedMarket = Supermarket.loadFromFile(in);
            market.copyFrom(loadedMarket);
            market.setCustomerArr(Customer.loadCustomerFromTextFile(customersFileName));
            return true;
        } catch (IOException e) {
            System.out.println("Error loading supermarket from file: " + e.getMessage());
            return false;
        }
    }

    protected static void createSampleCustomersFile(String fileName) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("5");
            writer.println("Shalom Ha Ari");
            writer.println("1 45.70");
            writer.println("Tamar Choen");
            writer.println("6 902.47");
            writer.println("Moshe Ben Daviv");
            writer.println("3 1230.60");
            writer.println("Micah Avi Levi");
            writer.println("5 1230.90");
            writer.println("Yaffa Beri");
            writer.println("7 2340.90");
        }
    }

    private static Supermarket createSampleSupermarket() {
        Supermarket market = new Supermarket();
        market.setName("Sample Application.Models.Supermarket");
        market.setLocation(new Address(123, "Application.Models.Main Street", "Metropolis"));

        market.getProductList().insert(new Product("Milk", "A12C34B", Product.ProductType.FRIDGE, 6.7f, 15));
        market.getProductList().insert(new Product("Bread", "F2AD91G", Product.ProductType.SHELF, 3.5f, 10));
        market.getProductList().insert(new Product("Tomato", "G997G3H", Product.ProductType.FRUIT_VEGETABLE, 4.5f, 30));

        return market;
    }}