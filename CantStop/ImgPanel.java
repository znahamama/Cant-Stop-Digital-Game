import javax.swing.*;
import java.awt.*;
public class ImgPanel extends JPanel
{
    private Image bgImage;
    public ImgPanel(Image bgImage) {
        this.bgImage = bgImage;
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(bgImage != null) {
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), null);
        }
    }
}