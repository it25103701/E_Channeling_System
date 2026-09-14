module com.echanneling.e_channeling_system {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.echanneling.e_channeling_system to javafx.fxml;
    exports com.echanneling.e_channeling_system;
}