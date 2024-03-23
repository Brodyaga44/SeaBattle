package com.example.bb.Game;

import com.example.bb.BotLogic.Bot;
import com.example.bb.Game.Board;
import com.example.bb.Game.Cell;
import com.example.bb.Game.Ship;
import com.example.bb.Player.Player;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Random;

public class Baza {

	private boolean running = false;
	public static Board enemyBoard, playerBoard; // Создание игрового поля игрока и бота
	public static int moveCounter = 0;//Счетчик ходов бота
	private int shipsToPlace = 4; // Сколько видов палуб нужно поставить на поле
	public int count = 10; // Сколько всего кораблей в игре

	private boolean enemyTurn = false; // Информация о том, чем ход

	private Random random = new Random(); // Рандомайзер

	int x = random.nextInt(10); // Х координаты для атак бота
	int y = random.nextInt(10); // У координаты для атак бота


	public Parent createContent(String currentName) { // Создание графического интерфейса игры
		Player player = new Player(currentName);
		BorderPane root = new BorderPane();
		root.setPrefSize(600, 800);
		root.setStyle("-fx-background-color: #FFFFFF;"); // Цвет фона

		Button btnExit = new Button("Сдаться");
		btnExit.setAlignment(Pos.CENTER);
		btnExit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.exit(0);
			}
		});

		enemyBoard = new Board(true, event -> { // Создание поля  бота
			if (!running)
				return;

			Cell cell = (Cell) event.getSource();
			if (cell.wasShot)
				return;

			enemyTurn = !cell.shoot(enemyBoard);

			if (enemyBoard.ships == 0) { // Отображение окна победителя с именем игрока, в случае его победы
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Результат игры");
				alert.setHeaderText(player.getName()+ " победил!");
				alert.showAndWait();
				System.exit(0);
			}
			if (enemyTurn)
				enemyMove();
		});

		playerBoard = new Board(false, event -> { // Создание поля игрока
			if (running)
				return;

			Cell cell = (Cell) event.getSource();
			if (playerBoard.placeShip(new Ship(shipsToPlace, event.getButton() == MouseButton.PRIMARY), cell.x,
					cell.y))
			{
				count--;
				// Расстановка кораблей в зависимости от количества палуб
				if (count == 9){
					shipsToPlace = 3;
				}
				if (count == 7){
					shipsToPlace = 2;
				}
				if (count == 4){
					shipsToPlace = 1;
				}
				if (count == 0) {
					startGame(); // Вызов запуска игры
				}
			}
		});



//////// Правая часть поля
		VBox vbRight = new VBox();
		vbRight.setAlignment(Pos.BASELINE_CENTER);
		vbRight.getChildren().addAll(btnExit, new Label(" Поле игрока БОТ "));

		Label emptyLabel = new Label();
		emptyLabel.setPrefHeight(300);

		vbRight.getChildren().addAll(emptyLabel, new Label("Поле игрока "+ player.getName()));
		vbRight.setSpacing(75);
		vbRight.setPadding(new Insets(45));

		VBox vbox = new VBox(50, enemyBoard, playerBoard);
		vbox.setAlignment(Pos.CENTER);

		root.setCenter(vbox);

		root.setRight(vbRight);
		return root;
	}
	///////

	public void enemyMove() { // Запуск бота и подсчет его ходов
		Bot b = new Bot();
		b.play();
		moveCounter++;

	}

	private void startGame() { // НАЧАЛО ИГРЫ
		// корабли бота
		int type = 4; // Тип палуб кораблей бота
		int countBot = 10; // Сколько кораблей нужно боту

		while (countBot > 0) {
			int x = random.nextInt(10);
			int y = random.nextInt(10);

			if (enemyBoard.placeShip(new Ship(type, Math.random() < 0.5), x, y)) {
				countBot--;
				//Расстановка разного количества кораблей в зпвисимости от их вида
				if (countBot == 9){
					type = 3;
				}
				if (countBot == 7){
					type = 2;
				}
				if (countBot == 4){
					type = 1;
				}

			}
		}

		running = true;
	}

}
