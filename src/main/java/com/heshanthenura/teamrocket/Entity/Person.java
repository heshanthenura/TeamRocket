package com.heshanthenura.teamrocket.Entity;

public class Person {

    private String name;
    private String netWorth;
    private int age;
    private String countryOrTerritory;
    private String source;
    private String industry;

    public Person(String name, String netWorth, int age, String countryOrTerritory, String source, String industry) {
        this.name = name;
        this.netWorth = netWorth;
        this.age = age;
        this.countryOrTerritory = countryOrTerritory;
        this.source = source;
        this.industry = industry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNetWorth() {
        return netWorth;
    }

    public void setNetWorth(String netWorth) {
        this.netWorth = netWorth;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCountryOrTerritory() {
        return countryOrTerritory;
    }

    public void setCountryOrTerritory(String countryOrTerritory) {
        this.countryOrTerritory = countryOrTerritory;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }
}
