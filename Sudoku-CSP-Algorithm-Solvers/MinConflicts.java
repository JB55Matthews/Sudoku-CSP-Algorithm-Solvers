import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MinConflicts {
    private int maxSteps;
    private SudokuBoard board;
    private SudokuManager sudokuManager;
    private Random random;
    private ArrayList<SudokuButton> solveButtons;

    public MinConflicts(SudokuManager sudokuManager, int maxSteps){
        this.sudokuManager = sudokuManager;
        this.board = sudokuManager.getBoard();
        this.maxSteps = maxSteps;
        random = new Random();
        solveButtons = new ArrayList<>();
    }


    public boolean solveCSP(){
        if (mainMinConflicts()){
            for (SudokuButton[] buttonRow: board.getBoard()){
                for (SudokuButton button: buttonRow){
                    if (solveButtons.contains(button)){
                        button.setBackground(Color.cyan);
                    }
                }
            }
            return true;
        }
        for (SudokuButton[] buttonRow: board.getBoard()){
            for (SudokuButton button: buttonRow){
                if (solveButtons.contains(button)){
                    button.setText("");
                }
            }
        }
        return false;
    }

    private boolean mainMinConflicts(){
        createInitialAssignment();
        for (int i = 0; i < maxSteps; i++){
            if (sudokuManager.checkFullGrid()){return true;}
            ArrayList<SudokuButton> conflicted = findConflicts();
            SudokuButton var = conflicted.get(random.nextInt(conflicted.size()));
            var.setText(vMinConflicts(var));
        }

        return false;
    }


    private void createInitialAssignment(){
        for (int x = 0; x<9; x++){
            for (int y = 0; y < 9; y++){
                if (board.getBoard()[x][y].getText().isEmpty()){
                    board.getBoard()[x][y].setText((((Integer)(random.nextInt(9)+1)).toString()));
                    solveButtons.add(board.getBoard()[x][y]);
                }
            }
        }
    }

    private String vMinConflicts(SudokuButton var){
        int[] conflictCount = new int[]{0,0,0,0,0,0,0,0,0};
        ArrayList<SudokuButton> rowNeighbours = var.getRowNeighbours();
        ArrayList<SudokuButton> colNeighbours = var.getColNeighbours();
        ArrayList<SudokuButton> boxNeighbours = var.getBoxNeighbours();
        for (int i = 0; i < 9; i++){
            var.setText(((Integer)(i+1)).toString());
            for (SudokuButton rowNeighbour: rowNeighbours){
                if (rowNeighbour.getText().equals(var.getText())){
                    conflictCount[i] = conflictCount[i] + 1;
                }
            }
            for (SudokuButton colNeighbour: colNeighbours){
                if (colNeighbour.getText().equals(var.getText())){
                    conflictCount[i] = conflictCount[i] + 1;
                }
            }
            for (SudokuButton boxNeighbour: boxNeighbours){
                if (boxNeighbour.getText().equals(var.getText())){
                    conflictCount[i] = conflictCount[i] + 1;
                }
            }

        };
        int minI = conflictCount[0];
        for (int i = 0; i <conflictCount.length; i++){
            if (conflictCount[i]<conflictCount[minI]){
                minI = i;
            }
        }
        return ((Integer)(minI+1)).toString();
    }

    private ArrayList<SudokuButton> findConflicts(){
        ArrayList<SudokuButton> conflicted = new ArrayList<>();
        for (SudokuButton button: solveButtons){
            boolean done = false;
            ArrayList<SudokuButton> rowNeighbours = button.getRowNeighbours();
            for (SudokuButton rowNeighbour: rowNeighbours){
                if (rowNeighbour.getText().equals(button.getText())){
                    conflicted.add(button);
                    done = true;
                    break;
                }
            }
            if (!done){
                ArrayList<SudokuButton> colNeighbours = button.getColNeighbours();
                for (SudokuButton colNeighbour: colNeighbours){
                    if (colNeighbour.getText().equals(button.getText())){
                        conflicted.add(button);
                        done = true;
                        break;
                    }
                }
            }
            if (!done){
                ArrayList<SudokuButton> boxNeighbours = button.getBoxNeighbours();
                for (SudokuButton boxNeighbour: boxNeighbours){
                    if (boxNeighbour.getText().equals(boxNeighbour.getText())){
                        conflicted.add(button);
                        break;
                    }
                }
            }
        }
        return conflicted;
    }
}
