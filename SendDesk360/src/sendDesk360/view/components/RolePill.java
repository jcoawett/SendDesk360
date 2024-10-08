package sendDesk360.view.components;


import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;



public class RolePill extends HBox {
	
	// VARIANTS
	// ------------------------------ //
	public enum PillVariant {
		INSTRUCTOR,
		ADMIN,
		STUDENT
	}
	// ------------------------------ //
	
	
	// SET STYLE
	// ------------------------------ //
	public void setVariant(PillVariant variant) {
		
		String baseStyle = "role-pill";
		String labelString = "";
		
		this.getStyleClass().clear();
		this.getStyleClass().add(baseStyle);
		
		//Initialize the dot
		Circle dot = new Circle(5);
		dot.setFill(null);
		
		//Initialize the Label
		Label pillLabel = new Label();
		pillLabel.setStyle("-fx-text-fill: #F8F8F8");
		
		
		switch(variant) {
		
		case INSTRUCTOR:
			
			dot.setFill(Color.web("#7300FF"));
			labelString = "Instructor";
			break;
			
		case ADMIN:
			
			dot.setFill(Color.web("#00AAFF"));
			labelString = "Admin";
			break;
			
		case STUDENT:
			
			dot.setFill(Color.web("#BF00FF"));
			labelString = "Student";
			break;
		
		}
		
		pillLabel.setText(labelString);
		
		this.getChildren().clear();
                        
		this.getChildren().addAll(dot, pillLabel);
		
        this.setSpacing(10);
        
        // Ensure this HBox doesn't expand to fill the width of its parent
        this.setMaxWidth(USE_COMPUTED_SIZE);
        this.setPrefWidth(USE_COMPUTED_SIZE);
        this.setMinWidth(USE_COMPUTED_SIZE);
	}
	// ------------------------------ //

	
	// CONSTRUCTOR
	// ------------------------------ //
	public RolePill(PillVariant variant) {
		setVariant(variant);
	}
	// ------------------------------ //
}