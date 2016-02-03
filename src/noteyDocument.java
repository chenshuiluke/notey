package notey;

import javafx.scene.control.Button;

//An array of this is preferrable to an overlycomplicated map
public class noteyDocument{
    private Button button;
    private String normalText;
    private String htmlText;
    private String title;

    public noteyDocument(){
        button = null;
        normalText = "";
        htmlText = "";
        title = "";
    }
    public noteyDocument(Button button, String normalText, String htmlText, String title){
        this.button = button;
        this.normalText = normalText;
        this.htmlText = htmlText;
        this.title = title;
    }

    public void setButton(Button button){
        this.button = button;
    }
    public void setNormalText(String normalText){
        this.normalText = normalText;
    }
    public void setHTMLText(String htmlText){
        this.htmlText = htmlText;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public Button getButton(){
        return button;
    }
    public String getNormalText(){
        return normalText;
    }
    public String getHTMLText(){
        return htmlText;
    }
    public String getTitle(){
        return title;
    }
    public void toggleButtonDisable(){
        button.setDisable(!button.isDisable());
    }
    public boolean equals(Object other){
        boolean result = false;
        if(other instanceof noteyDocument){
            noteyDocument that = (noteyDocument) other;
            result = this.getButton() == that.getButton();
                /*
                &&
                this.getNormalText() == that.getNormalText() &&
                this.getHTMLText() == that.getHTMLText();
                */
        }
        return result;
    }
    public boolean textEquals(String normalText){
        return this.normalText == normalText;
    }
}
