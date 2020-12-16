package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

public class SelectPlayer implements Initializable {
    @FXML Button goBack;
    // RadioButton is not serializable, so cannot use it in HashMaps and then serialize the mapping
    @FXML RadioButton userRadioButton1; @FXML   RadioButton userRadioButton2;   @FXML      RadioButton userRadioButton3;
    @FXML RadioButton userRadioButton4;   @FXML   RadioButton userRadioButton5;
    @FXML ToggleGroup chooseGameGroup;

    // map integers to buttons and store which integer maps to which gameplay.
    public static HashMap<Integer,Gameplay> numberToGamePlayMapping;// this mapping is saved in 'saveData.txt'
    public static HashMap<Integer, RadioButton> numberToButtonMapping;// this mapping is created everytime we change scene to 'SelectPlayer.fxml'
    private static boolean loadFromFile = true;

    public static UserProfile currentUser;
    public static Gameplay currentGameplay;
    public static ArrayList<Gameplay> gameplays;

    @Override
    public void initialize(URL location, ResourceBundle resources){

        numberToButtonMapping = new HashMap<>();
        numberToButtonMapping.put(1,userRadioButton1);
        numberToButtonMapping.put(2,userRadioButton2);
        numberToButtonMapping.put(3,userRadioButton3);
        numberToButtonMapping.put(4,userRadioButton4);
        numberToButtonMapping.put(5,userRadioButton5);

        gameplays = new ArrayList<>();

        // set Loaded Games names
        System.out.println("loadfromFile: "+loadFromFile);
        if(loadFromFile){
            try{
                MainLobby.deserialize();
            } catch (IOException e){

            } catch (ClassNotFoundException e){

            } finally{
                loadFromFile = false;
            }
//            System.out.println(gameplays.toString());
        }

        if(numberToGamePlayMapping != null){
            System.out.println("numberToGamePlayMapping: NOT NULL");
            System.out.println(numberToGamePlayMapping.toString());
            for(Map.Entry<Integer,Gameplay> entry: numberToGamePlayMapping.entrySet()){
                String username = entry.getValue().getUser().getUsername();
                numberToButtonMapping.get(entry.getKey()).setText(username);
            }
        }
        else{
            System.out.println("numberToGamePlayMapping: null");
        }
    }

    // Start playing game from here, will be called after loading the game and setting the
    // required attributes (like objects,stars, position of ball, angle of rotation of obstacles already present).
    public Scene startGame(){
        //CREATE ROOT NODE
        Group root = new Group();
        UserProfile player = new UserProfile("player", "player", "player");
        Gameplay game = new Gameplay(player, root,null);

        System.out.println("CURRENT GAMEPLAY: " + currentGameplay);

        //CREATE OBJECT
        ringSmall s = new ringSmall();
        ringMedium m = new ringMedium();
        ringLarge l = new ringLarge();
        SquareLine sql = new SquareLine();
        DiamondLine dl = new DiamondLine();
        LeftCross lc = new LeftCross();
        RightCross rc = new RightCross();
        ColorSwitch c = new ColorSwitch(250, 250);
        Star star = new Star(250, 150);
        Ball b = new Ball(root,null);

        //Add all Elements to root node
        root.getChildren().add(game.getBall().getShape());
        Scene scene = new Scene(root, 500, 700, Color.GREY);

        for(Shape x : c.components) {
            root.getChildren().add(x);
        }

        //MouseEvent
        scene.setOnMouseClicked(e -> {
            System.out.println("clicked");
            game.getBall().moveUp.setByY(-100);
            game.getBall().moveUp.play();
            System.out.println(b.getShape().getTranslateY());
        });

        return scene;
    }

    public void loadGameButtonPushed(ActionEvent event) throws IOException {
        RadioButton selectedRadioButton = (RadioButton) chooseGameGroup.getSelectedToggle();
        if(selectedRadioButton == null){
            return;
        }
        if(selectedRadioButton.getText().equals("EMPTY")){
            return;
        }
        // check which number maps to selected RadioButton
        int num=0;
        for(num=1;num<=5;num++){
            if(numberToButtonMapping.get(num).equals(selectedRadioButton)){
                break;
            }
        }
        SelectPlayer.setCurrentGameplay(numberToGamePlayMapping.get(num));
        currentUser = currentGameplay.getUser();
    }

    public void startGamePlayButtonPushed(ActionEvent event) throws IOException {
        Scene gameplayScene = startGame();
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(gameplayScene);
    }

