package com.group18.controller;


import com.group18.core.CreateLevelLayout;
import com.group18.core.FileReader;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;

public class GameController extends Application {
    private static int playerx;
    private static int playery;
    private Rectangle player;
    private static int levelWidth;
    private static int levelHeight;

    private final int speed = 64 ; // pixels / second
    private boolean up ;
    private boolean down ;
    private boolean left ;
    private boolean right ;
    private boolean pressed = false;
    private Pane pane;

    public static void setPlayerPos(int x, int y) {
        playerx = x;
        playery = y;
    }

    public static void setLevelSize(int x, int y) {
        levelWidth = x;
        levelHeight = x;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        pane = CreateLevelLayout.createLevel("./src/resources/Level1.txt");
        player = new Rectangle(64, 64, 64, 64);
        pane.getChildren().add(player);

        Scene scene = new Scene(new BorderPane(pane),500,500);
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(scene.widthProperty());
        clip.heightProperty().bind(scene.heightProperty());

        clip.xProperty().bind(Bindings.createDoubleBinding(
                () -> clampRange(player.getX() - scene.getWidth() / 2, 0, pane.getWidth() - scene.getWidth()),
                player.xProperty(), scene.widthProperty()));
        clip.yProperty().bind(Bindings.createDoubleBinding(
                () -> clampRange(player.getY() - scene.getHeight() / 2, 0, pane.getHeight() - scene.getHeight()),
                player.yProperty(), scene.heightProperty()));

        pane.setClip(clip);
        pane.translateXProperty().bind(clip.xProperty().multiply(-1));
        pane.translateYProperty().bind(clip.yProperty().multiply(-1));

        scene.setOnKeyPressed(e -> processKey(e.getCode()));
        scene.setOnKeyReleased(e -> pressed = false);

        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void movePlayer(int deltaX, int deltaY) {
        if (!pressed) {
            pressed = true;
            player.setX(clampRange(player.getX() + deltaX, 0, pane.getWidth() - player.getWidth()));
            player.setY(clampRange(player.getY() + deltaY, 0, pane.getHeight() - player.getHeight()));
        }
    }

    private double clampRange(double value, double min, double max) {
        if (value < min) return min ;
        if (value > max) return max ;
        return value ;
    }

    private void processKey(KeyCode code) {
        switch (code) {
            case LEFT:
                movePlayer(-speed, 0);
                break ;
            case RIGHT:
                movePlayer(speed, 0);
                break ;
            case UP:
                movePlayer(0, -speed);
                break ;
            case DOWN:
                movePlayer(0, speed);
                break ;
            default:
                break ;
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}

