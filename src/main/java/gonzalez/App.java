package gonzalez;

import java.util.HashSet;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

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
    private Sprite speedBoost = new Sprite(new Image("file:resource/SpeedBoost .png"), "Speed Boost");
    private Sprite ball = new Sprite(new Image("file:resource/Ball.png"), "Ball");
    private Sprite foot = new Sprite(new Image("file:resource/SoccerShoe.png"), "Player 1");
    private Sprite foot2 = new Sprite(new Image("file:resource/SoccerShoe.png"), "Player 2");
    private Group gameScreen = new Group(ball, foot2, foot);
    private AnimationTimer timer;
    private long previousTime = -1;
    private Timeline spawnSpeedBoostTimeline;
    private long spawnTime = 0;
    private long collectTime = -1;

    @Override
    public void start(Stage stage) {

        Group root = new Group();

        ball.setPosition(GAME_WIDTH / 2.2, GAME_HEIGHT / 2.2); // Set the ball's initial position.
        speedBoost.autosize();
        foot.setPosition(483, 224); // Set the foot's initial position.
        foot2.setPosition(83, 224); // Set the foot's initial position.

        Image field = new Image("file:resource/field.jpg"); // Set the background image.
        ImagePattern imageView = new ImagePattern(field); // Set the background image.
        ball.setVelocityY(100);

        spawnSpeedBoostTimeline = new Timeline(new KeyFrame(Duration.seconds(10), e -> spawnSpeedBoost())); // Spawn a speed boost every 10 seconds.
        spawnSpeedBoostTimeline.setCycleCount(Timeline.INDEFINITE); // Make the timeline repeat indefinitely.
        spawnSpeedBoostTimeline.play(); // Start the timeline.

        p1Score.setText("Player 1 Score: " + foot.getScore()); // Set the initial score.
        p1Score.setFont(Font.font("Verdana", FontWeight.BOLD, 20)); // Set the font.
        p1Score.setFill(Color.YELLOW); // Set the color.
        p1Score.setX(10);   // Set the x position.
        p1Score.setY(20);  // Set the y position.
        gameScreen.getChildren().add(p1Score); // Add the score to the game screen.

        p2Score.setText("Player 2 Score: " + foot2.getScore());     // Set the initial score.
        p2Score.setFont(Font.font("Verdana", FontWeight.BOLD, 20)); // Set the font.
        p2Score.setFill(Color.YELLOW); // Set the color.
        p2Score.setX(440); // Set the x position.
        p2Score.setY(20); // Set the y position.
        gameScreen.getChildren().add(p2Score); // Add the score to the game screen.

        startText.setText("Let's Play! Press Space to Start!");
        startText.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
        startText.setFill(Color.YELLOW);
        startText.setX(GAME_WIDTH / 2 - startText.getLayoutBounds().getWidth() / 2); // Center the text.
        startText.setY(120);
        gameScreen.getChildren().add(startText);

        gameScreen.setFocusTraversable(true); // Make the game screen focusable.

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) { // Update the game.
                if (previousTime != -1) {  // If the game is not paused.
                    double elapsedTime = (now - previousTime) / 1000000000.0;  // Calculate the elapsed time.
                    updateGame(elapsedTime); // Update the game.
                }
                previousTime = now;
            }

            public void start() { // Start the game.
                previousTime = System.nanoTime();
                super.start();
            }
 
            public void stop() { // Pause the game.
                previousTime = -1;
                super.stop();
            }
        };

        root.getChildren().addAll(gameScreen);

        Scene scene = new Scene(root, 640, 480, imageView);

        gameScreen.setOnKeyPressed(key -> keyPressed(key)); // Handle key presses.
        gameScreen.setOnKeyReleased(key -> keyReleased(key)); // Handle key releases.

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public void keyPressed(KeyEvent key) { // Handle key presses.
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

    public void handleKeyInput(double elapsedTime) { // Handle key presses.
        ball.update(elapsedTime);

        // Player 1
        if (keyboard.contains(KeyCode.LEFT)) { // Move left.
            foot.setVelocityX(-200);
            foot.update(elapsedTime);
        }
        if (keyboard.contains(KeyCode.RIGHT)) { // Move right.
            foot.setVelocityX(200);
            foot.update(elapsedTime);
        }
        if (keyboard.contains(KeyCode.UP)) { // Move up.
            foot.setVelocityY(-200);
            foot.update(elapsedTime);
        }
        if (keyboard.contains(KeyCode.DOWN)) { // Move down.
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

        if (keyboard.contains(KeyCode.ESCAPE)) { // Exit the game.
            System.exit(0);
        }
    }

    public boolean isPaused() { // Check if the game is paused.
        return previousTime == -1;
    }

    public void pause() { // Pause or unpause the game.
        if (isPaused()) {
            timer.start();
        } else {
            timer.stop();
        }
    }

    public void updateGame(double elapsedTime) {
        ballPhysics(elapsedTime);

        handleKeyInput(elapsedTime);

        // Reset player speed 3 seconds after collecting speed boost
        if (System.nanoTime() - collectTime >= 3000000000L) {
            foot.resetSpeed();
            foot2.resetSpeed();
            collectTime = -1; // Reset the collectTime
        }
    }

    public void ballPhysics(double elapsedTime) {
        ball.update(elapsedTime);

        if (ball.getPositionY() < 0) {
            ball.update(-elapsedTime);
            ball.setVelocityY(-ball.getVelocityY());
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

        // Remove speed boost after 3 seconds
        if (System.nanoTime() - spawnTime >= 3000000000L && gameScreen.getChildren().contains(speedBoost)) {
            gameScreen.getChildren().remove(speedBoost);
        }

        if (ball.getPositionX() + ball.getWidth() > GAME_WIDTH) {
            foot.setScore(foot.getScore() + 1);
            p1Score.setText("Player 1 Score: " + foot.getScore()); // Update Player 1 score text
            gameRestart();
        }
        if (foot.intersects(speedBoost)) {
            foot.speedBoost();
            gameScreen.getChildren().remove(speedBoost);
            collectTime = System.nanoTime();
        }

        if (foot2.intersects(speedBoost)) {
            foot2.speedBoost();
            gameScreen.getChildren().remove(speedBoost); // Remove the speed boost after it's been collected
            collectTime = System.nanoTime();
        }

        if (ball.intersects(foot)) {
            ball.update(-elapsedTime);
            ball.setVelocity(Math.random() * -300, Math.random() * -300);
        }

        if (ball.intersects(foot2)) {
            ball.update(-elapsedTime);
            ball.setVelocity(Math.random() * 300, Math.random() * 300);
        }
        ball.resetIntersection();
        speedBoost.resetIntersection();

        ball.addVelocity(0, 100 * elapsedTime);
    }

    public void gameRestart() {
        ball.setPosition(GAME_WIDTH / 2.2, GAME_HEIGHT / 2.2); // Reset the ball position
        foot.setPosition(483, 224); // Reset the player positions
        foot2.setPosition(83, 224); // Reset the player positions
        ball.setVelocity(0, 0);
        timer.stop(); // Pause the game
        startText.setVisible(true); // Show the start text
    }

    private void spawnSpeedBoost() { // Spawn the speed boost
        speedBoost.setPosition(Math.random() * (GAME_WIDTH - speedBoost.getWidth()), 
                Math.random() * (GAME_HEIGHT - speedBoost.getHeight()));
        if (!gameScreen.getChildren().contains(speedBoost)) {
            gameScreen.getChildren().add(speedBoost);
        }
        spawnTime = System.nanoTime();
    }

}