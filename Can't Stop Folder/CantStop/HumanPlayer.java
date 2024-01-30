import java.util.ArrayList;

public class HumanPlayer extends Player
{
    private int rows,columns,counter;
    private boolean playerRisk;
    private int selectionCounter;
    private int runnerCounter;
    private int rolledDice;
    private String name;
    private boolean failCombination;
    private ArrayList<Integer> runnerCoordinates;

    public HumanPlayer(String name)
    {
        super(name);
    }
}

