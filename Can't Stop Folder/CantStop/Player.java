import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


public class Player implements Serializable {
    public HashMap<Integer, Integer> player_markerCoordinates;
    private int rows, columns, counter;
    private boolean playerRisk;
    private int selectionCounter;
    private int runnerCounter;
    private int rolledDice;
    private String name;
    private boolean failCombination;
    private ArrayList<Integer> runnerCoordinates;
    private ArrayList<Integer> Columns_won;
    public int player_id;




    public Player(String name) {
        this.name = name;
        this.rows = rows;
        this.columns = columns;
        this.counter = 0;
        this.playerRisk = false;
        this.selectionCounter = 0;
        this.runnerCounter = 0;
        this.rolledDice = 0;
        this.failCombination = false;
        runnerCoordinates = new ArrayList<>();
        player_markerCoordinates = new HashMap<>();
        Columns_won = new ArrayList<>();
        player_id = 0;
    }




    public void setPlayerMarker(int column, int row) {
        // Check if a marker is already at the column
        // if so, remove that marker


        if (player_markerCoordinates.size() != 0)
        {
            player_markerCoordinates.entrySet().removeIf(key -> key.equals(column));
        }
        player_markerCoordinates.put(column, row);
    }


    public void setClaimedColumn(int column)
    {
        this.Columns_won.add(column);
    }




    public String getName() {
        return this.name;
    }


    public int getRunnerCounter() {
        return this.runnerCounter;
    }


    public void setRunnerCounter(int value) {
        this.runnerCounter = value;
    }


    public int getSelectionCounter() {
        return this.selectionCounter;
    }


    public void setSelectionCounter(int value) {
        this.selectionCounter = value;
    }


    public boolean getFailCombination() {
        return this.failCombination;
    }


    public void setFailCombination(boolean b) {
        this.failCombination = b;
    }


    public int getRolledDice() {
        return this.rolledDice;
    }


    public void setRolledDice(int value) {
        this.rolledDice = value;
    }


    public ArrayList getRunnerCoordinates() {
        return this.runnerCoordinates;
    }
    /*for(int s =0;s<runnerCoordinates.size();s++){
                    int r = runnerPosition.get(runnerCoordinates.get(s));
                    gridSquares[r][runnerCoordinates.get(s)].saveRunner();}*/


    public ArrayList get_ColumnsWon(){
        return this.Columns_won;
    }
}





