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
import javafx.scene.control.ToggleButton;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import notey.noteyDocument;
public class NoteyController{

    private boolean showSidePane;

    //Encapsulates a button and its associated documents
    private static ArrayList<noteyDocument> noteyDocumentArr = new ArrayList<>();
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
    @FXML
    private Button saveButton;
    @FXML
    private Button saveAllButton;
    @FXML
    private ToggleButton toggleWebViewButton;
    @FXML
    private WebView webViewBox;
    private boolean buttonIsSelected;

    public void toggleWebView(){
        if(webViewBox.isVisible()){
            webViewBox.setVisible(false);
            documentText.setVisible(true);
            documentTitle.setVisible(true);
            webViewBox.setPrefWidth(0);
            webViewBox.setPrefHeight(0);
        }
        else{
            webViewBox.setVisible(true);
            documentText.setVisible(false);
            documentTitle.setVisible(false);
            webViewBox.setPrefWidth(1500);
            webViewBox.setPrefHeight(1500);
            WebEngine engine = webViewBox.getEngine();
            System.out.println("Loading " + saveDocument(documentText, documentTitle, false));
            engine.loadContent(saveDocument(documentText, documentTitle, false));
        }
    }
    private static String saveDocument(TextArea dText, TextField dTitle, boolean all){
        String result = new String();
        for(noteyDocument document : noteyDocumentArr){
            if(all || document.getButton().isDisable()){
                if(document.getButton().isDisable()){
                    document.setNormalText(dText.getText());
                    if(dTitle.getText().length() > 0 && dText.getText().length() > 0){
                        document.setTitle(dTitle.getText());
                        document.setButtonText(document.getTitle());
                    }
                }
                boolean notEmptyDocTitle = !document.getTitle().equals("");
                document.convertDocumentToHTML();
                if(notEmptyDocTitle)
                    document.setButtonText(document.getTitle());
                else
                    continue;

                new File("notes").mkdir();
                String validChars = "(\\W)";
                File output = new File("notes/" + document.getTitle().replaceAll(validChars, "").substring(0, 10) + ".txt");
                try(BufferedWriter writer =  new BufferedWriter(new FileWriter(output))){
                    writer.write(document.getNormalText());
                }
                catch(IOException io_exc){
                    System.out.println(io_exc);
                }
                output = new File("notes/" + document.getTitle().replaceAll(validChars, "").substring(0, 10) + ".html");
                try(BufferedWriter writer =  new BufferedWriter(new FileWriter(output))){
                    writer.write(document.getHTMLText());
                }
                catch(IOException io_exc){
                    System.out.println(io_exc);
                }
                if(!all)
                    result = document.getHTMLText();
            }
        }
        return result;
    }
    public void saveDocumentOnClick(){
        saveDocument(documentText, documentTitle, false);
    }
    public void saveAllDocumentsOnClick(){
        saveDocument(documentText, documentTitle, true);
    }
    private static void searchForDisabledDocumentInArr(TextArea dText, TextField dTitle){
        //Enables any disabled buttons. Should find only one.
        for(noteyDocument document : noteyDocumentArr){
            if(document.getButton().isDisable()){
                System.out.println("Enabling");
                document.toggleButtonDisable();
                document.setNormalText(dText.getText());
                document.setTitle(dTitle.getText());
                boolean notEmptyDocTitle = !document.getTitle().equals("");
                if(notEmptyDocTitle)
                    document.setButtonText(document.getTitle());
                break;
            }
        }
    }
    private static void loadIntoWebView(String content, WebView box){
        WebEngine engine = box.getEngine();
        engine.loadContent(content);
    }
    public void addDocument(ActionEvent event){
        System.out.println("Add document");
        Button newButton = new Button("Document");
        newButton.setMinWidth(0);
        newButton.setPrefWidth(addDocumentButton.getPrefWidth());
        newButton.setMaxWidth(addDocumentButton.getMaxWidth());
        newButton.setOnAction(e -> {
            searchForDisabledDocumentInArr(documentText, documentTitle);
            documentTitle.setText(""); //Clears it on each button press
            if(documentTitle.isDisable() && documentText.isDisable() && saveButton.isDisable()){
                //Enables the document title box and text box on the first button click
                documentTitle.setDisable(false);
                documentText.setDisable(false);
                saveButton.setDisable(false);
                saveAllButton.setDisable(false);
            }
            System.out.println("Clicked");
            noteyDocument temp = new noteyDocument((Button)e.getSource(), "", "", "");
            int index = noteyDocumentArr.indexOf(temp);

            boolean buttonFoundInArray = index > -1;
            if(buttonFoundInArray){
                noteyDocument tempDocument = noteyDocumentArr.get(index);
                if(!tempDocument.textEquals("")){
                    System.out.println("button's normal text is not empty.");
                    System.out.println("button normal text:" + tempDocument.getNormalText());
                    loadIntoWebView(tempDocument.getHTMLText(), webViewBox);
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
                /*
                If the document isn't found, add the
                blank document to the array and clear
                the title box and text box
                */
                loadIntoWebView(temp.getHTMLText(), webViewBox);
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

            //sidePane.setVisible(false);
            showSidePane = true;
        }
        else{
            //sidePane.setVisible(true);
            mainViewPane.setDividerPositions(dividerPosition);
            showSidePane = false;
        }
    }
    public void initialize(URL location, Resources resources){
        showSidePane = true;
        System.out.println("initialized");
    }
}
