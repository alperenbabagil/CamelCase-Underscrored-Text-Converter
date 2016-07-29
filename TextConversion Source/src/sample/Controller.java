package sample;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class Controller implements Initializable {

    Main main;

    @FXML
    private RadioButton cFromUnderScored;

    @FXML
    private RadioButton  cToUnderScored;

    @FXML
    private CheckBox settingsConvertToLowerCase;

    @FXML
    private CheckBox settingsAlwaysTop;

    @FXML
    private TextField textFromTextField;

    @FXML
    private RadioButton  cFromCamelCase;

    @FXML
    private RadioButton  cFromNormalText;

    @FXML
    private RadioButton  cToNormalText;

    @FXML
    private RadioButton cToCamelCase;

    @FXML
    private CheckBox settingsCopyAfterTranslation;

    @FXML
    private CheckBox settingsCopyAfterClickFields;

    @FXML
    private TextField textToTextField;

    @FXML
    private CheckBox settingsWrapQuotation;

    @FXML
    private CheckBox settingsFirstLetterIsAlwaysUppercase;

    @FXML
    private CheckBox settingsFirstLetterIsAlwaysLowercase;

    @FXML
    private AnchorPane anchorPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        assert cFromUnderScored != null : "fx:id=\"cFromUnderScored\" was not injected: check your FXML file 'body.fxml'.";
        assert cToUnderScored != null : "fx:id=\"cToUnderScored\" was not injected: check your FXML file 'body.fxml'.";
        assert settingsConvertToLowerCase != null : "fx:id=\"settingsConvertToLowerCase\" was not injected: check your FXML file 'body.fxml'.";
        assert settingsAlwaysTop != null : "fx:id=\"settingsAlwaysTop\" was not injected: check your FXML file 'body.fxml'.";
        assert textFromTextField != null : "fx:id=\"textFromTextField\" was not injected: check your FXML file 'body.fxml'.";
        assert cFromCamelCase != null : "fx:id=\"cFromCamelCase\" was not injected: check your FXML file 'body.fxml'.";
        assert cFromNormalText != null : "fx:id=\"cFromNormalText\" was not injected: check your FXML file 'body.fxml'.";
        assert cToNormalText != null : "fx:id=\"cToNormalText\" was not injected: check your FXML file 'body.fxml'.";
        assert cToCamelCase != null : "fx:id=\"cToCamelCase\" was not injected: check your FXML file 'body.fxml'.";
        assert settingsCopyAfterTranslation != null : "fx:id=\"settingsCopyAfter\" was not injected: check your FXML file 'body.fxml'.";
        assert textToTextField != null : "fx:id=\"textToTextField\" was not injected: check your FXML file 'body.fxml'.";
        assert settingsWrapQuotation != null : "fx:id=\"settingsWrapQuotation\" was not injected: check your FXML file 'body.fxml'.";
        assert settingsFirstLetterIsAlwaysUppercase != null : "fx:id=\"settingsFirstLetterIsAlwaysUppercase\" was not injected: check your FXML file 'body.fxml'.";

        main=Main.getInstance();

        main.setController(this);

        main.getMainStage().addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        convert();
                    }
                }, 350);

            }
        });

        if(settingsAlwaysTop.isSelected()){
            main.getMainStage().setAlwaysOnTop(true);
        }

        textFromTextField.setOnMouseClicked(event -> {
            if(settingsCopyAfterClickFields.isSelected()) SystemClipboard.copy(textFromTextField.getText());

        });

        textToTextField.setOnMouseClicked(event -> {
            if(settingsCopyAfterClickFields.isSelected()) SystemClipboard.copy(textToTextField.getText());
        });

        setStateChangeListeners();
    }

    private void convert(){

        String str=textFromTextField.getText();

        if(str==null || str.length()==0) return;

        if(cFromCamelCase.isSelected()) str=convertCamelcase(str);

        if(cFromUnderScored.isSelected()) str=convertUnderScored(str);

        if(cFromNormalText.isSelected()) str=convertNormalText(str);

        if(settingsConvertToLowerCase.isSelected()) str=str.toLowerCase();

        if(settingsFirstLetterIsAlwaysUppercase.isSelected()) str=str.substring(0,1).toUpperCase()+str.substring(1);

        if(settingsFirstLetterIsAlwaysLowercase.isSelected()) str=str.substring(0,1).toLowerCase()+str.substring(1);

        if(settingsWrapQuotation.isSelected()) str="\""+str+"\"";

        if(settingsCopyAfterTranslation.isSelected()) {
            SystemClipboard.copy(str);
        }

        textToTextField.textProperty().setValue(str);

    }

    private String convertCamelcase(String str){
        if (cToCamelCase.isSelected()){
            return str;
        }
        if (cToUnderScored.isSelected()){
            return str.replaceAll("([^_A-Z])([A-Z])", "$1_$2");
        }
        if(cToNormalText.isSelected()){
            return str.replaceAll(
                    String.format("%s|%s|%s",
                            "(?<=[A-Z])(?=[A-Z][a-z])",
                            "(?<=[^A-Z])(?=[A-Z])",
                            "(?<=[A-Za-z])(?=[^A-Za-z])"
                    ),
                    " "
            );
        }
        return null;
    }

    private String convertUnderScored(String str){
        if (cToCamelCase.isSelected()){
//            String[] parts = str.split("_");
//            String camelCaseString = "";
//            for (String part : parts){
//                camelCaseString = camelCaseString + toProperCase(part);
//            }
//            return camelCaseString;
            String start = str;
            StringBuffer sb = new StringBuffer();
            for (String s : start.split("_")) {
                sb.append(Character.toUpperCase(s.charAt(0)));
                if (s.length() > 1) {
                    sb.append(s.substring(1, s.length()).toLowerCase());
                }
            }
            return sb.toString();
        }
        if (cToUnderScored.isSelected()){
            return str;
        }
        if(cToNormalText.isSelected()){
            return str.replaceAll("_"," ");
        }
        return null;
    }

    private String convertNormalText(String str){
        if (cToCamelCase.isSelected()){
            String[] parts = str.split(" ");
            String camelCaseString = "";
            for (String part : parts){
                if(part!=null && part.trim().length()>0)
                    camelCaseString = camelCaseString + toProperCase(part);
                else
                    camelCaseString=camelCaseString+part;
            }
            camelCaseString=camelCaseString.replaceAll(" ","");
            return camelCaseString;

            //return ret.toString();
        }
        if (cToUnderScored.isSelected()){
            return str.replaceAll(" ","_");
        }
        if(cToNormalText.isSelected()){
            return str;
        }
        return null;
    }

    private void setStateChangeListeners(){
        settingsAlwaysTop.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                if (isNowSelected) {
                    main.getMainStage().setAlwaysOnTop(true);
                } else {
                    main.getMainStage().setAlwaysOnTop(false);
                }
            }
        });

        settingsFirstLetterIsAlwaysUppercase.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                if (isNowSelected) {
                    settingsFirstLetterIsAlwaysLowercase.setSelected(false);
                }
            }
        });

        settingsFirstLetterIsAlwaysLowercase.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                if (isNowSelected) {
                    settingsFirstLetterIsAlwaysUppercase.setSelected(false);
                }
            }
        });

        textFromTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            convert();
        });


    }

    String toProperCase(String s) {
        String temp=s.trim();
        String spaces="";
        if(temp.length()!=s.length())
        {
            int startCharIndex=s.charAt(temp.indexOf(0));
            spaces=s.substring(0,startCharIndex);
        }
        temp=temp.substring(0, 1).toUpperCase() +
                spaces+temp.substring(1).toLowerCase()+" ";
        return temp;

    }

    public ArrayList<String> getAllCheckeds(){
        ArrayList<String> strings=new ArrayList<>();
        for (Node node : anchorPane.getChildren()) {
            if (node instanceof CheckBox ) {
                // clear

                if(((CheckBox) node).isSelected()){
                    strings.add(node.getId());
                }
            }
            if (node instanceof RadioButton) {
                // clear
                if(((RadioButton) node).isSelected()){
                    strings.add(node.getId());
                }
            }
        }
        return strings;
    }

    void setAllCheckeds(ArrayList<String> allCheckeds){
        for (Node node : anchorPane.getChildren()) {
            if(node instanceof CheckBox || node instanceof RadioButton){
                for(String id : allCheckeds){
                    if(node.getId().equals(id)){
                        if (node instanceof CheckBox ) {
                            Platform.runLater ( () -> ((CheckBox) node).setSelected(true));
                            // clear
//                            ((CheckBox) node).setSelected(true);
                        }
                        if (node instanceof RadioButton) {
                            Platform.runLater ( () -> ((RadioButton) node).setSelected(true));
                            // clear
      //                      ((RadioButton) node).setSelected(true);
                        }
                    }
                }
            }
        }
    }

}
