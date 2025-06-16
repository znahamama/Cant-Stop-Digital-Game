import java.awt.*;
import javax.swing.*;
public class MainMenuPanel extends JPanel
{
    private Image bgImage;
    private JButton[] buttons;
    private int vGap = 50;
    private int vOff = 220;
    public MainMenuPanel(Image bgImage, JButton... buttons) {
        this.buttons = buttons;
        this.bgImage = bgImage;
        setSize(800,600);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        add(Box.createRigidArea(new Dimension(0,vOff)));
        
        for(JButton button : buttons) {
            add(Box.createRigidArea(new Dimension(0,vGap)));
            add(button);
        }
    }
    
    public MainMenuPanel(JButton... buttons) {
        this(null, buttons);
    }
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(bgImage != null)
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(),this);
    }
}