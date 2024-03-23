package com.example.bb.Player;

public class Player implements PlayerInterface{
    private String name;
    int score;
    public String getName(){return name;}
    public int getScore(){return score;}
    public Player(String name){this.name = name;}

}
