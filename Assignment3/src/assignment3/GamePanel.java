package assignment3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private Player player1, player2;
    private ArrayList<Point> trail1, trail2;
    private DatabaseHandler dbHandler;
    private Image bike1Image, bike2Image;
    private int bikeWidth = 75;
    private int bikeHeight = 40;

    public GamePanel(DatabaseHandler dbHandler, Player player1, Player player2) {
        this.dbHandler = dbHandler;
        this.player1 = player1;
        this.player2 = player2;
        this.trail1 = new ArrayList<>();
        this.trail2 = new ArrayList<>();
        setFocusable(true);
        addKeyListener(this);
        timer = new Timer(100, this);

        try {
            BufferedImage originalBike1 = ImageIO.read(new File("src/assets/player1.png"));
            BufferedImage originalBike2 = ImageIO.read(new File("src/assets/player2.png"));

            bike1Image = originalBike1.getScaledInstance(bikeWidth, bikeHeight, Image.SCALE_SMOOTH);
            bike2Image = originalBike2.getScaledInstance(bikeWidth, bikeHeight, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            System.out.println("Error loading bike images: " + e.getMessage());
        }

        trail1.add(new Point(player1.getX(), player1.getY()));
        trail2.add(new Point(player2.getX(), player2.getY()));

        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(player1.getTrailColor());
        for (Point p : trail1) {
            g.fillRect(p.x, p.y, 20, 20);
        }

        g.setColor(player2.getTrailColor());
        for (Point p : trail2) {
            g.fillRect(p.x, p.y, 20, 20);
        }

        drawRotatedBike(g, player1.getX(), player1.getY(), bike1Image, player1.getDirection());

        drawRotatedBike(g, player2.getX(), player2.getY(), bike2Image, player2.getDirection());
    }

    private void drawRotatedBike(Graphics g, int x, int y, Image bikeImage, Direction direction) {
        if (bikeImage == null) {
            g.setColor(Color.BLACK);
            g.fillRect(x, y, 10, 10);
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();

        g2.translate(x + bikeWidth / 2.0, y + bikeHeight / 2.0);

        double angle = 0;
        boolean flipHorizontal = false;

        switch (direction) {
            case UP:
                angle = -90;
                break;
            case DOWN:
                angle = 90;
                break;
            case LEFT:
                flipHorizontal = true;
                break;
            case RIGHT:
                angle = 0;
                break;
        }

        if (angle != 0) {
            g2.rotate(Math.toRadians(angle));
        }

        if (flipHorizontal) {
            g2.scale(-1, 1);
        }

        g2.drawImage(bikeImage, -bikeWidth / 2, -bikeHeight / 2, null);
        g2.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        player1.move();
        player2.move();

        if (checkCollision()) {
            return;
        }

        trail1.add(new Point(player1.getX(), player1.getY()));
        trail2.add(new Point(player2.getX(), player2.getY()));

        repaint();
    }

    private boolean checkCollision() {
        if (player1.collidesWithBoundary(getWidth(), getHeight()) || player1.collidesWithTrail(trail2)) {
            timer.stop();
            dbHandler.updateScore(player2.getName());
            JOptionPane.showMessageDialog(this,"Player " + player2.getName() + " Wins!");
            return true;
        }

        if (player2.collidesWithBoundary(getWidth(), getHeight()) || player2.collidesWithTrail(trail1)) {
            timer.stop();
            dbHandler.updateScore(player1.getName());
            JOptionPane.showMessageDialog(this,"Player " + player1.getName() + " Wins!");
            return true;
        }

        return false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        player1.setDirection(e.getKeyCode());
        player2.setDirection(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void keyTyped(KeyEvent e) { }
}
