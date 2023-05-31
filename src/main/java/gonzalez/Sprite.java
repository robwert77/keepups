package gonzalez;

import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Sprite extends Group {
    private Image image;
    private ImageView imageView = new ImageView();
    private double positionX = 0;
    private double positionY = 0;
    private double width = 0;
    private double height = 0;
    private double velocityX = 0;
    private double velocityY = 0;

    public Sprite(Image i) {
        image = i;
        width = image.getWidth();
        height = image.getHeight();

        imageView.setImage(image);

        this.getChildren().add(imageView);
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

    public void setVelocity(double x, double y) {
        velocityX = x;
        velocityY = y;
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
    
}
