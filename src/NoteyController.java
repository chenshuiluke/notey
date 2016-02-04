package notey;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import java.net.URL;
import javafx.fxml.FXML;
import javax.annotation.Resources;
import java.util.*;
import java.io.*;
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
                    if(!dTitle.getText().equals("Document") && !dText.getText().equals("")){
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
                File output;
				String validated;
                if(document.getTitle().length()>10){
					validated = document.getTitle().replaceAll(validChars, "").substring(0, 10);
                    output= new File("notes/" + validated + ".txt");
                }
                else{
					validated = document.getTitle().replaceAll(validChars, "");
                    output= new File("notes/" + validated + ".txt");
                }
                try(BufferedWriter writer =  new BufferedWriter(new FileWriter(output))){
					System.out.println("Writing " + document.getNormalText() + " to " + validated);
                    writer.write(document.getNormalText());
                }
                catch(IOException io_exc){
                    System.out.println(io_exc);
                }
                if(document.getTitle().length()>10){
					validated = document.getTitle().replaceAll(validChars, "").substring(0, 10);
                    output= new File("notes/" + validated + ".html");
                }
                else{
					validated = document.getTitle().replaceAll(validChars, "");
                    output= new File("notes/" + document.getTitle().replaceAll(validChars, "") + ".html");
                }
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
                boolean notEmptyDocTitleAndText = !document.getTitle().equals("") && !document.getNormalText().equals("");
                if(notEmptyDocTitleAndText){
                    document.setButtonText(document.getTitle());
				}
				else{
             	  	document.setNormalText(dText.getText());
               		document.setTitle(dTitle.getText());
				}

                break;
            }
        }
    }
    private static void loadIntoWebView(String content, WebView box){
        WebEngine engine = box.getEngine();
        engine.loadContent(content);
    }
    private static Button setUpNoteyButton(TextArea documentText, TextField documentTitle,
    Button addDocumentButton, Button saveButton, Button saveAllButton, WebView webViewBox,
    String text, String title){
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
            noteyDocument temp = new noteyDocument((Button)e.getSource(), text, "", title);
            temp.convertDocumentToHTML();
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
                documentText.setText(text);
                documentTitle.setText(title);
                newButton.setText(title);
                System.out.println("Not found");
            }
            temp.toggleButtonDisable();
        });
        documentText.setText(text);
        documentTitle.setText(title);
        newButton.setText(title);
        return newButton;

    }
    public void addDocument(ActionEvent event){
        System.out.println("Add document");
        Button newButton = setUpNoteyButton(documentText, documentTitle,
        addDocumentButton, saveButton, saveAllButton, webViewBox, "", "Document");
        sideButtonHolder.getChildren().add(newButton);
    }
    private static void loadExistingNotes(VBox sideButtonHolder, TextArea documentText,
        TextField documentTitle, Button addDocumentButton, Button saveButton, Button saveAllButton,
        WebView webViewBox){
        File notesDir = new File("notes");
        String[] notes = notesDir.list();
        for(String note : notes){
            //System.out.println(note.substring(note.length() - 4, note.length()));
            System.out.println(note);
            if(note.substring(note.length() - 4, note.length()).equals(".txt")){
                System.out.println("In here");
                File inputFile = new File("notes/" + note);
                try(BufferedReader reader = new BufferedReader(new FileReader(inputFile))){
                    String text = new String();
                    String title = inputFile.getName();
					if(title.substring(title.length() - 5, title.length()).equals(".html")){
						title = title.substring(0, title.length() - 5);
					}
					else if(title.substring(title.length() - 4, title.length()).equals(".txt")){
						title = title.substring(0, title.length() - 4);
					}
					System.out.println("New title: " + title);
                    String line;
                    while((line = reader.readLine()) != null){
                        text+=line + System.getProperty("line.separator");
                    }
                    System.out.println(text);
                    Button newButton = setUpNoteyButton(documentText, documentTitle,
                    addDocumentButton, saveButton, saveAllButton, webViewBox, text, title);
                    sideButtonHolder.getChildren().add(newButton);
                }
                catch(IOException io_exc){
                    System.out.println(io_exc);
                }
            }
        }
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
    @FXML
    public void initialize(){
        showSidePane = true;
        loadExistingNotes(sideButtonHolder, documentText, documentTitle,
        addDocumentButton, saveButton, saveAllButton, webViewBox);
        System.out.println("initialized");
    }
}
