package sendDesk360.view.components;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.shape.SVGPath;
import javafx.scene.paint.Color;
import javafx.scene.control.ScrollPane;

import java.util.List;

public class ArticleGroupDropdown extends VBox {

    // Enum for the difficulty levels
    public enum ArticleDropdownVariant {
        BEGINNER,
        INTERMEDIATE,
        ADVANCED,
        EXPERT
    }

    // Constructor
    public ArticleGroupDropdown(ArticleDropdownVariant variant, List<ArticlePreviewCard> articlePreviewCards, int articleCount) {
    	
        // Label for difficulty level
        Label groupDiffLabel = new Label(setDiffLabelText(variant));
        groupDiffLabel.setStyle(""
        		+ "-fx-font-size: 16px; "
        		+ "-fx-font-weight: 600;"
        		+ "-fx-text-fill: #F8F8F8;"
        		+ "");
        
        Label articleCountLabel = new Label(String.valueOf(articleCount));
        articleCountLabel.setStyle(""
                + "-fx-font-size: 14px; "
                + "-fx-font-weight: 400;"
                + "-fx-text-fill: #969799;"
                + "");
      

        HBox labelContainer = new HBox(groupDiffLabel, articleCountLabel);
        labelContainer.setSpacing(8); 
        labelContainer.setAlignment(Pos.CENTER_LEFT);
        
        // SVG Icon based on the difficulty level
        HBox svgIcon = getVariantSVG(variant);

        // VBox to hold article preview cards
        VBox articlesContainer = new VBox();
        articlesContainer.setSpacing(0);

        // Add all preview cards to the articles container
        for (ArticlePreviewCard card : articlePreviewCards) {
            articlesContainer.getChildren().add(card);
        }
        
        HBox alignmentContainer = new HBox();
        alignmentContainer.getChildren().addAll(svgIcon, labelContainer, articlesContainer);
        alignmentContainer.setAlignment(Pos.CENTER_LEFT);
        alignmentContainer.setSpacing(8);
        alignmentContainer.setStyle(""
        		+ "-fx-background-color: #17181A;"
        		+ "-fx-padding: 8px;"
        		+ "");
  
        
        // Add components to the VBox (the dropdown)
        this.getChildren().addAll(alignmentContainer, articlesContainer);
        HBox.setHgrow(this, Priority.ALWAYS);
    }

    // Set the text based on the difficulty variant
    private String setDiffLabelText(ArticleDropdownVariant variant) {
    	 switch (variant) {
         case BEGINNER:
             return "Beginner";
         case INTERMEDIATE:
             return "Intermediate";
         case ADVANCED:
             return "Advanced";
         case EXPERT:
             return "Expert";
         default:
             return "Unknown";
    	 }
    }

