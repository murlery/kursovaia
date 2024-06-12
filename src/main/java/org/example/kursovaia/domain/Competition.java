package org.example.kursovaia.domain;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.example.kursovaia.data.DbWorker;

public class Competition {
    protected String id;
    protected String level;
    protected String name;
    protected String timing;
    protected String ageCategory;
    protected String location;
    protected Repository repository;

    public Competition(String id, String level, String name, String timing, String ageCategory, String location) {
        this.id = id;
        this.level = level;
        this.name = name;
        this.timing = timing;
        this.ageCategory = ageCategory;
        this.location = location;
        this.repository = new DbWorker();
    }
    public Competition(){
        this.repository = new DbWorker();
    }

    public ArrayList<Competition> getAllList() {
        try {
            return repository.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public String getId() {
        return id;
    }

    public String getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public String getTiming() {
        return timing;
    }

    public String getAgeCategory() {
        return ageCategory;
    }

    public String getLocation() {
        return location;
    }
    // Метод для получения всех соревнований
    public ArrayList<Competition> getAll() throws SQLException {
        return repository.getAll();
    }

    // Метод для получения соревнований по дате
    public ArrayList<Competition> getCompetitionsByDate() throws SQLException {
        return repository.getCompetitionsByDate();
    }

    // Метод для получения соревнований по уровню
    public ArrayList<Competition> getCompetitionsByLevel(String selectedLevel) throws SQLException {
        return repository.getCompetitionsByLevel(selectedLevel);
    }

    // Метод для получения соревнований по уровню и дате
    public ArrayList<Competition> getCompetitionsByLevelAndDate(String selectedLevel) throws SQLException {
        return repository.getCompetitionsByLevelAndDate(selectedLevel);
    }
}
