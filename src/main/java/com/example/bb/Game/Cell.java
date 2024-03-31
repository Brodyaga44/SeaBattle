package com.example.bb.Game;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.LinkedList;

public class Cell extends Rectangle {
	public int x, y;
	public Ship ship = null;
	public boolean wasShot = false;
	public static LinkedList<Cell> availableCells = new LinkedList();

	private Board board;

	public Cell(int x, int y, Board board) {

		super(35, 35);
		this.x = x;
		this.y = y;
		this.board = board;
		availableCells.add(this);
		setFill(Color.LIGHTBLUE); //
		setStroke(Color.GRAY);
	}

	public Cell(int x, int y) {
		this.x = x;
		this.y = y;

	}

	public Ship getShip() {
		return ship;
	}

	public int getXCord() {
		return x;
	}

	public void setXCord(int x) {
		this.x = x;
	}

	public int getYCord() {
		return y;
	}

	public void setCord(int y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "Cell [x=" + x + ", y=" + y + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((board == null) ? 0 : board.hashCode());
		result = prime * result + ((ship == null) ? 0 : ship.hashCode());
		result = prime * result + (wasShot ? 1231 : 1237);
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cell other = (Cell) obj;
		if (board == null) {
			if (other.board != null)
				return false;
		} else if (!board.equals(other.board))
			return false;
		if (ship == null) {
			if (other.ship != null)
				return false;
		} else if (!ship.equals(other.ship))
			return false;
		if (wasShot != other.wasShot)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	public boolean shoot(Board board) {
		//Dodelat?
		int checker[][] = { {-1,-1}, {-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1} };


		System.out.println("////");
		System.out.println(x);
		System.out.println(y);
		if(getFill() == Color.LIGHTBLUE || getFill() == Color.DARKOLIVEGREEN){
			setFill(Color.DARKGRAY);
			wasShot = true;
			if (ship != null ) {
				ship.hit();
				setFill(Color.DARKRED);
				availableCells.remove(this);
				if (!ship.isAlive()) {
					int shipSize = ship.type;
					Cell firstCell = board.getCell(x,y);
					if (shipSize == 1)
					{
						///
						for (int[] item: checker) {
							if (board.isValidPoint(x + item[0], y + item[1])) {
								Cell fillCell = board.getCell(x + item[0], y + item[1]);
								if (fillCell.getFill() != Color.DARKRED) {
									fillCell.setFill(Color.DARKGRAY);
									availableCells.remove(fillCell);
								}
							}
						}
						///
					}
					else if (ship.vertical)
					{
						if (y == 0) {
							firstCell = board.getCell(x, y);
						} else {
							int currY = y - 1;
							if (board.isValidPoint(x, currY)) {
								firstCell = board.getCell(x, currY);
								while (firstCell.getFill() == Color.DARKRED) {
									currY -= 1;
									if (board.isValidPoint(x, currY)) {
										firstCell = board.getCell(x, currY);
									}
								}
								if (board.isValidPoint(firstCell.x, firstCell.y + 1)) {
									firstCell.y += 1;
								}
							}
						}
						for (int i = 0; i < ship.type; i++) {
							if (board.isValidPoint(firstCell.x, firstCell.y + i)) {
								Cell nextCell = board.getCell(firstCell.x, firstCell.y + i);
								for (int[] item: checker) {
									if (board.isValidPoint(nextCell.x + item[0], nextCell.y + item[1])) {
										Cell fillCell = board.getCell(nextCell.x + item[0], nextCell.y + item[1]);
										if (fillCell.getFill() != Color.DARKRED) {
											fillCell.setFill(Color.DARKGRAY);
											availableCells.remove(fillCell);
										}
									}
								}
							}
						}
					}
					//////////
					else if (!ship.vertical)
					{
						if (x == 0) {
							firstCell = board.getCell(x, y);
						} else {
							int currX = x - 1;
							if (board.isValidPoint(currX, y)) {
								firstCell = board.getCell(currX, y);
								while (firstCell.getFill() == Color.DARKRED) {
									currX -= 1;
									if (board.isValidPoint(currX, y)) {
										firstCell = board.getCell(currX, y);
									}
								}
								if (board.isValidPoint(firstCell.x + 1, firstCell.y)) {
									firstCell.x += 1;
								}
							}
						}//
						for (int i = 0; i < ship.type; i++) {
							if (board.isValidPoint(firstCell.x + i, firstCell.y)) {
								Cell nextCell = board.getCell(firstCell.x + i, firstCell.y);
								for (int[] item: checker) {
									if (board.isValidPoint(nextCell.x + item[0], nextCell.y + item[1])) {
										Cell fillCell = board.getCell(nextCell.x + item[0], nextCell.y + item[1]);
										if (fillCell.getFill() != Color.DARKRED) {
											fillCell.setFill(Color.DARKGRAY);
											availableCells.remove(fillCell);
										}
									}
								}
							}
						}
					}
					/////////////

					board.ships--;
				}
				return true;
			}
		}

		return false;
	}
}
