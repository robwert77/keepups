package gonzalez;

import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Sprite extends Group {
    private Image image;
    private boolean hasIntersected = false;
    private ImageView imageView = new ImageView();
    private double positionX = 0;
    private double positionY = 0;
    private double width = 0;
    private double speedMultiplier = 1.0;
    private long lastIntersectionTime = 0;
    private double height = 0;
    private double velocityX = 0;
    private double velocityY = 0;
    private String name;
    private int score;

    public Sprite(Image i, String name) {
        image = i;
        width = image.getWidth();
        height = image.getHeight();

        imageView.setImage(image);

        this.name = name;
        this.score = 0;

        this.getChildren().add(imageView);
    }

    public String getName() {
        return name;
    }

    public void speedBoost() { // 4x speed
        speedMultiplier = 4.0;
    }

    public void resetSpeed() { // 1x speed
        speedMultiplier = 1.0;
    }

    public int getScore() { 
        return score;
    }

    public void setImage(Image i) {
        image = i;
        width = image.getWidth();
        height = image.getHeight();

        imageView.setImage(image);
    }

    public void setPosition(double x, double y) {
        positionX = x;
        positionY = y;
        this.relocate(positionX, positionY);
    }

    public void setPositionX(double x) {
        positionX = x;
        this.relocate(positionX, positionY);
    }

    public void setPositionY(double y) {
        positionY = y;
        this.relocate(positionX, positionY);
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void setVelocity(double x, double y) { // x and y are normalized
        velocityX = x * speedMultiplier;
        velocityY = y * speedMultiplier;
    }

    public void setVelocityX(double x) { 
        velocityX = x;
    }

    public void setVelocityY(double y) {
        velocityY = y;
    }

    public void addVelocity(double x, double y) {
        velocityX += x;
        velocityY += y;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void update(double elapsedTime) {
        positionX = positionX + velocityX * elapsedTime;
        positionY = positionY + velocityY * elapsedTime;

        this.relocate(positionX, positionY);
    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(positionX, positionY, width, height);
    }

    public boolean intersects(Sprite s) { // checks if sprite intersects with another sprite
        if (hasIntersected) {
            return false;
        } else {
            boolean intersects = s.getBoundary().intersects(this.getBoundary());
            if (intersects) {
                lastIntersectionTime = System.nanoTime();
            }
            hasIntersected = intersects;
            return intersects;
        }
    }

    public void resetIntersection() { // resets intersection after 1 second
        if (System.nanoTime() - lastIntersectionTime >= 500000000) { // 1 second = 1,000,000,000 nanoseconds
            hasIntersected = false;
        }
    }

    public void setScore(int score) {
        this.score = score;
    }

}
