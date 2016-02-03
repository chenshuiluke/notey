package notey;

import javafx.scene.control.Button;

//An array of this is preferrable to an overlycomplicated map
public class noteyDocument{
    private Button button;
    private String normalText;
    private String htmlText;

    public noteyDocument(){
        button = null;
        normalText = null;
        htmlText = null;
    }
    public noteyDocument(Button button, String normalText, String htmlText){
        this.button = button;
        this.normalText = normalText;
        this.htmlText = htmlText;
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

    public Button getButton(){
        return button;
    }
    public String getNormalText(){
        return normalText;
    }
    public String getHTMLText(){
        return htmlText;
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
