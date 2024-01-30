import java.io.Serializable;
import java.util.ArrayList;


public class GridSquareData implements Serializable {
    private final int xcoord, ycoord;
    // location in the grid
    private boolean runnerStatus, markerStatus;
    
    private ArrayList<Integer> markersId;

    private boolean allowedMarkers;


    private boolean[] runners;


    private int headerNum;


    private int colorDecider;


    public GridSquareData(int xcoord, int ycoord) {
        this.xcoord = xcoord;
        this.ycoord = ycoord;
        runners = new boolean[4];
        this.markersId = new ArrayList<>();
    }


    public boolean getRunnerStatus() {
        return runnerStatus;
    }
    
    public void putMarker(int id){
        markersId.add(id);
    }
    
    public boolean markerStatus(int id){
        if(markersId.contains(id)){
            return true;
        }
        else
           return false;
    }
    
    public void removeMarker(int id){
        markersId.remove(id);
    }
    
    public void setRunnerStatus(boolean runnerStatus) {
        this.runnerStatus = runnerStatus;
    }


    public boolean isAllowedMarkers() {
        return allowedMarkers;
    }


    public void setAllowedMarkers(boolean allowedMarkers) {
        this.allowedMarkers = allowedMarkers;
    }


    public boolean[] runners() {
        return runners;
    }


    public void setRunners(boolean[] runners) {
        this.runners = runners;
    }


    public int getHeaderNum() {
        return headerNum;
    }


    public void setHeaderNum(int headerNum) {
        this.headerNum = headerNum;
    }


    public int getColorDecider() {
        return colorDecider;
    }


    public void setColorDecider(int colorDecider) {
        this.colorDecider = colorDecider;
    }
}