    // Return the SVGPath based on the difficulty variant
    private HBox getVariantSVG(ArticleDropdownVariant variant) {
        SVGPath svgPath = new SVGPath();
        SVGPath svgPath1 = new SVGPath();
        SVGPath svgPath2 = new SVGPath();
        SVGPath svgPath3 = new SVGPath();

        switch (variant) {
            case BEGINNER:
            	svgPath.setContent("M27.5508 21.1661H30.3984C31.125 21.1661 31.6172 20.6504 31.6172 19.8887V2.09961C31.6172 1.33789 31.125 0.822266 30.3984 0.822266H27.5508C26.8242 0.822266 26.3203 1.33789 26.3203 2.09961V19.8887C26.3203 20.6504 26.8242 21.1661 27.5508 21.1661Z");
            	svgPath.setFill(Color.web("#404146"));

            	svgPath1.setContent("M18.7852 21.1661H21.6094C22.3359 21.1661 22.8398 20.6504 22.8398 19.8887V6.72851C22.8398 5.96679 22.3359 5.45117 21.6094 5.45117H18.7852C18.0469 5.45117 17.5547 5.96679 17.5547 6.72851V19.8887C17.5547 20.6504 18.0469 21.1661 18.7852 21.1661Z");
            	svgPath1.setFill(Color.web("#404146"));

            	svgPath2.setContent("M10.0078 21.1661H12.832C13.5703 21.1661 14.0625 20.6504 14.0625 19.8887V10.9707C14.0625 10.209 13.5703 9.69336 12.832 9.69336H10.0078C9.26953 9.69336 8.77734 10.209 8.77734 10.9707V19.8887C8.77734 20.6504 9.26953 21.1661 10.0078 21.1661Z");
            	svgPath2.setFill(Color.web("#404146"));

            	svgPath3.setContent("M1.23047 21.1661H4.05469C4.79297 21.1661 5.28516 20.6504 5.28516 19.8887V14.627C5.28516 13.8653 4.79297 13.3614 4.05469 13.3614H1.23047C0.492188 13.3614 0 13.8653 0 14.627V19.8887C0 20.6504 0.492188 21.1661 1.23047 21.1661Z");
            	svgPath3.setFill(Color.web("#F8F8F8"));
                break;

            case INTERMEDIATE:
            	svgPath.setContent("M27.5508 21.1661H30.3984C31.125 21.1661 31.6172 20.6504 31.6172 19.8887V2.09961C31.6172 1.33789 31.125 0.822266 30.3984 0.822266H27.5508C26.8242 0.822266 26.3203 1.33789 26.3203 2.09961V19.8887C26.3203 20.6504 26.8242 21.1661 27.5508 21.1661Z");
            	svgPath.setFill(Color.web("#404146"));
            	
            	svgPath1.setContent("M18.7852 21.1661H21.6094C22.3359 21.1661 22.8398 20.6504 22.8398 19.8887V6.72851C22.8398 5.96679 22.3359 5.45117 21.6094 5.45117H18.7852C18.0469 5.45117 17.5547 5.96679 17.5547 6.72851V19.8887C17.5547 20.6504 18.0469 21.1661 18.7852 21.1661Z");
            	svgPath1.setFill(Color.web("#404146"));

            	svgPath2.setContent("M10.0078 21.1661H12.832C13.5703 21.1661 14.0625 20.6504 14.0625 19.8887V10.9707C14.0625 10.209 13.5703 9.69336 12.832 9.69336H10.0078C9.26953 9.69336 8.77734 10.209 8.77734 10.9707V19.8887C8.77734 20.6504 9.26953 21.1661 10.0078 21.1661Z");
            	svgPath2.setFill(Color.web("#F8F8F8"));
            	
            	svgPath3.setContent("M1.23047 21.166H4.05469C4.79297 21.166 5.28516 20.6503 5.28516 19.8886V14.6269C5.28516 13.8652 4.79297 13.3613 4.05469 13.3613H1.23047C0.492188 13.3613 0 13.8652 0 14.6269V19.8886C0 20.6503 0.492188 21.166 1.23047 21.166Z");
            	svgPath3.setFill(Color.web("#F8F8F8"));
            	
                break;

            case ADVANCED:
            	svgPath.setContent("M27.5508 21.1661H30.3984C31.125 21.1661 31.6172 20.6504 31.6172 19.8887V2.09961C31.6172 1.33789 31.125 0.822266 30.3984 0.822266H27.5508C26.8242 0.822266 26.3203 1.33789 26.3203 2.09961V19.8887C26.3203 20.6504 26.8242 21.1661 27.5508 21.1661Z");
            	svgPath.setFill(Color.web("#404146"));
            	
            	svgPath1.setContent("M18.7852 21.1661H21.6094C22.3359 21.1661 22.8398 20.6504 22.8398 19.8887V6.72851C22.8398 5.96679 22.3359 5.45117 21.6094 5.45117H18.7852C18.0469 5.45117 17.5547 5.96679 17.5547 6.72851V19.8887C17.5547 20.6504 18.0469 21.1661 18.7852 21.1661Z");
            	svgPath1.setFill(Color.web("#F8F8F8"));
            	
            	svgPath2.setContent("M10.0078 21.1661H12.832C13.5703 21.1661 14.0625 20.6504 14.0625 19.8887V10.9707C14.0625 10.209 13.5703 9.69336 12.832 9.69336H10.0078C9.26953 9.69336 8.77734 10.209 8.77734 10.9707V19.8887C8.77734 20.6504 9.26953 21.1661 10.0078 21.1661Z");
            	svgPath2.setFill(Color.web("#F8F8F8"));
            	
            	svgPath3.setContent("M1.23047 21.166H4.05469C4.79297 21.166 5.28516 20.6503 5.28516 19.8886V14.6269C5.28516 13.8652 4.79297 13.3613 4.05469 13.3613H1.23047C0.492188 13.3613 0 13.8652 0 14.6269V19.8886C0 20.6503 0.492188 21.166 1.23047 21.166Z");
            	svgPath3.setFill(Color.web("#F8F8F8"));
                break;

            case EXPERT:
            	svgPath.setContent("M27.5508 21.1661H30.3984C31.125 21.1661 31.6172 20.6504 31.6172 19.8887V2.09961C31.6172 1.33789 31.125 0.822266 30.3984 0.822266H27.5508C26.8242 0.822266 26.3203 1.33789 26.3203 2.09961V19.8887C26.3203 20.6504 26.8242 21.1661 27.5508 21.1661Z");
            	svgPath.setFill(Color.web("#F8F8F8"));
            	
            	svgPath1.setContent("M18.7852 21.1661H21.6094C22.3359 21.1661 22.8398 20.6504 22.8398 19.8887V6.72851C22.8398 5.96679 22.3359 5.45117 21.6094 5.45117H18.7852C18.0469 5.45117 17.5547 5.96679 17.5547 6.72851V19.8887C17.5547 20.6504 18.0469 21.1661 18.7852 21.1661Z");
            	svgPath1.setFill(Color.web("#F8F8F8"));
            	
            	svgPath2.setContent("M10.0078 21.1661H12.832C13.5703 21.1661 14.0625 20.6504 14.0625 19.8887V10.9707C14.0625 10.209 13.5703 9.69336 12.832 9.69336H10.0078C9.26953 9.69336 8.77734 10.209 8.77734 10.9707V19.8887C8.77734 20.6504 9.26953 21.1661 10.0078 21.1661Z");
            	svgPath2.setFill(Color.web("#F8F8F8"));
            	
            	svgPath3.setContent("M1.23047 21.166H4.05469C4.79297 21.166 5.28516 20.6503 5.28516 19.8886V14.6269C5.28516 13.8652 4.79297 13.3613 4.05469 13.3613H1.23047C0.492188 13.3613 0 13.8652 0 14.6269V19.8886C0 20.6503 0.492188 21.166 1.23047 21.166Z");
            	svgPath3.setFill(Color.web("#F8F8F8"));
                break;

            default:
                svgPath.setContent("M0 0 L10 10 L0 10 Z");
                svgPath.setFill(Color.GRAY);
                break;
        }

        HBox svgIcon = new HBox();
        svgIcon.getChildren().addAll(svgPath3, svgPath2, svgPath1, svgPath);
        svgIcon.setAlignment(Pos.BOTTOM_LEFT);
        svgIcon.setSpacing(2);
        
        return svgIcon;
    }
}