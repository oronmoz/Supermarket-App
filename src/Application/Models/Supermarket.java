package Application.Models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.function.BiPredicate;

public class Supermarket {
    private String name;
    private Address location;
    private List<Customer> customerArr;
    private GeneralList<Product> productList;
    private SortOption sortOpt;

    public enum SortOption {
        NONE, NAME, TIME, SPEND
    }

    public Supermarket() {
        this.customerArr = new ArrayList<>();
        this.productList = new GeneralList<>();
        this.sortOpt = SortOption.NONE;
    }

    public boolean initSuperMarket(String fileName, String customersFileName) {
        this.customerArr = new ArrayList<>();
        this.productList = new GeneralList<>();
        this.sortOpt = SortOption.NONE;

        if (SuperFile.loadSuperMarketFromFile(this, fileName, customersFileName)) {
            System.out.println("Application.Models.Supermarket successfully loaded from files");
            return true;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter market name");
        this.name = scanner.nextLine();
        this.location = new Address();
        return this.location.initAddress();
    }

    public void saveToFile(PrintWriter out) throws IOException {
        out.println(name);
        location.saveAddressToFile(out);
        out.println(productList.size());
        productList.forEach(product -> {
            product.saveProductToFile(out);
        });
    }

    public static Supermarket loadFromFile(BufferedReader in) throws IOException {
        Supermarket market = new Supermarket();
        market.name = in.readLine();
        market.location = Address.loadAddressFromFile(in);
        int productCount = Integer.parseInt(in.readLine());
        for (int i = 0; i < productCount; i++) {
            Product product = Product.loadProductFromFile(in);
            market.productList.insert(product);
        }
        return market;
    }

    public void copyFrom(Supermarket other) {
        this.name = other.name;
        this.location = other.location;
        this.productList = other.productList;
        this.sortOpt = other.sortOpt;
    }

    public static boolean saveSuperMarketToCompressedFile(String fileName, String customersFileName) {
        // Implementation
        return true;
    }



    public void printSuperMarket() {
        System.out.printf("Super Market Name: %s\t", name);
        System.out.print("Application.Models.Address: ");
        System.out.println(location);
        System.out.println();
        printAllProducts();
        System.out.println();
        printAllCustomers();
        System.out.println();
    }

    public boolean addProduct() {
        String barcode = Product.getBarcodeCode();
        Product prod = getProductByBarcode(barcode);
        if (prod != null) {
            prod.updateProductCount();
        } else {
            return addNewProduct(barcode);
        }
        return true;
    }

    private boolean addNewProduct(String barcode) {
        Product prod = new Product();
        prod.initProductNoBarcode();
        prod.setBarcode(barcode);
        return productList.insert(prod);
    }

    public boolean addCustomer(Customer customer) {
        if (!isCustomerInMarket(customer)) {
            customerArr.add(customer);
            return true;
        }
        return false;
    }

    private boolean isCustomerInMarket(Customer cust) {
        return customerArr.stream().anyMatch(c -> c.getName().equals(cust.getName()));
    }

    public boolean doShopping() {
        Customer customer = getCustomerShopPay();
        if (customer == null) {
            return false;
        }
        if (customer.getCart() == null) {
            customer.setCart(new ShoppingCart());
        }
        fillCart(customer.getCart());
        if (customer.getCart().getItemCount() == 0) {
            customer.setCart(null);
        }
        System.out.println("---------- Shopping ended ----------");
        return true;
    }

    public void doPrintCart() {
        Customer customer = getCustomerShopPay();
        if (customer == null) {
            return;
        }
        if (customer.getCart() == null) {
            System.out.println("Application.Models.Customer cart is empty");
            return;
        }
        customer.getCart().printShoppingCart();
    }

    public boolean doPayment() {
        Customer customer = getCustomerShopPay();
        if (customer == null) {
            return false;
        }
        if (customer.getCart() == null) {
            System.out.println("Application.Models.Customer cart is empty");
            return false;
        }
        customer.pay();
        return true;
    }

    private void fillCart(ShoppingCart cart) {
        printAllProducts();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Do you want to shop for a product? y/Y, anything else to exit!!");
            String op = scanner.nextLine().trim().toLowerCase();
            if (!op.equals("y")) {
                break;
            }
            Product prod = getProductAndCount();
            if (prod != null) {
                cart.addItemToCart(prod.getBarcode(), prod.getPrice(), prod.getCount());
                prod.setCount(prod.getCount() - 1);
            }
        }
    }

    public void sortCustomers(SortOption option) {
        Comparator<Customer> comparator = switch (option) {
            case NAME -> Comparator.comparing(Customer::getName);
            case TIME -> Comparator.comparingInt(Customer::getShopTimes);
            case SPEND -> Comparator.comparingDouble(Customer::getTotalSpend);
            default -> null;
        };
        if (comparator != null) {
            customerArr.sort(comparator);
            sortOpt = option;
        }
    }

