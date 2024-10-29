package sendDesk360.view.components;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

public class TextBubble extends HBox {

	
	public enum BubbleVariant {	
		USER,
		AI,	
		ERROR
	}
	
	
	public TextBubble(BubbleVariant variant, String text) {
		
		initializeUI(variant, text);
		
	}
	
	
	public void setVariant(BubbleVariant variant) {
		
		this.getStyleClass().clear();
		this.getStyleClass().add("text-bubble-" + variant); 
		

	}
	
	
	public void initializeUI(BubbleVariant variant, String text) {
		
		SVGPath svgPath = new SVGPath();
        svgPath.setContent("M6 1C6.21595 1 6.40749 1.13864 6.47497 1.34377L7.43097 4.25028C7.45543 4.32466 7.49702 4.39225 7.55239 4.44762C7.60775 4.50298 7.67534 4.54457 7.74972 4.56903L10.6562 5.52503C10.8614 5.59251 11 5.78405 11 6C11 6.21595 10.8614 6.40749 10.6562 6.47497L7.74972 7.43097C7.67534 7.45543 7.60775 7.49702 7.55239 7.55239C7.49702 7.60775 7.45543 7.67534 7.43097 7.74972L6.47497 10.6562C6.40749 10.8614 6.21595 11 6 11C5.78405 11 5.59251 10.8614 5.52503 10.6562L4.56903 7.74972C4.54457 7.67534 4.50298 7.60775 4.44762 7.55239C4.39225 7.49702 4.32466 7.45543 4.25028 7.43097L1.34377 6.47497C1.13864 6.40749 1 6.21595 1 6C1 5.78405 1.13864 5.59251 1.34377 5.52503L4.25028 4.56903C4.32466 4.54457 4.39225 4.50298 4.44762 4.44762C4.50298 4.39225 4.54457 4.32466 4.56903 4.25028L5.52503 1.34377C5.59251 1.13864 5.78405 1 6 1ZM5.51897 4.56272C5.44558 4.78586 5.32081 4.98863 5.15472 5.15472C4.98863 5.32081 4.78586 5.44558 4.56273 5.51897L3.10025 6L4.56273 6.48103C4.78586 6.55442 4.98863 6.67919 5.15472 6.84528C5.32081 7.01137 5.44558 7.21415 5.51897 7.43728L6 8.89975L6.48103 7.43728C6.55442 7.21415 6.67919 7.01137 6.84528 6.84528C7.01137 6.67919 7.21415 6.55442 7.43728 6.48103L8.89975 6L7.43728 5.51897C7.21415 5.44558 7.01137 5.32081 6.84528 5.15472C6.67919 4.98863 6.55442 4.78586 6.48103 4.56272L6 3.10025L5.51897 4.56272Z");
        svgPath.setFill(Color.web("#F8F8F8"));

		//Make the icon
		VBox aiProfilePic = new VBox(svgPath);
		aiProfilePic.setStyle(""
				+ "-fx-background-color: #080808; "
				+ "-fx-border-color: #28292E;"
				+ "-fx-border-radius: 64px;"
				+ "-fx-background-radius: 64px;"
				+ "");
		aiProfilePic.setPrefWidth(24);
		aiProfilePic.setPrefHeight(24);
		aiProfilePic.setAlignment(Pos.CENTER);
		
		
		Label bubbleText = new Label();
		bubbleText.setText(text);
		bubbleText.setStyle("-fx-text-fill: #F8F8F8;");
		
		
		if (variant == BubbleVariant.ERROR) {
			
			bubbleText.setText("Oops an error occured! [ " + text + " ].");
			bubbleText.setStyle("-fx-text-fill: #FF9500;");

		}

		this.setSpacing(8);
		this.setVariant(variant);
		this.getChildren().addAll(aiProfilePic, bubbleText);
		this.setAlignment(Pos.CENTER_LEFT);
		
		
	}
	
	
}
