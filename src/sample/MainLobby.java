// Manas Gupta : 2019368
// Prachi Goyal: 2019186
package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainLobby extends Application {

    @FXML Button goBack;
    @FXML Button settingsButton;
    @FXML Button statsButton;
    @FXML Button playButton;
    @FXML Button prizesButton;
    @FXML Button creditsButton;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MainLobby.fxml" ));
        primaryStage.setTitle("Color Switch");
        primaryStage.setScene(new Scene(root, 500, 700));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    // Start playing game from here, will be called after loading the game and setting the
    // required attributes (like objects,stars, position of ball, angle of rotation of obstacles already present).
    public Scene startGame(){
        //CREATE ROOT NODE
        Group root = new Group();

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
        Ball b = new Ball(c,star,root);

        //Add all Elements to root node
        for(Shape x: c.components) {
            root.getChildren().add(x);
        }
        root.getChildren().addAll(b.getShape(), star.getShape());
        Scene scene = new Scene(root, 500, 700, Color.GREY);

        return scene;
    }

    public void startNewGame(ActionEvent event) throws IOException {
        Scene gameplayScene = startGame();
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(gameplayScene);
    }

    public void settingsButtonPushed(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Settings.fxml" ));
        Scene settingsScene = new Scene(root);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(settingsScene);
    }

    public void statsButtonPushed(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Stats.fxml"));
        Scene statsScene = new Scene(root);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(statsScene);
    }

    public void goBackButtonPushed(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainLobby.fxml" ));
        Scene mainLobby = new Scene(root);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainLobby);
    }

    public void playButtonPushed(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("SelectPlayer.fxml" ));
        Scene selectPlayerScene = new Scene(root);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(selectPlayerScene);
    }

    public void prizesButtonPushed(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Prizes.fxml" ));
        Scene prizeScene = new Scene(root);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(prizeScene);
    }

    public void creditsButtonPushed(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Credits.fxml" ));
        Scene creditsScene = new Scene(root);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(creditsScene);
    }
}
