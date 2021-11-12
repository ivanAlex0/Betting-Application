package user;

import org.json.JSONArray;

import java.util.ArrayList;

public class League {
    private ArrayList<Match> games;
    private final String name;
    private final String group;

    public League(String name, String group) {
        this.name = name;
        this.group = group;
    }

    /**
     * @param matches - the JSONArray that contains all the matches for the current league
     */
    public void setGames(JSONArray matches) {
        this.games = new ArrayList<>();
        for (int iter = 0; iter < matches.length(); iter++) {
            String team1 = matches.getJSONObject(iter).getJSONArray("teams").getString(0);
            String team2 = matches.getJSONObject(iter).getJSONArray("teams").getString(1);
            String commenceTime = matches.getJSONObject(iter).getString("commence_time");
            JSONArray odds = matches.getJSONObject(iter).getJSONArray("sites").getJSONObject(0).getJSONObject("odds").getJSONArray("h2h");
            ArrayList<Odd> currentOdds = new ArrayList<>();
            String date = commenceTime.substring(0, 10);
            int time = Integer.parseInt(commenceTime.substring(11, 13));
            time += 2;
            String dt = time + commenceTime.substring(13, 16);
            String dateTime = date + " : " + dt;
            for (int i = 0; i < odds.length(); i++) {
                String oddType;
                if (i == 0)
                    oddType = "1";
                else if (i == 1)
                    oddType = "X";
                else oddType = "2";
                Odd currentOdd = new Odd(Float.parseFloat(odds.get(i).toString()), team1, team2, oddType, dateTime);
                currentOdds.add(currentOdd);
            }
            if (currentOdds.size() < 3) {
                Odd nullOdd = new Odd(0.0f, team1, team2, "X", dateTime);
            }
            Match curr = new Match(team1, team2, currentOdds, dateTime);
            this.games.add(curr);
        }
    }

    public ArrayList<Match> getGames() {
        return games;
    }

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }
}
