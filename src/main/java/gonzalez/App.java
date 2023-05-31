package gonzalez;

import java.util.HashSet;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private final HashSet<KeyCode> keyboard = new HashSet<>();
    private final int GAME_WIDTH = 640;
    private final int GAME_HEIGHT = 480;
    private Text startText = new Text();
    private Text p1Score = new Text();
    private Text p2Score = new Text();
    private Sprite ball = new Sprite(new Image("file:resource/Ball.png"), "Ball");
    private Sprite foot = new Sprite(new Image("file:resource/SoccerShoe.png"), "Player 1");
    private Sprite foot2 = new Sprite(new Image("file:resource/SoccerShoe.png"), "Player 2");

    private Group gameScreen = new Group(ball, foot2, foot);
    private AnimationTimer timer;
    private long previousTime = -1;

    @Override
    public void start(Stage stage) {

        Group root = new Group();

        ball.setPosition(GAME_WIDTH / 2.2, GAME_HEIGHT / 2.2);
        foot.setPosition(483, 224);
        foot2.setPosition(83, 224);

        Image field = new Image("file:resource/field.jpg");
        ImagePattern imageView = new ImagePattern(field);
        ball.setVelocityY(100);

        p1Score.setText("Player 1 Score: " + foot.getScore());
        p1Score.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        p1Score.setFill(Color.YELLOW);
        p1Score.setX(10);
        p1Score.setY(20);
        gameScreen.getChildren().add(p1Score);

        p2Score.setText("Player 2 Score: " + foot2.getScore());
        p2Score.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        p2Score.setFill(Color.YELLOW);
        p2Score.setX(440);
        p2Score.setY(20);
        gameScreen.getChildren().add(p2Score);

        startText.setText("Let's Play! Press Space to Start!");
        startText.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
        startText.setFill(Color.YELLOW);
        startText.setX(GAME_WIDTH / 2 - startText.getLayoutBounds().getWidth() / 2);
        startText.setY(120);
        gameScreen.getChildren().add(startText);

        gameScreen.setFocusTraversable(true); // Make the game screen focusable.

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

        root.getChildren().addAll(gameScreen);

        Scene scene = new Scene(root, 640, 480, imageView);

        gameScreen.setOnKeyPressed(key -> keyPressed(key));
        gameScreen.setOnKeyReleased(key -> keyReleased(key));

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public void keyPressed(KeyEvent key) {
        if (!keyboard.contains(KeyCode.P)) {
            if (key.getCode() == KeyCode.SPACE) {
                startText.setVisible(false);
                pause();
            }
        }

        keyboard.add(key.getCode());
    }

    public void keyReleased(KeyEvent key) {
        keyboard.remove(key.getCode());
    }

    public void handleKeyInput(double elapsedTime) {
        // Player 1
        if (keyboard.contains(KeyCode.LEFT)) {
            foot.setVelocityX(-200);
            foot.update(elapsedTime);
        }
        if (keyboard.contains(KeyCode.RIGHT)) {
            foot.setVelocityX(200);
            foot.update(elapsedTime);
        }
        if (keyboard.contains(KeyCode.UP)) {
            foot.setVelocityY(-200);
            foot.update(elapsedTime);
        }
        if (keyboard.contains(KeyCode.DOWN)) {
            foot.setVelocityY(200);
            foot.update(elapsedTime);
        }

        // Player 2
        if (keyboard.contains(KeyCode.A)) {
            foot2.setVelocityX(-200);
            foot2.update(elapsedTime);
        }
        if (keyboard.contains(KeyCode.D)) {
            foot2.setVelocityX(200);
            foot2.update(elapsedTime);
        }
        if (keyboard.contains(KeyCode.W)) {
            foot2.setVelocityY(-200);
            foot2.update(elapsedTime);
        }
        if (keyboard.contains(KeyCode.S)) {
            foot2.setVelocityY(200);
            foot2.update(elapsedTime);
        }

        if (keyboard.contains(KeyCode.ESCAPE)) {
            System.exit(0);
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

        handleKeyInput(elapsedTime);
    }

    public void ballPhysics(double elapsedTime) {
        ball.update(elapsedTime);

        if (ball.getPositionY() < 0) {
            ball.update(-elapsedTime);
            ball.setVelocityX(-ball.getVelocityY());
        }

        // if the ball hits the bottom wall bounce it back
        if (ball.getPositionY() + ball.getHeight() > GAME_HEIGHT) {
            ball.update(-elapsedTime);
            ball.setVelocityY(-ball.getVelocityY());
        }
        // if the ball hits the left or right wall reset the ball
        if (ball.getPositionX() < 0) {
            foot2.setScore(foot2.getScore() + 1);
            p2Score.setText("Player 2 Score: " + foot2.getScore()); // Update Player 2 score text
            gameRestart();
        }

        if (ball.getPositionX() + ball.getWidth() > GAME_WIDTH) {
            foot.setScore(foot.getScore() + 1);
            p1Score.setText("Player 1 Score: " + foot.getScore()); // Update Player 1 score text
            gameRestart();
        }

        if (ball.intersects(foot) || ball.intersects(foot2)) {
            double newVelocityX = -150 * Math.random() - 100;
            double newVelocityY = 500 * Math.random() - 250;
            ball.setVelocity(newVelocityX, newVelocityY);
        }
        ball.addVelocity(0, 100 * elapsedTime);
    }

    public void gameRestart() {
        ball.setPosition(GAME_WIDTH / 2.2, GAME_HEIGHT / 2.2);
        foot.setPosition(483,s 224);
        foot2.setPosition(83, 224);
        ball.setVelocity(GAME_WIDTH, GAME_HEIGHT);
        startText.setVisible(true);
    }

}