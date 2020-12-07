package ru.otus.json;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class PersonData {
    private final String name;
    private final int age;
    private final Float currentSalary;
    private final PersonData[] relatives;
    private final Collection<String> formerJobs;
    private final boolean primBool = true;
    private final Boolean wrapBool = true;
    private final int[] primArray = {1, 2, 3};

    public PersonData() {
        name = "John";
        age = 35;
        currentSalary = 10000.50f;
        relatives = null;
        formerJobs = null;
    }

    public PersonData(String name, int age, Float currentSalary, PersonData[] relatives, Collection<String> formerJobs) {
        this.name = name;
        this.age = age;
        this.currentSalary = currentSalary;
        this.relatives = relatives;
        this.formerJobs = formerJobs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonData that = (PersonData) o;
        return age == that.age &&
                Objects.equals(name, that.name) &&
                Objects.equals(currentSalary, that.currentSalary) &&
                Arrays.equals(relatives, that.relatives) &&
                Objects.equals(formerJobs, that.formerJobs);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, age, currentSalary, formerJobs);
        result = 31 * result + Arrays.hashCode(relatives);
        return result;
    }

    @Override
    public String toString() {
        return "PersonData{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", currentSalary=" + currentSalary +
                ", relatives=" + Arrays.toString(relatives) +
                ", formerJobs=" + formerJobs +
                ", primBool=" + primBool +
                ", wrapBool=" + wrapBool +
                ", primArray=" + Arrays.toString(primArray) +
                '}';
    }
}
