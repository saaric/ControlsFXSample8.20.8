package org.controlsfx.samples;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import org.controlsfx.ControlsFXSample;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;
import org.controlsfx.dialog.Dialogs;

public class HelloSpreadsheetView extends ControlsFXSample {
    public static void main(String[] args) {
        launch(args);
    }

    private SpreadsheetView spreadSheetView;
    private final CheckBox rowHeader = new CheckBox();
    private final CheckBox columnHeader = new CheckBox();
    private final CheckBox selectionMode = new CheckBox();
    private final CheckBox editable = new CheckBox();
    
    //List for custom cells
    private final List<String> companiesList = Arrays.asList("", "ControlsFX", "Aperture Science", "Rapture", "Ammu-Nation", "Nuka-Cola", "Pay'N'Spray", "Umbrella Corporation");
    private final List<String> countryList = Arrays.asList("China", "France", "New Zealand", "United States", "Germany", "Canada");
    private final List<String> logoList = Arrays.asList("", "ControlsFX.png", "apertureLogo.png", "raptureLogo.png", "ammunationLogo.JPG", "nukaColaLogo.png", "paynsprayLogo.jpg", "umbrellacorporation.png");


    @Override public Node getPanel(Stage stage) {
        StackPane centerPane = new StackPane();
        GridBase gridBase = buildGridBase();
        spreadSheetView = new SpreadsheetView(gridBase);
        spreadSheetView.setShowRowHeader(rowHeader.isSelected());
        spreadSheetView.setShowColumnHeader(columnHeader.isSelected());
        spreadSheetView.setEditable(editable.isSelected());
        spreadSheetView.getSelectionModel().setSelectionMode(selectionMode.isSelected() ? SelectionMode.MULTIPLE : SelectionMode.SINGLE);
        generatePickers();
        spreadSheetView.getFixedRows().add(0);
        spreadSheetView.getColumns().get(0).setFixed(true);
        spreadSheetView.getColumns().get(1).setPrefWidth(250);
        centerPane.getChildren().setAll(spreadSheetView);
        spreadSheetView.getStylesheets().add(getClass().getResource("spreadsheetSample.css").toExternalForm());
        return centerPane;
    }
    
    //重点代码段 - 创建核心对象 - gridBase
    private GridBase buildGridBase() {
        
        //--初始化创建gridBase------------------------------------------------
        int rowCount = 31; //Will be re-calculated after if incorrect.
        int columnCount = 8;
        GridBase gridBase = new GridBase(rowCount, columnCount);        
        //----------------------------------------------------------------
        
        //--set row height-------------------------------------------------
        Map<Integer, Double> rowHeight = new HashMap<>();
        rowHeight.put(3, 100.0);        
        gridBase.setRowHeightCallback(new GridBase.MapBasedRowHeightFactory(rowHeight));        
        //------------------------------------------------------------------
        
        //--这些代码重点---------------------------------------------------------
        //--告诉你如何把内容一行一行插入gridBase中去
        ArrayList<ObservableList<SpreadsheetCell>> rows = new ArrayList<>(gridBase.getRowCount());
        int rowIndex = 0;
        
        //--step 1) 前面8行已经非常具有参考价值了！！--------------------------------
        rows.add(getCompanies(gridBase, rowIndex++));
        rows.add(getCountries(gridBase, rowIndex++));
        rows.add(getStartDate(gridBase, rowIndex++));
        rows.add(getLogos(gridBase, rowIndex++));
        rows.add(getIncome(gridBase, rowIndex++));
        rows.add(getIncrease(gridBase, rowIndex++));
        rows.add(getEmployees(gridBase, rowIndex++));
        rows.add(getWebSite(gridBase, rowIndex++));
        //--step 2) 中间3行Separators----------------------------------------
        rows.add(getSeparator(gridBase, rowIndex++));
        rows.add(getSeparator(gridBase, rowIndex++));
        rows.add(getSeparator(gridBase, rowIndex++));
        //--step 3) 接下来循环创建20行-------------------------------------------
        for (int i = rowIndex; i < rowIndex + 20; ++i) {
            final ObservableList<SpreadsheetCell> list = createFirstCell(i, "Random " + (i + 1), true);
            for (int column = 1; column < gridBase.getColumnCount(); column++) {
                list.add(generateCell(i, column, 1, 1));
            }
            rows.add(list);
        }
        //--step 4) 把8+3+20一共31行添加到gridBase中去-----------------------
        gridBase.setRows(rows);
        //--step 5) Cell合并---------------------------------------------
        gridBase.getRows().get(15).get(1).getStyleClass().add("span");
        gridBase.spanRow(2, 15, 1);         //向下合并2-1行，在坐标 - 第15+1行，第1+1列
        gridBase.spanColumn(2, 15, 1);      //向右合并2-1列，在坐标 - 第15+1行，第1+1列
        
        gridBase.getRows().get(18).get(1).getStyleClass().add("span");
        gridBase.spanColumn(4, 18, 1);      //向右合并4-1列，在坐标 - 第18+1行，第1+1列
        
        gridBase.getRows().get(19).get(1).getStyleClass().add("span");
        gridBase.spanRow(3, 19, 1);         //向下合并3-1列，在坐标 - 第19+1行，第1+1列
        //------------------------------------------------------------------
        return gridBase;
    }    
    
//==具体的把一行一行的信息插入gridBase============================================================================================================================
    
