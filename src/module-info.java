module SuperMarketDatabase {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    opens Application.Models to javafx.base;
    exports Application;
}