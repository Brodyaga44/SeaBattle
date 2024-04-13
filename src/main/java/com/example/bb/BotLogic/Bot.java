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

	private Cell firstHit; // Первая клетка, куда бот сделает выстрел

	Random r = new Random(); // Генератор случайных чисел для выбора клетки для выстрела
	public Boolean alive = false; // Флаг, указывающий на то, жив ли корабль
	public Boolean up = false; // Флаг, указывающий на направление атаки (вверх/вниз)
	public Boolean right = false; // Флаг, указывающий на направление атаки (вправо/влево)

	public void play() { // Метод для запуска хода бота

		if (alive)
			stillAlive(); // Если корабль еще жив, продолжаем атаковать вокруг первого попадания
		else {
			notAlive(); // Если корабль уничтожен, выбираем новую случайную клетку для атаки
		}

		gameOverCheck(); // Проверка условия окончания игры
	}

	public void stillAlive() { // Действия, если корабль еще жив
		Boolean hitFlag = true; // Флаг для проверки успешности выстрела
		Cell randomCell = Baza.playerBoard.getCell(firstHit.getXCord(), firstHit.getYCord()); // Получаем клетку вокруг первого попадания
		while (hitFlag) {
			if (!firstHit.ship.isVertical()) { // Если корабль не вертикален
				if (right)
					randomCell = goLeft(randomCell); // Направляем атаку влево
				if (!right)
					randomCell = goRight(randomCell); // Иначе направляем атаку вправо
			} else {
				if (up)
					randomCell = goDown(randomCell); // Если корабль вертикален и направлен вверх, нападаем вниз
				if (!up) {
					randomCell = goUp(randomCell); // Иначе нападаем вверх
				}
			}
			hitFlag = randomCell.shoot(Baza.playerBoard); // Выполняем выстрел в клетку
			System.out.println(randomCell);
		}
		if (!aliveCheck(firstHit)) { // Проверяем, не уничтожен ли корабль после выстрела
			alive = false; // Если корабль уничтожен, меняем флаг
		}
		if (aliveCheck(firstHit)) { // Если корабль еще жив
			alive = true;
		}
	}

	private void notAlive() { // Действия, если корабль уничтожен
		Boolean hitFlag = false; // Флаг для проверки успешности выстрела
		Cell randomCell = getRandomCell(); // Получаем случайную клетку для атаки
		randomCell = Baza.playerBoard.getCell(randomCell.getXCord(), randomCell.getYCord()); // Получаем объект клетки из доски игрока
		firstHit = Baza.playerBoard.getCell(randomCell.getXCord(), randomCell.getYCord()); // Задаем первое попадание в случайную клетку
		firstHit = randomCell;
		while(randomCell.wasShot) { // Пока клетка уже подстрелена
			randomCell = getRandomCell(); // Получаем новую случайную клетку
			firstHit = randomCell; // Обновляем первое попадание
			randomCell = Baza.playerBoard.getCell(randomCell.getXCord(), randomCell.getYCord()); // Получаем объект клетки из доски игрока
			firstHit = Baza.playerBoard.getCell(randomCell.getXCord(), randomCell.getYCord()); // Задаем первое попадание в случайную клетку
		}
		hitFlag = randomCell.shoot(Baza.playerBoard); // Выполняем выстрел в случайную клетку
		if (!hitFlag) {
			alive = false; // Если выстрел промахнулся, корабль считается уничтоженным
			return;
		}
		if (firstHit.getYCord() < 4.5) { // Если координата Y первого попадания меньше 4.5
			up = false; // Устанавливаем направление атаки вниз
		} else {
			up = true; // Иначе устанавливаем направление атаки вверх
		}
		if (firstHit.getXCord() < 4.5) { // Если координата X первого попадания меньше 4.5
			right = true; // Устанавливаем направление атаки вправо
		} else {
			right = false; // Иначе устанавливаем направление атаки влево
		}
		while (hitFlag) { // Пока успешно попадаем в корабль
			if (!firstHit.ship.isVertical()) { // Если корабль не вертикален
				if (right)
					randomCell = goRight(randomCell); // Нападаем вправо
				if (!right)
					randomCell = goLeft(randomCell); // Иначе нападаем влево
			} else {
				if (up)
					randomCell = goUp(randomCell); // Если корабль вертикален и направлен вверх, нападаем вверх
				if (!up)
					randomCell = goDown(randomCell); // Иначе нападаем вниз
			}
			hitFlag = randomCell.shoot(Baza.playerBoard); // Выполняем выстрел
		}
		if (!aliveCheck(firstHit)) { // Проверяем, не уничтожен ли корабль после выстрела
			alive = false; // Если корабль уничтожен, меняем флаг
		}
		if (aliveCheck(firstHit)) { // Если корабль еще жив
			alive = true;
		}
	}

	private boolean aliveCheck(Cell c) { // Проверка, жив ли корабль
		if (!nullCheck(c)) // Проверяем, не является ли клетка пустой
			if (c.ship.isAlive()) // Проверяем, не уничтожен ли корабль, находящийся в клетке
				return true; // Возвращаем true, если корабль жив
		return false; // Возвращаем false, если корабль уничтожен или клетка пуста
	}

	private Cell getRandomCell() { // Получение случайной клетки для атаки
		Cell c = Cell.availableCells.get(r.nextInt(Cell.availableCells.size())); // Выбираем случайную клетку из списка доступных клеток
		while (c.wasShot) { // Пока выбранная клетка уже подстрелена
			c = Cell.availableCells.get(r.nextInt(Cell.availableCells.size())); // Выбираем новую случайную клетку
		}
		return c; // Возвращаем выбранную случайную клетку
	}

	public void gameOverCheck() { // Проверка условия окончания игры
		if (Baza.playerBoard.ships <= 0) { // Если количество кораблей игрока меньше или равно 0
			Alert alert = new Alert(AlertType.INFORMATION); // Создаем информационное диалоговое окно
			alert.setTitle("Результат игры"); // Устанавливаем заголовок окна
			alert.setHeaderText("Вас победил БОТ за " + Baza.moveCounter + " ходов!"); // Устанавливаем текст заголовка
			alert.setOnCloseRequest(new EventHandler<DialogEvent>() { // Устанавливаем действие при закрытии окна
				@Override
				public void handle(DialogEvent event) {
					System.exit(0); // Закрываем приложение при закрытии окна
				}
			});
			alert.showAndWait(); // Отображаем окно и ждем его закрытия
		}
	}

	private Cell goUp(Cell c) { // Движение вверх от заданной клетки
		int x = c.getXCord(); // Получаем координату X клетки
		int y = c.getYCord(); // Получаем координату Y клетки

		if (y == 0) { // Если координата Y достигла верхней границы доски
			return goDown(c); // Возвращаемся вниз
		}

		c = new Cell(x, y); // Создаем новый объект клетки с текущими координатами
		c = Baza.playerBoard.getCell(x, --y); // Получаем клетку на одну строку выше
		while (c.wasShot) { // Пока выбранная клетка уже подстрелена
			c = getRandomCell(); // Выбираем новую случайную клетку
			c = Baza.playerBoard.getCell(c.getXCord(), c.getYCord()); // Получаем объект клетки из доски игрока
		}

		return c; // Возвращаем новую выбранную клетку
	}

	private Cell goDown(Cell c) { // Движение вниз от заданной клетки
		int x = c.getXCord(); // Получаем координату X клетки
		int y = c.getYCord(); // Получаем координату Y клетки
		if (y == 9) { // Если координата Y достигла нижней границы доски
			return goUp(c); // Возвращаемся вверх
		}
		c = new Cell(x, y); // Создаем новый объект клетки с текущими координатами
		c = Baza.playerBoard.getCell(x, ++y); // Получаем клетку на одну строку ниже
		while (c.wasShot) { // Пока выбранная клетка уже подстрелена
			c = getRandomCell(); // Выбираем новую случайную клетку
			c = Baza.playerBoard.getCell(c.getXCord(), c.getYCord()); // Получаем объект клетки из доски игрока
		}
		return c; // Возвращаем новую выбранную клетку
	}

	private Cell goRight(Cell c) { // Движение вправо от заданной клетки
		int x = c.getXCord(); // Получаем координату X клетки
		int y = c.getYCord(); // Получаем координату Y клетки
		if (x == 9) { // Если координата X достигла правой границы доски
			return goLeft(c); // Возвращаемся влево
		}
		c = new Cell(x, y); // Создаем новый объект клетки с текущими координатами
		c = Baza.playerBoard.getCell(++x, y); // Получаем клетку на один столбец правее
		while (c.wasShot) { // Пока выбранная клетка уже подстрелена
			c = getRandomCell(); // Выбираем новую случайную клетку
			c = Baza.playerBoard.getCell(c.getXCord(), c.getYCord()); // Получаем объект клетки из доски игрока
		}
		return c; // Возвращаем новую выбранную клетку
	}

	private Cell goLeft(Cell c) { // Движение влево от заданной клетки
		int x = c.getXCord(); // Получаем координату X клетки
		int y = c.getYCord(); // Получаем координату Y клетки
		if (x == 0) { // Если координата X достигла левой границы доски
			return goRight(c); // Возвращаемся вправо
		}
		c = new Cell(x, y); // Создаем новый объект клетки с текущими координатами
		c = Baza.playerBoard.getCell(--x, y); // Получаем клетку на один столбец левее
		while (c.wasShot) { // Пока выбранная клетка уже подстрелена
			c = getRandomCell(); // Выбираем новую случайную клетку
			c = Baza.playerBoard.getCell(c.getXCord(), c.getYCord()); // Получаем объект клетки из доски игрока
		}
		return c; // Возвращаем новую выбранную клетку
	}

	private Boolean nullCheck(Cell c) { // Проверка, является ли клетка пустой (null)
		if (c == null) { // Если клетка не инициализирована
			return true; // Возвращаем true
		} else
			return false; // Иначе возвращаем false
	}
}
