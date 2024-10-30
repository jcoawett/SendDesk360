package sendDesk360.view.components;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import sendDesk360.model.Article;
import sendDesk360.SendDesk360;

public class ArticlePreviewCard extends HBox {

    public ArticlePreviewCard(Article article, SendDesk360 mainApp) {
        // Title
        Label title = new Label(article.getTitle());
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: 600; -fx-text-fill: #F8F8F8;");

        // Subtitle (short description)
        Label subtitle = new Label(article.getShortDescription());
        subtitle.setStyle("-fx-font-size: 16px; -fx-font-weight: 400; -fx-text-fill: #969799;");

        // Layout container for text
        VBox textContainer = new VBox(title, subtitle);
        textContainer.setSpacing(0);

        // Configure this card's layout
        HBox.setHgrow(this, Priority.ALWAYS);
        this.getChildren().add(textContainer);
        this.setAlignment(Pos.CENTER_LEFT);
        this.getStyleClass().add("article-preview-card");

        // Add click event to navigate to the article detail view
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            mainApp.showArticleDetailView(article); // Navigate on click
        });
    }
}