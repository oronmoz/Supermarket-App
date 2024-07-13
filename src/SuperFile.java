import java.io.*;

public class SuperFile {
    public static boolean saveSuperMarketToFile(Supermarket market, String fileName, String customersFileName) {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(fileName))) {
            writeStringToFile(market.getName(), out);
            market.getLocation().saveAddressToFile(out);
            int productCount = market.getNumOfProductsInList();
            out.writeInt(productCount);
            market.getProductList().forEach(product -> {
                try {
                    product.saveProductToFile(out);
                } catch (IOException e) {
                    throw new RuntimeException("Error saving product", e);
                }
            });
            Customer.saveCustomerToTextFile(market.getCustomerArr().toArray(new Customer[0]), customersFileName);
            return true;
        } catch (IOException e) {
            System.out.println("Error saving supermarket to file: " + e.getMessage());
            return false;
        }
    }

    public static boolean loadSuperMarketFromFile(Supermarket market, String fileName, String customersFileName) {
        try (DataInputStream in = new DataInputStream(new FileInputStream(fileName))) {
            market.setName(readStringFromFile(in));
            market.setLocation(Address.loadAddressFromFile(in));
            int productCount = in.readInt();
            for (int i = 0; i < productCount; i++) {
                Product product = Product.loadProductFromFile(in);
                market.getProductList().insert(product);
            }
            market.setCustomerArr(Customer.loadCustomerFromTextFile(customersFileName));
            return true;
        } catch (IOException e) {
            System.out.println("Error loading supermarket from file: " + e.getMessage());
            return false;
        }
    }

    private static void writeStringToFile(String str, DataOutputStream out) throws IOException {
        out.writeInt(str.length());
        out.writeBytes(str);
    }

    private static String readStringFromFile(DataInputStream in) throws IOException {
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readFully(bytes);
        return new String(bytes);
    }

    // Add more methods for compressed file operations if needed
}