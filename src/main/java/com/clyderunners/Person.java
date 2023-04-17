package com.clyderunners;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Person {
    // set data type to simpleStringProperty , not string ,
    // because class SimpleStringProperty has provides methods adding listeners to be notified when the value changes.
    private SimpleStringProperty firstName = new SimpleStringProperty();
    private SimpleStringProperty lastName = new SimpleStringProperty();
    private SimpleDoubleProperty score = new SimpleDoubleProperty();


    //create Empty constructor
    public Person() {
    }

    //create constructor with 3 parameters

    public Person(SimpleStringProperty firstName, SimpleStringProperty lastName, SimpleDoubleProperty score) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.score = score;
    }


    //set getter and setter


    public String getFirstName() {
        return firstName.get();
    }

    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if(firstName.length()<=25){  // first name max character 25
            this.firstName.set(firstName);
        }else {
            this.firstName.set("--TooLong--");

        }
    }

    public String getLastName() {
        return lastName.get();
    }

    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if(lastName.length()<=25) { // last name max character 25
            this.lastName.set(lastName);
        }else {
            this.lastName.set("--TooLong--");

        }
    }

    public double getScore() {
        return score.get();
    }

    public SimpleDoubleProperty scoreProperty() {
        return score;
    }

    public void setScore(double score) {
        if(score <= 21600 && score>=0)  // score max less equal 21600s (6h)
            this.score.set(score);
        else if (score<0 && score >= -21600){
            this.score.set(score * -1); // if score negative converter it into positive  number

        }else {
            this.score.set(21600);// if user enter large number set score to max
        }
    }

}
