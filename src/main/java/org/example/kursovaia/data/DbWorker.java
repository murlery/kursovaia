package org.example.kursovaia.data;

import org.example.kursovaia.domain.Competition;
import org.example.kursovaia.domain.User;

import java.sql.*;
import java.util.ArrayList;

public class DbWorker{
    private static String jdbUrl = "jdbc:sqlite:C:\\SQLite\\sqlite-tools-win-x64-3450200\\competitiones.db";
    private static Connection connection;
    public static void initDB(){
        try {
            connection = DriverManager.getConnection(jdbUrl);
            createTable();

        }
        catch (SQLException e){
            System.out.println("Ошибка с подключением бд");
            e.printStackTrace();
        }
    }
    public static void createTable(){
        try {
            Statement statement=connection.createStatement();
            statement.execute("CREATE TABLE if not exists 'users'('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'login' text, 'phone_number' int, 'surname' text, 'name' text, 'patronymic' text, 'date_of_birth' DATE, 'gender' text, 'password' text);");
            statement.execute("CREATE TABLE if not exists 'athletes'('id_athlete' INTEGER PRIMARY KEY AUTOINCREMENT, 'weight_category' text,'sports_category' text,'coach' text, 'id' INTEGER, foreign key (id) references users(id));");
            statement.execute("CREATE TABLE if not exists 'competitions'('id_competition' INTEGER PRIMARY KEY AUTOINCREMENT, 'level' text,'name' text,'timing' DATE, 'age category' text, 'location' text);");
            statement.execute("CREATE TABLE if not exists 'athletes_in_competitions'('id_athlete' INTEGER, 'id_competition' INTEGER, foreign key (id_athlete) references athletes(id_athlete), foreign key (id_competition) references competitions(id_competition));");
            System.out.println("Таблицы успешно созданы");

            statement.close();
            connection.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void signUpUser(User user){
        try (Connection connection = DriverManager.getConnection(jdbUrl);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("INSERT INTO USERS (login, phone_number, surname, name, patronymic, date_of_birth, gender, password) VALUES ('" + user.getLogin() + "','" + user.getNumberPhone() + "','" + user.getName() + "','" + user.getSurname() + "','" + user.getPatronymic() + "','" + user.getDateOfBirth() + "','" + user.getGender() + "','" + user.getPassword() + "');");
            System.out.println("Пользователь зарегистрирован");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static boolean isUniqueLogin(String text) {
        try (Connection connection = DriverManager.getConnection(jdbUrl);
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users WHERE login = '" + text + "'");
            if (resultSet.next()) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
public static ResultSet getUser(User user) {
    ResultSet resultSet = null;
    String select = "SELECT * FROM USERS WHERE login =? AND password =? ";
    try {
        connection = DriverManager.getConnection(jdbUrl);
        PreparedStatement prSt = connection.prepareStatement(select);
        prSt.setString(1, user.getLogin());
        prSt.setString(2, user.getPassword());

        resultSet = prSt.executeQuery();
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    return resultSet;
}
    public static ArrayList<Competition> getAll() throws SQLException {
        ArrayList<Competition> list = new ArrayList<>();
        Connection conn = DriverManager.getConnection(jdbUrl);
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM competitions ORDER BY timing ASC;");
        while(resultSet.next()) {
            list.add(new Competition(resultSet.getString("id_competition"), resultSet.getString("level"), resultSet.getString("name"), resultSet.getString("timing"), resultSet.getString("age category"), resultSet.getString("location")));
        }
        conn.close();
        resultSet.close();
        return list;
    }

    public static ArrayList<Competition> getCompetitionsByLevel(String selectedLevel) throws SQLException {
        ArrayList<Competition> list = new ArrayList<>();
        Connection conn = DriverManager.getConnection(jdbUrl);
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM competitions WHERE level = ?");
        statement.setString(1, selectedLevel); // Set the parameter
        ResultSet resultSet = statement.executeQuery();

        while(resultSet.next()) {
            list.add(new Competition(resultSet.getString("id_competition"),
                    resultSet.getString("level"),
                    resultSet.getString("name"),
                    resultSet.getString("timing"),
                    resultSet.getString("age category"),
                    resultSet.getString("location")));
        }
        conn.close();
        resultSet.close();
        return list;
    }

    public static ArrayList<Competition> getCompetitionsByDate() throws SQLException {
        ArrayList<Competition> list = new ArrayList<>();
        Connection conn = DriverManager.getConnection(jdbUrl);
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM competitions WHERE timing > CURRENT_DATE");
        ResultSet resultSet = statement.executeQuery();

        while(resultSet.next()) {
            list.add(new Competition(resultSet.getString("id_competition"),
                    resultSet.getString("level"),
                    resultSet.getString("name"),
                    resultSet.getString("timing"),
                    resultSet.getString("age category"),
                    resultSet.getString("location")));
        }
        conn.close();
        resultSet.close();
        return list;
    }

    public static ArrayList<Competition> getCompetitionsByLevelAndDate(String selectedLevel) throws SQLException {
        ArrayList<Competition> list = new ArrayList<>();
        Connection conn = DriverManager.getConnection(jdbUrl);
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM competitions WHERE level = ? AND timing > CURRENT_DATE");
        statement.setString(1, selectedLevel); // Set the parameter
        ResultSet resultSet = statement.executeQuery();

        while(resultSet.next()) {
            list.add(new Competition(resultSet.getString("id_competition"),
                    resultSet.getString("level"),
                    resultSet.getString("name"),
                    resultSet.getString("timing"),
                    resultSet.getString("age category"),
                    resultSet.getString("location")));
        }
        conn.close();
        resultSet.close();
        return list;
    }
}

