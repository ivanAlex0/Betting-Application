package user;

import json.JSONDecode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Scraping {
    private ArrayList<League> leagues;

    /**
     * The function fetches all the Leagues with the corresponding Matches and Odds from the JSON file provided by the API.
     */
    public void getLeaguesFromJson() {
        this.leagues = new ArrayList<>();
        JSONObject jsonLeagues = null;
        String API_KEY = "04bcb58c7a109a6f7945016e3b26cbff";

        String url = "https://api.the-odds-api.com/v3/sports?apiKey=" + API_KEY;
        try {
            jsonLeagues = JSONDecode.getJsonFromHttp(url);
        } catch (Exception exception) {
            System.out.println("The JSONObject could not be instantiated.. try again");
        }

        assert jsonLeagues != null;
        JSONArray dataLeagues = jsonLeagues.getJSONArray("data");

        for (int iter = 0; iter < dataLeagues.length(); iter++) {
            String key = dataLeagues.getJSONObject(iter).getString("key");
            if (key.contains("soccer_epl") || key.contains("soccer_france_ligue_one") || (key.contains("soccer_germany_bundesliga") && !key.equals("soccer_germany_bundesliga2")) ||
                    key.contains("soccer_italy_serie_a") || key.contains("soccer_spain_la_liga") ||
                    key.contains("soccer_uefa_champs_league") || key.contains("soccer_uefa_europa_league") ||
                    key.contains("soccer_turkey_super_league") || key.contains("soccer_spain_segunda_division")) {
                String group = dataLeagues.getJSONObject(iter).getString("group");
                String name = dataLeagues.getJSONObject(iter).getString("details");
                League curr = new League(name, group);

                JSONObject jsonLeagueCurrent = null;

                url = "https://api.the-odds-api.com/v3/odds/?sport=" + key + "&region=uk&dateFormat=iso&apiKey=" + API_KEY;
                try {
                    jsonLeagueCurrent = JSONDecode.getJsonFromHttp(url);
                } catch (Exception exception) {
                    System.out.println("The JSONObject could not be instantiated.. try again");
                }

                System.out.println(iter);
                assert jsonLeagueCurrent != null;
                JSONArray dataCurrent = jsonLeagueCurrent.getJSONArray("data");
                curr.setGames(dataCurrent);

                this.leagues.add(curr);
            }
        }

    }

    public ArrayList<League> getLeagues() {
        return this.leagues;
    }
}
