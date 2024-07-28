module com.example.desrsa_app {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.desrsa_app to javafx.fxml;
    exports com.example.desrsa_app;
}