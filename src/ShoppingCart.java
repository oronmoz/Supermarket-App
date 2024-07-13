import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private List<ShoppingItem> itemArr;

    public ShoppingCart() {
        this.itemArr = new ArrayList<>();
    }

    public float getTotalPrice() {
        float price = 0;
        for (ShoppingItem item : itemArr) {
            price += item.getTotalPrice();
        }
        return price;
    }

    public boolean addItemToCart(String barcode, float price, int count) {
        ShoppingItem item = getItemByBarcode(barcode);
        if (item == null) {
            item = ShoppingItem.createItem(barcode, price, count);
            itemArr.add(item);
        } else {
            item.setCount(item.getCount() + count);
        }
        return true;
    }

    public float printShoppingCart() {
        float totalPrice = 0;
        for (ShoppingItem item : itemArr) {
            item.printItem();
            totalPrice += item.getTotalPrice();
        }
        System.out.printf("Total bill to pay: %.2f\n", totalPrice);
        return totalPrice;
    }

    public ShoppingItem getItemByBarcode(String barcode) {
        for (ShoppingItem item : itemArr) {
            if (item.getBarcode().equals(barcode)) {
                return item;
            }
        }
        return null;
    }

    public int getItemCount() {
        return itemArr.size();
    }

    public List<ShoppingItem> getItems() {
        return new ArrayList<>(itemArr);
    }
}