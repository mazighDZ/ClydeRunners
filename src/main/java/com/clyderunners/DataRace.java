package com.clyderunners;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

//singleton Class
public class DataRace {
    private String firstName;
    private String lastName;
    private int score;

    private static Boolean consolePass;


    private static String fileName = "race results.txt";
    private String filePrentPath = "" ; //
    private ObservableList<Person> persons;
//set class instance

    private static DataRace instance = null;

    private DataRace() {
        //private construct to prevent instantiation
        persons = FXCollections.observableArrayList();

    }


    public static DataRace getInstance() {
        // if instance null means there are no instance created yet then create new one
        //else return existed instance
        if (instance == null) {
            instance = new DataRace();
        }
        return instance;
    }


    //Method reading data from file using NIO with buffered
    public boolean loadFileData() {

        // check file if is in format ->" String String Double "
        // by calling verifiedFileInput return true if in that format otherwise false
        if (fileName != null && validateFileInput() ) {
            //if there is any data from other file path clear first then add new data into list
            persons.clear();
            Path path = FileSystems.getDefault().getPath(filePrentPath, fileName);
            try (BufferedReader br = Files.newBufferedReader(path)) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(" ");
                    if(data.length == 3){
                        Person person = new Person();
                        person.setFirstName(data[0].trim());
                        person.setLastName(data[1].trim());
                        person.setScore(Double.parseDouble(data[2].trim()));
                        persons.add(person);
                    }
                }
                System.out.println("load :" + persons.size());
                return true;

            } catch (IOException e) {
                System.out.println("can't find file ");
                return true;
            }
        } else {
            return true;
        }


    }

    public ObservableList<Person> getPersons() {
        return persons;
    }

    public static String getFileName() {
        return fileName;
    }

    public static void setFileName(String fileName) {
        DataRace.fileName = fileName;
    }

    public String getFilePrentPath() {
        return filePrentPath;
    }

    public void setFilePrentPath(String filePrentPath) {
        this.filePrentPath = filePrentPath;
    }

    public void setPersons(ObservableList<Person> persons) {
        this.persons = persons;
    }


    public boolean saveToDataBase(String fileName, List<Person> listToSave) {
        if(fileName!= null) {
            Path path = FileSystems.getDefault().getPath(filePrentPath, fileName);

            try (BufferedWriter bw = Files.newBufferedWriter(path)) {

                for (Person person : listToSave) {
                    bw.write(person.getFirstName() + " " + person.getLastName() + " " + person.getScore());
                    bw.newLine();
                }
                System.out.println("saved successfully ");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("can't saved  ");

                return false;
            }
        }
        return false;
    }


    public boolean saveSortedTimeToDataBase(String filePath, List<Double> listToSave) {

        Path path = FileSystems.getDefault().getPath(filePrentPath,filePath);

        try (BufferedWriter bw = Files.newBufferedWriter(path)) {

            for (Double timeRecord : listToSave) {
                bw.write(timeRecord +"");
                bw.newLine();
            }
            System.out.println("successfully save Ordered list of times from slowest to fastest  ");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("can't saved  ");

            return false;
        }
    }


    public void saveResult(double record, String fileName) {
        String filePath = fileName + "_" + DataRace.getFileName();
        Path path = FileSystems.getDefault().getPath(filePrentPath,filePath);
        try (BufferedWriter br = Files.newBufferedWriter(path)) {
            br.write(record + "");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public boolean validateFileInput(){

        if (fileName != null) {
            Path path = FileSystems.getDefault().getPath(filePrentPath, fileName);
            try (BufferedReader br = Files.newBufferedReader(path)) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(" ");
                    if(data.length >= 3){
                        try {
                            Double.parseDouble(data[2].trim());

                        }catch (NumberFormatException nfe){
                            System.out.println("file you selected are not recognized ");
                            fileName =null;
                            return false;
                        }
                    }else {
                        System.out.println("file you selected are not recognized ");
                        fileName =null;
                        return false;
                    }
                }
                System.out.println("load :" + persons.size());
            } catch (IOException e) {
                System.out.println("can't find file ");
                fileName =null;
                return false;

            }
        } else {
            System.out.println("can't find file ");
            fileName =null;
            return false;

        }

        return true;


    }


}
