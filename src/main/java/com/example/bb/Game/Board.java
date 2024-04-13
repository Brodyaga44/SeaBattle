package com.example.bb.Game;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Board extends Parent {
	private VBox rows = new VBox(); // Вертикальный контейнер для строк
	private boolean enemy = false; // Флаг, указывающий, является ли доска противником
	public int ships = 10; // Количество кораблей на доске

	public Board(boolean enemy, EventHandler<? super MouseEvent> handler) {
		this.enemy = enemy; // Установка флага противника
		for (int y = 0; y < 10; y++) { // Проход по строкам
			HBox row = new HBox(); // Создание горизонтальной строки
			for (int x = 0; x < 10; x++) { // Проход по столбцам
				Cell c = new Cell(x, y, this); // Создание ячейки
				c.setOnMouseClicked(handler); // Установка обработчика событий клика мыши
				row.getChildren().add(c); // Добавление ячейки в строку
			}

			rows.getChildren().add(row); // Добавление строки в контейнер строк
		}

		getChildren().add(rows); // Добавление контейнера строк в родительский элемент
	}

	@Override
	public int hashCode() {
		final int prime = 31; // Константа для хэширования
		int result = 1; // Начальное значение результата
		result = prime * result + (enemy ? 1231 : 1237); // Хэширование флага противника
		result = prime * result + ((rows == null) ? 0 : rows.hashCode()); // Хэширование контейнера строк
		result = prime * result + ships; // Хэширование количества кораблей
		return result; // Возврат результата
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) // Если объекты равны по ссылке, они равны
			return true;
		if (obj == null) // Если переданный объект null, они не равны
			return false;
		if (getClass() != obj.getClass()) // Если классы объектов разные, они не равны
			return false;
		Board other = (Board) obj; // Приведение объекта к типу Board
		if (enemy != other.enemy) // Проверка равенства флагов противника
			return false;
		if (rows == null) { // Если текущий контейнер строк null
			if (other.rows != null) // Если контейнер строк объекта other не null, они не равны
				return false;
		} else if (!rows.equals(other.rows)) // Если контейнеры строк не равны
			return false;
		if (ships != other.ships) // Проверка равенства количества кораблей
			return false;
		return true; // Объекты равны
	}

	public boolean placeShip(Ship ship, int x, int y) {
		if (canPlaceShip(ship, x, y)) { // Проверка возможности размещения корабля
			int length = ship.type; // Длина корабля

			if (ship.vertical) { // Если корабль вертикальный
				for (int i = y; i < y + length; i++) { // Проход по ячейкам, занимаемым кораблем
					Cell cell = getCell(x, i); // Получение ячейки
					cell.ship = ship; // Присваивание ячейке корабля

					if (!enemy) { // Если доска не противник
						cell.setFill(Color.DARKOLIVEGREEN); // Заполнение ячейки цветом
						cell.setStroke(Color.BLACK); // Установка цвета границы ячейки
					}
				}
			} else { // Если корабль горизонтальный
				for (int i = x; i < x + length; i++) { // Проход по ячейкам, занимаемым кораблем
					Cell cell = getCell(i, y); // Получение ячейки
					cell.ship = ship; // Присваивание ячейке корабля

					if (!enemy) { // Если доска не противник
						cell.setFill(Color.DARKOLIVEGREEN); // Заполнение ячейки цветом
						cell.setStroke(Color.BLACK); // Установка цвета границы ячейки
					}
				}
			}

			return true; // Возврат успешного размещения корабля
		}

		return false; // Возврат неудачи размещения корабля
	}

	public Cell getCell(int x, int y) {
		return (Cell) ((HBox) rows.getChildren().get(y)).getChildren().get(x); // Получение ячейки по координатам
	}

	public Cell[] getNeighbors(int x, int y) {
		Point2D[] points = new Point2D[]{new Point2D(x, y), new Point2D(x, y), new Point2D(x, y), new Point2D(x, y)}; // Массив точек-соседей

		List<Cell> neighbors = new ArrayList<Cell>(); // Список соседних ячеек

		for (Point2D p : points) { // Проход по точкам-соседям
			if (isValidPoint(p)) { // Проверка на валидность точки
				neighbors.add(getCell((int) p.getX(), (int) p.getY())); // Добавление соседней ячейки в список
			}
		}

		return neighbors.toArray(new Cell[0]); // Возврат массива соседних ячеек
	}

	private boolean checkNear(int k, int i) {
		int cheker[][] = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 0}, {0, 1}, {1, -1}, {1, 0}, {1, 1}}; // Массив смещений для проверки соседей
		for (int[] item : cheker) { // Проход по массиву смещений
			for (Cell neighbor : getNeighbors(k + item[0], i + item[1])) { // Проход по соседним ячейкам
				if (!isValidPoint(k + item[0], i + item[1])) // Если точка не валидна
					return false;
				if (neighbor.ship != null) { // Если соседняя ячейка занята кораблем
					return false; // Возврат false
				}
			}
		}
		return true; // Возврат true, если соседи свободны
	}

	private boolean canPlaceShip(Ship ship, int x, int y) {
		int length = ship.type; // Длина корабля

		if (ship.vertical) { // Если корабль вертикальный
			for (int i = y; i < y + length; i++) { // Проход по ячейкам, которые займет корабль
				if (!isValidPoint(x, i)) // Если ячейка не валидна
					return false; // Возврат false

				Cell cell = getCell(x, i); // Получение ячейки
				if (cell.ship != null) // Если ячейка уже занята кораблем
					return false; // Возврат false

				////////////////////////////////////////////////////
				if (!checkNear(x,i)) {return false;} // Проверка соседних ячеек
				////////////////////////////////////////////////////
			}
		} else { // Если корабль горизонтальный
			for (int i = x; i < x + length; i++) { // Проход по ячейкам, которые займет корабль
				if (!isValidPoint(i, y)) // Если ячейка не валидна
					return false; // Возврат false

				Cell cell = getCell(i, y); // Получение ячейки
				if (cell.ship != null) // Если ячейка уже занята кораблем
					return false; // Возврат false

				////////////////////////////////////////////////////
				if (!checkNear(i,y)) {return false;} // Проверка соседних ячеек
				////////////////////////////////////////////////////
			}
		}

		return true; // Возврат true, если корабль может быть размещен
	}

	private boolean isValidPoint(Point2D point) {
		return isValidPoint(point.getX(), point.getY()); // Проверка валидности точки
	}

	public boolean isValidPoint(double x, double y) {
		return x >= 0 && x <= 9 && y >= 0 && y <= 9; // Проверка валидности координаты
	}

}
