module com.example.deepspace {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.internal.le;


    opens com.example.deepspace to javafx.fxml;
    exports com.example.deepspace;
}