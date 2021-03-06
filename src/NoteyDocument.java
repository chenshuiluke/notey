package notey;

import javafx.scene.control.Button;

//An array of this is preferrable to an overlycomplicated map
public class NoteyDocument{
    private Button button;
    private String normalText;
    private String htmlText;
    private String title;

    public NoteyDocument(){
        button = null;
        normalText = "";
        htmlText = "";
        title = "";
    }
    public NoteyDocument(Button button, String normalText, String htmlText, String title){
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
        if(other instanceof NoteyDocument){
            NoteyDocument that = (NoteyDocument) other;
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
        String[] lines = normalText.split("[\\r\\n]+");
        String[] docSyntax = {"HEADING", "IMAGE"};
        String[] htmlSyntax = {"<h1>", "<img height='400px' width='400px' src='"};
        String[] htmlEndSyntax = {"</h1>", "'/>"};

        htmlText = "<html>\n<body>";
        for(String line : lines){
            boolean matched = false;
            int counter = 0;
            for(String tag : docSyntax){
                if(line.length() > tag.length() + 1 ){
                    String evaluate = line.substring(0, tag.length());
                    if(evaluate.equals(tag) ||
                        evaluate.equals(tag.toLowerCase())){
                        //System.out.println("Matched");
                        htmlText+=htmlSyntax[counter];
                        htmlText+=line.substring(tag.length() + 1, line.length());
                        htmlText+=htmlEndSyntax[counter];
                        matched = true;
                        break;
                    }
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
        htmlText += "</body></html>";
        System.out.println(htmlText);
    }
}
