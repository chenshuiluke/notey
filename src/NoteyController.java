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
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXML;
import javax.annotation.Resources;
public class NoteyController{

  private boolean showSidePane;
  private double dividerPosition;
  @FXML
	private SplitPane mainViewPane;
  @FXML
  private VBox sidePane;

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
