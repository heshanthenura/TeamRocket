module com.heshanthenura.teamrocket {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires java.sql;
    requires com.opencsv;


    opens com.heshanthenura.teamrocket to javafx.fxml;
    opens com.heshanthenura.teamrocket.Entity to javafx.base;
    exports com.heshanthenura.teamrocket;
}