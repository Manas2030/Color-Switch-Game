package sample;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;

import java.io.Serializable;
import javafx.scene.Group;
import javafx.util.Duration;

public final class Ball extends GameObject {
    private Shape shape;
    private int score;
    private int YMin;
    private int YBase;
    private Color color;
    private Group root;
    private Scene scene;
    protected TranslateTransition moveDown;
    protected TranslateTransition moveUp;
    private ParallelTransition pt;

    Ball(Group root, Scene scene) {
        this.X = 250;
        this.YBase = 0;
        this.Y = 500;
        this.YMin = 500;
        this.color = Color.CYAN;
        this.root = root;
        this.scene = scene;

        draw();
        motion();
    }

    public Shape getShape() { return shape; }

    @Override
    public void draw() {
        Circle c = new Circle(this.X, this.Y, 10, this.color);
        this.shape = c;
    }

    @Override
    public void motion() {
        this.moveUp = new TranslateTransition(Duration.millis(160), shape);
        moveDown = new TranslateTransition(Duration.millis(1000), shape);
        //this.pt = new ParallelTransition(moveDown, moveUp);
        new AnimationTimer() {
            @Override
            public void handle(long l) {
                moveDown.setToY(YBase);
                moveDown.play();
                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void mini_move_up() {
    }
    private void collides(){}
    private void destroy(){}
    private void updateScore(){}
}