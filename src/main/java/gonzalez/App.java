package gonzalez;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private Sprite ball = new Sprite(new Image("file:resource/Ball.png"));
    private Sprite foot = new Sprite(new Image("file:resource/SoccerShoe.png"));
    private Group gameScreen = new Group(ball, foot);
    private AnimationTimer timer;
    private long previousTime = -1;

    @Override
    public void start(Stage stage) {
        Group root = new Group();

        ball.setPosition(100, 100);
        foot.setPosition(200, 200);
        ball.setVelocityY(100);
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
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
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

        if(ball.getPositionY() + ball.getHeight() >= gameScreen.getHeight()) {
            ball.setPositionY(gameScreen.getHeight() - ball.getHeight());
            ball.setVelocityY(-ball.getVelocityY());
        }
    }
}