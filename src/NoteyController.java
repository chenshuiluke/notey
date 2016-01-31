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
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXML;
import javax.annotation.Resources;
import javafx.scene.control.Button;
import java.util.ArrayList;
public class NoteyController{

  private boolean showSidePane;
	private ArrayList<Button> buttonArr = new ArrayList<Button>();
  private double dividerPosition;
  @FXML
	private SplitPane mainViewPane;
  @FXML
  private ScrollPane sidePane;
	@FXML
	private VBox sideButtonHolder;
	@FXML
	private Button addDocumentButton;

	public void addDocument(ActionEvent event){
		System.out.println("Add document");
		Button newButton = new Button("Document");
		newButton.setMinWidth(0);
		newButton.setPrefWidth(addDocumentButton.getPrefWidth());
		newButton.setMaxWidth(addDocumentButton.getMaxWidth());
		newButton.setOnAction(e -> {
			for(Button button : buttonArr){
				if(button.isDisable()){
					button.setDisable(false);
				}
			}
			System.out.println("Clicked");
			Button temp = (Button)e.getSource();
			temp.setDisable(true);
			buttonArr.add(temp);
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
