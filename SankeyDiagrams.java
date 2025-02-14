import java.util.*;
import java.io.*;
import java.lang.Math;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SankeyDiagrams extends Application{
    //errorTime will count how many times you input wrong file names
    int errorTime = 0;
    //fileNames records the file ends with '.txt' in the project folder
    Map<String, Integer> fileNames = new HashMap<>();
    //colorTypes records the color types
    Set<String> colorTypes = new HashSet<>();

    //    Get the names of file in the project folder
    public void getFileNames(){
        File dir = new File(System.getProperty("user.dir"));
        String[] fileNames = dir.list();
        for (String fileName : fileNames){
            if (fileName.endsWith(".txt")){
                int index = fileName.lastIndexOf(".txt");
                String key = fileName.substring(0, index);
                this.fileNames.put(key, 1);
            }
        }
    }

    //    Add the color types to the colorTypes
    public void inputColorTypes(){
        colorTypes.add("REGULAR");
        colorTypes.add("RANDOM");
        colorTypes.add("UNIT");
    }

    //    When user inputs a wrong file name, the errorTime will add one;
    public void errorTimer(){
        errorTime++;
    }

    //    If user inputs a wrong file name for three times, an alert will be shown
    public void errorAlert(){
        if (errorTime % 3 == 0){
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "Warning for input error!!!",
                    ButtonType.CANCEL,
                    ButtonType.CLOSE);
            alert.show();
        }
    }

    /**
     Override start to show a graphical interface with a text box
     The user can input file name to get a Sankey diagram directly
     Or with a color type followed to get a Sankey diagram in different ways
     The default color type is REGULAR(show Sankey diagram in the regular way)
     If the user input wrong file name, it will get an error tip
     If the user input wrong file name for several times, an alert will be shown
     If the user input '.txt', system will delete it automatically
     There will be tips for guessing what the user wants to input
     There will be available to press 'Enter' to enter
     There will be available to click the 'Enter' bottom to enter
     There will be available to click the 'Clear' bottom to clear the text box
     */
    @Override
    public void start(Stage primaryStage){
        getFileNames();
        inputColorTypes();

//        Use gridPane to show a text, a textFiled and two buttons
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);

//        Set an animation for user getting an error tip after input incorrectly
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.seconds(1));
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.setNode(gridPane);

//        Creates some nodes for this graphical interface
        Label l1 = new Label("Input filename : ");
        l1.setFont(Font.font("Arial", FontWeight.BOLD, 20.0));

        TextField textField1 = new TextField();
        textField1.setFont(Font.font(15));
        Tooltip tip = new Tooltip("Please input correct filename!!!");
        tip.setHideDelay(Duration.INDEFINITE);
        tip.setFont(Font.font("Arial", FontWeight.BOLD, 20.0));
        textField1.setTooltip(tip);
        textField1.setPromptText("Welcome");
        textField1.setFocusTraversable(false);

//        Set the input tip always be shown when the mouse is on the textFiled
        textField1.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                tip.show(textField1, event.getScreenX(), event.getScreenY() + 10);
            }
        });

//        Set the input tip will be hidden when the mouse is not on the textFiled
        textField1.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                tip.hide();
            }
        });

//        Set the tips for guessing what the user want to input
//        Delete '.txt' automatically
        textField1.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
//                First loop is designed to determine the input has been shown in the fileNames
//                Second loop is designed to show what may be the user want to input
                String[] keywords = t1.split("");
                int n = t1.length();
                int findTimes;
                for (String x : fileNames.keySet()){
                    findTimes = 0;
                    for (int i = 0; i < n; i++){
                        String keyword = keywords[i];
                        if (x.indexOf(keyword) != -1){
                            findTimes++;
                        }
                    }
                    if (findTimes == n){
                        String str = "";
                        for (String y : fileNames.keySet()){
                            findTimes = 0;
                            for (int j = 0; j < n; j++){
                                String keyword = keywords[j];
                                if (y.indexOf(keyword) != -1){
                                    findTimes++;
                                    if (findTimes == n){
                                        str = str + y;
                                        str = str + "\n";
                                    }
                                }
                            }
                        }
                        tip.setText(str);
                    }
                }

