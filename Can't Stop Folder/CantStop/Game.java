

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Game implements Serializable {
    private List<Player> players;
    private final List<Integer> runnerCoordinates;
    private final HashMap<Integer, Integer> runnerPositionsMap;
    public Player currentTurnPlayer;
    private int runnerCounter;
    public ArrayList<Integer> game_claimedColumns;
    public ArrayList<Player> players_won;
    private int number_of_players;


    private int gameDifficulty; // game difficulty values:  [0 = easy, 1 = hard]


    private GridSquareData[][] gridData;


    public Game(List<Player> players, int gameDifficulty) {
        super();
        this.players = players;
        // for now, starting turn selects first player from player list
        this.currentTurnPlayer = players.get(0);
        this.gameDifficulty = gameDifficulty;
        runnerPositionsMap = new HashMap<>();
        runnerCoordinates = new ArrayList<>();
        this.game_claimedColumns = new ArrayList<>();
        this.players_won = new ArrayList<>();
        this.number_of_players = players.size();
    }


    public int getRunnerCounter() {
        return runnerCounter;
    }


    public void setRunnerCounter(int runnerCounter) {
        this.runnerCounter = runnerCounter;
    }


    public HashMap<Integer, Integer> runnerPositionsMap() {
        return runnerPositionsMap;
    }


    public List<Integer> runnerCoordinates() {
        return runnerCoordinates;
    }


    public void setCurrentPlayerMarker(int column, int row) {
        this.currentTurnPlayer.setPlayerMarker(column, row);
    }


    public void nextTurn() {
        // Iterate over player list to find index of current player
        int cpIndex = 0;   // current player index
        for (int i = 0; i < players.size(); i++) {
            if (currentTurnPlayer == players.get(i)) {
                cpIndex = i;
            }
        }


        // change current player to next player
        cpIndex = (cpIndex + 1) % players.size();
        currentTurnPlayer = players.get(cpIndex);
    }


    // add column to list of claimed columns
    public void gameClaimColumn(int column){
        this.game_claimedColumns.add(column);
        this.currentTurnPlayer.setClaimedColumn(column);
    }


    public void PlayerWon(){
        this.players_won.add(this.currentTurnPlayer);
        this.players.remove(this.currentTurnPlayer);
        this.number_of_players = players.size();


    }






    public List<Player> players() {
        return this.players;
    }


    public Player getCurrentTurnPlayer() {
        return this.currentTurnPlayer;
    }


    public void setCurrentTurnPlayer(Player currentTurnPlayer) {
        this.currentTurnPlayer = currentTurnPlayer;
    }


    public void saveGame() {
        SaveLoadUtil.saveGame(this);
    }


    public int getDifficulty() {
        return gameDifficulty;
    }


    public void setDifficulty(int gameDifficulty) {
        this.gameDifficulty = gameDifficulty;
    }


    public GridSquareData[][] getGridData() {
        return gridData;
    }


    public void setGridData(GridSquareData[][] data) {
        this.gridData = data;
    }


    public int get_numOfPlayers(){
        return this.number_of_players;
    }




}