    public void newGameButtonPushed(ActionEvent event) throws IOException {
        //check if new account can be make
        Integer radioButtonNumber;
        if((radioButtonNumber = emptyRadioButton()) == -1){
            return;
        }
        // Enter details to make a new account
        {
            Stage window = new Stage();
            window.setWidth(400);
            window.setHeight(350);
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Not Logged in...");

            Label welcomeText = new Label();
            welcomeText.setFont(new Font("Broadway", 30));
            welcomeText.setAlignment(Pos.CENTER);
            welcomeText.setText("WELCOME !!");
            welcomeText.setTranslateX(110);
            welcomeText.setTranslateY(10);

            Label messageLabel = new Label();
            messageLabel.setAlignment(Pos.CENTER);
            messageLabel.setText("We just need to ask you the following!");
            messageLabel.setFont(new Font("Broadway", 18));
            messageLabel.setAlignment(Pos.CENTER);
            messageLabel.setTranslateX(20);
            messageLabel.setTranslateY(50);

            TextField enterName = new TextField();
            // setFocusTraversable prevents the TextField from being selected by default
            enterName.setFocusTraversable(false);
            enterName.setTranslateX(130);
            enterName.setTranslateY(150);
            enterName.setPromptText("What's your name?");

            TextField enterUsername = new TextField();
            enterUsername.setFocusTraversable(false);
            enterUsername.setTranslateX(130);
            enterUsername.setTranslateY(200);
            enterUsername.setPromptText("Your nickname, sire?");

            PasswordField enterPassword = new PasswordField();
            enterPassword.setFocusTraversable(false);
            enterPassword.setTranslateX(130);
            enterPassword.setTranslateY(250);
            enterPassword.setPromptText("What's your secret code?");

            Button closeButton = new Button("Here are my details");
            closeButton.setTranslateX(145);
            closeButton.setTranslateY(300);
            closeButton.setFont(new Font("Broadway", 10));
            closeButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (enterName.getText().equals("") || enterUsername.getText().equals("") || enterPassword.getText().equals("")) {
                        return;
                    }
                    newGameHelper(enterName.getText(), enterUsername.getText(), enterPassword.getText());
                    window.close();
                }
            });

            Group root = new Group();
            root.getChildren().add(welcomeText);
            root.getChildren().add(messageLabel);
            root.getChildren().add(enterName);
            root.getChildren().add(enterUsername);
            root.getChildren().add(enterPassword);
            root.getChildren().add(closeButton);

            Scene scene = new Scene(root, 300, 300, Color.DARKGRAY);
            window.initStyle(StageStyle.UNDECORATED);
            window.setScene(scene);
            window.showAndWait();
            // after exiting the new Stage
        }

        UserProfile newUser = currentUser;

        System.out.println("BEFORE NEW GAME BUTTON PUSHED");
        System.out.println(numberToGamePlayMapping.toString());
        Gameplay gameplay = new Gameplay(newUser, new Group(),null);
        // Register the new account to the database
        System.out.println("gameplays: "+gameplays);
        System.out.println("gameplays.size(): "+gameplays.size());
        gameplays.add(gameplay);
        numberToGamePlayMapping.put(radioButtonNumber,gameplay);
        numberToButtonMapping.get(radioButtonNumber).setText(currentUser.getUsername());
        currentGameplay = gameplay;
        System.out.println("AFTER NEW GAME BUTTON PUSHED");
        System.out.println(numberToGamePlayMapping.toString());
    }

    void newGameHelper(String name, String username, String password){
        currentUser = new UserProfile(name,username,password);
    }

    public void goBackButtonPushed(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainLobby.fxml" ));
        Scene mainLobby = new Scene(root);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainLobby);
    }

    //checks which RadioButton is free
    Integer emptyRadioButton(){
        if(userRadioButton1.getText().equals("EMPTY")){
            return 1;
        }
        else if(userRadioButton2.getText().equals("EMPTY")){
            return 2;
        }
        else if(userRadioButton3.getText().equals("EMPTY")){
            return 3;
        }
        else if(userRadioButton4.getText().equals("EMPTY")){
            return 4;
        }
        else if(userRadioButton5.getText().equals("EMPTY")){
            return 5;
        }
        return -1;
    }

    public static boolean isLoggedIn(){
        return currentUser != null;
    }

    public static void notLoggedInError(){
        Stage window = new Stage();
        window.setWidth(300);
        window.setHeight(300);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Not Logged in...");

        Button closeButton = new Button("Oh, I'll Login");
        closeButton.setTranslateX(100);
        closeButton.setTranslateY(300);
        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                window.close();
            }
        });

        Label messageLabel = new Label();
        messageLabel.setFont(Font.font("Broadway"));
        messageLabel.setAlignment(Pos.CENTER);
        messageLabel.setText("Ours spies detected you are not logged in. How do we know you can be trusted...");
        messageLabel.setTranslateX(100);
        messageLabel.setTranslateY(100);

        Group root = new Group();
        root.getChildren().add(closeButton);
        root.getChildren().add(messageLabel);

        Scene scene = new Scene(root,300,300, Color.GREY);
        window.setScene(scene);
        window.showAndWait();
        return;
    }

    public static void setCurrentUser(UserProfile currentUser) {
        SelectPlayer.currentUser = currentUser;
    }
    public static void setCurrentGameplay(Gameplay currentGameplay) {
        SelectPlayer.currentGameplay = currentGameplay;
    }
}

class UserProfile implements Serializable {
    final private String name;
    private String username;
    private String password;
    private int levelsPlayed;
    private int starsBalance;
    private int starsSpent;
    private int endlessScore;
    private LocalDateTime latestSpinWheelTime;

    UserProfile(String name, String username, String password){
        this.name = name;
        this.username = username;
        this.password = password;
        levelsPlayed = 0;
        starsBalance = 0;
        starsSpent = 0;
        endlessScore = 0;
        latestSpinWheelTime = LocalDateTime.now().minusHours(10);
    }

    // getters from here
    public int getEndlessScore() {
        return endlessScore;
    }
    public int getLevelsPlayed() {
        return levelsPlayed;
    }
    public int getStarsBalance() {
        return starsBalance;
    }
    public int getStarsSpent() {
        return starsSpent;
    }

    public String getName() {
        return name;
    }
    public String getUsername() {
        return username;
    }
    public LocalDateTime getLatestSpinWheelTime() {
        return latestSpinWheelTime;
    }

    // setters from here
    public void setEndlessScore(int endlessScore) {
        this.endlessScore = endlessScore;
    }
    public void setLevelsPlayed(int levelsPlayed) {
        this.levelsPlayed = levelsPlayed;
    }
    public void setStarsBalance(int starsBalance) {
        this.starsBalance = starsBalance;
    }
    public void setStarsSpent(int starsSpent) {
        this.starsSpent = starsSpent;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setLatestSpinWheelTime(LocalDateTime latestSpinWheelTime) {
        this.latestSpinWheelTime = latestSpinWheelTime;
    }
}