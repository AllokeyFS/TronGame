package assignment3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

public class TronGame{ 
    public static void main(String[] args) {
        DatabaseHandler dbHandler = new DatabaseHandler();
        Random random = new Random();

        int frameWidth = 1400;
        int frameHeight = 800;
        
        int margin = 25;
        int playerSize = 20;

        int player1X = random.nextInt(frameWidth - playerSize - (margin * 2)) + margin;
        int player1Y = random.nextInt(frameHeight - playerSize - (margin * 2)) + margin;

        int player2X = random.nextInt(frameWidth - playerSize - (margin * 2)) + margin;
        int player2Y = random.nextInt(frameHeight - playerSize - (margin * 2)) + margin;

        String player1Name = enforceNameInput("Player 1");
        Color player1Color = enforceColorSelection("Player 1");
        dbHandler.addPlayer(player1Name, player1Color);

        String player2Name = enforceSecondNameInput("Player 2", player1Name);
        Color player2Color = enforceSecondColorSelection("Player 2", player1Color);
        dbHandler.addPlayer(player2Name, player2Color);

        Player player1 = new Player(player1Name, player1X, player1Y, player1Color, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D);
        Player player2 = new Player(player2Name, player2X, player2Y, player2Color, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT);

        JFrame frame = new JFrame("Tron Game");
        GamePanel gamePanel = new GamePanel(dbHandler, player1, player2);
        frame.add(gamePanel);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (confirmExit(frame)) {
                    System.exit(0);
                }
            }
        });

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Options");

        JMenuItem highScores = new JMenuItem("View High Scores");
        highScores.addActionListener(e -> JOptionPane.showMessageDialog(frame, dbHandler.getTopScores()));

        JMenuItem restartGame = new JMenuItem("Restart Game");
        restartGame.addActionListener(e -> {
            frame.dispose();
            main(null);
        });

        menu.add(highScores);
        menu.add(restartGame);
        menuBar.add(menu);

        frame.setJMenuBar(menuBar);
        frame.setSize(frameWidth, frameHeight);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setVisible(true);
    }
    private static String enforceNameInput(String player) {
        while (true) {
            String name = JOptionPane.showInputDialog(null, "Enter " + player + " Name:", player + " Setup", JOptionPane.PLAIN_MESSAGE);

            if (name == null) {
                if (confirmExit(null)) {
                    System.exit(0);
                } else {
                    continue; 
                }
            }

            name = name.trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Name cannot be empty. Please enter a valid name.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } else {
                return name;
            }
        }
    }

    private static String enforceSecondNameInput(String player, String firstPlayerName) {
        while (true) {
            String name = JOptionPane.showInputDialog(null, "Enter " + player + " Name:", player + " Setup", JOptionPane.PLAIN_MESSAGE);

            if (name == null) {
                if (confirmExit(null)) {
                    System.exit(0);
                } else {
                    continue;
                }
            }

            name = name.trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Name cannot be empty. Please enter a valid name.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } else if (name.equalsIgnoreCase(firstPlayerName)) {
                JOptionPane.showMessageDialog(null, "This name is already taken by Player 1. Please choose a different name.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } else {
                return name;
            }
        }
    }

    private static Color enforceColorSelection(String player) {
        while (true) {
            Color color = JColorChooser.showDialog(null, "Choose " + player + " Trail Color", Color.WHITE);

            if (color == null) {
                if (confirmExit(null)) {
                    System.exit(0);
                } else {
                    continue;
                }
            } else {
                return color;
            }
        }
    }

    private static Color enforceSecondColorSelection(String player, Color firstPlayerColor) {
        while (true) {
            Color color = JColorChooser.showDialog(null, "Choose " + player + " Trail Color", Color.WHITE);

            if (color == null) {
                if (confirmExit(null)) {
                    System.exit(0);
                } else {
                    continue; 
                }
            } else if (color.equals(firstPlayerColor)) {
                JOptionPane.showMessageDialog(null, "This color is already taken by Player 1. Please choose a different color.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } else {
                return color;
            }
        }
    }

    private static boolean confirmExit(Component parent) {
        int choice = JOptionPane.showConfirmDialog(
                parent,
                "Are you sure you want to exit the game?",
                "Exit Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        return choice == JOptionPane.YES_OPTION;
    }
}
