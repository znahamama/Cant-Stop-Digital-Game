import javax.swing.*;
/*
 *  The main class(JVM entrypoint) for this project
 */
public class Driver
{
    /**
     * Allows the project to run as a stand alone application
     */
    public static void main(String[] args)
    {
        // Set the look and feel of the game
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, fall back to cross-platform
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                // Not worth my time
            }
        }
        
        // create a new GUI window
        //WindowDemo demo = new WindowDemo(17, 17);
        MainMenu menu = new MainMenu();
    }
}
