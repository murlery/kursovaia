package org.example.kursovaia.domain;

public class Competition {
    private String id;
    private String level;
    private String name;
    private String timing;
    private String ageCategory;
    private String location;

    public Competition(String id, String level, String name, String timing, String ageCategory, String location) {
        this.id = id;
        this.level = level;
        this.name = name;
        this.timing = timing;
        this.ageCategory = ageCategory;
        this.location = location;
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
}
