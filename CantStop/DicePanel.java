import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class DicePanel extends JPanel {
    
    private static final long DEFAULT_ROLL_DURATION = 2000;
    private static final String DICE_ROLL_SOUND_PATH = "diceroll.wav";
    private final int FRAME_DURATION = 100;
    private final int DIE_MAX_VALUE = 6;
    private final Random random;
    private Timer timer;
    private int val1, val2, val3, val4;
    private long timeElapsed;
    private Consumer<List<Integer>> callback;
    private JButton rollButton;
    public DicePanel(int rollDuration) {
        random = new Random();
        val1 = 1;
        val2 = 1;
        val3 = 1;
        val4 = 1;
        setPreferredSize(new Dimension(300, 300));
        setLayout(new BorderLayout());
        rollButton = new JButton("Roll!");
        rollButton.setPreferredSize(new Dimension(100, 50));
        rollButton.addActionListener(e -> roll());
        add(rollButton, BorderLayout.SOUTH);
//        setBackground(Color.WHITE);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int dieSize = calculateDieSize();
        int y = getHeight() / 2;
        int x1 = dieSize / 5 + dieSize / 2;
        int x2 = x1 + dieSize + dieSize / 5;
        int x3 = x2 + dieSize + dieSize / 5;
        int x4 = x3 + dieSize + dieSize / 5;
        drawDie(x1, y, val1, g);
        drawDie(x2, y, val2, g);
        drawDie(x3, y, val3, g);
        drawDie(x4, y, val4, g);
    }

    private void drawDot(int centerX, int centerY, Graphics g) {
        int dieSize = calculateDieSize();
        int dotSize = dieSize / 6;
        int x = centerX - dotSize / 2;
        int y = centerY - dotSize / 2;

        g.setColor(Color.BLACK);
        g.fillOval(x, y, dotSize, dotSize);
    }

    private void drawDie(int centerX, int centerY, int val, Graphics g) {
        g.setColor(Color.WHITE);
        int dieSize = calculateDieSize();
        int x = centerX - dieSize / 2;
        int y = centerY - dieSize / 2;
        g.fillRect(x, y, dieSize, dieSize);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, dieSize, dieSize);

        switch (val) {
            case 1 -> drawDot(centerX, centerY, g);
            case 2 -> {
                int off = dieSize / 3;
                drawDot(centerX + off, centerY - off, g);
                drawDot(centerX - off, centerY + off, g);
            }
            case 3 -> {
                int off = dieSize / 3;
                drawDot(centerX, centerY, g);
                drawDot(centerX + off, centerY - off, g);
                drawDot(centerX - off, centerY + off, g);
            }
            case 4 -> {
                int off = dieSize / 3;
                drawDot(centerX + off, centerY - off, g);
                drawDot(centerX - off, centerY + off, g);
                drawDot(centerX - off, centerY - off, g);
                drawDot(centerX + off, centerY + off, g);
            }
            case 5 -> {
                int off = dieSize / 3;
                drawDot(centerX + off, centerY - off, g);
                drawDot(centerX - off, centerY + off, g);
                drawDot(centerX - off, centerY - off, g);
                drawDot(centerX + off, centerY + off, g);
                drawDot(centerX, centerY, g);
            }
            case 6 -> {
                int off = dieSize / 3;
                drawDot(centerX + off, centerY - off, g);
                drawDot(centerX - off, centerY + off, g);
                drawDot(centerX - off, centerY - off, g);
                drawDot(centerX + off, centerY + off, g);
                drawDot(centerX + off, centerY, g);
                drawDot(centerX - off, centerY, g);
            }
        }
    }

    public void setCallback(Consumer<List<Integer>> callback) {
        this.callback = callback;
    }

    private void randomizeDice() {
        val1 = random.nextInt(DIE_MAX_VALUE) + 1;
        val2 = random.nextInt(DIE_MAX_VALUE) + 1;
        val3 = random.nextInt(DIE_MAX_VALUE) + 1;
        val4 = random.nextInt(DIE_MAX_VALUE) + 1;
    }

    private int calculateDieSize() {
        return getWidth() / 5;
    }

    public List<Integer> rollInstantly() {
        randomizeDice();
        repaint();
        return List.of(val1, val2, val3, val4);
    }

    public void roll(long rollDuration) {
        if (timer != null && timer.isRunning()) {
            System.out.println("ALREADY RUNNING");
            return;
        }
        SoundUtil.playSound(DICE_ROLL_SOUND_PATH);
        timer = new Timer(FRAME_DURATION, e -> {
            timeElapsed += FRAME_DURATION;
            if (timeElapsed >= rollDuration) {
                if (callback != null)
                    callback.accept(List.of(val1, val2, val3, val4));
                rollButton.setEnabled(true);
                repaint();
                timer.stop();
                return;
            }
            randomizeDice();
            repaint();
        });
        rollButton.setEnabled(false);
        timeElapsed = 0;
        timer.start();
    }

    public List<Integer> getRollValues() {
        return List.of(val1, val2, val3, val4);
    }

    public void roll() {
        roll(DEFAULT_ROLL_DURATION);
    }
}
