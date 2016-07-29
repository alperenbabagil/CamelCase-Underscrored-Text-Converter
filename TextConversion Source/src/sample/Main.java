package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application implements DbObserver{


    private static Main instance;

    private Stage mainStage;

    Controller controller;

    private FileOps fileOps;

    @Override
    public void start(Stage primaryStage) throws Exception{
        instance=this;
        mainStage=primaryStage;
        fileOps=new FileOps(this);
        Parent root = FXMLLoader.load(getClass().getResource("body.fxml"));
        primaryStage.setTitle("Text Converter");
        primaryStage.setScene(new Scene(root, 800, 350));
        primaryStage.show();

    }

    @Override
    public void stop(){

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.exit(0);
            }
        }, 5000);

        fileOps.writeCheckedsToFile(controller.getAllCheckeds());
        System.out.println("Stage is closing");
        // Save file
    }

    public Stage getMainStage(){
        return mainStage;
    }

    public void setController(Controller controller){
        this.controller=controller;
    }

    public static void main(String[] args) {
        launch(args);
    }


    public static Main getInstance() {
        return instance;
    }

    @Override
    public void dbIsReady() {
        //Printer.p("db isReady");
        fileOps.readCheckedsFromFile();
    }

    @Override
    public void dbResultsReady(ArrayList<String> strings) {
        controller.setAllCheckeds(strings);
        //Printer.p("db dbResultsReady");
    }

    @Override
    public void dbResultsWritten() {
        //Printer.p("written");
        System.exit(0);
    }
}
