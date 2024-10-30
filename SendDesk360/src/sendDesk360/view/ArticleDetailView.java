package sendDesk360.view;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import sendDesk360.viewModel.ArticleViewModel;
import sendDesk360.SendDesk360;
import sendDesk360.view.components.PrimaryButton;
import sendDesk360.view.components.PrimaryButton.ButtonVariant;

public class ArticleDetailView extends VBox {

    public ArticleDetailView(ArticleViewModel viewModel, SendDesk360 mainApp) {
        // Title
        Label title = new Label(viewModel.getTitle());
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #F8F8F8;");

        // Body
        Label body = new Label(viewModel.getBody());
        body.setWrapText(true);
        body.setStyle("-fx-font-size: 18px; -fx-text-fill: #D3D3D3;");

        // Layout setup
        this.setAlignment(Pos.TOP_CENTER);
        this.setSpacing(20);
        this.getChildren().addAll(title, body);

        // Admin-only controls (edit/delete)
        if (viewModel.isAdmin()) {
        	
            PrimaryButton editButton = new PrimaryButton(ButtonVariant.FILLED, "Edit Article", event -> {
            	System.out.println("ADD EDIT FUNCTION HERE");
            });
            
            PrimaryButton deleteButton = new PrimaryButton(ButtonVariant.TEXT_ONLY, "Delete Article", event -> {
            	
                try {
                    viewModel.deleteArticle();  // Delete article using the ViewModel
                    mainApp.showDashboard();    // Navigate back to dashboard
                } catch (Exception ex) {
                    ex.printStackTrace();  // Handle error gracefully
                    
                }
            });

            this.getChildren().addAll(editButton, deleteButton);
        }
        
        
    }
}