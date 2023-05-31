package gonzalez;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private final int GAME_WIDTH = 640;
    private final int GAME_HEIGHT = 480;
    private Sprite ball = new Sprite(new Image("file:resource/Ball.png"));
    private Sprite foot = new Sprite(new Image("file:resource/SoccerShoe.png"));
    private Group gameScreen = new Group(ball, foot);
    private AnimationTimer timer;
    private long previousTime = -1;

    @Override
    public void start(Stage stage) {
        Group root = new Group();

        ball.setPosition(150, 150);
        foot.setPosition(150, 300);
        ball.setVelocityY(100);

        gameScreen.setFocusTraversable(true); // Make the game screen focusable.
        gameScreen.requestFocus();

        Button time = new Button("Time");
        time.setOnAction(e -> {
            pause();
        });

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (previousTime != -1) {
                    double elapsedTime = (now - previousTime) / 1000000000.0;
                    updateGame(elapsedTime);
                }
                previousTime = now;
            }

            public void start() {
                previousTime = System.nanoTime();
                super.start();
            }

            public void stop() {
                previousTime = -1;
                super.stop();
            }
        };

        root.getChildren().addAll(time, gameScreen);

        var scene = new Scene(root, 640, 480, Color.GREEN);

        gameScreen.setOnKeyPressed(key -> keyPressed(key));
        gameScreen.setOnKeyReleased(key -> keyReleased(key));

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public void keyPressed(KeyEvent key) {
        if (key.getCode() == KeyCode.LEFT) {
            foot.setVelocityX(-200);
            foot.update(0.10);
        } else if (key.getCode() == KeyCode.RIGHT) {
            foot.setVelocityX(200);
            foot.update(0.10);
        }
    }

    public void keyReleased(KeyEvent key) {
        if (key.getCode() == KeyCode.LEFT || key.getCode() == KeyCode.RIGHT) {
            foot.setVelocityX(0);
        }
    }

    public boolean isPaused() {
        return previousTime == -1;
    }

    public void pause() {
        if (isPaused()) {
            timer.start();
        } else {
            timer.stop();
        }
    }

    public void updateGame(double elapsedTime) {
        ballPhysics(elapsedTime);
    }

    public void ballPhysics(double elapsedTime) {
        ball.update(elapsedTime);

        if (ball.getPositionX() < 0) {
            ball.update(-elapsedTime);
            ball.setVelocityX(-ball.getVelocityX());
        }

        if (ball.getPositionX() + ball.getWidth() > GAME_WIDTH) {
            ball.update(-elapsedTime);
            ball.setVelocityX(-ball.getVelocityX());
        }

        if (ball.intersects(foot)) {
            double newVelocityX = -150 * Math.random() - 100;
            double newVelocityY = 500 * Math.random() - 250;
            ball.setVelocity(newVelocityX, newVelocityY);

        }
        ball.addVelocity(0, 100 * elapsedTime);
    }

}