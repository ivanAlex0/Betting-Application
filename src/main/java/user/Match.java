package user;

import java.util.ArrayList;

public class Match {
    private final String team1;
    private final String team2;
    private final ArrayList<Odd> odds;
    private final String dateTime;

    public Match(String t1, String t2, ArrayList<Odd> odds, String commenceTime) {
        this.team1 = t1;
        this.team2 = t2;
        this.odds = odds;
        this.dateTime = commenceTime;
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public ArrayList<Odd> getOdds() {
        return odds;
    }

    public Odd getOdd(int index) {
        return this.odds.get(index);
    }

    public String getDateTime() {
        return dateTime;
    }
}
