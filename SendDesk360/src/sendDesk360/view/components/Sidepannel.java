package sendDesk360.view.components;

import java.awt.Label;

import javafx.scene.layout.VBox;

public class Sidepannel extends VBox {
	
	VBox container = new VBox();
	
	public void initializeUI() {
		
		Label chatLabel = new Label();
		chatLabel.setText("LUNA01");
	}
	
	public Sidepannel() {
		
		this.initializeUI();
	}
}
