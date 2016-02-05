package notey;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import java.net.URL;
import javafx.fxml.FXML;
import javax.annotation.Resources;
import java.util.*;
import java.io.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import notey.NoteyDocument;
public class NoteyController{

    private boolean showSidePane;

    //Encapsulates a button and its associated documents
    private static ArrayList<NoteyDocument> noteyDocumentArr = new ArrayList<>();
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
    private static void writeTextAndHTMLToFile(String text, String html, String title){
        try{
            File output;
            String validated;
            String validChars = "(\\W)";
            if(title.length()>10){
				validated = title.replaceAll(validChars, "").substring(0, 10);
                output= new File("notes/" + validated + ".txt");
            }
            else{
				validated = title.replaceAll(validChars, "");
                output= new File("notes/" + validated + ".txt");
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(output));
			System.out.println("writing " + text + " to " + validated);
            writer.write(text);
            writer.close();
            output= new File("notes/" + validated + ".html");
            writer = new BufferedWriter(new FileWriter(output));
            writer.write(html);
            writer.close();
        }
        catch(IOException io_exc){
            System.out.println(io_exc);
        }

    }
    private static String saveDocument(TextArea dText, TextField dTitle, boolean saveAll){
        String result = new String();
        for(NoteyDocument document : noteyDocumentArr){
            if(saveAll || document.getButton().isDisable()){
                //If saveAll is true, all documents are saved
                if(document.getButton().isDisable()){
                    /*
                    a button being disabled means its currently selected
                    the normal text is saved in all cases

                    */
                    document.setNormalText(dText.getText());
                    document.setTitle(dTitle.getText());
                    document.setButtonText(document.getTitle());
                }
                boolean notEmptyDocTitle = !document.getTitle().equals("");
                document.convertDocumentToHTML();
                if(notEmptyDocTitle)
                    document.setButtonText(document.getTitle());
                else
                    continue;
                new File("notes").mkdir();
                writeTextAndHTMLToFile(document.getNormalText(), document.getHTMLText(),
                    document.getTitle());
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
        for(NoteyDocument document : noteyDocumentArr){
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
            setSideButtonsOnClickHandler(documentText, documentTitle, saveButton, saveAllButton,
            e, title,text, webViewBox);
        });
        documentText.setText(text);
        documentTitle.setText(title);
        newButton.setText(title);
        return newButton;

    }
    private static void setSideButtonsOnClickHandler(TextArea documentText,
        TextField documentTitle, Button saveButton, Button saveAllButton,
        ActionEvent click, String title, String text, WebView webViewBox){
            searchForDisabledDocumentInArr(documentText, documentTitle);
            documentTitle.setText(""); //Clears it on each button press
            if(documentTitle.isDisable() && documentText.isDisable() && saveButton.isDisable()
                && saveAllButton.isDisable()){
                //Enables the document title box and text box on the first button click
                documentTitle.setDisable(false);
                documentText.setDisable(false);
                saveButton.setDisable(false);
                saveAllButton.setDisable(false);
            }
            System.out.println("Clicked");
            NoteyDocument temp = new NoteyDocument((Button)click.getSource(), text, "", title);
            temp.convertDocumentToHTML();
            int index = noteyDocumentArr.indexOf(temp);

            boolean buttonFoundInArray = index > -1;
            if(buttonFoundInArray){
                temp = noteyDocumentArr.get(index);
                if(!temp.textEquals("")){
                    System.out.println("button's normal text is not empty.");
                    System.out.println("button normal text:" + temp.getNormalText());
                    loadIntoWebView(temp.getHTMLText(), webViewBox);
                    documentText.setText(temp.getNormalText());
                    if(!temp.getTitle().equals(""))
                        documentTitle.setText(temp.getTitle());
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
                ((Button)click.getSource()).setText(title);
                System.out.println("Not found");
            }
            temp.toggleButtonDisable();

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
            String fileExtension = note.substring(note.length() - 4, note.length());
            if(fileExtension.equals(".txt")){
                System.out.println("In here");
                File inputFile = new File("notes/" + note);
                try(BufferedReader reader = new BufferedReader(new FileReader(inputFile))){
                    String text = new String();
                    String title = inputFile.getName();
                    String titleWithoutExtension = title.substring(0, title.length() - 4);
					System.out.println("New title: " + titleWithoutExtension);
                    String line;
                    while((line = reader.readLine()) != null){
                        text+=line + System.getProperty("line.separator");
                    }
                    System.out.println(text);
                    Button newButton = setUpNoteyButton(documentText, documentTitle,
                        addDocumentButton, saveButton, saveAllButton, webViewBox, text,
                        titleWithoutExtension);
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
