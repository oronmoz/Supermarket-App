package Application.Models;

import java.io.*;
import java.util.Scanner;

public class Product {
    public enum ProductType {
        FRUIT_VEGETABLE("Fruit Vegetable"),
        FRIDGE("Fridge"),
        FROZEN("Frozen"),
        SHELF("Shelf");

        private final String displayName;

        ProductType(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    private static final int NAME_LENGTH = 20;
    private static final int BARCODE_LENGTH = 7;
    private static final int MIN_DIG = 3;
    private static final int MAX_DIG = 5;

    private String name;
    private String barcode;
    private ProductType type;
    private float price;
    private int count;

    public Product() {}

    public Product(String barcode) {
        this.barcode = barcode;
    }

    public Product(Product other) {
        this.name = other.name;
        this.barcode = other.barcode;
        this.type = other.type;
        this.price = other.price;
        this.count = other.count;
    }

    // New constructor
    public Product(String name, String barcode, ProductType type, float price, int count) {
        this.name = name;
        this.barcode = barcode;
        this.type = type;
        this.price = price;
        this.count = count;
    }

    public void initProduct() {
        initProductNoBarcode();
        this.barcode = getBarcodeCode();
    }

    public void initProductNoBarcode() {
        initProductName();
        this.type = getProductType();
        this.price = getPositiveFloat("Enter product price\t");
        this.count = getPositiveInt("Enter product number of items\t");
    }

    private void initProductName() {
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.printf("Enter product name up to %d chars\n", NAME_LENGTH);
            this.name = scanner.nextLine().trim();
        } while (this.name.isEmpty());
    }

    public void printProduct() {
        System.out.printf("%-20s %-10s\t", name, barcode);
        System.out.printf("%-20s %5.2f %10d\n", type, price, count);
    }

    public void saveProductToFile(PrintWriter out) {
        out.println(name);
        out.println(barcode);
        out.println(type.ordinal());
        out.println(price);
        out.println(count);
    }

    public static Product loadProductFromFile(BufferedReader in) throws IOException {
        Product product = new Product();
        product.name = in.readLine();
        product.barcode = in.readLine();
        product.type = ProductType.values()[Integer.parseInt(in.readLine())];
        product.price = Float.parseFloat(in.readLine());
        product.count = Integer.parseInt(in.readLine());
        return product;
    }

    public static String getBarcodeCode() {
        Scanner scanner = new Scanner(System.in);
        String code;
        boolean ok;
        String msg = String.format("Code should be of %d length exactly\n" +
                        "UPPER CASE letter and digits\n" +
                        "Must have %d to %d digits\n" +
                        "First and last chars must be UPPER CASE letter\n" +
                        "For example A12B40C\n",
                BARCODE_LENGTH, MIN_DIG, MAX_DIG);

        do {
            System.out.print("Enter product barcode ");
            code = scanner.nextLine().trim();
            ok = validateBarcode(code);
            if (!ok) {
                System.out.println(msg);
            }
        } while (!ok);

        return code;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public static boolean validateBarcode(String code) {
        if (code.length() != BARCODE_LENGTH) return false;
        if (!Character.isUpperCase(code.charAt(0)) || !Character.isUpperCase(code.charAt(BARCODE_LENGTH - 1))) return false;

        int digCount = 0;
        for (int i = 1; i < BARCODE_LENGTH - 1; i++) {
            char c = code.charAt(i);
            if (!Character.isUpperCase(c) && !Character.isDigit(c)) return false;
            if (Character.isDigit(c)) digCount++;
        }

        return digCount >= MIN_DIG && digCount <= MAX_DIG;
    }

    public static ProductType getProductType() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nPlease enter one of the following types");
        for (ProductType type : ProductType.values()) {
            System.out.printf("%d for %s\n", type.ordinal(), type);
        }
        int option;
        do {
            option = scanner.nextInt();
        } while (option < 0 || option >= ProductType.values().length);
        return ProductType.values()[option];
    }

    private float getPositiveFloat(String prompt) {
        Scanner scanner = new Scanner(System.in);
        float value;
        do {
            System.out.print(prompt);
            value = scanner.nextFloat();
        } while (value < 0);
        return value;
    }

    private int getPositiveInt(String prompt) {
        Scanner scanner = new Scanner(System.in);
        int value;
        do {
            System.out.print(prompt);
            value = scanner.nextInt();
        } while (value < 0);
        return value;
    }

    public boolean isProduct(String barcode) {
        return this.barcode.equals(barcode);
    }

    public static int compareProductByBarcode(Product p1, Product p2) {
        return p1.barcode.compareTo(p2.barcode);
    }

    public void updateProductCount() {
        Scanner scanner = new Scanner(System.in);
        int count;
        do {
            System.out.print("How many items to add to stock?");
            count = scanner.nextInt();
        } while (count < 1);
        this.count += count;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public String getBarcode() {
        return barcode;
    }

    public ProductType getType() {
        return type;
    }

    public float getPrice() {
        return price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}