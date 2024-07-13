import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Customer {
    private String name;
    private int shopTimes;
    private float totalSpend;
    private ShoppingCart cart;

    public Customer() {
        this.shopTimes = 0;
        this.totalSpend = 0;
        this.cart = null;
    }

    public boolean initCustomer() {
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("Enter customer name");
            this.name = scanner.nextLine().trim();
        } while (this.name.isEmpty());
        return true;
    }

    public void printCustomer() {
        System.out.printf("Name: %s\t", name);
        System.out.printf("has shopped %d times spent: %.2f IS\t", shopTimes, totalSpend);
        if (cart == null) {
            System.out.println("Shopping cart is empty!");
        } else {
            System.out.println("Doing shopping now!!!");
        }
    }

    public void pay() {
        if (cart == null) return;
        System.out.printf("---------- Cart info and bill for %s ----------\n", name);
        float p = cart.printShoppingCart();
        System.out.println("!!! --- Payment was received!!!! --- ");
        shopTimes++;
        totalSpend += p;
        cart = null;
    }

    public boolean isCustomer(String name) {
        return this.name.equals(name);
    }

    public static int compareCustomerByName(Customer c1, Customer c2) {
        return c1.name.compareTo(c2.name);
    }

    public static int compareCustomerByShopTime(Customer c1, Customer c2) {
        return Integer.compare(c1.shopTimes, c2.shopTimes);
    }

    public static int compareCustomerBySpent(Customer c1, Customer c2) {
        return Float.compare(c1.totalSpend, c2.totalSpend);
    }

    public static void saveCustomerToTextFile(Customer[] customerArr, String customersFileName) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(customersFileName))) {
            writer.println(customerArr.length);
            for (Customer customer : customerArr) {
                writer.printf("%s\n%d %.2f\n", customer.name, customer.shopTimes, customer.totalSpend);
            }
        }
    }

    public static List<Customer> loadCustomerFromTextFile(String customersFileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(customersFileName))) {
            int customerCount = Integer.parseInt(reader.readLine());
            Customer[] customerArr = new Customer[customerCount];
            for (int i = 0; i < customerCount; i++) {
                Customer customer = new Customer();
                customer.name = reader.readLine();
                String[] data = reader.readLine().split(" ");
                customer.shopTimes = Integer.parseInt(data[0]);
                customer.totalSpend = Float.parseFloat(data[1]);
                customerArr[i] = customer;
            }
            return new ArrayList<>(Arrays.asList(customerArr));
        }
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public int getShopTimes() {
        return shopTimes;
    }

    public float getTotalSpend() {
        return totalSpend;
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public void setCart(ShoppingCart cart) {
        this.cart = cart;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShopTimes(int shopTimes) {
        this.shopTimes = shopTimes;
    }

    public void setTotalSpend(float totalSpend) {
        this.totalSpend = totalSpend;
    }
}