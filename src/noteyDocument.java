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
    public void setButtonText(String title){
        button.setText(title);
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
    public String getButtonText(){
        return button.getText();
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
    public void convertDocumentToHTML(){
        String[] lines = normalText.split(System.getProperty("line.separator"));
        String[] docSyntax = {"HEADING", "IMAGE"};
        String[] htmlSyntax = {"<h1>", "<img src='"};
        String[] htmlEndSyntax = {"</h1>", "'/>"};

        htmlText = "<html>\n";
        for(String line : lines){
            boolean matched = false;
            int counter = 0;
            for(String tag : docSyntax){
                if(line.length() > tag.length() + 1 && line.substring(0, tag.length()).equals(tag)){
                    //System.out.println("Matched");
                    htmlText+=htmlSyntax[counter];
                    htmlText+=line.substring(tag.length() + 1, line.length());
                    htmlText+=htmlEndSyntax[counter];
                    matched = true;
                    break;
                }
                counter++;
            }
            if(!matched){
                htmlText+="<p>";
                htmlText+=line.substring(0, line.length());
                htmlText+="</p>";
            }
            System.out.println(line);
        }
        htmlText += "</html>";
        System.out.println(htmlText);
    }
}
