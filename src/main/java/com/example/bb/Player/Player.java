package com.example.bb.Player;

public class Player implements PlayerInterface{
    private String name;
    int score;
    public String getName(){return name;} //Получение имени игрока
    public Player(String name){this.name = name;} // Установка имени игрока

}
