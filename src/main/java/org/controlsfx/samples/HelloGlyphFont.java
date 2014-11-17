package org.controlsfx.samples;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import org.controlsfx.ControlsFXSample;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

//这个程序主要演示如何创建带图标的button
public class HelloGlyphFont extends ControlsFXSample {
    static {
        //所有的图标都定义在这个ttf font文件中，这个文件要丢到src/main/resources文件夹下面
        GlyphFontRegistry.register("icomoon", HelloGlyphFont.class.getResourceAsStream("icomoon.ttf") , 16);
    }

    private GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");
    private GlyphFont icoMoon = GlyphFontRegistry.font("icomoon");

    //这些unicode定义太无聊，我上哪里去找这些图标的unicode值呀------------------------------
    private static char FAW_GEAR  = '\uf013';
    private static char IM_BOLD        = '\ue027';
    private static char IM_UNDERSCORED = '\ue02b';
    private static char IM_ITALIC      = '\ue13e';
    //------------------------------------------------------------------------

    @Override
    public String getSampleName() {
        return "Glyph Font";
    }

    @Override
    public String getJavaDocURL() {
        return Utils.JAVADOC_BASE + "org/controlsfx/glyphfont/GlyphFont.html";
    }

    @Override
    public Node getPanel(final Stage stage) {

        //---创建VBox Container ---------------------------------------------------------
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setMaxHeight(Double.MAX_VALUE);
        //-----------------------------------------------------------------------------
        
        
        //---VBox添加label ------------------------------------------------------------------
        Label title = new Label("Using FontAwesome(CDN)");
        vbox.getChildren().add(title);
        //-----------------------------------------------------------------------------
        
        
        //---VBox添加toolbar, 上面有很多button， 这个程序重点就是展示这些个button  ---------------------------------------------------------------
        ToolBar toolbar = new ToolBar(
                
                //--这里绝对重点！！！------------------------------------------------------------------------------------------------
                //创建带图标的按钮方法有很多. 如这里就列出了6种之多。
                //但是我最推荐第1种.原因是很多图标你不知道它的字符串名字。第1种方法可以直接进入Glyph类使用eclipse的提示功能来获得那个图标的变量
                new Button("", fontAwesome.create(FontAwesome.Glyph.SMILE_ALT)),    // Use the font-instance with a enum
                
                new Button("", new Glyph("FontAwesome", FontAwesome.Glyph.STAR)),   // Use the Glyph-class with a known enum value
                new Button("", new Glyph("FontAwesome", "TRASH_ALT")),              // Use the Glyph-class with a icon name
                new Button("", Glyph.create("FontAwesome|BUG")),                    // Use the static Glyph-class create protocol
                new Button("", fontAwesome.create("REBEL")),                        // Use the font-instance with a name
                new Button("", fontAwesome.create(FAW_GEAR).color(Color.RED))       // Use the font-instance with a unicode char
                //--------------------------------------------------------------------------------------------------------------
                
        );
        vbox.getChildren().add(toolbar);
        //---------------------------------------------------------------------------------------------------------------------        
        
        
        //---VBox添加label -----------------------------------------------------------------------------------------------------        
        title = new Label("Using IcoMoon (Local)");
        vbox.getChildren().add(title);
        //---------------------------------------------------------------------------------------------------------------------
        

        //---VBox添加toolbar, 上面有很多button， 这个程序重点就是展示这些个button  -----------------------------------------------------------
        Glyph effectGlyph = icoMoon.create(IM_UNDERSCORED) //这里可以设置color / size / 特殊效果等等
                                    .color(Color.BLUE)
                                    .size(48)
                                    .useHoverEffect();

        Glyph effectGlyph2 = icoMoon.create(IM_UNDERSCORED)
                                    .color(Color.BLUE)
                                    .size(48)
                                    .useGradientEffect()
                                    .useHoverEffect();
        
        toolbar = new ToolBar(
                // Since we have a custom font without named characters, we have to use unicode character codes for the icons:
                new Button("", icoMoon.create(IM_BOLD).size(16)),
                new Button("", icoMoon.create(IM_UNDERSCORED).color(Color.GREEN).size(32)),
                new Button("", icoMoon.create(IM_ITALIC).size(48)),
                new Button("", effectGlyph),
                new Button("", effectGlyph2));
        vbox.getChildren().add(toolbar);
        //---------------------------------------------------------------------------------------------------------------------               
        
        
        //下面这段布局设计也是非常经典的代码--------------------------------------------------------
        
        //---定义一个新的gridPane--------------------------------------------
        GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        //---gridPan放在scrollPan里面----------------------------------------
        ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setFitToWidth(true);
        //---scrollPane放在TabPane的一个tab下面--------------------------------
        TabPane tabPane = new TabPane();
        Tab tab = new Tab("FontAwesome Glyph Demo");
        tab.setContent(scrollPane);
        tabPane.getTabs().add(tab);       
        //---tabPane再放在vbox下面--------------------------------------------
        vbox.getChildren().add(tabPane);
        VBox.setVgrow(tabPane, Priority.ALWAYS);
        //-----------------------------------------------------------------
        
        //---------------------------------------------------------------------------------
        
        
        //---真正的内容是全都放在最里面的gridPane的--------------------------------------------------
        int maxColumns = 10;
        int col = 0;
        int row = 0;
        for ( FontAwesome.Glyph glyph:  FontAwesome.Glyph.values() ){
        	Color randomColor = new Color( Math.random(), Math.random(), Math.random(), 1);
        	Glyph graphic = Glyph.create( "FontAwesome|" + glyph.name()).sizeFactor(2).color(randomColor).useGradientEffect();
        	Button button = new Button(glyph.name(), graphic);
        	button.setContentDisplay(ContentDisplay.TOP);
        	button.setMaxWidth(Double.MAX_VALUE);
        	col = col % maxColumns + 1;
        	if ( col == 1 ) row++;
        	gridPane.add( button, col, row);
        	GridPane.setFillHeight(button, true);
        	GridPane.setFillWidth(button, true);
        }
        //---------------------------------------------------------------------------------

        return vbox;
    }

    public static void main(String[] args) {
        launch(args);
    }
}