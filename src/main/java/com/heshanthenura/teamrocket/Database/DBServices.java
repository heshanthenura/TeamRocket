package com.heshanthenura.teamrocket.Database;

import com.heshanthenura.teamrocket.Entity.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DBServices {

    static Logger logger = Logger.getLogger("logger");

    public void addPerson(Person person) {
        try (Connection connection = DBConnection.getConnection()) {
            String insertPersonSQL = "INSERT INTO person (name, netWorth, age, countryOrTerritory, source, industry) VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertPersonSQL)) {
                preparedStatement.setString(1, person.getName());
                preparedStatement.setString(2, person.getNetWorth());
                preparedStatement.setInt(3, person.getAge());
                preparedStatement.setString(4, person.getCountryOrTerritory());
                preparedStatement.setString(5, person.getSource());
                preparedStatement.setString(6, person.getIndustry());
                preparedStatement.executeUpdate();
                logger.info("Person added to the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Person> getAllPersons() {
        ObservableList<Person> personList = FXCollections.observableArrayList();
        try (Connection connection = DBConnection.getConnection()) {
            String selectAllPersonsSQL = "SELECT * FROM person";

            try (PreparedStatement preparedStatement = connection.prepareStatement(selectAllPersonsSQL);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String netWorth = resultSet.getString("netWorth");
                    int age = resultSet.getInt("age");
                    String countryOrTerritory = resultSet.getString("countryOrTerritory");
                    String source = resultSet.getString("source");
                    String industry = resultSet.getString("industry");
                    Person person = new Person(name, netWorth, age, countryOrTerritory, source, industry);
                    personList.add(person);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return personList;
    }

    public static void main(String[] args) {

    }
}
