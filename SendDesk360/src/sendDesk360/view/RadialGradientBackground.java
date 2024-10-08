package sendDesk360.view;

import javafx.scene.layout.*;
import javafx.scene.paint.*;

public class RadialGradientBackground {

    public static void applyLayeredRadialGradient(Region region) {
        // First, solid dark background
        BackgroundFill solidBackground = new BackgroundFill(
            Color.web("#080808"),  // Dark background color
            CornerRadii.EMPTY,     // No corner radius
            null                   // No insets
        );

        // Second, the radial gradient overlay
        RadialGradient radialGradient = new RadialGradient(
            0,                      // focusAngle
            0,                      // focusDistance
            0.5,                    // centerX
            0,                    // centerY
            0.8,                    // radius
            true,                   // proportional
            CycleMethod.NO_CYCLE,    // no cycle
            new Stop(0.05, Color.web("#F8F8F8", 0.08)),  // Light white at the center, 8% opacity
            new Stop(1.0, Color.web("#000000", 0.0))     // Transparent black at the edges
        );

        BackgroundFill gradientOverlay = new BackgroundFill(
            radialGradient,          // Apply the radial gradient
            CornerRadii.EMPTY,       // No corner radius
            null                     // No insets
        );

        // Create the final background by combining both layers
        Background background = new Background(solidBackground, gradientOverlay);
        region.setBackground(background);
    }
}