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
import javafx.scene.control.TextField;
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

import notey.noteyDocument;
public class NoteyController{

    private boolean showSidePane;
    private Map<Button, String> buttonMap = new HashMap<Button, String>();

    //Encapsulates a button and its associated documents
    private ArrayList<noteyDocument> noteyDocumentArr = new ArrayList<>();
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
    @FXML
    private TextField documentTitle;

    public void addDocument(ActionEvent event){
        System.out.println("Add document");
        Button newButton = new Button("Document");
        newButton.setMinWidth(0);
        newButton.setPrefWidth(addDocumentButton.getPrefWidth());
        newButton.setMaxWidth(addDocumentButton.getMaxWidth());
        newButton.setOnAction(e -> {
            for(noteyDocument document : noteyDocumentArr){
                if(document.getButton().isDisable()){
                    System.out.println("Enabling");
                    document.toggleButtonDisable();
                    document.setNormalText(documentText.getText());
                    document.setTitle(documentTitle.getText());
                    if(!document.getTitle().equals(""))
                        document.setButtonText(document.getTitle());
                }
            }
            documentTitle.setText("");
            if(documentTitle.isDisable() && documentText.isDisable()){
                documentTitle.setDisable(false);
                documentText.setDisable(false);
            }
            System.out.println("Clicked");
            noteyDocument temp = new noteyDocument((Button)e.getSource(), "", "", "");
            int index = noteyDocumentArr.indexOf(temp);
            if(index > -1){
                noteyDocument tempDocument = noteyDocumentArr.get(index);
                if(!tempDocument.textEquals("")){
                    System.out.println("button's normal text is not empty.");
                    System.out.println("button normal text:" + tempDocument.getNormalText());
                    documentText.setText(tempDocument.getNormalText());
                    if(!tempDocument.getTitle().equals(""))
                        documentTitle.setText(tempDocument.getTitle());
                }
                else{
                    documentText.setText("");
                    documentTitle.setText("");
                }
            }
            else{
                //If the document isn't found
                //buttonMap.put(temp, "");
                noteyDocumentArr.add(temp);
                documentText.setText("");
                documentTitle.setText("");
                System.out.println("Not found");
            }
            temp.toggleButtonDisable();
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
