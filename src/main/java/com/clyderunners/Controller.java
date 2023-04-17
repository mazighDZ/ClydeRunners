package com.clyderunners;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class Controller {


    @FXML
    private VBox mainVbox;
    @FXML
    private Label readFile;

    @FXML
    private Label sortLF; // lowest to fastest
    @FXML
    private Label sortFL; // fastest to lowest
    @FXML
    private Label findFastest;
    @FXML
    private Label findLowest;
    @FXML
    private Label search;
    @FXML
    private Label timeOccurrence;

    @FXML
    private Label exit;

    @FXML
    private TableView<Person> tableViewRecord;

    @FXML
    private TableColumn<Person, String> firstname;
    @FXML
    private TableColumn<Person, String> lastName;
    @FXML
    private TableColumn<Person, Double> score;


    // when we get data from singleton class  we save it into recordList Arraylist  (observable)
    private ObservableList<Person> personList = FXCollections.observableArrayList();


    //2 colors for label Menu to make difference between lines
    private Color whiteLabel = Color.rgb(255, 255, 255);
    private Color grayLabel = Color.rgb(232, 232, 232);

    @FXML
    public void initialize() {
        // set background color to blue when mouse hover on labels Menu ,
        //we pass label and his origin color to return his origin color when onMouseExit
        handelOnMouseEnterMenu(readFile, grayLabel);
        handelOnMouseEnterMenu(sortLF, whiteLabel);
        handelOnMouseEnterMenu(sortFL, grayLabel);
        handelOnMouseEnterMenu(findFastest, whiteLabel);
        handelOnMouseEnterMenu(findLowest, grayLabel);
        handelOnMouseEnterMenu(search, whiteLabel);
        handelOnMouseEnterMenu(timeOccurrence, grayLabel);
        handelOnMouseEnterMenu(exit, whiteLabel);

        tableViewRecord.setEditable(true);
        // associate each tableColumn with fields in  Person class
        // example column firstname we associated value to the field firstName in the Person class
        firstname.setCellValueFactory(new PropertyValueFactory<Person, String>("firstName"));
        firstname.setCellFactory(TextFieldTableCell.forTableColumn());


        lastName.setCellValueFactory(new PropertyValueFactory<Person, String>("lastName"));
        lastName.setCellFactory(TextFieldTableCell.forTableColumn());

        score.setCellValueFactory(new PropertyValueFactory<Person, Double>("score"));
        score.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Double>() {
            @Override
            public String toString(Double doubleNum) {
                return doubleNum.toString();
            }

            @Override
            public Double fromString(String s) {
                return Double.parseDouble(s);
            }
        }));


        // load data from DataRace class and put it into personTableList observableArraylist
        personList.addAll(DataRace.getInstance().getPersons());

        // add personTableList to tableViewRecord
        tableViewRecord.setItems(personList);
        //hide table record at start
        tableViewRecord.setVisible(false);
        // set table to be multiple selection
        tableViewRecord.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


    }

    public void handelOnMouseEnterMenu(Label label, Color background) {
        label.setOnMouseEntered(event -> {
            label.setBackground(Background.fill(Color.CORNFLOWERBLUE));
        });
        label.setOnMouseExited(event -> {
            label.setBackground(Background.fill(background));
        });

    }


    public void save() {
        if (DataRace.getFileName() != null) {
            String filepath = DataRace.getFileName();
            System.out.println("list to save " + tableViewRecord.getItems().get(0).getFirstName());
            DataRace.getInstance().saveToDataBase(filepath, tableViewRecord.getItems());

        }
    }

    public void close() {
        Platform.exit();
    }

    public void readFile() {


        // if file path exist then load data
        if (DataRace.getInstance().loadFileData()) {
            // check if the file race was empty notify the user

            if (DataRace.getInstance().getPersons().isEmpty()) {
                //create alert
                alertFileEmpty();

            }
            tableViewRecord.setItems(DataRace.getInstance().getPersons());
            //show the table record when user click read file
            tableViewRecord.setVisible(true);


        } else {

            System.out.println("call open file method to select file race");

        }

    }

    public void slowestRecord() {
        if (!DataRace.getInstance().getPersons().isEmpty()) {// check if table not empty
            Person slowest = DataRace.getInstance().getPersons().get(0);//
            for (Person p : DataRace.getInstance().getPersons()) {
                if (slowest.getScore() < p.getScore()) {
                    slowest = p;

                }
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            // Set the owner to change icon
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("icon.png"));
            alert.setTitle("Slowest record");
            alert.setHeaderText("The slowest record is " + slowest.getScore() + "s");
            alert.showAndWait();
            DataRace.getInstance().saveResult(slowest.getScore(), "slowest");
        } else {
            alertFileEmpty();

        }
    }

    public void fastestRecord() {
        if (!DataRace.getInstance().getPersons().isEmpty()) {// check if file is not empty
            List<Person> runners = DataRace.getInstance().getPersons();
            Person fastest = runners.get(0);// set first runner to fastest
            for (int i = 1; i < runners.size(); i++) {
                if (fastest.getScore() > runners.get(i).getScore()) {//if fastest score great then other runner score set fastest to that runner
                    fastest = runners.get(i);

                }
            }

            //show message dialog
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            // Set the owner to change icon
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("icon.png"));
            alert.setTitle("Fastest record");
            alert.setHeaderText("The fastest record is " + fastest.getScore() + "s");
            alert.showAndWait();
            //save to new file the fastest score
            DataRace.getInstance().saveResult(fastest.getScore(), "fastest");
        } else {
            alertFileEmpty();

        }

    }

    //i use simple sort
    public void sortListOfTimeSlowestToFastest() {
        if (!DataRace.getInstance().getPersons().isEmpty()) {
            //create new ArrayList to save sorted list
            List<Person> sortedList = new ArrayList<>(DataRace.getInstance().getPersons());
            // this list for score
            List<Double> timeFromSlowestToFastest = new ArrayList<>();
            for (int i = 0; i < sortedList.size(); i++) {

                for (int j = i + 1; j < sortedList.size(); j++) {

                    if (sortedList.get(i).getScore() < sortedList.get(j).getScore()) {//check first element score against second element score
                        //if first person score in arraylist great then second person score
                        //save first person to temporary location to swap between them
                        Person temp = sortedList.get(i);
                        sortedList.set(i, sortedList.get(j));
                        sortedList.set(j, temp);
                    }
                }

            }
            //after sorter list  add score into timeFromSlowestToFastest
            for (Person runners : sortedList) {
                timeFromSlowestToFastest.add(runners.getScore());
            }

            personList.clear();// clear list table
            personList.addAll(sortedList);// display sorted list into table
            tableViewRecord.setVisible(true);//show table
            //create suitable name file
            String fileName = "TimeListSlowestToFastest" + "_" + DataRace.getFileName();
            // before invoke method saveToDataBase we need change persons list to new sorted list
            //   DataRace.getInstance().setPersons(personList);
            tableViewRecord.setItems(personList);

            //if we want save ordered runners from slowest to fasted
            // DataRace.getInstance().saveToDataBase(fileName, sortedList);
            // save only time from slowest to fastest
            DataRace.getInstance().saveSortedTimeToDataBase(fileName, timeFromSlowestToFastest);


        } else {
            alertFileEmpty();

        }


    }

    public void sortFastestToSlowest() {
        if (!DataRace.getInstance().getPersons().isEmpty()) {
            List<Person> sortedList = new ArrayList<>(DataRace.getInstance().getPersons());
            // use sort method in List Collection and override compare method
            sortedList.sort(new Comparator<Person>() {
                @Override
                public int compare(Person o1, Person o2) {
                    if (o1.getScore() > o2.getScore()) {
                        return 1;
                    } else if (o1.getScore() < o2.getScore()) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });
            personList.clear();// clear list table
            personList.addAll(sortedList);// display sorted list into table

            String fileName = "FastestToSlowest" + "_" + DataRace.getFileName();
            tableViewRecord.setItems(personList);
            tableViewRecord.setVisible(true);//show table
            // save ordered list to new file
            DataRace.getInstance().saveToDataBase(fileName, sortedList);

        } else {
            alertFileEmpty();
        }

    }


    public void search() {

        if (!DataRace.getInstance().getPersons().isEmpty()) {
            //create dialog
            Dialog<ButtonType> dialog = new Dialog<>();
            //initialize owner windows
            dialog.initOwner(mainVbox.getScene().getWindow());
            dialog.setTitle("search");

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("searchController.fxml"));
            // load into dialog
            try {
                dialog.getDialogPane().setContent(fxmlLoader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            //add button
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

            //from fxmlLoader get controller and assign to searchController reference
            SearchController searchController = fxmlLoader.getController();
            //use lambda expression call runLater method to set focus to textField
            //            Platform.runLater(() -> searchController.setFocusTimeTextField());// must add JavaFx.graphic library otherwise not work in JAR file
            Platform.runLater(searchController::setFocusTimeTextField); //must add JavaFx.graphic library otherwise will not work in JAR file
            //show dialog and assign result
            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                //code for Search
                //get value user entered in text field
                double userInput = searchController.getTimeTextField();
                if (userInput >= 0) {
                    List<Person> runnersList = new ArrayList<>(DataRace.getInstance().getPersons());
                    List<Person> searchList = new ArrayList<>();


                    for (int i = 0; i < runnersList.size(); i++) {
                        if (runnersList.get(i).getScore() == userInput) {
                            //add to searchList if score exist on list
                            searchList.add(runnersList.get(i));
                        }

                    }
                    // check if record time there is not in list
                    if (searchList.size() == 0) {
                        //show alert message that record time that they enter not in race list
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        // Set the owner to change icon
                        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                        stage.getIcons().add(new Image("icon.png"));
                        alert.setTitle("info");
                        alert.setContentText("the following time " + userInput + "s " + "has not been recorded ");
                        alert.showAndWait();

                    } else {

                        personList.clear();// clear list
                        personList.addAll(searchList);// display sorted list into table

                        String fileName = "scored_" + userInput + "_" + DataRace.getFileName();
                        tableViewRecord.setItems(personList);
                        tableViewRecord.setVisible(true);//show table
                        // save ordered list to new file
                        DataRace.getInstance().saveToDataBase(fileName, searchList);


                    }

                } else {
                    //if user enter characters or negative number will show alert message
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    // Set the owner to change icon
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("icon.png"));
                    alert.setContentText("Time input accept only positive number  ");
                    alert.setTitle("Wrong input");
                    alert.showAndWait();
                }


            }
        } else {// if tableview is Empty notified client to read race
            alertFileEmpty();


        }


    }


    public void timeOccurrence() {
        if (!DataRace.getInstance().getPersons().isEmpty()) {
            //create dialog
            Dialog<ButtonType> dialog = new Dialog<>();
            //initialize owner windows
            dialog.initOwner(mainVbox.getScene().getWindow());
            dialog.setTitle("Time Occurrence");
//        dialog.setHeaderText("Enter time(s) :");

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("searchController.fxml"));
            // load into dialog
            try {
                dialog.getDialogPane().setContent(fxmlLoader.load());

            } catch (IOException e) {
                e.printStackTrace();


            }
            //add button
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

            //from fxmlLoader get controller and assign to searchController reference
            SearchController searchController = fxmlLoader.getController();

            // lambda expression call runLater method to set focus to textField
            Platform.runLater(searchController::setFocusTimeTextField);//must add JavaFx.graphic library otherwise will not work in JAR file

            //show dialog and assign result
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {

                //get value user entered in text field
                double userInput = searchController.getTimeTextField();
                if (userInput >= 0) {

                    List<Person> list = new ArrayList<>(DataRace.getInstance().getPersons());
                    List<Person> searchList = new ArrayList<>();
                    int count = 0;

                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getScore() == userInput) {
                            //add to count
                            count++;
                        }

                    }
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    // Set the owner to change icon
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("icon.png"));
                    // check if record time there is not in list
                    if (count == 0) {
                        //show alert message that record time that they enter not in race list
                        alert.setTitle("info");
                        alert.setContentText("The following time " + userInput + "s " + "has not been recorded in this race");
                        alert.showAndWait();

                    } else {
                        alert.setHeaderText("Occurrence");
                        alert.setTitle("Time Occurrence");
                        alert.setContentText("Number of Occurrence for " + userInput + "s is : " + count);
                        alert.showAndWait();


                        String fileName = "Occurrence_" + userInput + "s";
                        DataRace.getInstance().saveResult(count, fileName);

                    }

                } else {
                    //if user enter characters or negative number will show alert message
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    // Set the owner to change icon
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("icon.png"));
                    alert.setTitle("Wrong input");
                    alert.setContentText("Time input accept only positive number  ");
                    alert.showAndWait();
                }


            }
        } else {// if tableview is Empty notified client to read race
            alertFileEmpty();
        }


    }


    public void alertFileEmpty() {
        Alert alertFileEmpty = new Alert(Alert.AlertType.WARNING);
        // Set the owner to change icon
        Stage stage = (Stage) alertFileEmpty.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("icon.png"));
        alertFileEmpty.setTitle("File");
        alertFileEmpty.setHeaderText("File race is empty");
        if (DataRace.getFileName() == null) {
            alertFileEmpty.setContentText("Please Select File ");
        } else {
            alertFileEmpty.setContentText("The file \"" + DataRace.getFileName() + "\" is empty ");
        }
        alertFileEmpty.showAndWait();

    }


    //open new race file
    public void openNewFile() {
        FileChooser fileChooser = new FileChooser();
        //add extension filter read only .txt file
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("txt", "*.txt"));
        File file = fileChooser.showOpenDialog(mainVbox.getScene().getWindow());
        if (file != null) {
            //before load open new file , save current file first prevent lost and data
            save();

            //set parent path in our DataRace
            DataRace.getInstance().setFilePrentPath(file.getParent());
            // set file name in our DataRace
            DataRace.setFileName(file.getName());
            // read file to table
            readFile();
        }
    }

    public void saveAs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("text", "*.txt"));
        File file = fileChooser.showSaveDialog(mainVbox.getScene().getWindow());
        if (file != null) {
            DataRace.setFileName(file.getName());
            DataRace.getInstance().setFilePrentPath(file.getParent());
            save();
        }

    }


    public void logout() {
        try {

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
            //
            Stage stage = (Stage) (mainVbox.getScene().getWindow());
            stage.setTitle("login");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void deleteRunners(ObservableList<Person> selectedRunners) {
        if (selectedRunners != null && !selectedRunners.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            // Set the owner to change icon
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("icon.png"));
            //if selected only one runner
            if (selectedRunners.size() == 1) {
                alert.setTitle("Delete runner");
                alert.setHeaderText("Delete runner record ");
                alert.setContentText("are you sure to delete runner \"" + selectedRunners.get(0).getFirstName() + "\" ?");
            }// if multiple selection
            else if (selectedRunners.size() > 1) {
                alert.setTitle("Delete runners");
                alert.setHeaderText("Delete runners records");
                StringBuilder runnersNames = new StringBuilder();
                for (Person runners : selectedRunners) {
                    runnersNames.append("\"").append(runners.getFirstName()).append("\"").append(" ");
                }
                alert.setContentText("are you sure to delete runners " + runnersNames + " ?");
            }

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // before deleting group of items in database  we must convert our Observable list to Fixed list (normal) that we received in Multiselect
                //  because when we're deleting items our observable list update  so our for loop get Indexing Error
                List<Person> tempPerson = new ArrayList<>(selectedRunners);
                for (Person person : tempPerson) {
                    System.out.println(" row " + person.getFirstName() + "was deleted");
                    tableViewRecord.getItems().remove(person);
                }
                save();
                tableViewRecord.getSelectionModel().selectFirst();
            } else {
                System.out.println(" delete was canceled ");
            }
        }

    }


    public void callDeleteRunners(ActionEvent event) {
        ObservableList<Person> selectedRunner = tableViewRecord.getSelectionModel().getSelectedItems();

        deleteRunners(selectedRunner);

    }
}
