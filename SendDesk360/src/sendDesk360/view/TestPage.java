package sendDesk360.view;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import sendDesk360.view.components.RadialGradientBackground;
import sendDesk360.view.components.TextBubble;
import sendDesk360.view.components.TextBubble.BubbleVariant;

public class TestPage extends VBox {

    public TestPage() {
    	
    

        TextBubble userTest = new TextBubble(BubbleVariant.USER, "PP POO POO CHECK");
        TextBubble aiTest = new TextBubble(BubbleVariant.AI, "PP POO POO CHECK");
        TextBubble aiErrorTest = new TextBubble(BubbleVariant.ERROR, "PP POO POO CHECK");
        
        userTest.setMaxWidth(400);
        aiTest.setMaxWidth(400);
        aiErrorTest.setMaxWidth(400);

        this.setStyle("-fx-padding: 80px;");
        this.getChildren().addAll(userTest, aiTest, aiErrorTest);
        this.setSpacing(16);
        this.setAlignment(Pos.CENTER);
        RadialGradientBackground.applyLayeredRadialGradient(this);
        
    }
}

