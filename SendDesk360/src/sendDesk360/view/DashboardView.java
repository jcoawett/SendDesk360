package sendDesk360.view;

import sendDesk360.SendDesk360;
import sendDesk360.view.components.NavBar;

import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import sendDesk360.viewModel.DashboardViewModel;

public class DashboardView extends VBox {
	
	private final DashboardViewModel dashboardViewModel;
	
	public DashboardView(SendDesk360 mainApp) {
		this.dashboardViewModel = new DashboardViewModel(mainApp);
		initializeUI();
	}
	
	private void initializeUI() {
		
		
        NavBar navBar = new NavBar();
        navBar.setAlignment(Pos.TOP_LEFT);
        VBox.setVgrow(navBar, Priority.ALWAYS);
        navBar.setMaxWidth(300);
        navBar.setMaxHeight(Double.MAX_VALUE);
        
        Label pageTitle = new Label();
        pageTitle.setText("Dashboard");
        pageTitle.setStyle("-fx-font-size: 32px; -fx-font-weight: 700; -fx-text-fill: #F8F8F8;");
        
        VBox pageBody = new VBox(pageTitle);
        pageBody.setAlignment(Pos.TOP_LEFT);
        pageBody.setStyle("-fx-padding: 32px");
        HBox.setHgrow(pageBody, Priority.ALWAYS);
        
        HBox mainPageBody = new HBox(navBar, pageBody);
        mainPageBody.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(mainPageBody, Priority.ALWAYS);
        VBox.setVgrow(mainPageBody, Priority.ALWAYS);
        
        this.getChildren().addAll(mainPageBody);
        this.getStyleClass().add("root");
	} 
	
}