//                If the user want to input a color type, it will guess what the user what to input
//                First loop is designed to determine the input has been shown in the colorTypes
//                Second loop is designed to  show what may be the user want to input
                String[] type = t1.split(" ");
                if (type.length == 2){
                    String color = type[1];
                    String str = type[0] + " ";
                    String[] colorKeywords = color.split("");
                    int m = colorKeywords.length;

                    for (String x: colorTypes){
                        findTimes = 0;
                        for (int i = 0; i < m; i++){
                            String keyword = colorKeywords[i];
                            if (x.indexOf(keyword) != -1){
                                findTimes++;
                            }
                        }
                        if (findTimes == m){
                            for (String y : colorTypes){
                                findTimes = 0;
                                for (int j = 0; j < m; j++){
                                    String keyword = colorKeywords[j];
                                    if (y.indexOf(keyword) != -1){
                                        findTimes++;
                                        if (findTimes == m){
                                            str = str + y;
                                            str = str + "\n";
                                        }
                                    }
                                }
                            }
                        }
                        tip.setText(str);
                    }
                }

//                If the user did not input anything, the tip will show the primary message
                if (t1.isEmpty()){
                    tip.setText("Please input correct filename!!!");
                }

//                Delete '.txt' automatically
                Platform.runLater(() -> {
                    int position = textField1.getCaretPosition();
                    if (t1.indexOf(".txt") != -1){
                        int index = t1.indexOf(".txt");
                        String str = t1.substring(0, index);
                        textField1.setText(str);
                    }
                    textField1.positionCaret(position);
                });
            }
        });

//        Be available to press 'Enter' to input
        textField1.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().getName().equals(KeyCode.ENTER.getName())){
                    String str = textField1.textProperty().getValue();
                    String[] type = str.split(" ");
                    if (fileNames.get(type[0]) != null && fileNames.get(type[0]) == 1){
                        if (type.length == 1){
                            showSankeyDiagrams(str);
                        }
                        else if (type.length == 2){
                            showSankeyDiagrams(type[0], type[1]);
                        }
                    }
                    else {
                        primaryStage.setTitle("Filename does not exist!");
                        fadeTransition.play();
                        errorTimer();
                        errorAlert();
                    }
                }
            }
        });

//        Creat  a button for input
        Button b2 = new Button();
        b2.setText("Enter");
        b2.setPrefHeight(10);
        b2.setPrefWidth(60);
        b2.setFont(Font.font("Arial", 15));
        b2.setStyle("-fx-background-radius: 10;");

//        Be available to click the button to input
        b2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 1 && event.getButton().name().equals(MouseButton.PRIMARY.name())){
                    String str = textField1.textProperty().getValue();
                    String[] type = str.split(" ");
                    if (fileNames.get(type[0]) != null && fileNames.get(type[0]) == 1){
                        if (type.length == 1){
                            showSankeyDiagrams(str);
                        }
                        else if (type.length == 2){
                            showSankeyDiagrams(type[0], type[1]);
                        }
                    }
                    else {
                        primaryStage.setTitle("Filename does not exist!");
                        fadeTransition.play();
                        errorTimer();
                        errorAlert();
                    }
                }
            }
        });

//        Be available to press 'Enter' to input
        b2.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().getName().equals(KeyCode.ENTER.getName())){
                    String str = textField1.textProperty().getValue();
                    String[] type = str.split(" ");
                    if (fileNames.get(type[0]) != null && fileNames.get(type[0]) == 1){
                        if (type.length == 1){
                            showSankeyDiagrams(str);
                        }
                        else if (type.length == 2){
                            showSankeyDiagrams(type[0], type[1]);
                        }
                    }
                    else {
                        primaryStage.setTitle("Filename does not exist!");
                        fadeTransition.play();
                        errorTimer();
                        errorAlert();
                    }
                }
            }
        });

//        Creat  a button for clear the textFiled
        Button b1 = new Button();
        b1.setText("Clear");
        b1.setPrefHeight(10);
        b1.setPrefWidth(60);
        b1.setFont(Font.font("Arial", 15));
        b1.setStyle("-fx-background-radius: 10;");

