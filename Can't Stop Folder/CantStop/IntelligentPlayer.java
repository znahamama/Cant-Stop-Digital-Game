import java.util.ArrayList;

public class IntelligentPlayer extends Player
{
    private int rows,columns,counter;
    private boolean playerRisk;
    private int selectionCounter;
    private int runnerCounter;
    private int rolledDice;
    private String name;
    private boolean failCombination;
    private ArrayList<Integer> runnerCoordinates;
    
    public IntelligentPlayer(String name)
    {
        super(name);
    }
     
}

