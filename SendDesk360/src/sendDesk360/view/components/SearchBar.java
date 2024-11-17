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
	
	
	//TODO: FIX THIS

    private TextField searchField;
    private PrimaryButton createNewArticle;
    private SmallButton filterButton;

    public SearchBar() {
        initializeUI();
    }	

    private void initializeUI() {
        // Create the search field
        searchField = new TextField();
        searchField.setPromptText("Search...");
        searchField.getStyleClass().add(null);
        

        // Add the magnifying glass icon inside the search field
        ImageView searchIconView = new ImageView(new Image(getClass().getResourceAsStream("/sendDesk360/view/assets/search.png")));        
        searchIconView.setFitHeight(16); // Adjust size as needed
        searchIconView.setPreserveRatio(true);


        HBox searchWrapper = new HBox();
        
        
        // Create the 'Filter' button
        filterButton = new SmallButton("Filters", event -> {
            // Function here should be written elsewhere in the file
            onFilter(event);
        });
        
        
        searchWrapper.getChildren().addAll(searchIconView, searchField);
        searchWrapper.getStyleClass().add("-fx-font-size: 16px;"
        		+ "    -fx-font-weight: 400;"
        		+ "    -fx-text-fill: #F8F8F8;"
        		+ "    -fx-padding: 14.5px 14px;"
        		+ "    -fx-background-color: #0F1011;"
        		+ "    -fx-border-color: #28292E;"
        		+ "    -fx-border-radius: 5pxn"
        		+ "    -fx-background-radius: 5px;"
        		+ "    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.64), 6, 0.0, 0, 6);");
        HBox.setHgrow(searchWrapper, Priority.ALWAYS);

        // Spacer between search field and buttons
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        

        // Add components to the HBox
        
        this.getChildren().addAll(searchField, filterButton, spacer);

        // Set alignment and spacing
        this.setPrefHeight(USE_COMPUTED_SIZE);
        this.setAlignment(Pos.CENTER);
        this.setSpacing(8); // Adjust spacing as needed
        this.setPadding(new Insets(16)); // Optional padding
        HBox.setHgrow(this, Priority.ALWAYS);
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
}