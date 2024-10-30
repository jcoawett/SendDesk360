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
    private VBox articleList;

    public DashboardView(SendDesk360 mainApp) {
        this.dashboardViewModel = new DashboardViewModel(mainApp);
        initializeUI();
        initializeUserListCards();
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

        articleList = new VBox();
        articleList.setAlignment(Pos.TOP_LEFT);
        articleList.setStyle("-fx-padding: 32px 0px 32px 0px;");
        articleList.setSpacing(16); // Add spacing between user cards
        HBox.setHgrow(articleList, Priority.ALWAYS);

        VBox pageBody = new VBox(pageTitle, articleList);
        pageBody.setAlignment(Pos.TOP_LEFT);
        pageBody.setStyle("-fx-padding: 32px;");
        pageBody.setSpacing(16); // Add spacing between title and list
        HBox.setHgrow(pageBody, Priority.ALWAYS);

        HBox mainPageBody = new HBox(navBar, pageBody);
        mainPageBody.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(mainPageBody, Priority.ALWAYS);
        VBox.setVgrow(mainPageBody, Priority.ALWAYS);

        this.getChildren().addAll(mainPageBody);
        this.setStyle("-fx-background-color: #101011;");
    }

    public void initializeUserListCards() {
    	
    	
    }
}