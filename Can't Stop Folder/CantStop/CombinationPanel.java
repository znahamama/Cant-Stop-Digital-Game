import java.awt.*;
import javax.swing.*;

public class CombinationPanel extends JPanel {
    //    private int value;
    private int val1;
    private int val2;
    private boolean invalid;

    /**
     * Constructor for objects of class Combination
     */
    public CombinationPanel(int val1, int val2) {
        super();
        this.val1 = val1;
        this.val2 = val2;
        setPreferredSize(new Dimension(80, 100));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setBorder(BorderFactory.createRaisedBevelBorder());

    }

    public CombinationPanel(int val) {
        this(val, val);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int dieSize = getHeight() - 10;
        int y = getHeight() / 2;
        int x1 = dieSize / 5 + dieSize / 2;
        int x2 = x1 + dieSize + dieSize / 5;

        DrawDiceUtil.drawDie(x1, y, val1, dieSize, g);
        DrawDiceUtil.drawDie(x2, y, val2, dieSize, g);
    }

    public void setColor(int decider) {
        switch (decider) {
            case 1 -> this.setBackground(Color.orange);
            case 2 -> this.setBackground(Color.BLUE);
            case 3 -> this.setBackground(Color.PINK);
            case 4 -> this.setBackground(Color.GREEN);
            case 5 -> this.setBackground(Color.RED);
            case 6 -> this.setBackground(Color.yellow);
        }
    }

    public void setValues(int val1, int val2) {
        this.val1 = val1;
        this.val2 = val2;
        repaint();
    }

    public int getSum() {
        return val1 + val2;
    }

    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
        repaint();
    }
}
