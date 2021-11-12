package user;

import java.sql.Date;
import java.util.ArrayList;

public class Bet {
    private ArrayList<Odd> allOdds;
    private float amountBet, totalOdd;
    private Date date;
    private boolean won;

    public Bet(ArrayList<Odd> allOdds, float amountBet, float totalOdd, Date date, boolean won) {
        this.allOdds = allOdds;
        this.amountBet = amountBet;
        this.totalOdd = totalOdd;
        this.date = date;
        this.won = won;
    }

    public ArrayList<Odd> getAllOdds() {
        return allOdds;
    }

    public float getAmountBet() {
        return amountBet;
    }

    public float getTotalOdd() {
        return totalOdd;
    }

    public Date getDate() {
        return date;
    }

    public boolean isWon() {
        return won;
    }
}