    //第1行 - 具体的把company信息插入gridBase--------------------------------------------------------------------------------
    private ObservableList<SpreadsheetCell> getCompanies(GridBase gridBase, int row) {
        final ObservableList<SpreadsheetCell> companies = FXCollections.observableArrayList();
        SpreadsheetCell cell = SpreadsheetCellType.STRING.createCell(row, 0, 1, 1, "Company : "); //0表示第1列
        cell.setEditable(false); //缺省的是可以修改的，你如果想要只读，必须setEditable = false
        companies.add(cell);
        ((SpreadsheetCellBase) cell).setTooltip("This cell displays a custom toolTip."); //这里看看！可以设置ToolTip呦！
        
        
        for (int column = 1; column < gridBase.getColumnCount(); ++column) { //接下来就是从第2列开始循环了
            cell = SpreadsheetCellType.STRING.createCell(row, column, 1, 1, companiesList.get(column));
            cell.setEditable(false); //缺省的是可以修改的，你如果想要只读，必须setEditable = false
            cell.getStyleClass().add("compagny"); //注意！这里可以引用css style!
            companies.add(cell);
        }
        return companies;
    }
    //第2行 - 具体的把country信息插入gridBase--------------------------------------------------------------------------------
    private ObservableList<SpreadsheetCell> getCountries(GridBase grid, int row) {
        final ObservableList<SpreadsheetCell> list = createFirstCell(row, "Countries", false);
        SpreadsheetCell cell;
        for (int column = 1; column < grid.getColumnCount(); ++column) {
            cell = SpreadsheetCellType.LIST(countryList).createCell(row, column, 1, 1, countryList.get((int) (Math.random() * 6)));
            list.add(cell);
        }
        return list;
    }    
    //第3行 - 具体的把Start Date信息插入gridBase--------------------------------------------------------------------------------    
    private ObservableList<SpreadsheetCell> getStartDate(GridBase grid, int row) {
        final ObservableList<SpreadsheetCell> list = createFirstCell(row, "Start day", false);
        for (int column = 1; column < grid.getColumnCount(); ++column) {
            list.add(generateDateCell(row, column, 1, 1));
        }
        return list;
    }    
    //第4行 - 插入Logo 图片--------------------------------------------------------------------------------   
    private ObservableList<SpreadsheetCell> getLogos(GridBase grid, int row) {
        final ObservableList<SpreadsheetCell> list = createFirstCell(row, "Logo", false);
        SpreadsheetCell cell;        
        for (int column = 1; column < grid.getColumnCount(); ++column) {
            cell = SpreadsheetCellType.STRING.createCell(row, column, 1, 1, null);
            cell.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(logoList.get(column)))));
            cell.getStyleClass().add("logo");
            cell.setEditable(false);
            list.add(cell);
        }
        return list;
    }    
    //第5行 - 插入Income, 格式化数字--------------------------------------------------------------------------------   
    private ObservableList<SpreadsheetCell> getIncome(GridBase grid, int row) {
        final ObservableList<SpreadsheetCell> list = createFirstCell(row, "Income", false);
        SpreadsheetCell cell;               
        SpreadsheetCell cell2 = SpreadsheetCellType.STRING.createCell(row, 1, 1, 1, "It's over 9000!");
        list.add(cell2);
        for (int column = 2; column < grid.getColumnCount(); ++column) {
            cell = generateDoubleCell(row, column, 1, 1);
            cell.setFormat("#,##0.00" + "\u20AC");
            list.add(cell);
        }
        return list;
    }    
    private SpreadsheetCell generateDoubleCell(int row, int column, int rowSpan, int colSpan) {
        final double random = Math.random();
        SpreadsheetCell cell;
        cell = SpreadsheetCellType.DOUBLE.createCell(row, column, rowSpan, colSpan, (double) Math.round((random * 100) * 100) / 100);
        return cell;
    }    
    //第6行 - 插入Increase, 格式化数字--------------------------------------------------------------------------------   
    private ObservableList<SpreadsheetCell> getIncrease(GridBase grid, int row) {
        final ObservableList<SpreadsheetCell> list = createFirstCell(row, "Increase", false);
        SpreadsheetCell cell;     
        for (int column = 1; column < grid.getColumnCount(); ++column) {
            cell = SpreadsheetCellType.DOUBLE.createCell(row, column, 1, 1, (double) Math.random());
            if (column % 2 == 1) {
                cell.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("exclamation.png")))); //注意！你的cell可以添加图片！！
            }
            cell.setFormat("#" + "%");
            list.add(cell);
        }
        return list;
    }
    //第7行 - 插入Employee, Integer--------------------------------------------------------------------------------   
    private ObservableList<SpreadsheetCell> getEmployees(GridBase grid, int row) {
        final ObservableList<SpreadsheetCell> list = createFirstCell(row, "Number of employees", false);
        SpreadsheetCell cell;             
        for (int column = 1; column < grid.getColumnCount(); ++column) {
            cell = SpreadsheetCellType.INTEGER.createCell(row, column, 1, 1, Math.round((float) Math.random() * 10));
            list.add(cell);
        }
        return list;
    }    
    //第8行 - 插入WebSite, URL--------------------------------------------------------------------------------      
    private ObservableList<SpreadsheetCell> getWebSite(GridBase grid, int row) {
        final ObservableList<SpreadsheetCell> list = createFirstCell(row, "WebSite ", false);
        SpreadsheetCell cell;
        
        //@formatter:off
        final List<String> webSiteList = Arrays.asList("", 
                                                       "http://fxexperience.com/controlsfx/", 
                                                       "http://aperturescience.com/", 
                                                       "", 
                                                       "http://fr.gta.wikia.com/wiki/Ammu-Nation", 
                                                       "http://e-shop.nuka-cola.eu/", 
                                                       "http://fr.gta.wikia.com/wiki/Pay_%27n%27_Spray", 
                                                       "http://www.umbrellacorporation.net/");       
        //@formatter:on
        
        for (int column = 1; column < grid.getColumnCount(); ++column) {
            cell = SpreadsheetCellType.STRING.createCell(row, column, 1, 1, null);
            Hyperlink link = new Hyperlink(webSiteList.get(column));
            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            URI uri;
            try {
                uri = new URI(link.getText());
                if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                    link.setOnAction(t -> {
                            try {
                                desktop.browse(uri);
                            }
                            catch (IOException ex) {}
                        }
                    );
                }
            }
            catch (URISyntaxException ex) {}
            cell.setGraphic(link);
            cell.setEditable(false);
            list.add(cell);
        }
        return list;
    }    
    //第9/10/11行 - 插入空行--------------------------------------------------------------------------------   
    private ObservableList<SpreadsheetCell> getSeparator(GridBase grid, int row) {
        final ObservableList<SpreadsheetCell> separator = FXCollections.observableArrayList();
        for (int column = 0; column < grid.getColumnCount(); ++column) {
            SpreadsheetCell cell = SpreadsheetCellType.STRING.createCell(row, column, 1, 1, "");
            cell.setEditable(false);
            cell.getStyleClass().add("separator");
            separator.add(cell);
        }
        return separator;
    }    
    //第12 - 31行 - 随机内容--------------------------------------------------------------------------------       
    private SpreadsheetCell generateCell(int row, int column, int rowSpan, int colSpan) {
        SpreadsheetCell cell;
        List<String> cityList = Arrays.asList("Shanghai", "Paris", "New York City", "Bangkok", "Singapore", "Johannesburg", "Berlin", "Wellington", "London", "Montreal");
        final double random = Math.random();
        if (random < 0.25) {
            cell = SpreadsheetCellType.LIST(countryList).createCell(row, column, rowSpan, colSpan, countryList.get((int) (Math.random() * 6)));
        }
        else if (random >= 0.25 && random < 0.5) {
            cell = SpreadsheetCellType.STRING.createCell(row, column, rowSpan, colSpan, cityList.get((int) (Math.random() * 10)));
        }
        else if (random >= 0.5 && random < 0.75) {
            cell = generateNumberCell(row, column, rowSpan, colSpan);
        }
        else {
            cell = generateDateCell(row, column, rowSpan, colSpan);
        }
        // Styling for preview
        if (row % 5 == 0) {
            cell.getStyleClass().add("five_rows");
        }
        return cell;
    }
    private SpreadsheetCell generateNumberCell(int row, int column, int rowSpan, int colSpan) {
        final double random = Math.random();
        SpreadsheetCell cell;
        if (random < 0.3) {
            cell = SpreadsheetCellType.INTEGER.createCell(row, column, rowSpan, colSpan, Math.round((float) Math.random() * 100));
        }
        else {
            cell = SpreadsheetCellType.DOUBLE.createCell(row, column, rowSpan, colSpan, (double) Math.round((Math.random() * 100) * 100) / 100);
            final double randomFormat = Math.random();
            if (randomFormat < 0.25) {
                cell.setFormat("#,##0.00" + "\u20AC");
            }
            else if (randomFormat < 0.5) {
                cell.setFormat("0.###E0 km/h");
            }
            else {
                cell.setFormat("0.###E0");
            }
        }
        return cell;
    }    
    private SpreadsheetCell generateDateCell(int row, int column, int rowSpan, int colSpan) {
        SpreadsheetCell cell = SpreadsheetCellType.DATE.createCell(row, column, rowSpan, colSpan, LocalDate.now().plusDays((int) (Math.random() * 10)));
        final double random = Math.random();
        if (random < 0.25) {
            cell.setFormat("EEEE d");
        }
        else if (random < 0.5) {
            cell.setFormat("dd/MM :YY");
        }
        else {
            cell.setFormat("dd/MM/YYYY");
        }
        return cell;
    }    
    //--我发现从第2行开始，第一个cell的代码都是一样的，因此我就把它抽取出来了--------------
    private ObservableList<SpreadsheetCell> createFirstCell(int row, String columnName, boolean editable) {
        ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();
        SpreadsheetCell cell = SpreadsheetCellType.STRING.createCell(row, 0, 1, 1, columnName);
        if(!editable) {
            cell.setEditable(false); //缺省的是可以修改的
        }
        cell.getStyleClass().add("first-cell"); //注意！这里可以引用css style!
        list.add(cell);
        return list;
    }
    //------------------------------------------------------------------    
