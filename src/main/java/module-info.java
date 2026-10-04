module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;

    exports com.example to javafx.graphics;

    opens com.example to javafx.fxml;
}
