package Application;

import Application.Models.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;
import javafx.scene.layout.VBox;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.*;
import java.util.List;
import java.util.Optional;
import javafx.stage.FileChooser;

public class SupermarketGUI extends Application {
    private Supermarket supermarket;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        supermarket = new Supermarket();

        BorderPane root = new BorderPane();

        MenuBar menuBar = createMenuBar();
        root.setTop(menuBar);

        Label statusLabel = new Label("Welcome to Supermarket Management System");
        root.setBottom(statusLabel);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Supermarket Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        // File Menu
        Menu fileMenu = new Menu("File");
        MenuItem loadItem = new MenuItem("Load Supermarket");
        MenuItem saveItem = new MenuItem("Save Supermarket");
        MenuItem exitItem = new MenuItem("Exit");
        fileMenu.getItems().addAll(loadItem, saveItem, new SeparatorMenuItem(), exitItem);

        // Products Menu
        Menu productsMenu = new Menu("Products");
        MenuItem addProductItem = new MenuItem("Add Product");
        MenuItem viewProductsItem = new MenuItem("View Products");
        MenuItem searchProductItem = new MenuItem("Search Product");
        productsMenu.getItems().addAll(addProductItem, viewProductsItem, searchProductItem);

        // Customers Menu
        Menu customersMenu = new Menu("Customers");
        MenuItem addCustomerItem = new MenuItem("Add Customer");
        MenuItem viewCustomersItem = new MenuItem("View Customers");
        MenuItem searchCustomerItem = new MenuItem("Search Customer");
        customersMenu.getItems().addAll(addCustomerItem, viewCustomersItem, searchCustomerItem);

        // Shopping Menu
        Menu shoppingMenu = new Menu("Shopping");
        MenuItem startShoppingItem = new MenuItem("Start Shopping");
        MenuItem viewCartItem = new MenuItem("View Cart");
        MenuItem checkoutItem = new MenuItem("Checkout");
        shoppingMenu.getItems().addAll(startShoppingItem, viewCartItem, checkoutItem);

        // Reports Menu
        Menu reportsMenu = new Menu("Reports");
        MenuItem sortCustomersItem = new MenuItem("Sort Customers");
        MenuItem printProductByTypeItem = new MenuItem("Print Product by Type");
        reportsMenu.getItems().addAll(sortCustomersItem, printProductByTypeItem);

        menuBar.getMenus().addAll(fileMenu, productsMenu, customersMenu, shoppingMenu, reportsMenu);

        setMenuActions(loadItem, saveItem, exitItem, addProductItem, viewProductsItem, searchProductItem,
                addCustomerItem, viewCustomersItem, searchCustomerItem, startShoppingItem, viewCartItem,
                checkoutItem, sortCustomersItem, printProductByTypeItem);

