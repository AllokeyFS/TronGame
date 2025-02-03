package assignment3;

import java.awt.*;
import java.util.List;

enum Direction {
    UP, DOWN, LEFT, RIGHT
}

public class Player {
    private int x, y, dx, dy;
    private String name;
    private Color trailColor;
    private int up, down, left, right;
    private Direction direction;

    public Player(String name, int x, int y, Color trailColor, int up, int down, int left, int right) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.trailColor = trailColor;
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.direction = Direction.RIGHT;
    }

    public void move() {
        x += dx;
        y += dy;
    }

    public void setDirection(int key) {
        if (key == up) {
            direction = Direction.UP;
            dx = 0;
            dy = -10;
        } else if (key == down) {
            direction = Direction.DOWN;
            dx = 0;
            dy = 10;
        } else if (key == left) {
            direction = Direction.LEFT;
            dx = -10;
            dy = 0;
        } else if (key == right) {
            direction = Direction.RIGHT;
            dx = 10;
            dy = 0;
        }
    }

    public boolean collidesWithBoundary(int width, int height) {
        return x < 0 || y < 0 || x > width - 20 || y > height - 20;
    }

    public boolean collidesWithTrail(List<Point> trail) {
        Rectangle playerRect = new Rectangle(x, y, 20, 20);
        for (Point p : trail) {
            Rectangle trailRect = new Rectangle(p.x, p.y, 20, 20);
            if (playerRect.intersects(trailRect)) {
                return true;
            }
        }
        return false;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public String getName() { return name; }
    public Color getTrailColor() { return trailColor; }
}
