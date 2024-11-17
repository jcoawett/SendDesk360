package sendDesk360.view.components;

import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import sendDesk360.SendDesk360;
import sendDesk360.model.Article;

import java.util.List;
import java.util.stream.Collectors;

public class SearchBar extends StackPane {

    private TextField searchField;
    private VBox searchResultsContainer; // Container for displaying search results
    private final List<Article> articles; // List of articles to search from
    private Popup resultsPopup; // For dropdown behavior
    private final SendDesk360 mainApp;

    public SearchBar(List<Article> articles, SendDesk360 mainApp) {
        this.articles = articles;
        this.mainApp = mainApp;
        initializeUI();
    }

    private void initializeUI() {
        // Search field with placeholder
        searchField = new TextField();
        searchField.setPromptText("Search...");
        searchField.setStyle("-fx-font-size: 16px; -fx-text-fill: #F8F8F8; -fx-background-color: #0F1011; -fx-border-color: #28292E; -fx-border-radius: 5px; -fx-padding: 10px;");
        searchField.setPrefWidth(400);

        // Search icon
        ImageView searchIcon = new ImageView(new Image(getClass().getResourceAsStream("/sendDesk360/view/assets/search.png")));
        searchIcon.setFitHeight(24);
        searchIcon.setPreserveRatio(true);

        // Create a container for the search field and icon
        HBox searchFieldContainer = new HBox(8, searchIcon, searchField);
        searchFieldContainer.setAlignment(Pos.CENTER_LEFT);

        // Buttons container
        SmallButton clearButton = new SmallButton("Clear", event -> clearSearch());
        SmallButton filterButton = new SmallButton("Filters", event -> System.out.println("TODO: make filter function"));
        HBox actionButtons = new HBox(8, filterButton, clearButton);
        actionButtons.setAlignment(Pos.CENTER);

        // Initialize popup for search results
        resultsPopup = new Popup();
        resultsPopup.setAutoHide(true);

        searchResultsContainer = new VBox();
        searchResultsContainer.setSpacing(8);
        searchResultsContainer.setStyle("-fx-background-color: #17181A; -fx-border-radius: 8px; -fx-padding: 8px;");
        resultsPopup.getContent().add(searchResultsContainer);

        // Listen for text input and update the results
        searchField.textProperty().addListener((observable, oldValue, newValue) -> updateSearchResults(newValue));

        // Combine all elements
        HBox searchBarWithButtons = new HBox(16, searchFieldContainer, actionButtons);
        searchBarWithButtons.setAlignment(Pos.CENTER_LEFT);
        this.getChildren().add(searchBarWithButtons);
    }

    private void clearSearch() {
        searchField.clear();
        searchResultsContainer.getChildren().clear();
        resultsPopup.hide();
    }

    private void updateSearchResults(String query) {
        searchResultsContainer.getChildren().clear();

        if (query.isEmpty()) {
            resultsPopup.hide();
            return;
        }

        List<Article> matchingArticles = articles.stream()
                .filter(article -> article.getTitle().toLowerCase().contains(query.toLowerCase())
                        || article.getShortDescription().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());

        for (Article article : matchingArticles) {
            HBox resultItem = new HBox();
            resultItem.setStyle("-fx-background-color: #28292E; -fx-padding: 10px; -fx-border-radius: 5px;");
            resultItem.setAlignment(Pos.CENTER_LEFT);
            resultItem.setOnMouseClicked(event -> openArticle(article)); // Add click behavior

            Label resultText = new Label(article.getTitle() + ": " + article.getShortDescription());
            resultText.setStyle("-fx-text-fill: #F8F8F8; -fx-font-size: 14px;");
            resultItem.getChildren().add(resultText);

            searchResultsContainer.getChildren().add(resultItem);
        }

        if (!resultsPopup.isShowing()) {
            Window window = this.getScene().getWindow();
            resultsPopup.setWidth(searchField.getWidth()); // Match the dropdown width with the search field
            resultsPopup.show(window,
                    window.getX() + searchField.localToScene(0, 0).getX() + searchField.getScene().getX(),
                    window.getY() + searchField.localToScene(0, 0).getY() + searchField.getScene().getY() + searchField.getHeight());
        }
    }

    private void openArticle(Article article) {
        System.out.println("Navigating to article: " + article.getTitle());
        mainApp.showArticleDetailView(article); // Navigate to the article detail view
        resultsPopup.hide(); // Close the dropdown
    }
}