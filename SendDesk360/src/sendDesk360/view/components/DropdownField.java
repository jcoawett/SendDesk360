package sendDesk360.view.components;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;

public class DropdownField extends VBox {

    private ComboBox<String> dropdown;

    // Constructor
    public DropdownField(String placeholderText, List<String> items) {
        initializeUI(placeholderText, items);
    }

    private void initializeUI(String placeholderText, List<String> items) {
        // Set up the VBox container
        this.setAlignment(Pos.CENTER_LEFT);

        // Create the dropdown container
        HBox dropdownContainer = new HBox();
        dropdownContainer.setAlignment(Pos.CENTER_LEFT);
        dropdownContainer.setMaxWidth(Double.MAX_VALUE);

        // Create the dropdown
        dropdown = new ComboBox<>(FXCollections.observableArrayList(items));
        dropdown.setPromptText(placeholderText);
        dropdown.setVisibleRowCount(5);
        dropdown.setMaxWidth(Double.MAX_VALUE);

        // Style the dropdown to match other fields
        dropdown.getStyleClass().add("primary-field-variant-default");

        // Add dropdown to the container
        dropdownContainer.getChildren().add(dropdown);
        HBox.setHgrow(dropdown, Priority.ALWAYS);

        // Add the dropdown container to the VBox
        this.getChildren().add(dropdownContainer);
        this.prefHeight(USE_COMPUTED_SIZE);
    }

    // Methods for interacting with the dropdown
    public void setItems(List<String> items) {
        dropdown.setItems(FXCollections.observableArrayList(items));
    }

    public void setPlaceholder(String text) {
        dropdown.setPromptText(text);
    }

    public ObjectProperty<String> selectedValueProperty() {
        return dropdown.valueProperty();
    }

    public ComboBox<String> getDropdown() {
        return dropdown;
    }

    public void setSelectedValue(String value) {
        dropdown.setValue(value);
    }

    public String getSelectedValue() {
        return dropdown.getValue();
    }
}