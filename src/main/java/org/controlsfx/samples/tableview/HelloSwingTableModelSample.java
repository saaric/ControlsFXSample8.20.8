package org.controlsfx.samples.tableview;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.controlsfx.ControlsFXSample;
import org.controlsfx.control.table.model.JavaFXTableModels;
import org.controlsfx.control.table.model.TableModelTableView;
import org.controlsfx.samples.Utils;

// TODO sorting doesn't work due to readonlyunbacked list
public class HelloSwingTableModelSample extends ControlsFXSample {

    @Override public String getSampleName() {
        return "Swing TableModel TableView";
    }
    
    @Override public String getJavaDocURL() {
        return Utils.JAVADOC_BASE + "org/controlsfx/control/table/TableModelTableView.html";
    }
    
    @Override public Node getPanel(final Stage stage) {
        TableModel swingTableModel = new DefaultTableModel(
            new Object[][] { /* Data: row, column */
                { "1", "2", "3" }, 
                { "4", "5", "6" },
                { "7", "8", "9" },
                { "10", "11", "12" },
            }, 
            new String[] {  /* Column names */
                "Column 1", "Column 2", "Column 3"
            }
        );
        
        TableModelTableView<String> tableView = new TableModelTableView<>(JavaFXTableModels.wrap(swingTableModel));
        
//        tableView.getSelectionModel().selectedItemProperty().addListener((o, oldRow, newRow) -> {
//            System.out.println("Old row: " + oldRow + ", new row: " + newRow);
//        });
        
        VBox root = new VBox();
        root.getChildren().add(tableView);
        return root;
    }
    
    public static void main(String[] args) {
        launch(args);
    } 
}
