package org.example.kursovaia.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface Repository {
    void signUpUser(User user);
    boolean isUniqueLogin(String text);
    ResultSet getUser(User user);
    ArrayList<Competition> getAll() throws SQLException;
    ArrayList<Competition> getCompetitionsByLevel(String selectedLevel) throws SQLException;
    ArrayList<Competition> getCompetitionsByDate() throws SQLException;
    ArrayList<Competition> getCompetitionsByLevelAndDate(String selectedLevel) throws SQLException;


}
