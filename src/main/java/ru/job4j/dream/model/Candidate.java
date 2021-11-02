package ru.job4j.dream.model;

import java.time.LocalDate;
import java.util.Objects;

public class Candidate {
    private int id;
    private String name;
    private int cityId;
    private LocalDate registered = LocalDate.now();

    public Candidate(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Candidate(int id, String name, int cityId, LocalDate registered) {
        this.id = id;
        this.name = name;
        this.cityId = cityId;
        this.registered = registered;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public LocalDate getRegistered() {
        return registered;
    }

    public void setRegistered(LocalDate registered) {
        this.registered = registered;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Candidate candidate = (Candidate) o;
        return id == candidate.id && cityId == candidate.cityId && Objects.equals(name, candidate.name) && Objects.equals(registered, candidate.registered);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, cityId, registered);
    }

    @Override
    public String toString() {
        return "Candidate{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", cityId=" + cityId
                + ", registered=" + registered
                + '}';
    }
}