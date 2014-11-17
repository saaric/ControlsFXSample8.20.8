package org.controlsfx.samples;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import org.controlsfx.ControlsFXSample;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.control.PropertySheet.Item;
import org.controlsfx.tools.Borders;

public class HelloBorders extends ControlsFXSample {
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override public String getSampleName() {
        return "Borders";
    }
    
    @Override public String getJavaDocURL() {
        return Utils.JAVADOC_BASE + "org/controlsfx/tools/Borders.html";
    }
    
    @Override public boolean isVisible() {
        return true;
    }
    
    @Override public Node getPanel(Stage stage) {
        Pane root = new Pane();
        
        
        
//--这个程序这段代码是核心的。告诉你如何给Button创建边框-----------------------------------------        
        Button button = new Button("Hello World!");
        Node wrappedButton = Borders.wrap(button)
                
                                                 .lineBorder()
                                                 .title("Line")
                                                 .color(Color.GREEN)
                                                 .thickness(1, 0, 0, 0)
                                                 .thickness(1)
                                                 .radius(0, 5, 5, 0)
                                                 .build()
                                                     
                                                 .emptyBorder()
                                                 .padding(20)
                                                 .build()
                                                     
                                                 .etchedBorder()
                                                 .title("Etched")
                                                 .build()
                                                     
                                                 .emptyBorder()
                                                 .padding(20)
                                                 .build()
                                                     
                                                 .build();
//----------------------------------------------------------------------------------
        
        
        
        root.getChildren().add(wrappedButton);
        
        return root;
    }
    
    @Override public String getSampleDescription() {
        return "A utility class that allows you to wrap JavaFX Nodes with a border, "
                + "in a way somewhat analogous to the Swing BorderFactory (although "
                + "with less options as a lot of what the Swing BorderFactory offers "
                + "resulted in ugly borders!)."
                + "\n\nThe Borders class provides a fluent API for specifying the "
                + "properties of each border. It is possible to create multiple "
                + "borders around a Node simply by continuing to call additional "
                + "methods before you call the final build() method. To use the "
                + "Borders class, you simply call wrap(Node), passing in the Node "
                + "you wish to wrap the border(s) around.";
    }
    
    
//  下面这些个代码都可以忽略。它是在UI上添加了一个控制台。--------------------------------------------------
    @Override
    public Node getControlPanel() {
        // current borders
        ListView<String> currentBordersListView = new ListView<String>();
        currentBordersListView.setPrefHeight(100);
        Node borderedListView = Borders.wrap(currentBordersListView)
                
                .etchedBorder()
                .title("Current Borders:")
                .build()
                
                .emptyBorder()
                .padding(5)
                .build()
                
                .build();
        
        // add new borders
        Tab lineBorderTab = buildLineBorderTab();
        Tab etchedBorderTab = new Tab("Etched");
        Tab emptyBorderTab = new Tab("Empty");
        
        TabPane tabPane = new TabPane();
        tabPane.getStyleClass().add(TabPane.STYLE_CLASS_FLOATING);
        tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        tabPane.setMaxHeight(Double.MAX_VALUE);
        tabPane.getTabs().addAll(lineBorderTab, etchedBorderTab, emptyBorderTab);
        
        Region borderedTabPane = (Region) Borders.wrap(tabPane)
                
            .lineBorder()
            .thickness(1, 0, 0, 0)
            .title("Add a Border:")
            .build()
            
            .emptyBorder()
            .padding(5, 0, 0, 0)
            .build()
            
            .build();
        
        borderedTabPane.setMaxHeight(Double.MAX_VALUE);
        
        VBox vbox = new VBox(borderedListView, borderedTabPane);
        vbox.setMaxHeight(Double.MAX_VALUE);
        vbox.setSpacing(10);
        
        StackPane stackPane = new StackPane(tabPane);
        stackPane.setMaxHeight(Double.MAX_VALUE);
        
        return stackPane;
    }

    private Tab buildLineBorderTab() {
        PropertySheet lineBorderPropertySheet = new PropertySheet();
        lineBorderPropertySheet.setModeSwitcherVisible(false);
        lineBorderPropertySheet.setSearchBoxVisible(false);
        lineBorderPropertySheet.setMaxHeight(Double.MAX_VALUE);
        
        Item titleProperty = new BorderItem("Title");
        lineBorderPropertySheet.getItems().add(titleProperty);
        
        Item colorProperty = new BorderItem("Color", Color.class);
        lineBorderPropertySheet.getItems().add(colorProperty);
        
        Item radiusProperty = new BorderItem("Radius", Number.class);
        lineBorderPropertySheet.getItems().add(radiusProperty);
        
        Item thicknessProperty = new BorderItem("Thickness", Number.class);
        lineBorderPropertySheet.getItems().add(thicknessProperty);
        
        Tab tab = new Tab("Line");
        tab.setContent(lineBorderPropertySheet);
        return tab;
    }
    
    private static class BorderItem implements Item {
        private final String displayText;
        private final Class<?> type;
        
        public BorderItem(String displayText) {
            this(displayText, null);
        }
        
        public BorderItem(String displayText, Class<?> type) {
            this.displayText = displayText;
            this.type = type == null ? String.class : type;
        }

        @Override public Class<?> getType() {
            return type;
        }

        @Override public String getCategory() {
            return null;
        }

        @Override public String getName() {
            return displayText;
        }

        @Override public String getDescription() {
            return null;
        }

        @Override public Object getValue() {
            return null;
        }

        @Override public void setValue(Object value) {
        }
    }
}
