module com.example.sqlbank {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.sqlbank to javafx.fxml;
    exports com.example.sqlbank;
}