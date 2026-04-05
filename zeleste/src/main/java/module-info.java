module com.iza.zeleste {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.iza.zeleste to javafx.fxml;
    exports com.iza.zeleste;
}