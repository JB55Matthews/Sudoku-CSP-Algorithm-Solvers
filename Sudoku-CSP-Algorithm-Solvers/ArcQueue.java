import java.util.ArrayList;
import java.util.LinkedList;

public class ArcQueue {
    private LinkedList<SudokuButton[]> arcQueue;

    public ArcQueue(SudokuBoard board){
        arcQueue = new LinkedList<>();

        for (SudokuButton[] buttonRow: board.getBoard()){
            for (SudokuButton button: buttonRow){
                ArrayList<SudokuButton> rowNeighbours = button.getRowNeighbours();
                ArrayList<SudokuButton> colNeighbours = button.getColNeighbours();
                ArrayList<SudokuButton> boxNeighbours = button.getBoxNeighbours();

                for (SudokuButton rowNeighbour: rowNeighbours){
                    if (rowNeighbour.getYcoord() != button.getYcoord()){
                        arcQueue.add(new SudokuButton[]{button, rowNeighbour});
                    }
                }
                for (SudokuButton colNeighbour: colNeighbours){
                    if (colNeighbour.getXcoord() != button.getXcoord()){
                        arcQueue.add(new SudokuButton[]{button, colNeighbour});
                    }
                }
                for (SudokuButton boxNeighbour: boxNeighbours){
                    if (boxNeighbour.getXcoord() != button.getXcoord() && boxNeighbour.getYcoord() != button.getYcoord()){
                        arcQueue.add(new SudokuButton[]{button, boxNeighbour});
                    }
                }
            }
        }

    }

    public void updateQueue(SudokuButton Xi, SudokuButton Xj){
        ArrayList<SudokuButton> rowNeighbours = Xi.getRowNeighbours();
        ArrayList<SudokuButton> colNeighbours = Xi.getColNeighbours();
        ArrayList<SudokuButton> boxNeighbours = Xi.getBoxNeighbours();

        for (SudokuButton rowNeighbour: rowNeighbours){
            if (rowNeighbour.getYcoord() != Xi.getYcoord() && rowNeighbour.getYcoord() != Xj.getYcoord()){
                arcQueue.add(new SudokuButton[]{rowNeighbour, Xi});
            }
        }
        for (SudokuButton colNeighbour: colNeighbours){
            if (colNeighbour.getXcoord() != Xi.getXcoord() && colNeighbour.getXcoord() != Xj.getXcoord()){
                arcQueue.add(new SudokuButton[]{colNeighbour, Xi});
            }
        }
        for (SudokuButton boxNeighbour: boxNeighbours){
            if (boxNeighbour.getXcoord() != Xi.getXcoord() && boxNeighbour.getYcoord() != Xi.getYcoord() && boxNeighbour.getXcoord() != Xj.getXcoord() && boxNeighbour.getYcoord() != Xj.getYcoord()){
                arcQueue.add(new SudokuButton[]{boxNeighbour, Xi});
            }
        }

    }

    public LinkedList<SudokuButton[]> getArcQueue(){
        return arcQueue;
    }


}