//        Be available to click the button to clear the textFiled
        b1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 1 && event.getButton().name().equals(MouseButton.PRIMARY.name())){
                    textField1.setText("");
                }
            }
        });

        gridPane.add(l1, 0, 0);
        gridPane.add(textField1, 1, 0);
        gridPane.add(b2, 2, 0);
        gridPane.add(b1, 3, 0);

        Scene scene = new Scene(gridPane, 600, 300);
        primaryStage.setTitle("SankeyDiagrams");
        primaryStage.setMinWidth(490);
        primaryStage.setMinHeight(120);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //    Here are two method with Overload to using class SankeyPane to show Sankey diagram from files
    public  static void showSankeyDiagrams(String filename){
        String fullName = filename + ".txt";
        String title = getTitle(fullName);
        Map<String, Integer> map = getValue(fullName);
        int mapSize = getValue(fullName).size();
        String[] array = getOrder(fullName, mapSize);
        SankeyPane sankeyPane = new SankeyPane(map, array);
        Scene scene = new Scene(sankeyPane, 600, 500);
        Stage sankeyStage = new Stage();
        sankeyStage.setMinWidth(450);
        sankeyStage.setMinHeight(450);
        sankeyStage.setTitle(title);
        sankeyStage.setScene(scene);
        sankeyStage.show();
    }
    public  static void showSankeyDiagrams(String filename, String colorType){
        String fullName = filename + ".txt";
        String title = getTitle(fullName);
        Map<String, Integer> map = getValue(fullName);
        int mapSize = getValue(fullName).size();
        String[] array = getOrder(fullName, mapSize);
        //If the user input wrong color type will be viewed as REGULAR
        int ColorType = 1;
        if (colorType.equals("RANDOM")){
            ColorType = -1;
        }
        if (colorType.equals("UNIT")){
            ColorType = 0;
        }
        SankeyPane sankeyPane = new SankeyPane(map, array, ColorType);
        Scene scene = new Scene(sankeyPane, 600, 500);
        Stage sankeyStage = new Stage();
        sankeyStage.setMinWidth(450);
        sankeyStage.setMinHeight(450);
        sankeyStage.setTitle(title);
        sankeyStage.setScene(scene);
        sankeyStage.show();
    }

    //    Get the value of the data for Sankey diagram from the file
    public static Map<String, Integer> getValue(String fileName){
        File file = new File(fileName);
        Map<String, Integer> map = new HashMap<>();

        if (!file.exists()){
            System.out.println("This file does not exist!");
            System.exit(0);
        }

        try{
            Scanner input = new Scanner(file);
            String title = input.nextLine();
            String sumWord = input.nextLine();
            int sum = 0;
            while (input.hasNextLine()){
                String[] line = input.nextLine().split(" ");
                int n = line.length;
                String tempWord = "";
                for (int i = 0; i < n - 1; i++){
                    if (i == n - 2){
                        tempWord += line[i];
                    }
                    else {
                        tempWord += line[i];
                        tempWord += " ";
                    }
                }
                map.put(tempWord, Integer.parseInt(line[n - 1]));
                sum += Integer.parseInt(line[n - 1]);
            }
            map.put(sumWord, sum);

        }
        catch (IOException ioe){
            System.out.println(ioe.getMessage());
        }
        return map;
    }

    //    Get the order of the data for Sankey diagram from the file
    public static String[] getOrder(String fileName, int arrayLength){
        File file = new File(fileName);
        String[] order = new String[arrayLength];

        if (!file.exists()){
            System.out.println("This file does not exist!");
            System.exit(0);
        }

        try {
            Scanner input = new Scanner(file);
            String title = input.nextLine();
            order[0] = input.nextLine();
            while (input.hasNextLine()){
                for (int i = 1; i < arrayLength; i++){
                    String[] line = input.nextLine().split(" ");
                    int n = line.length;
                    String tempWord = "";
                    for (int j = 0; j < n - 1; j++){
                        if (j == n - 2){
                            tempWord += line[j];
                        }
                        else {
                            tempWord += line[j];
                            tempWord += " ";
                        }
                    }
                    order[i] = tempWord;
                }
            }
        }
        catch (IOException ioe){
            System.out.println(ioe.getMessage());
        }
        return order;
    }

    //    Get the title for Sankey diagram from the file
    public static String getTitle(String fileName){
        File file = new File(fileName);
        String title = "";

        if (!file.exists()){
            System.out.println("This file does not exist!");
            System.exit(0);
        }

        try{
            Scanner input = new Scanner(file);
            title = input.nextLine();
        }
        catch (IOException ioe){
            System.out.println(ioe.getMessage());
        }
        return title;
    }
}
class SankeyPane extends Pane{
    //map gets the value of file
    Map<String, Integer> map;
    //array gets the order of the file
    String[] array;
    //mapSize counts how many key-value pairs in map
    int mapSize;
    //colorType gets the value of colorType
    //value 1 means REGULAR(show Sankey diagram in the regular way)
    //value 2 means RANDOM(show Sankey diagram in random colors)
    //value 0 means UNIT(show Sankey diagram with branches in the same color)
    int colorType;
    //randomColor will random a index value for the colorList
    int randomColor;

