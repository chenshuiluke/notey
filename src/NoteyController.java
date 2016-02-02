package notey;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXML;
import javax.annotation.Resources;
import javafx.scene.control.Button;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
public class NoteyController{

    private boolean showSidePane;
    private Map<Button, String> buttonMap = new HashMap<Button, String>();
    private double dividerPosition;

    @FXML
	private SplitPane mainViewPane;
    @FXML
    private ScrollPane sidePane;
    @FXML
    private VBox sideButtonHolder;
    @FXML
    private Button addDocumentButton;
    @FXML
    private TextArea documentText;

    public void addDocument(ActionEvent event){
        System.out.println("Add document");
        Button newButton = new Button("Document");
        newButton.setMinWidth(0);
        newButton.setPrefWidth(addDocumentButton.getPrefWidth());
        newButton.setMaxWidth(addDocumentButton.getMaxWidth());
        newButton.setOnAction(e -> {
            for(Map.Entry<Button, String> button : buttonMap.entrySet()){
                if(button.getKey().isDisable()){
                    button.getKey().setDisable(false);
                    /*
                    System.out.println(button.getValue());
                    System.out.println(documentText.getText());
                    */
                    button.setValue(documentText.getText());
                }

            }
            System.out.println("Clicked");
            Button temp = (Button)e.getSource();
            if(buttonMap.containsKey(temp)){
                if(!buttonMap.get(temp).equals("")){
                    System.out.println("Not equal empty");
                    System.out.println("button map content " + buttonMap.get(temp));
                    documentText.setText(buttonMap.get(temp));
                }
                else{
                    documentText.setText("");
                }
            }
            else{
                buttonMap.put(temp, "");
                documentText.setText("");
            }
            temp.setDisable(true);
        });
        sideButtonHolder.getChildren().add(newButton);
    }
    public void sidePaneVisibility(ActionEvent event){
        if(!showSidePane){
            dividerPosition = mainViewPane.getDividerPositions()[0];
            mainViewPane.setDividerPositions(0.0);

            sidePane.setVisible(false);
            showSidePane = true;
        }
        else{
            sidePane.setVisible(true);
            mainViewPane.setDividerPositions(dividerPosition);
            showSidePane = false;
        }
    }
    public void initialize(URL location, Resources resources){
        showSidePane = true;
        System.out.println("initialized");
    }
}