//========================================================================================================================================================================================================================================================
    



    /**
     * Add some pickers into the SpreadsheetView in order to give some
     * information.
     */
    private void generatePickers() {
        spreadSheetView.getRowPickers().addAll(0, 1, 2, 3, 4, 5, 6);
        spreadSheetView.setRowPickerCallback(new Callback<Integer, Void>() {
            @Override public Void call(Integer p) {
                String message;
                switch (p) {
                case 0:
                    message = "This row contains several fictive companies. " + "The cells are not editable.\n" + "A custom tooltip is applied for the first cell.";
                    break;
                case 1:
                    message = "This row contains cells that can only show a list.";
                    break;
                case 2:
                    message = "This row contains cells that display some dates.";
                    break;
                case 3:
                    message = "This row contains some Images displaying logos of the companies.";
                    break;
                case 4:
                    message = "This row contains Double editable cells. " + "Except for ControlsFX compagny where it's a String.";
                    break;
                case 5:
                    message = "This row contains Double editable cells with " + "a special format (%). Some cells also have " + "a little icon next to their value.";
                    break;
                case 6:
                    message = "This row contains Integer editable cells.";
                    break;
                default:
                    message = "You clicked on row " + (p + 1);
                }
                Dialogs.create().title("You clicked on row " + (p + 1)).message(message).showInformation();
                return null;
            }
        });
        spreadSheetView.getColumnPickers().addAll(0);
        spreadSheetView.setColumnPickerCallback(new Callback<Integer, Void>() {
            @Override public Void call(Integer p) {
                String message;
                switch (p) {
                case 0:
                    message = "Each cell of this column (except for the " + "separator in the middle) has a particular css " + "class for changing its color.\n";
                    break;
                default:
                    message = "You clicked on column " + (p + 1);
                }
                Dialogs.create().title("You clicked on column " + (p + 1)).message(message).showInformation();
                return null;
            }
        });
    }
    
    
    
    //--这个方法用来创建UI右下角的控制面板------------------------------------------------------------------------------------------
    
    //Build a common control Grid with some options on the left to control the SpreadsheetViewInternal
    @Override public Node getControlPanel() {
        final GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setPadding(new Insets(5, 5, 5, 5));
        int row = 0;
        // row header
        Label rowHeaderLabel = new Label("Row header: ");
        rowHeaderLabel.getStyleClass().add("property");
        gridPane.add(rowHeaderLabel, 0, row);
        rowHeader.setSelected(true);
        spreadSheetView.setShowRowHeader(true);
        gridPane.add(rowHeader, 1, row++);
        rowHeader.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
                spreadSheetView.setShowRowHeader(arg2);
            }
        });
        // column header
        Label columnHeaderLabel = new Label("Column header: ");
        columnHeaderLabel.getStyleClass().add("property");
        gridPane.add(columnHeaderLabel, 0, row);
        columnHeader.setSelected(true);
        spreadSheetView.setShowColumnHeader(true);
        gridPane.add(columnHeader, 1, row++);
        columnHeader.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
                spreadSheetView.setShowColumnHeader(arg2);
            }
        });
        // editable
        Label editableLabel = new Label("Editable: ");
        editableLabel.getStyleClass().add("property");
        gridPane.add(editableLabel, 0, row);
        editable.setSelected(true);
        spreadSheetView.setEditable(true);
        gridPane.add(editable, 1, row++);
        spreadSheetView.editableProperty().bind(editable.selectedProperty());
        //Row Header width
        Label rowHeaderWidth = new Label("Row header width: ");
        rowHeaderWidth.getStyleClass().add("property");
        gridPane.add(rowHeaderWidth, 0, row);
        Slider slider = new Slider(15, 100, 30);
        spreadSheetView.rowHeaderWidthProperty().bind(slider.valueProperty());
        gridPane.add(slider, 1, row++);
        // Multiple Selection
        Label selectionModeLabel = new Label("Multiple selection: ");
        selectionModeLabel.getStyleClass().add("property");
        gridPane.add(selectionModeLabel, 0, row);
        selectionMode.setSelected(true);
        gridPane.add(selectionMode, 1, row++);
        selectionMode.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean isSelected) {
                spreadSheetView.getSelectionModel().clearSelection();
                spreadSheetView.getSelectionModel().setSelectionMode(isSelected ? SelectionMode.MULTIPLE : SelectionMode.SINGLE);
            }
        });
        return gridPane;
    }
    
    
    //--useless methods which can be ignored -------------------------------------------
    
    @Override public String getSampleName() {
        return "SpreadsheetView";
    }    

    @Override public String getSampleDescription() {
        return "The SpreadsheetView is a control similar to the JavaFX TableView control " + "but with different functionalities and use cases. The aim is to have a " + "powerful grid where data can be written and retrieved.\n\n" + "Here you have an example where some information about fictive "
                + "companies are displayed. They have different type and format.\n\n" + "After that, some random generated cells are displayed with some span.\n\n" + "Don't forget to right-click on headers and cells to discover some features.";
    }    

    @Override public String getControlStylesheetURL() {
        return "/org/controlsfx/samples/spreadsheetSample.css";
    }    

    @Override public String getJavaDocURL() {
        return Utils.JAVADOC_BASE + "org/controlsfx/control/spreadsheet/SpreadsheetView.html";
    }    
    
}
