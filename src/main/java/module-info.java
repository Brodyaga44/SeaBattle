module com.example.bb {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.example.bb to javafx.fxml;
    exports com.example.bb;
    exports com.example.bb.BotLogic;
    opens com.example.bb.BotLogic to javafx.fxml;
    exports com.example.bb.Game;
    opens com.example.bb.Game to javafx.fxml;
}