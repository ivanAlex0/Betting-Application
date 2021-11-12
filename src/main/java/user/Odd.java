package user;

public class Odd {
    private final float odd;
    private final String Team1;
    private final String Team2;
    private final String oddType;
    private String dateTime;

    public Odd(Float odd, String t1, String t2, String oddType, String dateTime) {
        this.odd = odd;
        this.Team1 = t1;
        this.Team2 = t2;
        this.oddType = oddType;
        this.dateTime = dateTime;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getOddType() {
        return oddType;
    }

    public float getOdd() {
        return odd;
    }

    public String getTeam1() {
        return Team1;
    }

    public String getTeam2() {
        return Team2;
    }
}
