package sendDesk360.view.components;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.control.Label;

public class UserCard extends VBox {

    public UserCard(String iconLetter, String usernameText, String emailText) {

        Label firstLetter = new Label();
        firstLetter.setText(iconLetter);
        firstLetter.setStyle("-fx-text-fill: #000000;");

        Circle profileBG = new Circle();
        profileBG.setRadius(24);
        profileBG.setStyle("-fx-background-color: #FFFFFF;");

        firstLetter.toFront();

        Pane profileIcon = new Pane();
        profileIcon.getChildren().addAll(profileBG, firstLetter);

        Label username = new Label();
        username.setText(usernameText);
        username.setStyle(""
                + "-fx-font-size: 16px;\n"
                + "-fx-font-weight: 700;\n"
                + "-fx-text-alignment: center;\n"
                + "-fx-text-fill: white;"
        );

        Label email = new Label();
        email.setText(emailText);
        email.setStyle(""
                + "-fx-font-size: 16px;\n"
                + "-fx-font-weight: 700;\n"
                + "-fx-text-alignment: center;\n"
                + "-fx-text-fill: white;"
        );

        HBox heading = new HBox(profileIcon, username);
        heading.setSpacing(8);

        VBox contentWrapper = new VBox(heading, email);
        contentWrapper.setSpacing(4);

        this.getChildren().add(contentWrapper);
        this.setAlignment(Pos.CENTER_LEFT);
        this.setStyle(""
                + "-fx-background-color: #17181A;\n"
                + "-fx-padding: 16px 8px 16px 8px;\n"
                + "-fx-border-color: #28292E;\n"
                + "-fx-border-radius: 5px;\n"
                + "-fx-background-radius: 5px;\n"
                + "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.64), 12, 0, 0, 6);"
        );
    }
}