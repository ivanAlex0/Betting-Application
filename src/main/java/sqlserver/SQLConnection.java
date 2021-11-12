package sqlserver;

import sample.SceneController;
import user.Bet;
import user.Odd;
import user.User;

import java.sql.*;
import java.util.ArrayList;

public class SQLConnection {
    private ArrayList<User> users;
    static String DATABASE_URL = "jdbc:sqlserver://localhost:1433;databaseName=BettingAppDB;";
    static String DATABASE_USERNAME = "alex";
    static String DATABASE_PASSWORD = "1234567";

    public SQLConnection() {
        this.setUsers();
    }

    /**
     * The function inserts the User u in the UserX table from the Database
     */
    public void insertUser(User u) {
        Connection sqlConnection = null;
        try {
            sqlConnection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

            PreparedStatement stmt = sqlConnection.prepareStatement("insert into UserX values (?, ?, ?, ?, ?, ?, 0)");
            stmt.setInt(1, this.users.size() + 1);
            stmt.setString(2, u.getUsername());
            stmt.setString(3, u.getPassword());
            stmt.setString(4, u.getName());
            stmt.setString(5, u.getCNP());
            stmt.setFloat(6, u.getBalance());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (sqlConnection != null && !sqlConnection.isClosed()) {
                    sqlConnection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * @return the last id from the Bets table from the Database
     */
    public int getBetsId(Connection conn) throws SQLException {
        String betsStmtString = "SELECT id FROM Bets";
        Statement betStmt = conn.createStatement();
        ResultSet betsStatement = betStmt.executeQuery(betsStmtString);

        int id = 0;
        while (betsStatement.next()) {
            id = betsStatement.getInt("id");
        }

        return id;
    }

    /**
     * @return the last id from the Odd table from the Database
     */
    public int getOddId(Connection conn) throws SQLException {
        String betsStmtString = "SELECT id FROM Odd";
        Statement betStmt = conn.createStatement();
        ResultSet betsStatement = betStmt.executeQuery(betsStmtString);

        int id = 0;
        while (betsStatement.next()) {
            id = betsStatement.getInt("id");
        }

        return id;
    }

    /**
     * @return the last id from the MapBets table from the Database
     */
    public int getMapId(Connection conn) throws SQLException {
        String betsStmtString = "SELECT id FROM MapBets";
        Statement betStmt = conn.createStatement();
        ResultSet betsStatement = betStmt.executeQuery(betsStmtString);

        int id = 0;
        while (betsStatement.next()) {
            id = betsStatement.getInt("id");
        }
        return id;
    }

    /**
     * The function updates the balance in the Database for the User user
     */
    public void updateBalance(User user) {
        Connection sqlConnection = null;
        try {
            sqlConnection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

            PreparedStatement stmt = sqlConnection.prepareStatement("UPDATE UserX SET balance = ? WHERE id = ?");
            stmt.setFloat(1, user.getBalance());
            stmt.setInt(2, user.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (sqlConnection != null && !sqlConnection.isClosed()) {
                    sqlConnection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * The function inserts the Bet bet in the Bet table from the Database
     */
    public void insertBet(Bet bet, User user) {
        Connection sqlConnection = null;
        try {
            sqlConnection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
            int betId = getBetsId(sqlConnection) + 1;
            if (betId == 0)
                betId++;

            PreparedStatement stmt = sqlConnection.prepareStatement("insert into Bets values (?, ?, ?, ?, ?, ?, ?)");
            stmt.setInt(1, betId);
            stmt.setInt(2, user.getId());
            stmt.setFloat(3, bet.getAmountBet());
            stmt.setFloat(4, bet.getTotalOdd());
            stmt.setFloat(5, 0);
            stmt.setDate(6, bet.getDate());
            stmt.setBoolean(7, bet.isWon());
            stmt.executeUpdate();

            insertOdds(bet.getAllOdds(), betId);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (sqlConnection != null && !sqlConnection.isClosed()) {
                    sqlConnection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * The function inserts all the Odds from the ArrayList in the Odd table from the Database
     */
    public void insertOdds(ArrayList<Odd> odds, int betsId) {
        Connection sqlConnection = null;
        try {
            sqlConnection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

            for (Odd o : odds) {
                int oddId = getOddId(sqlConnection) + 1;
                if (oddId == 0)
                    oddId++;
                PreparedStatement stmtOdd = sqlConnection.prepareStatement("insert into Odd values (?, ?, ?, ?, ?, ?)");
                stmtOdd.setInt(1, oddId);
                stmtOdd.setString(2, o.getTeam1());
                stmtOdd.setString(3, o.getTeam2());
                stmtOdd.setFloat(4, o.getOdd());
                stmtOdd.setString(5, o.getOddType());
                stmtOdd.setString(6, o.getDateTime());
                stmtOdd.executeUpdate();

                int mapId = getMapId(sqlConnection) + 1;
                if (mapId == 0)
                    mapId++;
                PreparedStatement stmtMap = sqlConnection.prepareStatement("insert into MapBets values (?, ?, ?)");
                stmtMap.setInt(1, mapId);
                stmtMap.setInt(2, oddId);
                stmtMap.setInt(3, betsId);
                stmtMap.executeUpdate();

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (sqlConnection != null && !sqlConnection.isClosed()) {
                    sqlConnection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * The function fetches all the Users from the UserX table from the Database
     */
    public void setUsers() {
        ArrayList<User> users = new ArrayList<>();
        Connection sqlConnection = null;
        try {
            sqlConnection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

            assert sqlConnection != null;
            Statement usersStatement = sqlConnection.createStatement();
            ResultSet usersResultSet;

            usersResultSet = usersStatement.executeQuery("SELECT id, username, password, CNP, name, balance, admin FROM UserX");

            while (usersResultSet.next()) {
                ArrayList<Bet> currBets = new ArrayList<>();
                setBets(sqlConnection, usersResultSet, currBets);

                users.add(new User(usersResultSet.getString("username"), usersResultSet.getString("password"),
                        usersResultSet.getString("CNP"), usersResultSet.getString("name"), usersResultSet.getFloat("balance"),
                        usersResultSet.getBoolean("admin"), SceneController.scraping.getLeagues(), currBets, usersResultSet.getInt("id")));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (sqlConnection != null && !sqlConnection.isClosed()) {
                    sqlConnection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        this.users = users;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    /**
     * The function fetches all the Bets from the Database with all the corresponding Odds
     */
    private void setBets(Connection conn, ResultSet usersResult, ArrayList<Bet> bets) throws SQLException {
        String betsStmtString = "SELECT id, valuePlaced, totalOdd, totalWon, date, won FROM Bets WHERE userId = " + usersResult.getInt("id");
        Statement betStmt = conn.createStatement();
        ResultSet betsStatement = betStmt.executeQuery(betsStmtString);

        while (betsStatement.next()) {
            ArrayList<Odd> currOdds = new ArrayList<>();

            int idBets = betsStatement.getInt("id");

            setBetsMapped(conn, idBets, currOdds);

            float ammountBet = betsStatement.getFloat("valuePlaced");
            float totalOdd = betsStatement.getFloat("totalOdd");
            Date date = betsStatement.getDate("date");
            boolean won = betsStatement.getBoolean("won");
            Bet currBet = new Bet(currOdds, ammountBet, totalOdd, date, won);

            bets.add(currBet);
        }
    }

    private void setBetsMapped(Connection conn, int idBet, ArrayList<Odd> odds) throws SQLException {
        String mapStmtString = "SELECT betId FROM MapBets WHERE betsId = " + idBet;
        Statement mapStmt = conn.createStatement();
        ResultSet mapStatement = mapStmt.executeQuery(mapStmtString);

        while (mapStatement.next()) {
            int betId = mapStatement.getInt("betId");

            setOddsMapped(conn, betId, odds);
        }
    }

    private void setOddsMapped(Connection conn, int betId, ArrayList<Odd> odds) throws SQLException {
        String oddStmtString = "SELECT team1, team2, odd, oddType, dateTime FROM Odd WHERE id = " + betId;
        Statement oddStmt = conn.createStatement();
        ResultSet oddStatement = oddStmt.executeQuery(oddStmtString);

        while (oddStatement.next()) {
            float oddValue = oddStatement.getFloat("odd");
            String team1 = oddStatement.getString("team1");
            String team2 = oddStatement.getString("team2");
            String oddType = oddStatement.getString("oddType");
            String dateTime = oddStatement.getString("dateTime");
            Odd currOdd = new Odd(oddValue, team1, team2, oddType, dateTime);

            odds.add(currOdd);
        }
    }

    public static String getDatabaseUrl() {
        return DATABASE_URL;
    }

    public static String getDatabaseUsername() {
        return DATABASE_USERNAME;
    }

    public static String getDatabasePassword() {
        return DATABASE_PASSWORD;
    }
}
