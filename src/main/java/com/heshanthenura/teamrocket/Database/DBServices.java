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

    public void updatePerson(Person oldPerson, Person newPerson) {
        try (Connection connection = DBConnection.getConnection()) {
            String updatePersonSQL = "UPDATE person SET name=?, netWorth=?, age=?, countryOrTerritory=?, source=?, industry=? WHERE name=? AND netWorth=? AND age=? AND countryOrTerritory=? AND source=? AND industry=?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(updatePersonSQL)) {
                preparedStatement.setString(1, newPerson.getName());
                preparedStatement.setString(2, newPerson.getNetWorth());
                preparedStatement.setInt(3, newPerson.getAge());
                preparedStatement.setString(4, newPerson.getCountryOrTerritory());
                preparedStatement.setString(5, newPerson.getSource());
                preparedStatement.setString(6, newPerson.getIndustry());

                // Use the old person's values for WHERE clause
                preparedStatement.setString(7, oldPerson.getName());
                preparedStatement.setString(8, oldPerson.getNetWorth());
                preparedStatement.setInt(9, oldPerson.getAge());
                preparedStatement.setString(10, oldPerson.getCountryOrTerritory());
                preparedStatement.setString(11, oldPerson.getSource());
                preparedStatement.setString(12, oldPerson.getIndustry());

                preparedStatement.executeUpdate();
                logger.info("Person updated in the database.");
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

    public ObservableList<Person> searchPersons(String name, String netWorth, Integer age, String countryOrTerritory, String source, String industry) {
        ObservableList<Person> searchResults = FXCollections.observableArrayList();
        try (Connection connection = DBConnection.getConnection()) {
            String searchPersonsSQL = "SELECT * FROM person WHERE "
                    + "(? IS NULL OR name LIKE ?) AND "
                    + "(? IS NULL OR netWorth LIKE ?) AND "
                    + "(? IS NULL OR age = ?) AND "
                    + "(? IS NULL OR countryOrTerritory LIKE ?) AND "
                    + "(? IS NULL OR source LIKE ?) AND "
                    + "(? IS NULL OR industry LIKE ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(searchPersonsSQL)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, name == null ? "%" : "%" + name + "%");
                preparedStatement.setString(3, netWorth);
                preparedStatement.setString(4, netWorth == null ? "%" : "%" + netWorth + "%");
                preparedStatement.setObject(5, age);
                preparedStatement.setObject(6, age);
                preparedStatement.setString(7, countryOrTerritory);
                preparedStatement.setString(8, countryOrTerritory == null ? "%" : "%" + countryOrTerritory + "%");
                preparedStatement.setString(9, source);
                preparedStatement.setString(10, source == null ? "%" : "%" + source + "%");
                preparedStatement.setString(11, industry);
                preparedStatement.setString(12, industry == null ? "%" : "%" + industry + "%");

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String resultName = resultSet.getString("name");
                        String resultNetWorth = resultSet.getString("netWorth");
                        int resultAge = resultSet.getInt("age");
                        String resultCountryOrTerritory = resultSet.getString("countryOrTerritory");
                        String resultSource = resultSet.getString("source");
                        String resultIndustry = resultSet.getString("industry");
                        Person resultPerson = new Person(resultName, resultNetWorth, resultAge, resultCountryOrTerritory, resultSource, resultIndustry);
                        searchResults.add(resultPerson);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return searchResults;
    }


    public void deletePerson(Person person) {
        try (Connection connection = DBConnection.getConnection()) {
            String deletePersonSQL = "DELETE FROM person WHERE name=? AND netWorth=? AND age=? AND countryOrTerritory=? AND source=? AND industry=?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(deletePersonSQL)) {
                preparedStatement.setString(1, person.getName());
                preparedStatement.setString(2, person.getNetWorth());
                preparedStatement.setInt(3, person.getAge());
                preparedStatement.setString(4, person.getCountryOrTerritory());
                preparedStatement.setString(5, person.getSource());
                preparedStatement.setString(6, person.getIndustry());

                preparedStatement.executeUpdate();
                logger.info("Person deleted from the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }
}
