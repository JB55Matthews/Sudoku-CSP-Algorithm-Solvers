import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;

public class Driver {

    public static void main(String[] args){
        try {
            UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
            UIManager.put("Button.disabledText", new ColorUIResource(Color.BLACK));
        } catch (Exception e) {
            e.printStackTrace();
        }
        SudokuManager sudokuManager = new SudokuManager();
        new SudokuScreen(sudokuManager);
    }
}
