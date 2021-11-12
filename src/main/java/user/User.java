package user;

import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private final String CNP;
    private String name;
    private float balance;
    private boolean admin;
    private ArrayList<League> leagues;
    private ArrayList<Bet> bets;
    private int id;

    public User(String username, String password, String CNP, String name, float balance, boolean admin, ArrayList<League> leagues, ArrayList<Bet> bets, int id) {
        this.username = username;
        this.password = password;
        this.CNP = CNP;
        this.name = name;
        this.balance = balance;
        this.admin = admin;
        this.leagues = leagues;
        this.bets = bets;
        this.id = id;
    }

    public ArrayList<Bet> getBets() {
        return bets;
    }

    public void setBets(ArrayList<Bet> bets) {
        this.bets = bets;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<League> getLeagues() {
        return leagues;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getCNP() {
        return CNP;
    }

    public String getName() {
        return name;
    }

    public float getBalance() {
        return balance;
    }

    public boolean isAdmin() {
        return admin;
    }
}