    //    Here are two constructors
    public SankeyPane(Map<String, Integer> map, String[] array) {
        this.map = map;
        this.array = array;
        mapSize = map.size();
        colorType = 1;
        randomColor = 1 + (int)(Math.random() * 84);
    }
    public SankeyPane(Map<String, Integer> map, String[] array, int colorType) {
        this.map = map;
        this.array = array;
        mapSize = map.size();
        this.colorType = colorType ;
        randomColor = 1 + (int)(Math.random() * 84);
    }

//    The method paint will visualise data in file in a Sankey diagram
//    This method will be invoked in the method getWeight and the method getHeight to realize resizing

    public void paint(){
        getChildren().clear();

//        For beauty, here are some colors chosen to the list named colorList
//        All colors will be chosen in the colorList
        Color color1 = Color.rgb(104, 167, 210);
        Color color2 = Color.rgb(195, 86, 255);
        Color color3 = Color.rgb(193, 215, 236);
        Color color4 = Color.rgb(224, 80, 28);
        Color color5 = Color.rgb(250, 133, 63);
        Color color6 = Color.rgb(252, 167, 103);
        Color color7 = Color.rgb(252, 204, 156);
        Color color8 = Color.rgb(228, 176, 255);
        Color color9 = Color.rgb(205, 217, 225);
        Color color10 = Color.rgb(241, 173, 140);
        Color color11 = Color.rgb(253, 199, 163);
        Color color12 = Color.rgb(253, 215, 183);
        Color color13 = Color.rgb(138, 234, 135);
        Color color14 = Color.web("#A8FEAB");
        Color color15 = Color.web("#C4FE9D");
        Color color16 = Color.web("#E0FD8E");
        Color color17 = Color.web("#FCFC7F");
        Color color18 = Color.web("#2F9969");
        Color color19 = Color.web("#52B785");
        Color color20 = Color.web("#8AD4AC");
        Color color21 = Color.web("#C0F1D3");
        Color color22 = Color.web("#23965D");
        Color color23 = Color.web("#43BB73");
        Color color24 = Color.web("#81D5A2");
        Color color25 = Color.web("#C1E7CD");
        Color color26 = Color.web("#4D982E");
        Color color27 = Color.web("#70B643");
        Color color28 = Color.web("#A2D370");
        Color color29 = Color.web("#C1E7CD");
        Color color30 = Color.web("#617C52");
        Color color31 = Color.web("#889F64");
        Color color32 = Color.web("#B0C277");
        Color color33 = Color.web("#D7E589");
        Color color34 = Color.web("#1B4242");
        Color color35 = Color.web("#5C8374");
        Color color36 = Color.web("#9EC8B9");
        Color color37 = Color.web("#B5E6D4");
        Color color38 = Color.web("#85656D");
        Color color39 = Color.web("#AC8C93");
        Color color40 = Color.web("#B8ADAF");
        Color color41 = Color.web("#D1C5C6");
        Color color42 = Color.web("#A24F47");
        Color color43 = Color.web("#BD968F");
        Color color44 = Color.web("#ECCAB7");
        Color color45 = Color.web("#C9BCB9");
        Color color46 = Color.web("#8C85A9");
        Color color47 = Color.web("#B29EBC");
        Color color48 = Color.web("#D6B9D0");
        Color color49 = Color.web("#F8D6E5");
        Color color50 = Color.web("#91A3BB");
        Color color51 = Color.web("#A0C1D4");
        Color color52 = Color.web("#B4CFE2");
        Color color53 = Color.web("#DAD8E5");
        Color color54 = Color.web("#3C79B4");
        Color color55 = Color.web("#78A3CC");
        Color color56 = Color.web("#B3CDE4");
        Color color57 = Color.web("#EEF7FC");
        Color color58 = Color.web("#3163EB");
        Color color59 = Color.web("#5882F8");
        Color color60 = Color.web("#84A1F9");
        Color color61 = Color.web("#ADBFFB");
        Color color62 = Color.web("#A97C26");
        Color color63 = Color.web("#D29C2F");
        Color color64 = Color.web("#F2BE38");
        Color color65 = Color.web("#F5DF7A");
        Color color66 = Color.web("#C65323");
        Color color67 = Color.web("#E96B3D");
        Color color68 = Color.web("#F9885E");
        Color color69 = Color.web("#FAB590");
        Color color70 = Color.web("#D0241C");
        Color color71 = Color.web("#F54D40");
        Color color72 = Color.web("#F9877D");
        Color color73 = Color.web("#F7B3AC");
        Color color74 = Color.web("#D98777");
        Color color75 = Color.web("#E3A995");
        Color color76 = Color.web("#ECCAB7");
        Color color77 = Color.web("#F7EBDB");
        Color color78 = Color.web("#AE4AD9");
        Color color79 = Color.web("#D664F8");
        Color color80 = Color.web("#E48CF9");
        Color color81 = Color.web("#F0B5FF");
        Color color82 = Color.web("#FF007D");
        Color color83 = Color.web("#FF529A");
        Color color84 = Color.web("#FF7AAD");
        Color color85 = Color.web("#FFA3C3");

        List<Color> colorList = new ArrayList<>();
        colorList.addAll(Arrays.asList(color1, color2, color8, color3, color9, color4, color10, color5, color11, color6, color12, color7, color13,
                color14, color15, color16, color17, color18, color19, color20, color21, color22, color23, color24, color25, color26, color27, color28,
                color29, color30, color31, color32, color33, color34, color35, color36, color37, color38, color39, color40, color41, color42, color43,
                color44, color45, color46, color47, color48, color49, color50, color51, color52, color53, color54, color55, color56, color57, color58,
                color59, color60, color61, color62, color63, color64, color65, color66, color67, color68, color69, color70, color71, color72, color73,
                color74, color75, color76, color77, color78, color79, color80, color81, color82, color83, color84, color85));

//        Here are some preparations for different color types
//        For colorType : RANDOM, will firstly count how many groups can be used for diagram
//        Then random a group to get colors
//        For colorType : REGULAR, will choose the first group
//        For colorType : UNIT, will choose a color in the colorList directly
        int colorsNeed = (mapSize - 1) * 2;
        int group = 84 / colorsNeed;
        while (randomColor > group * colorsNeed){
            randomColor = 1 + (int) (Math.random() * 84);
        }
        int temp = (randomColor - 1) % colorsNeed;
        int k = (randomColor - 1 - temp) / colorsNeed;
        if (colorType == 1){
            k = 0;
        }


        // Initialization a font for beauty
        Font font1 = Font.font("Arial", FontWeight.BOLD, 20.0);

        double midy = getHeight() / 2;
        double rectangleWidth = getWidth() / 32;
        double scale = map.get(array[0]) / getHeight() * 5000 / 2025;
        //gap means the length between each branch
        double gap = 0;
        //if only one branch gap will be zero
        if (mapSize == 2){
            gap = 0;
        }
        else {
            gap = map.get(array[0]) / (mapSize - 2) / scale;
        }

//        Calculate for middle branch to define the values for the left rectangle
        int sum = 0;
        int midIndex = 0;
        double midHeight = 0;
        for (int i = 1; i < mapSize; i++){
            sum += map.get(array[i]);
            if (sum >= map.get(array[0]) / 2){
                midIndex = i;
                midHeight = map.get(array[i]) / scale;
                break;
            }
        }

//        The values for the left rectangle
        double x1 = getWidth() / 16 * 5;
        double y1 = midy - midHeight / 2;
        for (int i = 1; i < midIndex; i++){
            y1 -= (double) map.get(array[i]) / scale;
        }

//        The values for the first branch rectangle
        double x2 = getWidth() / 64 * 45;
        double y2 = midy - midHeight / 2;
        for (int i = 1; i < midIndex; i++){
            y2 -= map.get(array[i]) / scale;
            y2 -= gap;
        }

        //record the first value of y of first branch rectangle for painting branches and texts
        double y2Start = y2;

//        Paint the branch rectangles
        for (int i = 1; i < mapSize; i++){
            double height = map.get(array[i]) / scale;
            sum += map.get(array[i]);
            Rectangle r2 = new Rectangle(x2, y2, rectangleWidth,  height - 1);
            r2.setStroke(colorList.get(((2 * i - 1 + colorsNeed * k) * Math.abs(colorType) + (-1) * (Math.abs(colorType) - 1) * randomColor)));
            r2.setFill(colorList.get(((2 * i - 1 + colorsNeed * k) * Math.abs(colorType) + (-1) * (Math.abs(colorType) - 1) * randomColor)));
            getChildren().add(r2);
            if (i > 1){
                Line l1 = new Line(x2, y2, x2 + rectangleWidth, y2);
                l1.setStroke(Color.BLACK);
                getChildren().add(l1);
            }
            y2 += height;
            if (i < mapSize - 1){
                Line l1 = new Line(x2, y2, x2 + rectangleWidth, y2);
                l1.setStroke(Color.BLACK);
                getChildren().add(l1);
            }
            y2 += gap;
        }

//        Paint the left rectangle
        double height1 = map.get(array[0]) / scale;
        Rectangle r1 = new Rectangle(x1, y1, rectangleWidth, height1 - 1);
        r1.setStroke(color1);
        r1.setFill(color1);
        getChildren().add(r1);

//        Set the text of the left rectangle
        VBox vBox1 = new VBox();
        vBox1.setAlignment(Pos.CENTER_RIGHT);
        double vBox1Width = getWidth() / 64 * 19;
        vBox1.setPrefWidth(vBox1Width);
        Text text1 = new Text(x1 / 3, y1, array[0] + ": " + map.get(array[0]));
        text1.setFill(Color.BLACK);
        text1.setFont(font1);
        vBox1.setMargin(text1, new Insets(height1 / 2 + y1 - 10, 0,0 , 0));
        vBox1.getChildren().add(text1);
        getChildren().add(vBox1);

//        Paint the branches
        x1 += rectangleWidth;
        y2 = y2Start;
        for (int i = 1; i < mapSize; i++){
            double height = map.get(array[i]) / scale;
            for (int j = 1; j <= height; j++){
                CubicCurve c1 = new CubicCurve();
                c1.setStartX(x1);
                c1.setStartY(y1);
                c1.setEndX(x2);
                c1.setEndY(y2);
                c1.setControlX1(x1 + (x2 - x1) / 4);
                c1.setControlY1(y1);
                c1.setControlX2(x2 - (x2 - x1) / 4);
                c1.setControlY2(y2);
                c1.setFill(Color.TRANSPARENT);
                c1.setStroke(colorList.get((2 * i + colorsNeed * k) * Math.abs(colorType) + (-1) * (Math.abs(colorType) - 1) * randomColor));
                getChildren().add(c1);
                y1++;
                y2++;
            }
            y2 += gap;
        }

//        Set texts for the branch rectangles
        y2 = y2Start;
        double vBox2Width = getWidth() / 64 * 43;
        VBox vBox2 = new VBox();
        vBox2.setAlignment(Pos.CENTER_RIGHT);
        vBox2.setPrefWidth(vBox2Width);
        for (int i = 1; i < mapSize; i++){
            double heightNow = map.get(array[i]) / scale;
            double heightPrevious = map.get(array[i - 1]) / scale;
            if (i == 1){
                Text text2 = new Text(array[i] + ": " + map.get(array[i]));
                text2.setFill(Color.BLACK);
                text2.setFont(font1);
                vBox2.setMargin(text2, new Insets(heightNow / 2 + y2 - 10, 0,0 , 0));
                vBox2.getChildren().add(text2);
            }
            else {
                Text text2 = new Text(array[i] + ": " + map.get(array[i]));
                text2.setFill(Color.BLACK);
                text2.setFont(font1);
                vBox2.setMargin(text2, new Insets((heightNow + heightPrevious) / 2 + gap - 25, 0,0 , 0));
                vBox2.getChildren().add(text2);
            }
        }
        getChildren().add(vBox2);
    }

    //    Override setWidth to invoke the method paint to realize resizing
    @Override
    public void setWidth(double width){
        super.setWidth(width);
        paint();
    }
    //    Override setHeight to invoke the method paint to realize resizing
    @Override
    public void setHeight(double height){
        super.setHeight(height);
        paint();
    }
}