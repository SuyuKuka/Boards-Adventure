package model.config;

import java.util.ArrayList;

import model.config.role.Role;

public class Player {
    private static int lastid = 0;
    private int id;
    private int money = 0;
    private int nbkingturn=0;
    private Role role;
    private ArrayList<Property> proprietes = new ArrayList<>();

    public Player() {
        lastid++;
        id=lastid;
    }
    public void addMoney(int amount){this.money+=amount;}
    public void addProperty(Property property){this.proprietes.add(property);}
    public void addKingTurn(){this.nbkingturn++;}
    public void removeProperty(Property property){this.proprietes.remove(property);}
    public int getMoney(){return this.money;}
    public int getNbKingTurn(){return this.nbkingturn;}
    public Role getRole(){return this.role;}
    public void setRole(Role role){this.role=role;}
    public boolean hasEnoughMoney(int price){return this.money>=price;}

}