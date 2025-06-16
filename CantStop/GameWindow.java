import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GameWindow extends JFrame {

    private static final String BG_IMAGE_PATH = "ingamebg.jpg";
    private final JPanel glassPane;
    private final JLabel turnLabel, select_label;
    
    private final DicePanel dicePanel;
    
    private final Game currentGame;
    private final List<CombinationPanel> combinationPanels;
    private GridSquare[][] gridSquares;
    private Image bgImage;

    public GameWindow(ArrayList<Player> players, int gameDifficulty) {
        
        dicePanel = new DicePanel(3000);
        dicePanel.setCallback(this::handleRoll);
        int rows = 15;
        int columns = 15;
        this.setSize(1000, 900);
        
        combinationPanels = new ArrayList<>();

        currentGame = new Game(players, gameDifficulty);
        currentGame.setDifficulty(gameDifficulty);
        currentGame.setRunnerCounter(0);
        currentGame.setCurrentTurnPlayer(players.get(0));
        loadAssets();
        ImgPanel bgPanel = new ImgPanel(bgImage);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        JPanel bottomPanel0 = new JPanel();
        bottomPanel0.setLayout(new BorderLayout());
        bottomPanel0.setBorder(BorderFactory.createLineBorder(Color.black));

        JPanel bottomPanel1 = new JPanel();
        bottomPanel1.setBorder(BorderFactory.createLineBorder(Color.black));
        bottomPanel1.setPreferredSize(new Dimension(300, 50));

        JPanel bottomPanel2 = new JPanel();
        bottomPanel2.setLayout(new GridLayout(rows, columns));
        bottomPanel2.setSize(200, 200);
        bottomPanel2.setBorder(BorderFactory.createLineBorder(Color.black));

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
        rightPanel.setPreferredSize(new Dimension(300, 200));
        rightPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        rightPanel.add(dicePanel);

        JButton topButton = new JButton("Options");

        topPanel.add(topButton);
        // create board
        createBoard(bottomPanel2, rows, columns);

        bottomPanel0.add(bottomPanel2, BorderLayout.CENTER);

        JButton endTurnButton = new JButton("End Turn");
        endTurnButton.setAlignmentX(CENTER_ALIGNMENT);
        endTurnButton.setPreferredSize(new Dimension(50, 50));

        JPanel combinationsPanel = new JPanel();
        combinationsPanel.setLayout(new GridLayout(3, 2, 10, 10));
        for (int i = 0; i < 6; i++) {
            CombinationPanel combPanel = new CombinationPanel(0);
            combPanel.setAlignmentX(CENTER_ALIGNMENT);
            combPanel.setFont((new Font("Ariel", Font.PLAIN, 30)));
            combPanel.setVisible(false);
            MouseAdapter combMouseAdapter = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent mevt) {
                    Object selected = mevt.getSource();
                    Player currPlayer = currentGame.getCurrentTurnPlayer();
                    if (selected instanceof CombinationPanel && currPlayer.getSelectionCounter() < 2) {

                        CombinationPanel currCombinationPanel = (CombinationPanel) selected;
                        int column = currCombinationPanel.getSum();
                        // check if column is disabled
                        if (currentGame.game_claimedColumns.contains(column))
                        {
                            select_label.setText("Column is disabled");
                            select_label.setForeground(Color.RED);
                            return;
                        }  
                        currPlayer.setSelectionCounter(currPlayer.getSelectionCounter() + 1);
                        select_label.setText("You selected "+ column);
                        select_label.setForeground(new Color( 0, 153, 0));
                        placeRunners(column);
                    } else if (selected instanceof CombinationPanel && currPlayer.getSelectionCounter() == 2) {
                        select_label.setText("Invalid Selection");
                        select_label.setForeground(Color.RED);
                    }
                    }
            };
            
            combPanel.addMouseListener(combMouseAdapter);
            combinationPanels.add(combPanel);
            combinationsPanel.add(combPanel);
        }

        turnLabel = new JLabel(currentGame.players().get(0).getName());
        turnLabel.setAlignmentX(CENTER_ALIGNMENT);
        turnLabel.setFont(new Font("Ariel", Font.PLAIN, 30));

        select_label = new JLabel("");
        select_label.setAlignmentX(CENTER_ALIGNMENT);
        select_label.setFont(new Font("Ariel", Font.PLAIN, 30));

        rightPanel.add(turnLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        rightPanel.add(Box.createVerticalGlue());

        rightPanel.add(combinationsPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 150)));
        
        rightPanel.add(select_label);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        rightPanel.add(endTurnButton);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 150)));

        bottomPanel0.setOpaque(false);
        bottomPanel1.setOpaque(false);
        bottomPanel2.setOpaque(false);
        
        // now add the top and bottom panels to the main frame
        bgPanel.setLayout(new BorderLayout());
        bgPanel.add(topPanel, BorderLayout.NORTH);
        bgPanel.add(bottomPanel0, BorderLayout.CENTER);
        bgPanel.add(new JScrollPane(rightPanel), BorderLayout.EAST);
        setContentPane(bgPanel);

        glassPane = (JPanel) this.getGlassPane();
        glassPane.addMouseListener(new MouseAdapter() {
        });
        glassPane.setLayout(new GridBagLayout());

        topButton.addActionListener(e -> glassPane.setVisible(true));
        endTurnButton.addActionListener(e -> endTurn());

        JPanel glassJPanel = new JPanel();
        glassJPanel.setLayout(new BoxLayout(glassJPanel, BoxLayout.PAGE_AXIS));
        glassJPanel.setPreferredSize(new Dimension(300, 420));
        glassJPanel.setBackground(Color.red);
        glassJPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        glassJPanel.setAlignmentX(CENTER_ALIGNMENT);
        glassJPanel.setAlignmentY(CENTER_ALIGNMENT);

        // buttons to save or exit game
        JButton returnToGameButton = new JButton("Return to game");
        returnToGameButton.addActionListener(e -> glassPane.setVisible(false));

        JButton exitGameButton = new JButton("Return to Main Menu");
        exitGameButton.addActionListener(e -> {
            dispose();
            new MainMenu();
        });

        JButton saveGameButton = new JButton("Save game");
        saveGameButton.addActionListener(e -> this.saveGame());

        JButton[] buttons = {returnToGameButton, saveGameButton, exitGameButton};
        arrangeButtons(buttons);
        
        //add components to glassPanel
        glassJPanel.add(Box.createVerticalGlue());
        glassJPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        glassJPanel.add(returnToGameButton);
        glassJPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        glassJPanel.add(exitGameButton);
        glassJPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        glassJPanel.add(saveGameButton);
        glassJPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        glassPane.add(glassJPanel);

        // housekeeping : behaviour
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setVisible(true);
    }

    //constructor for loading the game
    public GameWindow(Game gameData) {
        this.currentGame = gameData;
        int gameDifficulty = gameData.getDifficulty();
        dicePanel = new DicePanel(3000);
        dicePanel.setCallback(this::handleRoll);
        int rows = 15;
        int columns = 15;
        this.setSize(1000, 900);
        combinationPanels = new ArrayList<>();
        loadAssets();
        ImgPanel bgPanel = new ImgPanel(bgImage);
        
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        JPanel bottomPanel0 = new JPanel();
        bottomPanel0.setLayout(new BorderLayout());
        bottomPanel0.setBorder(BorderFactory.createLineBorder(Color.black));

        JPanel bottomPanel1 = new JPanel();
        bottomPanel1.setBorder(BorderFactory.createLineBorder(Color.black));
        bottomPanel1.setPreferredSize(new Dimension(300, 50));

        JPanel bottomPanel2 = new JPanel();
        bottomPanel2.setLayout(new GridLayout(rows, columns));
        bottomPanel2.setSize(200, 200);
        bottomPanel2.setBorder(BorderFactory.createLineBorder(Color.black));

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
        rightPanel.setPreferredSize(new Dimension(300, 200));
        rightPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        rightPanel.add(dicePanel);

        JButton topButton = new JButton("Options");

        topPanel.add(topButton);

        // create board
        createBoard(gameData.getGridData(), bottomPanel2);

        int columnSize = currentGame.game_claimedColumns.size(); 
        for (int i = 0; i < columnSize; i++)
        {
            deactivateColumn(currentGame.game_claimedColumns.get(i));
        }

        bottomPanel0.add(bottomPanel2, BorderLayout.CENTER);

        JButton endTurnButton = new JButton("End Turn");
        endTurnButton.setAlignmentX(CENTER_ALIGNMENT);
        endTurnButton.setPreferredSize(new Dimension(50, 50));

        JPanel combinationsPanel = new JPanel();
        combinationsPanel.setLayout(new GridLayout(3, 2));
        MouseAdapter combMouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mevt) {
                Object selected = mevt.getSource();
                Player currPlayer = currentGame.getCurrentTurnPlayer();
                if (selected instanceof CombinationPanel && currPlayer.getSelectionCounter() < 2) {

                    CombinationPanel currCombinationPanel = (CombinationPanel) selected;
                        int column = currCombinationPanel.getSum();

                        // check if column is disabled
                        if (currentGame.game_claimedColumns.contains(column))
                        {
                            select_label.setText("Column is disabled");
                            select_label.setForeground(Color.RED);
                            return;
                        }  
                    currPlayer.setSelectionCounter(currPlayer.getSelectionCounter() + 1);
                    select_label.setText("You selected " + column);
                    select_label.setForeground(new Color( 0, 153, 0));
                    placeRunners(column);
                } else if (selected instanceof CombinationPanel && currPlayer.getSelectionCounter() == 2) {
                    select_label.setText("Invalid Selection");
                    select_label.setForeground(Color.RED);
                    
                }
                
            }
        };
        
        for (int i = 0; i < 6; i++) {
            CombinationPanel combPanel = new CombinationPanel(0);
            combPanel.setAlignmentX(CENTER_ALIGNMENT);
            combPanel.setFont((new Font("Ariel", Font.PLAIN, 30)));
            combPanel.setVisible(false);
            combPanel.addMouseListener(combMouseAdapter);
            combinationPanels.add(combPanel);
            combinationsPanel.add(combPanel);
        }

        turnLabel = new JLabel(currentGame.getCurrentTurnPlayer().getName());
        turnLabel.setAlignmentX(CENTER_ALIGNMENT);
        turnLabel.setFont(new Font("Ariel", Font.PLAIN, 30));

        select_label = new JLabel("");
        select_label.setAlignmentX(CENTER_ALIGNMENT);
        select_label.setFont(new Font("Ariel", Font.PLAIN, 30));

        rightPanel.add(turnLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        rightPanel.add(Box.createVerticalGlue());

        rightPanel.add(combinationsPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 150)));
        rightPanel.add(select_label);
        rightPanel.add(endTurnButton);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 150)));

        bottomPanel0.setOpaque(false);
        bottomPanel1.setOpaque(false);
        bottomPanel2.setOpaque(false);


        // now add the top and bottom panels to the main frame
        bgPanel.setLayout(new BorderLayout());
        bgPanel.add(topPanel, BorderLayout.NORTH);
        bgPanel.add(bottomPanel0, BorderLayout.CENTER);
        bgPanel.add(new JScrollPane(rightPanel), BorderLayout.EAST);
        setContentPane(bgPanel);

        glassPane = (JPanel) this.getGlassPane();
        glassPane.addMouseListener(new MouseAdapter() {
        });
        glassPane.setLayout(new GridBagLayout());

        topButton.addActionListener(e -> glassPane.setVisible(true));
        endTurnButton.addActionListener(e -> endTurn());

        JPanel glassJPanel = new JPanel();
        glassJPanel.setLayout(new BoxLayout(glassJPanel, BoxLayout.PAGE_AXIS));
        glassJPanel.setPreferredSize(new Dimension(300, 420));
        glassJPanel.setBackground(Color.red);
        glassJPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        glassJPanel.setAlignmentX(CENTER_ALIGNMENT);
        glassJPanel.setAlignmentY(CENTER_ALIGNMENT);

        // buttons to save or exit game
        JButton returnToGameButton = new JButton("Return to game");
        returnToGameButton.addActionListener(e -> glassPane.setVisible(false));

        JButton exitGameButton = new JButton("Return to Main Menu");
        exitGameButton.addActionListener(e -> {
            dispose();
            new MainMenu();
        });

        JButton saveGameButton = new JButton("Save game");
        saveGameButton.addActionListener(e -> this.saveGame());

        JButton[] buttons = {returnToGameButton, saveGameButton, exitGameButton};
        arrangeButtons(buttons);

        //add components to glassPanel
        glassJPanel.add(Box.createVerticalGlue());
        glassJPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        glassJPanel.add(returnToGameButton);
        glassJPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        glassJPanel.add(exitGameButton);
        glassJPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        glassJPanel.add(saveGameButton);
        glassJPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        glassPane.add(glassJPanel);

        // housekeeping : behaviour
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setVisible(true);
    }

    private void saveGame() {
        int rows = gridSquares.length;
        int cols = gridSquares[0].length;
        GridSquareData[][] data = new GridSquareData[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = gridSquares[i][j].getData();
            }
        }
        currentGame.setGridData(data);
        currentGame.saveGame();
    }
    
    private void createBoard(GridSquareData[][] gridData, JPanel boardPanel) {
        int rows = gridData.length;
        int columns = gridData[0].length;
        gridSquares = new GridSquare[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                gridSquares[i][j] = new GridSquare(gridData[i][j]);
                boardPanel.add(gridSquares[i][j]);
            }
        }
        for (int x = 0; x < 14; x++) {
            if (x < 5) {
                for (int z = 6 - x; z <= 6 + x; z++) {
                    gridSquares[x + 1][z + 1].setSize(300, 300);
                    gridSquares[x + 1][z + 1].setBorder(BorderFactory.createLineBorder(Color.WHITE));
                }
            }

            if (x >= 5 && x <= 7) {
                for (int z = 6 - 5; z <= 6 + 5; z++) {
                    gridSquares[x + 1][z + 1].setSize(300, 300);
                    gridSquares[x + 1][z + 1].setBorder(BorderFactory.createLineBorder(Color.WHITE));
                }
            }
            if (x >= 8 && x <= 12) {
                for (int z = x - 6; z <= 18 - x; z++) {
                    gridSquares[x + 1][z + 1].setSize(300, 300);
                    gridSquares[x + 1][z + 1].setBorder(BorderFactory.createLineBorder(Color.WHITE));
                }
            }
        }
    }

    private void createBoard(JPanel boardPanel, int rows, int columns) {
        gridSquares = new GridSquare[rows][columns];
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < columns; y++) {
                gridSquares[x][y] = new GridSquare(x, y);
                gridSquares[x][y].setColor(1);
                boardPanel.add(gridSquares[x][y]);
            }
        }
        for (int x = 0; x < 14; x++) {
            if (x < 5) {
                for (int z = 6 - x; z <= 6 + x; z++) {
                    gridSquares[x + 1][z + 1].setColor(2);
                    gridSquares[x + 1][z + 1].setSize(300, 300);
                    gridSquares[x + 1][z + 1].setBorder(BorderFactory.createLineBorder(Color.WHITE));
                    gridSquares[x + 1][z + 1].allowMarkers();
                }
            }

            if (x >= 5 && x <= 7) {
                for (int z = 6 - 5; z <= 6 + 5; z++) {
                    gridSquares[x + 1][z + 1].setColor(2);
                    gridSquares[x + 1][z + 1].setSize(300, 300);
                    gridSquares[x + 1][z + 1].setBorder(BorderFactory.createLineBorder(Color.WHITE));
                    gridSquares[x + 1][z + 1].allowMarkers();
                }
            }
            if (x >= 8 && x <= 12) {
                for (int z = x - 6; z <= 18 - x; z++) {
                    gridSquares[x + 1][z + 1].setColor(2);
                    gridSquares[x + 1][z + 1].setSize(300, 300);
                    gridSquares[x + 1][z + 1].setBorder(BorderFactory.createLineBorder(Color.WHITE));
                    gridSquares[x + 1][z + 1].allowMarkers();
                }
            }
        }
        // column numbers
        int r = 5;
        for (int c = 2; c <= 12; c++) {
            gridSquares[Math.abs(r)][c].setHeader(c);
            r--;
        }

        // set TopTile gridsquares (top tile in a column)
        r = 6;
        for (int c = 2;c <= 12; c++){
            gridSquares[Math.abs(r)][c].setTopTile();
            r--;
            if(r==1){r=-1;}
        }

    }

    public void loadAssets() {
        try {
            bgImage = ImageIO.read(new File(BG_IMAGE_PATH));
        } catch (Exception e) {
            System.out.println("failed to load in-game background image; " + e.getMessage());
        }
    }

    public List<Integer> giveFail() {
        List<Integer> rollValues = dicePanel.getRollValues();
        while (!currentGame.getCurrentTurnPlayer().getFailCombination()) {
            if (!currentGame.runnerCoordinates().contains(rollValues.get(0) + rollValues.get(1))
                && !currentGame.runnerCoordinates().contains(rollValues.get(1) + rollValues.get(2))
                && !currentGame.runnerCoordinates().contains(rollValues.get(2) + rollValues.get(3))
                && !currentGame.runnerCoordinates().contains(rollValues.get(0) + rollValues.get(2))
                && !currentGame.runnerCoordinates().contains(rollValues.get(1) + rollValues.get(3))
                && !currentGame.runnerCoordinates().contains(rollValues.get(0) + rollValues.get(3))) {
                currentGame.getCurrentTurnPlayer().setFailCombination(true);
            } else {
                rollValues = dicePanel.rollInstantly();
            }

        }
        return rollValues;
    }

    public void changeCombinationLabels(List<Integer> rollValues) {
        int i1 = 0, i2 = 1;
        for (CombinationPanel combinationPanel : combinationPanels) {
            combinationPanel.setValues(rollValues.get(i1), rollValues.get(i2));
            if (++i2 == rollValues.size()) {
                i2 = 0;
                i1++;
            }
            combinationPanel.setVisible(true);
        }
    }
    
    public void placeRunners(int column) {
        boolean runnerFound = false;
        if (1 < column && column < 8) {
            for (int r = 8 - column; r <= column + 6; r++) {
                
                if (gridSquares[r][column].containsRunner() ) {
                    gridSquares[r][column].removeRunner();
                    gridSquares[r - 1][column].setRunner();
                    currentGame.runnerPositionsMap().put(column, r - 1);
                    runnerFound = true;
                    break;
                }
                if(gridSquares[r][column].containsMarker(currentGame.currentTurnPlayer.player_id)){
                    gridSquares[r - 1][column].setRunner();
                    currentGame.runnerPositionsMap().put(column, r - 1);
                    runnerFound = true;
                }       
            }
            if (!runnerFound && currentGame.getRunnerCounter() < 3) {
                gridSquares[column + 6][column].setRunner();
                currentGame.runnerCoordinates().add(column);
                currentGame.runnerPositionsMap().put(column, column + 6);
                currentGame.setRunnerCounter(currentGame.getRunnerCounter() + 1);
            }
        } else if (7 < column && column < 13) {
            for (int r = column - 6; r <= (20 - column); r++) {
                if (gridSquares[r][column].containsRunner() ) {
                    gridSquares[r - 1][column].setRunner();
                    currentGame.runnerPositionsMap().put(column, r - 1);
                    gridSquares[r][column].removeRunner();
                    runnerFound = true;
                    break;
                }
                if(gridSquares[r][column].containsMarker(currentGame.currentTurnPlayer.player_id)){
                    gridSquares[r - 1][column].setRunner();
                    currentGame.runnerPositionsMap().put(column, r - 1);
                    runnerFound = true;
                } 
            }
            if (!runnerFound && currentGame.getRunnerCounter() < 3) {
                gridSquares[20 - column][column].setRunner();
                currentGame.runnerCoordinates().add(column);
                currentGame.runnerPositionsMap().put(column, 20 - column);
                currentGame.setRunnerCounter(currentGame.getRunnerCounter() + 1);
            }
        }
    }

    public void selectionWork(int value) {
        int delay = 3000;
        Timer ourTime = new Timer(delay, e -> {
            if (currentGame.getCurrentTurnPlayer().getSelectionCounter() < 2) {
                currentGame.getCurrentTurnPlayer().setSelectionCounter(currentGame.getCurrentTurnPlayer().getSelectionCounter() + 1);
                CombinationPanel currCombinationPanel = combinationPanels.get(value);
                int column = currCombinationPanel.getSum();
                int d = 2000;
                Timer miniDelay = new Timer(d, f -> {
                });
                miniDelay.start();
                miniDelay.setRepeats(false);
                placeRunners(column);
            }
        });
        ourTime.start();
        ourTime.setRepeats(false);
    }

    public void autoSelection() {
        Random rand = new Random();
        int val1 = rand.nextInt(6);
        int val2 = rand.nextInt(6);
        while (val1 == val2) {
            val1 = rand.nextInt(6);
            val2 = rand.nextInt(6);
        }
        selectionWork(val1);
        selectionWork(val2);
        currentGame.getCurrentTurnPlayer().setRolledDice(currentGame.getCurrentTurnPlayer().getRolledDice() + 1);
        int delay = 3000;
        Timer ourTime = new Timer(delay, e -> simulateDiceRoll());
        ourTime.start();
        ourTime.setRepeats(false);
    }

    private void simulateDiceRoll() {
        select_label.setText("");  
        dicePanel.setCallback(this::handleRoll);
        dicePanel.roll();
    }

    public void startNewPlayerTurn() {
        currentGame.runnerCoordinates().clear();
        currentGame.runnerPositionsMap().clear();
        currentGame.getCurrentTurnPlayer().setRolledDice(0);
        currentGame.getCurrentTurnPlayer().setSelectionCounter(0);
        currentGame.getCurrentTurnPlayer().setFailCombination(false);
        currentGame.setRunnerCounter(0);
        for (int r = 0; r < 15; r++) {
            for (int c = 0; c < 15; c++) {
                if (gridSquares[r][c].containsRunner())
                    gridSquares[r][c].removeRunner();
            }
        }
        currentGame.nextTurn();
        turnLabel.setText(currentGame.getCurrentTurnPlayer().getName());
        
        // Check if new current player is AI, then let it auto play
        if (currentGame.getCurrentTurnPlayer() instanceof IntelligentPlayer) {
            int delay = 3000;
            Timer ourTime = new Timer(delay, e -> {
                currentGame.getCurrentTurnPlayer().setRolledDice(
                currentGame.getCurrentTurnPlayer().getRolledDice() + 1);
                simulateDiceRoll();
            });
            ourTime.start();
            ourTime.setRepeats(false);
        }
    }

    public void placeMarkers() {
        int p = 1;
        for (int i : currentGame.runnerPositionsMap().keySet()) 
        {
            p = currentGame.getCurrentTurnPlayer().player_id;

            // check if runner is in top tile
            if (gridSquares[currentGame.runnerPositionsMap().get(i)][i].isTopTile())
            {
                // method to record total claimed column
                currentGame.gameClaimColumn(i);
                // call a method to set the column as inactive (pass in column)
                deactivateColumn(i);
                // check if curr player has three claimed columns
                boolean win_con = (currentGame.getCurrentTurnPlayer().get_ColumnsWon().size() >= 3);
 
                // current player won
                if (win_con){
                    currentGame.PlayerWon();
                    if (currentGame.get_numOfPlayers() <= 1) // one player remaining 
                    {
                        alert("Player "+p+" won!,  Game has ended");
                    }                    
                    else
                    {
                        // display player won message
                        alert("Player "+p+" won!");
                    }
                }
            }
            // else just remove runner and place marker
            else
            {   
                // if the column has a marker
                if( currentGame.getCurrentTurnPlayer().player_markerCoordinates.containsKey(i))
                {
                    int m_row = currentGame.getCurrentTurnPlayer().player_markerCoordinates.get(i);
                    gridSquares[m_row][i].removeMarker(p);
                }

                gridSquares[currentGame.runnerPositionsMap().get(i)][i].removeRunner();
                gridSquares[currentGame.runnerPositionsMap().get(i)][i].saveRunner(p);
                gridSquares[currentGame.runnerPositionsMap().get(i)][i].setMarker(p);
                currentGame.setCurrentPlayerMarker(i, currentGame.runnerPositionsMap().get(i));
            }

        }   
        
        currentGame.runnerPositionsMap().clear();
        currentGame.runnerCoordinates().clear();
        currentGame.setRunnerCounter(0);
    }

    public void deactivateColumn(int column){
        // deactivate column 
        if ( 1 < column && column < 8)
        {
            for (int row = 8 - column; row <= column + 6; row++)
            {
                System.out.println("Deactivating " + row);
                gridSquares[row][column].deactivateTile();
            }
            //gridSquares[row][column].removeRunner();
        }
        else if (7<column && column < 13)
        {
            for (int row = column - 6; row <= (20 - column); row++)
            {
                gridSquares[row][column].deactivateTile();
            }
        }
    }


    public void endTurn() {
        combinationPanels.forEach(combinationPanel -> combinationPanel.setVisible(false));
        placeMarkers();
        select_label.setText(" ");
        startNewPlayerTurn();
    }


    public void arrangeButtons(JButton[] buttons) {
        for (JButton button : buttons) {
            button.setAlignmentX(CENTER_ALIGNMENT);
        }
    }

    public void handleRoll(List<Integer> rollValues) {
        Player currPlayer = currentGame.getCurrentTurnPlayer();
        currPlayer.setRolledDice(currPlayer.getRolledDice() + 1);
        currPlayer.setSelectionCounter(0);
        List<Integer> diceValues = rollValues;
        
        if (currentGame.getRunnerCounter() == 3 && !currentGame.runnerCoordinates().contains(diceValues.get(0) + diceValues.get(1))
            && !currentGame.runnerCoordinates().contains(diceValues.get(1) + diceValues.get(2))
            && !currentGame.runnerCoordinates().contains(diceValues.get(2) + diceValues.get(3))
            && !currentGame.runnerCoordinates().contains(diceValues.get(0) + diceValues.get(2))
            && !currentGame.runnerCoordinates().contains(diceValues.get(1) + diceValues.get(3))
            && !currentGame.runnerCoordinates().contains(diceValues.get(0) + diceValues.get(3))) {
            currPlayer.setFailCombination(true);
        }

        if (currPlayer.getRolledDice() > 4 && !currPlayer.getFailCombination() && currentGame.getRunnerCounter() == 3 && currPlayer instanceof HumanPlayer) {
            diceValues = giveFail();
        }

        if (currPlayer.getRolledDice() > 6 && currentGame.getRunnerCounter() == 3 && currPlayer instanceof IntelligentPlayer) {
            Random rand = new Random();
            int possibility = rand.nextInt(2);
            if (possibility == 0) {
                diceValues = giveFail();
            } else {
                endTurn();
            }
        }
        changeCombinationLabels(diceValues);

        if (currPlayer.getFailCombination()) {
            turnLabel.setText(currPlayer.getName() + " failed");
            int delay = 3000;
            Timer ourTime = new Timer(delay, e -> startNewPlayerTurn());
            ourTime.start();
            ourTime.setRepeats(false);
        } else if (currPlayer instanceof IntelligentPlayer) {
            autoSelection();
        }
    }

    private void alert(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}
