package com.clyderunners;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class SearchController {

@FXML
private TextField timeTextField;
    public void initialize() {
        timeTextField.requestFocus();
    }
public double getTimeTextField(){



    //validation
    double in ;
    try {
        in = Double.parseDouble(timeTextField.getText());
    }catch (NumberFormatException ime){
        //if user enter not numbers will return -1
        return -1;
    }

    if (in>= 0){
    return in  ;

    }else {
        //if user enter negative time will return -1
        return -1;
    }


}

public  void setFocusTimeTextField(){
    timeTextField.requestFocus();
}
}
