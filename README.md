# Betting_Application 

This Betting Application was made for the OOP 2020/2021 project assignment.

## Author: 
Ivan Alexandru-Ionut | 2nd Year Computer Science | Technical University of Cluj-Napoca

## Description:
The application was built based on a `Maven Project`. The dependencies are:
```bash
json
sql
jdbc
javafx
```
It makes use of a free, online [API](https://the-odds-api.com/) by fetching all the necessary data from its JSON files. The fetch is done using an _HTTP Request_ that is later converted into usable data. 

All the user's data: **personal information**, **placed bets** and the corresponding **odds** are stored in an `SQLServer Database` which is accessed through an _SQL Connection_ realized by the [jdbc API](https://en.wikipedia.org/wiki/Java_Database_Connectivity) and all the updates to the `Database` is done using the same kind of _Connection_. The data is always fetched again in this way so that the **User** can see its record on the app.

The **User Interface** part was done using [JavaFX](https://openjfx.io/) and [SceneBuilder](https://gluonhq.com/products/scene-builder/). Some fields had to be generated automatically due to the fact that we can easily add new  `Leagues` and new `Bets`.

**Main Classes:**
-
```java
class User;            //the User with the personal data and record of past Bets
class League;          //the League which has a list of Matches
class Match;           //the Match has two teams and a list of Odds
class Odd;             //the Odd has two teams and an odd
class Bet;             //the Bet has a list of Odds, an ammout, a date etc.
class Scraping;        //used to fetch the data
```
There are also other classes for `SQLConnection`, `JSONDecode`, `Controller`(JavaFX) etc.

## Database:
The database is managed by `SQLServer` and is fetched using the `jdbc API`. When a new `User` is created, it is immediately inserted into the `Database`. The Diagram is:

![image](https://github.com/OOP-Projects-2020-2021/Betting_Application/blob/master/res/diagramDB.png)

## JSON\:
All the `Leagues`, `Odds` and `Matches` are fetched from the `JSON API` which looks like this:

![image](https://github.com/OOP-Projects-2020-2021/Betting_Application/blob/master/res/json.png)

# Visuals

Here I posted some screenshots of the main scenes.

- `Sign In Page`

![image](https://github.com/OOP-Projects-2020-2021/Betting_Application/blob/master/res/signIn.png)

- `Bet Menu Page`

![image](https://github.com/OOP-Projects-2020-2021/Betting_Application/blob/master/res/betMenu.png)

- `Bet History Page`

![image](https://github.com/OOP-Projects-2020-2021/Betting_Application/blob/master/res/betHistory.png)

- `Admin Menu Page`  //which is a special kind of `User`

![image](https://github.com/OOP-Projects-2020-2021/Betting_Application/blob/master/res/adminMenu.png)

# Further Development

- The next step in developing this application is to include a verification of the bets (in order to know which bet was won and which wasn't).
- Show also the Group for every League and League for every Match in the History Pages.
