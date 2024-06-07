package org.example.kursovaia.domain;

import java.time.LocalDate;

public class User {



    protected String login;
    protected String numberPhone;
    protected String surname;
    protected String name;
    protected String patronymic;
    protected LocalDate dateOfBirth;
    protected String gender;
    protected String password;
    public User(String login, String numberPhone , String surname, String name, String patronymic, LocalDate dateOfBirth, String gender, String password){

        this.login = login;
        this.numberPhone = numberPhone;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.password = password;
    }

    public User() {

    }


    public String getLogin() {
        return login;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public String getPassword() {
        return password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
