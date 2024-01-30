import javax.swing.*;
import java.awt.*;
import java.util.Arrays;


/*
 *  A GUI component
 *
 *  A simple extension of JPanel which records its
 *  coordinates in xcoord and ycoord, NOT in 'x' and 'y'.
 *  Why not? Because 'x' and 'y' are already attributes of
 *  the panel (super) class which say where to draw it in the window.
 *
 *  The game grid and allows the background colour to be set with ease.
 *
 *  @author mhatcher
 */
public class GridSquare extends JPanel {


    private final GridSquareData data;
    private JPanel p1_Panel, p2_Panel, p3_Panel, p4_Panel;
    private boolean activeStatus;
    private boolean top_tile;
    


    // constructor takes the x and y coordinates of this square
    public GridSquare(int xcoord, int ycoord) {
        super();
        data = new GridSquareData(xcoord, ycoord);
        this.setSize(100, 100);
        activeStatus = true;
        top_tile = false;
    }


    public GridSquare(GridSquareData data) {
        this.data = data;
        if (data.isAllowedMarkers()) {
            allowMarkers();
        }
        setColor(data.getColorDecider());
        setHeader(data.getHeaderNum());
        if (data.getRunnerStatus()) setRunner();
        int bound = data.runners().length + 1;
        for (int i = 1; i < bound; i++) {
            if (data.runners()[i - 1]) {
                saveRunner(i);
            }
        }


    }




    public void deactivateTile()
    {
        this.activeStatus = false;
        // hide panels (player marker)
        p1_Panel.setVisible(false);
        p2_Panel.setVisible(false);
        p3_Panel.setVisible(false);
        p4_Panel.setVisible(false);


        this.setBackground(Color.DARK_GRAY);


    }






    public void allowMarkers() {
        data.setAllowedMarkers(true);
        this.setLayout(new GridLayout(2, 2));
        p1_Panel = new JPanel();
        p1_Panel.setVisible(false);
        p2_Panel = new JPanel();
        p2_Panel.setVisible(false);
        p3_Panel = new JPanel();
        p3_Panel.setVisible(false);
        p4_Panel = new JPanel();
        p4_Panel.setVisible(false);
        this.add(p1_Panel);
        this.add(p2_Panel);
        this.add(p3_Panel);
        this.add(p4_Panel);


    }




    public void saveRunner(int p) {
        data.runners()[p - 1] = true;
        if (p == 1) {
            JLabel pLabel = new JLabel("1");
            this.p1_Panel.setBackground(Color.CYAN);
            this.p1_Panel.add(pLabel);
            this.p1_Panel.setVisible(true);
        } else if (p == 2) {
            JLabel pLabel = new JLabel("2");
            this.p2_Panel.setBackground(Color.MAGENTA);
            this.p2_Panel.add(pLabel);
            this.p2_Panel.setVisible(true);
        } else if (p == 3) {
            JLabel pLabel = new JLabel("3");
            this.p3_Panel.setBackground(Color.ORANGE);
            this.p3_Panel.add(pLabel);
            this.p3_Panel.setVisible(true);
        } else if (p == 4) {
            JLabel pLabel = new JLabel("4");
            this.p4_Panel.setBackground(Color.GREEN);
            this.p4_Panel.add(pLabel);
            this.p4_Panel.setVisible(true);
        }
        //this.setBackground(Color.GREEN);
//        data.runnerStatus = false;
        data.setRunnerStatus(false);
    }




    // if the decider is even, it chooses black, otherwise white (for 'column+row' will allow a chequerboard effect)
    public void setColor(int decider) {
        data.setColorDecider(decider);
        switch (decider) {
            case 2 -> {
                this.setBackground(Color.RED);
                setOpaque(true);
            }
            case 1 ->
                //this.setBackground(Color.gray);
                    setOpaque(false);
        }
    }


    public void setHeader(int num) {
        if (num == 0) return;
        data.setHeaderNum(num);
        JLabel head = new JLabel(num + "");
        head.setFont(new Font("Ariel", Font.BOLD, 30));
        head.setForeground(Color.WHITE);
        this.add(head);
    }


    public boolean containsRunner() {
        return data.getRunnerStatus();
    }
    
    public boolean containsMarker(int id){
        return data.markerStatus(id);
    }

    public void setRunner() {
        this.setBackground(Color.WHITE);
        data.setRunnerStatus(true);
    }
    
    public void setMarker(int id){
        data.putMarker(id);
    }
    

    public void removeRunner(){
        if(activeStatus)
        {
        this.setBackground(Color.RED);
        data.setRunnerStatus(false);
        }
    }

    public void removeMarker(int p){
        if (p==1){  p1_Panel.setVisible(false);}
        else if (p==2){  p2_Panel.setVisible(false);}
        else if (p==3){  p3_Panel.setVisible(false);}
        else if (p==4){  p4_Panel.setVisible(false);}
        data.removeMarker(p);
    }


    public GridSquareData getData() {
        return data;
    }


    public void setTopTile(){
        this.top_tile = true;
    }

    public boolean isTopTile(){
        return this.top_tile;
    }
}

