package com.example.library_system.dto;

public class AuthorDTO {
    private String firstName;
    private String lastName;
    private int birthYear;
    private String nationality;

    public AuthorDTO() {
    }

    public AuthorDTO(String firstName, String lastName, int birthYear, String nationality) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthYear = birthYear;
        this.nationality = nationality;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }
}
