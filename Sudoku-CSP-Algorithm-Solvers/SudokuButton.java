import javax.swing.*;
import java.util.ArrayList;

public class SudokuButton extends JButton {

    private int xcoord, ycoord, boxVal;
    private String buttonVal;
    private ArrayList<SudokuButton> boxNeighbours, rowNeighbours, colNeighbours;

    public SudokuButton(){
        xcoord = 0;
        ycoord = 0;
        buttonVal = "";
        boxNeighbours = new ArrayList<>(8);
        rowNeighbours = new ArrayList<>(8);
        colNeighbours = new ArrayList<>(8);
    }

    public int getXcoord() {
        return xcoord;
    }

    public int getYcoord() {
        return ycoord;
    }

    public int getBoxVal(){return boxVal;}

    public ArrayList<SudokuButton> getBoxNeighbours(){return boxNeighbours;}

    public ArrayList<SudokuButton> getRowNeighbours(){return rowNeighbours;}

    public ArrayList<SudokuButton> getColNeighbours(){return colNeighbours;}

    public String getButtonVal() {return buttonVal;}

    public void setXcoord(int xcoord) {
        this.xcoord = xcoord;
    }

    public void setYcoord(int ycoord) {
        this.ycoord = ycoord;
    }

    public void setBoxVal(int boxVal){this.boxVal = boxVal;}

    public void setButtonVal(String buttonVal){this.buttonVal = buttonVal;}
}
