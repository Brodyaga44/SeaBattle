package com.example.bb.Game;

import javafx.scene.Parent;

	public class Ship extends Parent {
	    public int type; // Тип корабля
	    public boolean vertical = true; // Вертикальный ли кораль

	    private int health; // Количество "жизней" корабля

	    public Ship(int type, boolean vertical) { // Информация о корабле
	        this.type = type; // количество палуб
	        this.vertical = vertical; // Вертикальность/горизонтальность корабля
	        health = type; // Присвоение кораблю количества жизней
	    }

	    public void hit() {
	        health--;
	    } // Попадание по кораблю

	    public boolean isAlive() { // Проверка жив ли корабль
	    	if(health>0) return true;
	    	else {
	    		return false;
	    	}
	    }

		public boolean isVertical() {
			return vertical;
		} // Проверка вертикален ли корабль
	}

