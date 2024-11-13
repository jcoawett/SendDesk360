package sendDesk360.view.components;

import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Region;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class SearchBar extends HBox {

    private TextField searchField;
    private PrimaryButton createNewArticle;
    private PrimaryButton filterButton;

    public SearchBar() {
        initializeUI();
    }	

    private void initializeUI() {
        // Create the search field
        searchField = new TextField();
        searchField.setPromptText("Search...");
        searchField.getStyleClass().add("primary-field-variant-default");
        searchField.setMaxWidth(Double.MAX_VALUE);

        // Add the magnifying glass icon inside the search field
        ImageView searchIconView = new ImageView(new Image(getClass().getResourceAsStream("/icons/search-icon.png")));
        searchIconView.setFitHeight(16); // Adjust size as needed
        searchIconView.setPreserveRatio(true);

        // Create a StackPane to hold the TextField and the icon
        StackPane searchFieldStack = new StackPane();
        searchFieldStack.getChildren().addAll(searchField);

        // Position the icon to the right inside the TextField
        StackPane.setAlignment(searchIconView, Pos.CENTER_RIGHT);
        StackPane.setMargin(searchIconView, new Insets(0, 8, 0, 0)); // 8px spacing to the right

        // Add the icon to the StackPane
        searchFieldStack.getChildren().add(searchIconView);

        // Create the 'Create New' button
        createNewArticle = new PrimaryButton(PrimaryButton.ButtonVariant.FILLED, "Create New", event -> {
            // Function here should be written elsewhere in the file
            onCreateNewArticle(event);
        });

        // Create the 'Filter' button
        filterButton = new PrimaryButton(PrimaryButton.ButtonVariant.FILLED, "Filter", event -> {
            // Function here should be written elsewhere in the file
            onFilter(event);
        });

        // Spacer between search field and buttons
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Add components to the HBox
        this.getChildren().addAll(searchFieldStack, spacer, filterButton, createNewArticle);

        // Set alignment and spacing
        this.setAlignment(Pos.CENTER_LEFT);
        this.setSpacing(8); // Adjust spacing as needed
        this.setPadding(new Insets(8)); // Optional padding
    }

    // Event handlers (to be implemented elsewhere)
    private void onCreateNewArticle(ActionEvent event) {
        // Implementation here
    }

    private void onFilter(ActionEvent event) {
        // Implementation here
    }

    // Getters for components if needed
    public TextField getSearchField() {
        return searchField;
    }

    public PrimaryButton getCreateNewArticleButton() {
        return createNewArticle;
    }

    public PrimaryButton getFilterButton() {
        return filterButton;
    }
}