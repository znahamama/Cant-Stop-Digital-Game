import java.io.File;
import javax.sound.sampled.*;

public class SoundUtil {
    public static void playSound(String audioFilePath) {
        try {
            File audioFile = new File(audioFilePath).getAbsoluteFile();
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch(Exception e) {
            System.out.println("Failed to play sound: " + e.getMessage());
        }
    }
}