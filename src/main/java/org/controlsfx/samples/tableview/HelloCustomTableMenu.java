package org.controlsfx.samples.tableview;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.controlsfx.ControlsFXSample;
import org.controlsfx.control.table.TableMenuButtonAccessor;
import org.controlsfx.samples.Utils;

/**
 *
 */
public class HelloCustomTableMenu extends ControlsFXSample {
    
    @Override public String getSampleName() {
        return "Custom TableMenuButton";
    }
    
    @Override public String getJavaDocURL() {
        return Utils.JAVADOC_BASE + "org/controlsfx/control/table/TableMenuButtonAccessor.html";
    }
    
    @Override public Node getPanel(final Stage stage) {
        // boring - setting up TableView
        TableView<String> tableView = new TableView<>();
        tableView.getItems().addAll("Jonathan", "Julia", "Henry");
        
        TableColumn<String, String> names = new TableColumn<>("Name");
        names.setCellValueFactory(cdf -> new ReadOnlyStringWrapper(cdf.getValue()));
        
        tableView.getColumns().add(names);
        
        VBox root = new VBox();
        root.getChildren().add(tableView);
        
        // This is where it gets interesting - we modify the context menu
        tableView.setTableMenuButtonVisible(true);
        TableMenuButtonAccessor.modifyTableMenu(tableView, menu -> {
            menu.getItems().addAll(new SeparatorMenuItem(), new MenuItem("Hello World!"));
        });
        
        return root;
    }
    
    public static void main(String[] args) {
        launch(args);
    } 
}
