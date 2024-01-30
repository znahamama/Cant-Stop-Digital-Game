import java.awt.*;

public class DrawDiceUtil {
    public static void drawDot(int centerX, int centerY, int dotSize, Graphics g) {
//        int dieSize = calculateDieSize();
//        int dotSize = dieSize / 6;
        int x = centerX - dotSize / 2;
        int y = centerY - dotSize / 2;

        g.setColor(Color.BLACK);
        g.fillOval(x, y, dotSize, dotSize);
    }

    public static void drawDie(int centerX, int centerY, int val, int dieSize, Graphics g) {
        g.setColor(Color.WHITE);
//        int dieSize = calculateDieSize();
        int x = centerX - dieSize / 2;
        int y = centerY - dieSize / 2;
        int dotSize = dieSize / 6;
        g.fillRect(x, y, dieSize, dieSize);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, dieSize, dieSize);

        switch (val) {
            case 1 -> drawDot(centerX, centerY, dotSize, g);
            case 2 -> {
                int off = dieSize / 3;
                drawDot(centerX + off, centerY - off, dotSize, g);
                drawDot(centerX - off, centerY + off, dotSize, g);
            }
            case 3 -> {
                int off = dieSize / 3;
                drawDot(centerX, centerY, dotSize, g);
                drawDot(centerX + off, centerY - off, dotSize, g);
                drawDot(centerX - off, centerY + off, dotSize, g);
            }
            case 4 -> {
                int off = dieSize / 3;
                drawDot(centerX + off, centerY - off, dotSize, g);
                drawDot(centerX - off, centerY + off, dotSize, g);
                drawDot(centerX - off, centerY - off, dotSize, g);
                drawDot(centerX + off, centerY + off, dotSize, g);
            }
            case 5 -> {
                int off = dieSize / 3;
                drawDot(centerX + off, centerY - off, dotSize, g);
                drawDot(centerX - off, centerY + off, dotSize, g);
                drawDot(centerX - off, centerY - off, dotSize, g);
                drawDot(centerX + off, centerY + off, dotSize, g);
                drawDot(centerX, centerY, dotSize, g);
            }
            case 6 -> {
                int off = dieSize / 3;
                drawDot(centerX + off, centerY - off, dotSize, g);
                drawDot(centerX - off, centerY + off, dotSize, g);
                drawDot(centerX - off, centerY - off, dotSize, g);
                drawDot(centerX + off, centerY + off, dotSize, g);
                drawDot(centerX + off, centerY, dotSize, g);
                drawDot(centerX - off, centerY, dotSize, g);
            }
        }
    }

}
