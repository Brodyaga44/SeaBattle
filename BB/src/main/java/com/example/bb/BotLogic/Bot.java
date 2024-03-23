package com.example.bb.BotLogic;

import com.example.bb.Game.Baza;
import com.example.bb.Game.Cell;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogEvent;
import javafx.scene.paint.Color;

import java.util.Random;

public class Bot extends BotLogic {

	private Cell firstHit; // Первый удар

	Random r = new Random(); //Рандомайзер
	public Boolean alive = false; // Флаг на проверку жив ли корабль
	public Boolean up = false; // Флаг для пометки направления вверх/вниз
	public Boolean right=false; // Флаг для пометки направления вправо/влево

	public void play() { // Запуск бота

		if (alive)
			stillAlive();
		else {
			notAlive();
		}

		gameOverCheck();
	}

	public void stillAlive() { // Что делать в случае, если корабль до сих пор жив
		Boolean hitFlag = true;
		Cell randomCell = Baza.playerBoard.getCell(firstHit.getXCord(), firstHit.getYCord());
		while (hitFlag) {
			if (!firstHit.ship.isVertical()) {
				if (right)
					randomCell = goLeft(randomCell);
				if(!right)
					randomCell = goRight(randomCell);
			}

			else {
				if (up)
					randomCell = goDown(randomCell);
				if (!up) {
					randomCell = goUp(randomCell);
				}

			}
			hitFlag = randomCell.shoot(Baza.playerBoard);
			System.out.println(randomCell);

		}

		if (!aliveCheck(firstHit)) {
			alive=false;
		}
		if (aliveCheck(firstHit)) {
			alive = true;
		}

	}

	private void notAlive() { //Если корабль не жив
		Boolean hitFlag = false;
		Cell randomCell = getRandomCell();
		randomCell = Baza.playerBoard.getCell(randomCell.getXCord(), randomCell.getYCord());
		firstHit = Baza.playerBoard.getCell(randomCell.getXCord(), randomCell.getYCord());
		firstHit=randomCell;
	while(randomCell.wasShot) {
	randomCell = getRandomCell();
	firstHit=randomCell;
	randomCell = Baza.playerBoard.getCell(randomCell.getXCord(), randomCell.getYCord());
	firstHit = Baza.playerBoard.getCell(randomCell.getXCord(), randomCell.getYCord());
	
}
		hitFlag = randomCell.shoot(Baza.playerBoard);
		
		if (!hitFlag) {
			alive = false;
			return;
		}
		if (firstHit.getYCord() < 4.5) { //В случае если координата У меньше 4.5, бот будет атаковать Вниз

			up = false;
		} else {

			up = true;
		}
		
		if (firstHit.getXCord() < 4.5) {//В случае если координата Х меньше 4.5, бот будет атаковать Вправо

			right = true;
		} else {

			right = false;
		}
		
		
		while (hitFlag) {// Реализация атаки, написанной выше
			if (!firstHit.ship.isVertical()) {
				if (right)
					randomCell = goRight(randomCell);
				if(!right)
					randomCell = goLeft(randomCell);
			} else {
				if (up)
					randomCell = goUp(randomCell);
				if(!up)
					randomCell = goDown(randomCell);
				

			}
			hitFlag = randomCell.shoot(Baza.playerBoard);

		}
		if (!aliveCheck(firstHit)) { // Проверка жив ли однопалубный корабль
			alive = false;
	
		}
		if (aliveCheck(firstHit)) {
			alive = true;
		}
	}

	private boolean aliveCheck(Cell c) { // Проверка жив ли корабль
		if (!nullCheck(c))
			if (c.ship.isAlive())
				return true;
		return false;
	}

	private Cell getRandomCell() { // Получение рандомной клетки для хода
		Cell c = Cell.availableCells.get(r.nextInt(Cell.availableCells.size()));
		while (c.wasShot) {
			c = Cell.availableCells.get(r.nextInt(Cell.availableCells.size()));

		}
		return c;
	}

	public void gameOverCheck() { // Проверка не закончилась ли игра
		if (Baza.playerBoard.ships <= 0) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Результат игры");
			alert.setHeaderText("Вас победил БОТ за  "+Baza.moveCounter+" ходов!");
			alert.setOnCloseRequest(new EventHandler<DialogEvent>() {
				@Override
				public void handle(DialogEvent event) {
					System.exit(0);
				}
			});

			alert.showAndWait();
		}
	}

	private Cell goUp(Cell c) {
		int x = c.getXCord();
		int y = c.getYCord();

		if (y == 0) {
			return goDown(c);

		}

		c = new Cell(x, y);
		c = Baza.playerBoard.getCell(x, --y);
		while (c.wasShot) {
			c = getRandomCell();
			c = Baza.playerBoard.getCell(c.getXCord(), c.getYCord());

		}

		return c;
	}

	private Cell goDown(Cell c) {
		int x = c.getXCord();
		int y = c.getYCord();
		if (y == 9) {

			return goUp(c);
		}
		c = new Cell(x, y);
		c = Baza.playerBoard.getCell(x, ++y);
		while (c.wasShot) {
			c = getRandomCell();
			c = Baza.playerBoard.getCell(c.getXCord(), c.getYCord());

		}

		return c;
	}

	private Cell goRight(Cell c) {
		int x = c.getXCord();
		int y = c.getYCord();
		if (x == 9) {
			return goLeft(c);

		}
		c = new Cell(x, y);
		c = Baza.playerBoard.getCell(++x, y);
		while (c.wasShot) {

			c = getRandomCell();
			c = Baza.playerBoard.getCell(c.getXCord(), c.getYCord());
		}

		return c;

	}

	private Cell goLeft(Cell c) {
		int x = c.getXCord();
		int y = c.getYCord();
		if (x == 0) {
			return goRight(c);

		}
		c = new Cell(x, y);
		c = Baza.playerBoard.getCell(--x, y);
		while (c.wasShot) {

			c = getRandomCell();
			c = Baza.playerBoard.getCell(c.getXCord(), c.getYCord());

		}

		return c;
	}

	private Boolean nullCheck(Cell c) {
		if (c == null) {
			return true;
		} else
			return false;
	}

}
