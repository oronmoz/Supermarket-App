public class ShoppingItem {
    private String barcode;
    private float price;
    private int count;

    public ShoppingItem(String barcode, float price, int count) {
        this.barcode = barcode;
        this.price = price;
        this.count = count;
    }

    public void printItem() {
        System.out.printf("Item %s count %d price per item %.2f\n", barcode, count, price);
    }

    public static ShoppingItem createItem(String barcode, float price, int count) {
        return new ShoppingItem(barcode, price, count);
    }

    // Getters and setters
    public String getBarcode() {
        return barcode;
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

    public float getTotalPrice() {
        return price * count;
    }
}