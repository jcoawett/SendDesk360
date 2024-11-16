package sendDesk360.view.components;


import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Paint;



public class SpecialGroupTag extends HBox {
	
	
	// CONSTRUCTOR
	// ------------------------------ //
	public SpecialGroupTag(String groupName, Paint dotColor) {
		
		String baseStyle = "role-pill";
		String labelString = groupName;
		
		this.getStyleClass().clear();
		this.getStyleClass().add(baseStyle);
		
		//Initialize the dot
		Circle dot = new Circle(5);
		dot.setFill(dotColor);
		
		//Initialize the Label
		Label pillLabel = new Label();
		pillLabel.setStyle("-fx-text-fill: #F8F8F8; -fx-font-size: 12px;");
		pillLabel.setPrefHeight(16);
		pillLabel.setAlignment(Pos.CENTER);

		
		pillLabel.setText(labelString);
		
		this.getChildren().clear();
                        
		this.getChildren().addAll(dot, pillLabel);
		
        this.setSpacing(10);
        
        // Ensure this HBox doesn't expand to fill the width of its parent
        this.setAlignment(Pos.CENTER);
        this.setPrefWidth(USE_COMPUTED_SIZE);
        this.setPrefHeight(USE_COMPUTED_SIZE);
	
	}
	// ------------------------------ //
}