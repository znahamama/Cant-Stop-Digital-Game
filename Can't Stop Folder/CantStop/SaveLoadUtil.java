import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SaveLoadUtil {
    private static final String FILEPATH = "game.gamedata";
    public static void saveGame(Game game) {

        try {
            FileOutputStream fileOut = new FileOutputStream(FILEPATH, false);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(game);
            objectOut.close();
            System.out.println("The game was successfully saved to " + FILEPATH);

        } catch (Exception ex) {
            System.out.println("Failed to save game: " + ex.getMessage());
        }

    }

    public static Game loadGame() {
        return loadGame(FILEPATH);
    }

    public static Game loadGame(String filepath) {
        try {
            FileInputStream fileIn = new FileInputStream(filepath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            Game game = (Game) (objectIn.readObject());
            objectIn.close();
            System.out.println("Game loaded!");
            return game;

        } catch (Exception ex) {
            System.out.println("Failed to load game from file " + filepath);
            ex.printStackTrace();
            return null;
        }

    }
}
