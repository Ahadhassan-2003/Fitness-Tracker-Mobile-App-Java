package com.example.fitnesstracker;

public class UserModel {
    private String name,number,email,password,steps;
    private double weight,skeletalMuscle,BodyFat,height,caloriesburned,calorieseaten;

    public UserModel(){}

    public UserModel(String name, String number, String email, String password,String steps,double weight,double skeletalMuscle,double BodyFat,double height,double caloriesburned,double calorieseaten) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.password = password;
        this.steps = steps;
        this.BodyFat = BodyFat;
        this.skeletalMuscle = skeletalMuscle;
        this.weight = weight;
        this.height = height;
        this.caloriesburned = caloriesburned;
        this.calorieseaten = calorieseaten;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getSkeletalMuscle() {
        return skeletalMuscle;
    }

    public void setSkeletalMuscle(double skeletalMuscle) {
        this.skeletalMuscle = skeletalMuscle;
    }

    public double getBodyFat() {
        return BodyFat;
    }

    public void setBodyFat(double bodyFat) {
        BodyFat = bodyFat;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getCaloriesBurned() {
        return caloriesburned;
    }

    public void setCaloriesBurned(double calories) {
        this.caloriesburned = caloriesburned;
    }

    public double getCalorieseaten() {
        return calorieseaten;
    }

    public void setCalorieseaten(double calorieseaten) {
        this.calorieseaten = calorieseaten;
    }
}