        return menuBar;
    }

    private void setMenuActions(MenuItem loadItem, MenuItem saveItem, MenuItem exitItem,
                                MenuItem addProductItem, MenuItem viewProductsItem, MenuItem searchProductItem,
                                MenuItem addCustomerItem, MenuItem viewCustomersItem, MenuItem searchCustomerItem,
                                MenuItem startShoppingItem, MenuItem viewCartItem, MenuItem checkoutItem,
                                MenuItem sortCustomersItem, MenuItem printProductByTypeItem) {
        // File Menu Actions
        loadItem.setOnAction(e -> loadSupermarket());
        saveItem.setOnAction(e -> saveSupermarket());
        exitItem.setOnAction(e -> exitApplication());

        // Products Menu Actions
        addProductItem.setOnAction(e -> showAddProductDialog());
        viewProductsItem.setOnAction(e -> viewProducts());
        searchProductItem.setOnAction(e -> searchProduct());

        // Customers Menu Actions
        addCustomerItem.setOnAction(e -> addCustomer());
        viewCustomersItem.setOnAction(e -> viewCustomers());
        searchCustomerItem.setOnAction(e -> searchCustomer());

        // Shopping Menu Actions
        startShoppingItem.setOnAction(e -> startShopping());
        viewCartItem.setOnAction(e -> viewCart());
        checkoutItem.setOnAction(e -> checkout());

        // Reports Menu Actions
        sortCustomersItem.setOnAction(e -> sortCustomers());
        printProductByTypeItem.setOnAction(e -> printProductByType());
    }

    private void loadSupermarket() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Supermarket File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Supermarket Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                supermarket = Supermarket.loadFromFile(reader);
                showAlert("Load Successful", "Supermarket data loaded successfully.");
            } catch (IOException e) {
                showAlert("Error", "Failed to load supermarket data: " + e.getMessage());
            }
        }
    }

    private void saveSupermarket() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Supermarket File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Supermarket Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                supermarket.saveToFile(writer);
                showAlert("Save Successful", "Supermarket data saved successfully.");
            } catch (IOException e) {
                showAlert("Error", "Failed to save supermarket data: " + e.getMessage());
            }
        }
    }

    private void exitApplication() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Application");
        alert.setHeaderText("Are you sure you want to exit?");
        alert.setContentText("Any unsaved changes will be lost.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    private void viewProducts() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("View Products");
        dialog.setHeaderText("List of all products");

        ButtonType closeButtonType = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(closeButtonType);

        TableView<Product> table = new TableView<>();
        ObservableList<Product> data = FXCollections.observableArrayList();
        supermarket.getProductList().forEach(data::add);

        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Product, String> barcodeCol = new TableColumn<>("Barcode");
        barcodeCol.setCellValueFactory(new PropertyValueFactory<>("barcode"));

        TableColumn<Product, Product.ProductType> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Product, Float> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Product, Integer> countCol = new TableColumn<>("Count");
        countCol.setCellValueFactory(new PropertyValueFactory<>("count"));

        table.getColumns().addAll(nameCol, barcodeCol, typeCol, priceCol, countCol);
        table.setItems(data);

        VBox vbox = new VBox(table);
        dialog.getDialogPane().setContent(vbox);

        dialog.showAndWait();
    }

    private void searchProduct() {
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setTitle("Search Product");
        inputDialog.setHeaderText("Enter product barcode to search");
        inputDialog.setContentText("Barcode:");

        Optional<String> result = inputDialog.showAndWait();
        result.ifPresent(barcode -> {
            Product product = supermarket.getProductByBarcode(barcode);
            if (product != null) {
                Dialog<Void> resultDialog = new Dialog<>();
                resultDialog.setTitle("Product Found");
                resultDialog.setHeaderText("Product Details");

                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                grid.add(new Label("Name:"), 0, 0);
                grid.add(new Label(product.getName()), 1, 0);
                grid.add(new Label("Barcode:"), 0, 1);
                grid.add(new Label(product.getBarcode()), 1, 1);
                grid.add(new Label("Type:"), 0, 2);
                grid.add(new Label(product.getType().toString()), 1, 2);
                grid.add(new Label("Price:"), 0, 3);
                grid.add(new Label(String.format("%.2f", product.getPrice())), 1, 3);
                grid.add(new Label("Count:"), 0, 4);
                grid.add(new Label(String.valueOf(product.getCount())), 1, 4);

                resultDialog.getDialogPane().setContent(grid);
                ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
                resultDialog.getDialogPane().getButtonTypes().add(closeButton);

                resultDialog.showAndWait();
            } else {
                showAlert("Product Not Found", "No product found with the given barcode.");
            }
        });
    }

    private void addCustomer() {
        Dialog<Customer> dialog = new Dialog<>();
        dialog.setTitle("Add New Customer");
        dialog.setHeaderText("Enter customer details");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String name = nameField.getText().trim();
                if (!name.isEmpty()) {
                    Customer customer = new Customer();
                    customer.setName(name);
                    return customer;
                } else {
                    showAlert("Invalid Input", "Customer name cannot be empty.");
                    return null;
                }
            }
            return null;
        });

        Optional<Customer> result = dialog.showAndWait();
        result.ifPresent(customer -> {
            if (supermarket.addCustomer(customer)) {
                showAlert("Customer Added", "Customer has been successfully added to the supermarket.");
            } else {
                showAlert("Error", "Failed to add the customer. They may already exist.");
            }
        });
    }

    private void viewCustomers() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("View Customers");
        dialog.setHeaderText("List of all customers");

        ButtonType closeButtonType = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(closeButtonType);

        TableView<Customer> table = new TableView<>();
        ObservableList<Customer> data = FXCollections.observableArrayList(supermarket.getCustomerArr());

        TableColumn<Customer, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Customer, Integer> shopTimesCol = new TableColumn<>("Shop Times");
        shopTimesCol.setCellValueFactory(new PropertyValueFactory<>("shopTimes"));

        TableColumn<Customer, Float> totalSpendCol = new TableColumn<>("Total Spend");
        totalSpendCol.setCellValueFactory(new PropertyValueFactory<>("totalSpend"));

        table.getColumns().addAll(nameCol, shopTimesCol, totalSpendCol);
        table.setItems(data);

        VBox vbox = new VBox(table);
        dialog.getDialogPane().setContent(vbox);

        dialog.showAndWait();
    }

    private void searchCustomer() {
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setTitle("Search Customer");
        inputDialog.setHeaderText("Enter customer name to search");
        inputDialog.setContentText("Name:");

        Optional<String> result = inputDialog.showAndWait();
        result.ifPresent(name -> {
            Customer customer = supermarket.findCustomerByName(name);
            if (customer != null) {
                Dialog<Void> resultDialog = new Dialog<>();
                resultDialog.setTitle("Customer Found");
                resultDialog.setHeaderText("Customer Details");

                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                grid.add(new Label("Name:"), 0, 0);
                grid.add(new Label(customer.getName()), 1, 0);
                grid.add(new Label("Shop Times:"), 0, 1);
                grid.add(new Label(String.valueOf(customer.getShopTimes())), 1, 1);
                grid.add(new Label("Total Spend:"), 0, 2);
                grid.add(new Label(String.format("%.2f", customer.getTotalSpend())), 1, 2);

                resultDialog.getDialogPane().setContent(grid);
                ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
                resultDialog.getDialogPane().getButtonTypes().add(closeButton);

                resultDialog.showAndWait();
            } else {
                showAlert("Customer Not Found", "No customer found with the given name.");
            }
        });
    }

    private void startShopping() {
        // First, select a customer
        ChoiceDialog<Customer> customerDialog = new ChoiceDialog<>();
        customerDialog.setTitle("Select Customer");
        customerDialog.setHeaderText("Choose a customer to start shopping");
        customerDialog.setContentText("Customer:");
        customerDialog.getItems().addAll(supermarket.getCustomerArr());

        Optional<Customer> customerResult = customerDialog.showAndWait();

        customerResult.ifPresent(customer -> {
            // Now, let's create a shopping cart for this customer
            ShoppingCart cart = new ShoppingCart();
            customer.setCart(cart);

            while (true) {
                // Show product selection dialog
                ChoiceDialog<Product> productDialog = new ChoiceDialog<>();
                productDialog.setTitle("Add Product to Cart");
                productDialog.setHeaderText("Choose a product to add to the cart");
                productDialog.setContentText("Product:");
                productDialog.getItems().addAll(supermarket.getAllProducts());  // Use the new method here

                Optional<Product> productResult = productDialog.showAndWait();

                if (productResult.isPresent()) {
                    Product selectedProduct = productResult.get();

                    // Ask for quantity
                    TextInputDialog quantityDialog = new TextInputDialog("1");
                    quantityDialog.setTitle("Enter Quantity");
                    quantityDialog.setHeaderText("Enter the quantity for " + selectedProduct.getName());
                    quantityDialog.setContentText("Quantity:");

                    Optional<String> quantityResult = quantityDialog.showAndWait();

                    quantityResult.ifPresent(quantityString -> {
                        try {
                            int quantity = Integer.parseInt(quantityString);
                            if (quantity > 0 && quantity <= selectedProduct.getCount()) {
                                cart.addItemToCart(selectedProduct.getBarcode(), selectedProduct.getPrice(), quantity);
                                selectedProduct.setCount(selectedProduct.getCount() - quantity);
                                showAlert("Product Added", quantity + " " + selectedProduct.getName() + "(s) added to cart.");
                            } else {
                                showAlert("Invalid Quantity", "Please enter a valid quantity (1-" + selectedProduct.getCount() + ").");
                            }
                        } catch (NumberFormatException e) {
                            showAlert("Invalid Input", "Please enter a valid number for quantity.");
                        }
                    });
                } else {
                    // User closed the dialog without selecting a product, end shopping
                    break;
                }

                // Ask if the user wants to continue shopping
                Alert continueAlert = new Alert(Alert.AlertType.CONFIRMATION);
                continueAlert.setTitle("Continue Shopping");
                continueAlert.setHeaderText("Do you want to add more products?");
                continueAlert.setContentText("Choose your option.");

                Optional<ButtonType> continueResult = continueAlert.showAndWait();
                if (continueResult.get() != ButtonType.OK){
                    break;
                }
            }

            showAlert("Shopping Completed", "Shopping session ended for " + customer.getName());
        });
    }

    private void viewCart() {
        // First, select a customer
        ChoiceDialog<Customer> customerDialog = new ChoiceDialog<>();
        customerDialog.setTitle("Select Customer");
        customerDialog.setHeaderText("Choose a customer to view cart");
        customerDialog.setContentText("Customer:");
        customerDialog.getItems().addAll(supermarket.getCustomerArr());

        Optional<Customer> result = customerDialog.showAndWait();

        result.ifPresent(customer -> {
            ShoppingCart cart = customer.getCart();
            if (cart == null || cart.getItemCount() == 0) {
                showAlert("Empty Cart", "The shopping cart for " + customer.getName() + " is empty.");
            } else {
                Dialog<Void> cartDialog = new Dialog<>();
                cartDialog.setTitle("Shopping Cart");
                cartDialog.setHeaderText("Shopping Cart for " + customer.getName());

                TableView<ShoppingItem> table = new TableView<>();
                ObservableList<ShoppingItem> data = FXCollections.observableArrayList(cart.getItems());

                TableColumn<ShoppingItem, String> barcodeCol = new TableColumn<>("Barcode");
                barcodeCol.setCellValueFactory(new PropertyValueFactory<>("barcode"));

                TableColumn<ShoppingItem, Float> priceCol = new TableColumn<>("Price");
                priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

                TableColumn<ShoppingItem, Integer> countCol = new TableColumn<>("Count");
                countCol.setCellValueFactory(new PropertyValueFactory<>("count"));

                table.getColumns().addAll(barcodeCol, priceCol, countCol);
                table.setItems(data);

                VBox vbox = new VBox(table);
                vbox.getChildren().add(new Label("Total: $" + String.format("%.2f", cart.getTotalPrice())));
                cartDialog.getDialogPane().setContent(vbox);

                ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
                cartDialog.getDialogPane().getButtonTypes().add(closeButton);

                cartDialog.showAndWait();
            }
        });
    }

    private void checkout() {
        // First, select a customer
        ChoiceDialog<Customer> customerDialog = new ChoiceDialog<>();
        customerDialog.setTitle("Select Customer");
        customerDialog.setHeaderText("Choose a customer to checkout");
        customerDialog.setContentText("Customer:");
        customerDialog.getItems().addAll(supermarket.getCustomerArr());

        Optional<Customer> result = customerDialog.showAndWait();

        result.ifPresent(customer -> {
            ShoppingCart cart = customer.getCart();
            if (cart == null || cart.getItemCount() == 0) {
                showAlert("Empty Cart", "The shopping cart for " + customer.getName() + " is empty. Cannot checkout.");
            } else {
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirm Checkout");
                confirmAlert.setHeaderText("Checkout for " + customer.getName());
                confirmAlert.setContentText("Total amount: $" + String.format("%.2f", cart.getTotalPrice()) + "\nDo you want to proceed with the checkout?");

                Optional<ButtonType> confirmResult = confirmAlert.showAndWait();
                if (confirmResult.get() == ButtonType.OK){
                    customer.pay();
                    showAlert("Checkout Completed", "Checkout completed for " + customer.getName() + ". Total paid: $" + String.format("%.2f", cart.getTotalPrice()));
                } else {
                    showAlert("Checkout Cancelled", "Checkout was cancelled.");
                }
            }
        });
    }

    private void sortCustomers() {
        ChoiceDialog<Supermarket.SortOption> dialog = new ChoiceDialog<>(Supermarket.SortOption.NAME, Supermarket.SortOption.values());
        dialog.setTitle("Sort Customers");
        dialog.setHeaderText("Choose sorting option");
        dialog.setContentText("Sort by:");

        Optional<Supermarket.SortOption> result = dialog.showAndWait();
        result.ifPresent(sortOption -> {
            supermarket.sortCustomers(sortOption);
            showAlert("Customers Sorted", "Customers have been sorted by " + sortOption.toString());
            viewCustomers(); // Show the sorted list
        });
    }

    private void printProductByType() {
        ChoiceDialog<Product.ProductType> dialog = new ChoiceDialog<>(Product.ProductType.FRUIT_VEGETABLE, Product.ProductType.values());
        dialog.setTitle("Print Products by Type");
        dialog.setHeaderText("Choose product type");
        dialog.setContentText("Product type:");

        Optional<Product.ProductType> result = dialog.showAndWait();
        result.ifPresent(productType -> {
            List<Product> products = supermarket.getProductsByType(productType);

            Dialog<Void> resultDialog = new Dialog<>();
            resultDialog.setTitle("Products by Type");
            resultDialog.setHeaderText("Products of type: " + productType);

            TableView<Product> table = new TableView<>();
            ObservableList<Product> data = FXCollections.observableArrayList(products);

            TableColumn<Product, String> nameCol = new TableColumn<>("Name");
            nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

            TableColumn<Product, String> barcodeCol = new TableColumn<>("Barcode");
            barcodeCol.setCellValueFactory(new PropertyValueFactory<>("barcode"));

            TableColumn<Product, Float> priceCol = new TableColumn<>("Price");
            priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

            TableColumn<Product, Integer> countCol = new TableColumn<>("Count");
            countCol.setCellValueFactory(new PropertyValueFactory<>("count"));

            table.getColumns().addAll(nameCol, barcodeCol, priceCol, countCol);
            table.setItems(data);

            VBox vbox = new VBox(table);
            resultDialog.getDialogPane().setContent(vbox);

            ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
            resultDialog.getDialogPane().getButtonTypes().add(closeButton);

            resultDialog.showAndWait();
        });
    }

    private void showAddProductDialog() {
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Add New Product");
        dialog.setHeaderText("Enter product details");

        // Set the button types
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create the product form fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        TextField barcodeField = new TextField();
        ComboBox<Product.ProductType> typeComboBox = new ComboBox<>(FXCollections.observableArrayList(Product.ProductType.values()));
        TextField priceField = new TextField();
        TextField countField = new TextField();

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Barcode:"), 0, 1);
        grid.add(barcodeField, 1, 1);
        grid.add(new Label("Type:"), 0, 2);
        grid.add(typeComboBox, 1, 2);
        grid.add(new Label("Price:"), 0, 3);
        grid.add(priceField, 1, 3);
        grid.add(new Label("Count:"), 0, 4);
        grid.add(countField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a product when the add button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    String name = nameField.getText();
                    String barcode = barcodeField.getText();
                    Product.ProductType type = typeComboBox.getValue();
                    float price = Float.parseFloat(priceField.getText());
                    int count = Integer.parseInt(countField.getText());
                    return new Product(name, barcode, type, price, count);
                } catch (NumberFormatException e) {
                    showAlert("Invalid input", "Please enter valid numeric values for price and count.");
                    return null;
                }
            }
            Optional<Product> result = dialog.showAndWait();
            result.ifPresent(product -> {
                if (supermarket.getProductList().insert(product)) {
                    showAlert("Product Added", "Product has been successfully added to the supermarket.");
                } else {
                    showAlert("Error", "Failed to add the product. It may already exist.");
                }
            });
            return null;
        });

        Optional<Product> result = dialog.showAndWait();
        result.ifPresent(product -> {
            if (supermarket.getProductList().insert(product)) {
                showAlert("Product Added", "Product has been successfully added to the supermarket.");
            } else {
                showAlert("Error", "Failed to add the product. It may already exist.");
            }
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}