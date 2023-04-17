package com.clyderunners;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {



        int array_variable [] = new int[10];

        for (int i = 0; i < 10; ++i)

        {

            array_variable[i] = i;

            System.out.print(array_variable[i] + " ");

            i++;

        }




        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 860, 660);
        stage.setTitle("CLYDE RUNNERS CLUB");
        Image icon = new Image("icon.png");
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();

    }

    @Override
    public void init() throws Exception {
        DataRace.getInstance().loadFileData();
        super.init();
    }

    @Override
    public void stop() throws Exception {
        //save current file before close it
        DataRace.getInstance().saveToDataBase(DataRace.getFileName() ,DataRace.getInstance().getPersons());
        super.stop();

    }
}