    private SortOption showSortMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Based on what field do you want to sort?");
        for (SortOption option : SortOption.values()) {
            if (option != SortOption.NONE) {
                System.out.printf("Enter %d for %s\n", option.ordinal(), option);
            }
        }
        int choice = scanner.nextInt();
        return SortOption.values()[choice];
    }

    public void findCustomer() {
        if (sortOpt == SortOption.NONE) {
            System.out.println("The search cannot be performed, array not sorted");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        Customer searchCustomer = new Customer();

        switch (sortOpt) {
            case NAME:
                System.out.println("Enter customer name");
                searchCustomer.setName(scanner.nextLine());
                break;
            case TIME:
                System.out.println("Enter time in market");
                searchCustomer.setShopTimes(scanner.nextInt());
                break;
            case SPEND:
                System.out.println("Enter spent amount");
                searchCustomer.setTotalSpend(scanner.nextFloat());
                break;
        }

        int index = Collections.binarySearch(customerArr, searchCustomer, getComparator(sortOpt));
        if (index >= 0) {
            System.out.println("Application.Models.Customer found, ");
            customerArr.get(index).printCustomer();
        } else {
            System.out.println("Application.Models.Customer was not found");
        }
    }

    private Comparator<Customer> getComparator(SortOption option) {
        switch (option) {
            case NAME:
                return Comparator.comparing(Customer::getName);
            case TIME:
                return Comparator.comparingInt(Customer::getShopTimes);
            case SPEND:
                return Comparator.comparingDouble(Customer::getTotalSpend);
            default:
                return null;
        }
    }

    public void printProductByType() {
        if (productList.isEmpty()) {
            System.out.println("No products in market");
            return;
        }

        Product.ProductType type = Product.getProductType();
        final int[] count = {0}; // Use an array to make it effectively final

        productList.forEach(product -> {
            if (product.getType() == type) {
                product.printProduct();
                count[0]++; // Increment the count
            }
        });

        if (count[0] == 0) {
            System.out.printf("There are no products of type %s in market %s\n", type, name);
        }
    }

    private void printAllProducts() {
        System.out.println("There are " + productList.size() + " products");
        System.out.printf("%-20s %-10s\t%-20s %-10s %s\n", "Name", "Barcode", "Type", "Price", "Count In Stock");
        System.out.println("--------------------------------------------------------------------------------");
        productList.forEach(Product::printProduct);
    }

    private void printAllCustomers() {
        System.out.println("There are " + customerArr.size() + " listed customers");
        customerArr.forEach(Customer::printCustomer);
    }

    private Customer getCustomerShopPay() {
        if (customerArr.isEmpty()) {
            System.out.println("No customer listed to market");
            return null;
        }
        if (productList.isEmpty()) {
            System.out.println("No products in market - cannot shop");
            return null;
        }
        return getCustomerWhoShop();
    }

    private Customer getCustomerWhoShop() {
        printAllCustomers();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Who is shopping? Enter customer name");
        String name = scanner.nextLine().trim();
        return findCustomerByName(name);
    }

    public Customer findCustomerByName(String name) {
        return customerArr.stream()
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    private Product getProductAndCount() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter product barcode:");
        String barcode = scanner.nextLine().trim();
        Product prod = getProductByBarcode(barcode);
        if (prod == null) {
            System.out.println("No such product");
            return null;
        }
        if (prod.getCount() == 0) {
            System.out.println("This product is out of stock");
            return null;
        }
        int count;
        do {
            System.out.printf("How many items do you want? max %d\n", prod.getCount());
            count = scanner.nextInt();
        } while (count <= 0 || count > prod.getCount());
        Product tempProd = new Product(prod);
        tempProd.setCount(count);
        return tempProd;
    }

    public Product getProductByBarcode(String barcode) {
        BiPredicate<Product, Product> comparator = (p, b) -> p.getBarcode().equals(b.getBarcode());
        return productList.find(new Product(barcode), comparator);
    }

    public List<Product> getProductsByType(Product.ProductType type) {
        List<Product> result = new ArrayList<>();
        productList.forEach(product -> {
            if (product.getType() == type) {
                result.add(product);
            }
        });
        return result;
    }

    public void handleCustomerStillShoppingAtExit() {
        for (Customer customer : customerArr) {
            if (customer.getCart() != null) {
                System.out.println("Market is closing must pay!!!");
                customer.pay();
            }
        }
    }

    public void freeMarket() {
        // In Java, we don't need to manually free memory
        // This method can be used for any cleanup operations if needed
        customerArr.clear();
        productList.clear();
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getLocation() {
        return location;
    }

    public void setLocation(Address location) {
        this.location = location;
    }

    public List<Customer> getCustomerArr() {
        return customerArr;
    }

    public void setCustomerArr(List<Customer> customerArr) {
        this.customerArr = customerArr;
    }

    public GeneralList<Product> getProductList() {
        return productList;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        productList.forEach(products::add);
        return products;
    }

    public int getNumOfProductsInList() {
        return productList.size();
    }
}