import java.io.File;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;


public class SetUpGame extends JFrame {
    private static final String BG_IMAGE_PATH = "setupgamebg.jpeg";
    private final JButton[] playerButtons;
    private final JButton addPlayerButton;
    private final JButton createGameButton;
    private final JButton removePlayerButton;
    private final JTextField[] playerNameInputs;
    private final JLabel[] playerLabels;
    private int player_counter;
    private ArrayList<Player> players;
    private JComboBox ai_difficultyBox;


    public SetUpGame() {
        // top panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
        
        playerButtons = new JButton[4];
        
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 15));
        //backButton.setBackground(Color.red);
        backButton.setPreferredSize(new Dimension(90, 30));
        backButton.addActionListener(e -> backToMainMenu());
        topPanel.add(backButton);
        topPanel.add(Box.createHorizontalGlue());
        
        // bottom panel - contains create game button
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(2, 1));

        JPanel bottomPanel1 = new JPanel();
        bottomPanel1.setLayout(new BoxLayout(bottomPanel1, BoxLayout.LINE_AXIS));

        bottomPanel1.add(Box.createHorizontalGlue());


        JPanel bottomPanel2 = new JPanel();
        bottomPanel2.setLayout(new FlowLayout());


        createGameButton = new JButton("Create Game");
        createGameButton.setBackground(Color.ORANGE);
        createGameButton.setFont(new Font("Arial", Font.BOLD, 17));
        createGameButton.setPreferredSize(new Dimension(150, 30));
        createGameButton.addActionListener(e -> createGame());
        createGameButton.setVisible(true);

        removePlayerButton = new JButton("Remove Player");
        removePlayerButton.setFont(new Font("Arial", Font.BOLD, 17));
        removePlayerButton.setBackground(Color.PINK);
        removePlayerButton.setPreferredSize(new Dimension(150, 30));
        removePlayerButton.addActionListener(e -> removePlayer());
        removePlayerButton.setVisible(false);

        bottomPanel2.add(createGameButton);
        bottomPanel2.add(removePlayerButton);

        bottomPanel.add(bottomPanel2);
        bottomPanel.add(bottomPanel1);

        // center panel - contains addPlayer button
        JPanel centerPanel3 = new JPanel();
        centerPanel3.setLayout(new BoxLayout(centerPanel3, BoxLayout.PAGE_AXIS));

        playerLabels = new JLabel[4];
        centerPanel3.setPreferredSize(new Dimension(100, 200));
        for (int i = 0; i < playerLabels.length; i++) {
            JLabel pl = new JLabel("Player " + (i + 1));
            pl.setMaximumSize(new Dimension(80, 50));
            pl.setFont(new Font("Arial", Font.PLAIN, 17));
            pl.setAlignmentX(RIGHT_ALIGNMENT);
            pl.setForeground(Color.white);
            if (i != 0)
                pl.setVisible(false);
            centerPanel3.add(pl);
            playerLabels[i] = pl;
        }

        JPanel centerPanel1 = new JPanel();
        centerPanel1.setLayout(new BoxLayout(centerPanel1, BoxLayout.PAGE_AXIS));
        
        playerNameInputs = new JTextField[4];

        centerPanel1.setPreferredSize(new Dimension(110, 200));

        for (int i = 0; i < playerNameInputs.length; i++) {
            JTextField pnf = new JTextField("Player "+ (i+1));
            pnf.setMaximumSize(new Dimension(110, 50));
            pnf.setBackground(Color.gray);
            pnf.setPreferredSize(new Dimension(new Dimension(200, 50)));
            pnf.setFont(new Font("Arial", Font.PLAIN, 15));
            pnf.setAlignmentX(RIGHT_ALIGNMENT);
            if (i != 0)
                pnf.setVisible(false);
            centerPanel1.add(pnf);
            playerNameInputs[i] = pnf;
        }

        JPanel centerPanel2 = new JPanel();
        centerPanel2.setLayout(new BoxLayout(centerPanel2, BoxLayout.PAGE_AXIS));

        for (int i = 0; i < playerButtons.length; i++) {
            JButton pBtn = new JButton("Switch to Computer Player");
            pBtn.setFont(new Font("Arial", Font.BOLD, 9));
            pBtn.setForeground(Color.ORANGE);
            pBtn.setBackground(Color.red);
            pBtn.setMaximumSize(new Dimension(150, 50));
            pBtn.addActionListener(e -> {
                int playerButtonIndex = IntStream.range(0, 4)
                        .filter(idx -> e.getSource() == playerButtons[idx])
                        .findFirst().orElse(-1);


                if (playerButtonIndex != -1) {
                    clickPlayerButton(playerButtonIndex);
                }
            });
            pBtn.setVisible(false);
            centerPanel2.add(pBtn);
            playerButtons[i] = pBtn;
        }
        playerButtons[0].setVisible(true); // first button should be visible initially

        centerPanel2.setPreferredSize(new Dimension(140, 200));
        
        // center panel
        addPlayerButton = new JButton("Add Player");
        addPlayerButton.setPreferredSize(new Dimension(100, 30));
        addPlayerButton.setFont(new Font("Arial", Font.PLAIN, 15));
        addPlayerButton.setForeground(Color.GREEN);
        addPlayerButton.setBackground(Color.darkGray);
        addPlayerButton.addActionListener(e -> addPlayer());
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new FlowLayout());
        centerPanel.add(centerPanel3);
        centerPanel.add(centerPanel1);
        centerPanel.add(centerPanel2);
        centerPanel.add(addPlayerButton);




        // centerPanel1.setBorder(BorderFactory.createLineBorder(Color.black));
        // centerPanel2.setBorder(BorderFactory.createLineBorder(Color.black));
        // centerPanel3.setBorder(BorderFactory.createLineBorder(Color.black));
        // bottomPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        // lower center panel - (components for choosing game difficulty)
        JPanel centerPanel_Lower = new JPanel();
        String difficulty[] = {"Easy", "Hard"};
        
        ai_difficultyBox = new JComboBox<>(difficulty);
        ai_difficultyBox.setPreferredSize(new Dimension(100,30));
        ai_difficultyBox.setFont(new Font("Arial", Font.PLAIN, 15));

        JLabel gameDifficulty_label = new JLabel("Computer Player difficulty: ");
        
        gameDifficulty_label.setForeground(Color.WHITE);
        gameDifficulty_label.setFont(new Font("Arial", Font.PLAIN, 15));
        gameDifficulty_label.setPreferredSize(new Dimension(200,30));  
        
        centerPanel_Lower.add(gameDifficulty_label);
        centerPanel_Lower.add(ai_difficultyBox);
        
        // whole center panel        
        JPanel centerPanel_Whole = new JPanel();
        centerPanel_Whole.setLayout(new GridLayout(2,0));
        centerPanel_Whole.add(centerPanel);
        centerPanel_Whole.add(centerPanel_Lower);

        // centerPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        // centerPanel_Lower.setBorder(BorderFactory.createLineBorder(Color.black));
        // mainPanel
        JPanel mainPanel;
        try {
            Image bgImage = ImageIO.read(new File(BG_IMAGE_PATH));
            mainPanel = new ImgPanel(bgImage);


            // make the panels transparent, so we can see the background image
            Arrays.asList(topPanel, centerPanel, bottomPanel, centerPanel1, centerPanel2, centerPanel3, centerPanel_Lower, centerPanel_Whole ,bottomPanel1, bottomPanel2)
                    .forEach(jPanel -> jPanel.setOpaque(false));


        } catch (Exception e) {
            System.out.println("Failed to load setup game image : " + e.getMessage());
            mainPanel = new JPanel();
        }
        mainPanel.setLayout(new BorderLayout());


        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        mainPanel.add(centerPanel_Whole, BorderLayout.CENTER);




        setContentPane(mainPanel);


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        setVisible(true);




        players = new ArrayList<>();
        players.add(new HumanPlayer("Player 1"));
        player_counter = 1;
    }


    private void removePlayer() {
        if (player_counter == 2) {
            removePlayerButton.setVisible(false);
        } else if (player_counter == 4) {
            addPlayerButton.setVisible(true);
        }
        playerButtons[player_counter - 1].setVisible(false);
        playerNameInputs[player_counter - 1].setVisible(false);
        playerLabels[player_counter - 1].setVisible(false);
        players.remove(player_counter-1);
        player_counter--;
    }


    private void addPlayer() 
    {
        if (player_counter < 4) {
            player_counter++;
            if (player_counter == 2) {
                removePlayerButton.setVisible(true);
            } else if (player_counter == 4) {
                addPlayerButton.setVisible(false);
                createGameButton.setVisible(true);
            }
            playerButtons[player_counter - 1].setVisible(true);
            playerNameInputs[player_counter - 1].setVisible(true);
            playerLabels[player_counter - 1].setVisible(true);
            players.add(new HumanPlayer("Player " + (player_counter)));
        }
    }


    private void backToMainMenu() {
        dispose();
        new MainMenu();
    }

    private void createGame() {
        int gameDifficulty = 0; // game difficulty variable, 0 = easy, 1 = hard
        String difficulty = "" + ai_difficultyBox.getItemAt(ai_difficultyBox.getSelectedIndex());

        if (difficulty.equals("Hard")){ gameDifficulty = 1;  }    
       
        // set player's id before starting
        for(int i = 0; i < players.size(); i++)
        {
            players.get(i).player_id = i+1;
        }


        dispose();        
        new GameWindow(players, gameDifficulty);
    }


    private void clickPlayerButton(int index) {
        String name = playerNameInputs[index].getText();
        //players[index] = new HumanPlayer(name);




        if (players.get(index) instanceof HumanPlayer)
        {
            playerButtons[index].setText("Switch to Human Player");
            players.set(index, new IntelligentPlayer("Computer Player "+index));
        }


        else
        {
            playerButtons[index].setText("Switch to Computer Player");
            players.set(index, new HumanPlayer("Player "+index));
        }


    }
}

