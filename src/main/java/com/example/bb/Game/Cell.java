package com.example.bb.Game;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.LinkedList;

public class Cell extends Rectangle {
	public int x, y; // Координаты ячейки
	public Ship ship = null; // Корабль, который может занимать ячейку
	public boolean wasShot = false; // Флаг, указывающий, была ли ячейка атакована
	public static LinkedList<Cell> availableCells = new LinkedList(); // Список доступных ячеек

	private Board board; // Доска, на которой находится ячейка


	// Конструктор для ячейки с указанием координат и привязкой к доске
	public Cell(int x, int y, Board board) {
		super(35, 35); // Вызов конструктора родительского класса Rectangle
		this.x = x; // Установка координаты x
		this.y = y; // Установка координаты y
		this.board = board; // Установка доски, на которой находится ячейка
		availableCells.add(this); // Добавление ячейки в список доступных
		setFill(Color.LIGHTBLUE); // Установка цвета заливки ячейки
		setStroke(Color.GRAY); // Установка цвета границы ячейки
	}

	// Конструктор для ячейки с указанием координат (без привязки к доске)
	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}

	// Получение корабля в ячейке
	public Ship getShip() {
		return ship;
	}

	// Получение координаты x
	public int getXCord() {
		return x;
	}

	// Установка координаты x
	public void setXCord(int x) {
		this.x = x;
	}

	// Получение координаты y
	public int getYCord() {
		return y;
	}

	// Установка координаты y
	public void setCord(int y) {
		this.y = y;
	}

	// Переопределение метода toString для вывода информации о координатах ячейки
	@Override
	public String toString() {
		return "Cell [x=" + x + ", y=" + y + "]";
	}

	// Переопределение метода hashCode для правильной работы хеш-таблиц
	@Override
	public int hashCode() {
		final int prime = 31; // Константа для хэширования
		int result = 1; // Начальное значение результата
		result = prime * result + ((board == null) ? 0 : board.hashCode()); // Хэширование доски
		result = prime * result + ((ship == null) ? 0 : ship.hashCode()); // Хэширование корабля
		result = prime * result + (wasShot ? 1231 : 1237); // Хэширование флага wasShot
		result = prime * result + x; // Хэширование координаты x
		result = prime * result + y; // Хэширование координаты y
		return result; // Возврат результата
	}

	// Переопределение метода equals для корректного сравнения ячеек
	@Override
	public boolean equals(Object obj) {
		if (this == obj) // Если объекты равны по ссылке, они равны
			return true;
		if (obj == null) // Если переданный объект null, они не равны
			return false;
		if (getClass() != obj.getClass()) // Если классы объектов разные, они не равны
			return false;
		Cell other = (Cell) obj; // Приведение объекта к типу Cell
		if (board == null) { // Если текущая доска null
			if (other.board != null) // Если доска объекта other не null, они не равны
				return false;
		} else if (!board.equals(other.board)) // Если доски не равны
			return false;
		if (ship == null) { // Если текущий корабль null
			if (other.ship != null) // Если корабль объекта other не null, они не равны
				return false;
		} else if (!ship.equals(other.ship)) // Если корабли не равны
			return false;
		if (wasShot != other.wasShot) // Если флаги wasShot не равны
			return false;
		if (x != other.x) // Если координаты x не равны
			return false;
		if (y != other.y) // Если координаты y не равны
			return false;
		return true; // Объекты равны
	}

	// Метод для атаки по ячейке
	public boolean shoot(Board board) {
		int checker[][] = { {-1,-1}, {-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1} }; // Массив смещений для проверки соседних ячеек

		if(getFill() == Color.LIGHTBLUE || getFill() == Color.DARKOLIVEGREEN){ // Если ячейка не была атакована ранее или занята кораблем
			setFill(Color.DARKGRAY); // Установка цвета заливки ячейки
			wasShot = true; // Пометка ячейки как атакованной
			if (ship != null ) { // Если в ячейке есть корабль
				ship.hit(); // Попадание по кораблю
				setFill(Color.DARKRED); // Установка цвета заливки атакованной ячейки
				availableCells.remove(this); // Удаление ячейки из списка доступных
				if (!ship.isAlive()) { // Если корабль уничтожен
					int shipSize = ship.type; // Получение размера корабля
					Cell firstCell = board.getCell(x,y); // Получение первой ячейки корабля
					if (shipSize == 1) { // Если корабль состоит из одной ячейки
						for (int[] item: checker) { // Проход по массиву смещений
							if (board.isValidPoint(x + item[0], y + item[1])) { // Если соседняя ячейка существует
								Cell fillCell = board.getCell(x + item[0], y + item[1]); // Получение соседней ячейки
								if (fillCell.getFill() != Color.DARKRED) { // Если ячейка не была атакована ранее
									fillCell.setFill(Color.DARKGRAY); // Установка цвета заливки ячейки
									availableCells.remove(fillCell); // Удаление ячейки из списка доступных
								}
							}
						}
					}
					else if (ship.vertical) { // Если корабль вертикальный
						if (y == 0) { // Если координата y первой ячейки равна 0
							firstCell = board.getCell(x, y); // Получение первой ячейки корабля
						} else { // Иначе
							int currY = y - 1; // Уменьшение координаты y на 1
							if (board.isValidPoint(x, currY)) { // Если координаты ячейки в пределах доски
								firstCell = board.getCell(x, currY); // Получение первой ячейки корабля
								while (firstCell.getFill() == Color.DARKRED) { // Пока ячейка была атакована ранее
									currY -= 1; // Уменьшение координаты y на 1
									if (board.isValidPoint(x, currY)) { // Если координаты ячейки в пределах доски
										firstCell = board.getCell(x, currY); // Получение первой ячейки корабля
									}
								}
								if (board.isValidPoint(firstCell.x, firstCell.y + 1)) { // Если существует ячейка ниже
									firstCell.y += 1; // Увеличение координаты y на 1
								}
							}
						}
						for (int i = 0; i < ship.type; i++) { // Проход по ячейкам корабля
							if (board.isValidPoint(firstCell.x, firstCell.y + i)) { // Если ячейка в пределах доски
								Cell nextCell = board.getCell(firstCell.x, firstCell.y + i); // Получение следующей ячейки
								for (int[] item: checker) { // Проход по массиву смещений
									if (board.isValidPoint(nextCell.x + item[0], nextCell.y + item[1])) { // Если существует соседняя ячейка
										Cell fillCell = board.getCell(nextCell.x + item[0], nextCell.y + item[1]); // Получение соседней ячейки
										if (fillCell.getFill() != Color.DARKRED) { // Если ячейка не была атакована ранее
											fillCell.setFill(Color.DARKGRAY); // Установка цвета заливки ячейки
											availableCells.remove(fillCell); // Удаление ячейки из списка доступных
										}
									}
								}
							}
						}
					}
					else if (!ship.vertical) { // Если корабль горизонтальный
						if (x == 0) { // Если координата x первой ячейки равна 0
							firstCell = board.getCell(x, y); // Получение первой ячейки корабля
						} else { // Иначе
							int currX = x - 1; // Уменьшение координаты x на 1
							if (board.isValidPoint(currX, y)) { // Если координаты ячейки в пределах доски
								firstCell = board.getCell(currX, y); // Получение первой ячейки корабля
								while (firstCell.getFill() == Color.DARKRED) { // Пока ячейка была атакована ранее
									currX -= 1; // Уменьшение координаты x на 1
									if (board.isValidPoint(currX, y)) { // Если координаты ячейки в пределах доски
										firstCell = board.getCell(currX, y); // Получение первой ячейки корабля
									}
								}
								if (board.isValidPoint(firstCell.x + 1, firstCell.y)) { // Если существует ячейка справа
									firstCell.x += 1; // Увеличение координаты x на 1
								}
							}
						}
						for (int i = 0; i < ship.type; i++) { // Проход по ячейкам корабля
							if (board.isValidPoint(firstCell.x + i, firstCell.y)) { // Если ячейка в пределах доски
								Cell nextCell = board.getCell(firstCell.x + i, firstCell.y); // Получение следующей ячейки
								for (int[] item: checker) { // Проход по массиву смещений
									if (board.isValidPoint(nextCell.x + item[0], nextCell.y + item[1])) { // Если существует соседняя ячейка
										Cell fillCell = board.getCell(nextCell.x + item[0], nextCell.y + item[1]); // Получение соседней ячейки
										if (fillCell.getFill() != Color.DARKRED) { // Если ячейка не была атакована ранее
											fillCell.setFill(Color.DARKGRAY); // Установка цвета заливки ячейки
											availableCells.remove(fillCell); // Удаление ячейки из списка доступных
										}
									}
								}
							}
						}
					}
					board.ships--; // Уменьшение количества оставшихся кораблей на доске
				}
				return true; // Возврат true, если было попадание по кораблю
			}
		}
		return false; // Возврат false, если ячейка уже была атакована или не занята кораблем
	}
}