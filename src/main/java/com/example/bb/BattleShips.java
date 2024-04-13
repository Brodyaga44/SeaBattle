package com.example.bb;

import com.example.bb.Game.Baza;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BattleShips extends Application {
    Baza game = new Baza(); // Создание экземпляра игры
    @Override
    public void start(Stage primaryStage) throws Exception { // Начало игры
        primaryStage.setTitle("Морской бой");
        Label labelPlayer = new Label("Введите имя игрока:");
        TextField namePlayer = new TextField();// Заполение поля имени игрока
        Button submitButton = new Button("Начать игру"); // Подтверждение заполненного поля
        submitButton.setOnAction(e -> {
            if (namePlayer.getText().trim().isEmpty() ) { // Проверка не оставил ли игрок поле имя пустым
                return;
            }
            primaryStage.close(); //Закрывает окно ввода имени пользователя
            Scene scene = new Scene(game.createContent(namePlayer.getText())); // Получение имени игрока для использования в игре
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show(); // Отображение игры на экран
        });
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(labelPlayer, namePlayer, submitButton);//Создание окна ввода имени пользователя
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();//Отображение окна ввода имени игрока
    }
    public static void main(String[] args) {
        launch(args);
    }
}
