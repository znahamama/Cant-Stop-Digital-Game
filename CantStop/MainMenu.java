import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainMenu extends JFrame implements ActionListener {

    private final String BG_IMAGE_PATH = "mainmenubg.jpg";

    private JButton newGameButton, loadGameButton, exitButton;

    public MainMenu() {
        newGameButton = new JButton("New Game");
        newGameButton.setAlignmentX(CENTER_ALIGNMENT);
        newGameButton.setAlignmentY(CENTER_ALIGNMENT);
        newGameButton.setFont(new Font("Arial", Font.PLAIN, 15));

        loadGameButton = new JButton("Load Game");
        loadGameButton.setAlignmentX(CENTER_ALIGNMENT);
        loadGameButton.setAlignmentY(CENTER_ALIGNMENT);
        loadGameButton.setFont(new Font("Arial", Font.PLAIN, 15));

        exitButton = new JButton("Exit");
        JButton[] buttons = {newGameButton, loadGameButton, exitButton};
        for (JButton button : buttons) {
            button.setAlignmentX(CENTER_ALIGNMENT);
            button.setAlignmentY(CENTER_ALIGNMENT);
            button.setFont(new Font("Arial", Font.PLAIN, 15));
            button.addActionListener(this);
        }

        try {
            Image bgImage = ImageIO.read(new File(BG_IMAGE_PATH));
            setContentPane(new MainMenuPanel(bgImage, buttons));
        } catch (Exception e) {
            System.out.println("Failed to load main menu image : " + e.getMessage());
            setContentPane(new MainMenuPanel(buttons));
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setResizable(false);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == newGameButton) {
            dispose();

            SetUpGame gamesetup = new SetUpGame();
        } else if (evt.getSource() == loadGameButton) {

            Game game = SaveLoadUtil.loadGame();
            new GameWindow(game);
        } else if (evt.getSource() == exitButton) {
            System.exit(0);
        }

    }
}
