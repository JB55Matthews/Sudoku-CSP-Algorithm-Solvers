import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SudokuManager {
    final int ROWS = 9;
    final int COLS = 9;
    private SudokuBoard board;
    private int selectedX, selectedY;
    private SudokuScreen screen;


    public SudokuManager(){
        board = new SudokuBoard(this);
        selectedX = 0;
        selectedY = 0;
    }

    public SudokuBoard getBoard() {
        return board;
    }

    public void updateSelected(int x, int y){
        board.getBoard()[selectedX][selectedY].setBackground(Color.WHITE);
        selectedX = x;
        selectedY = y;
        board.getBoard()[selectedX][selectedY].setBackground(Color.lightGray);
    }

    public void wipeSelected(){
        board.getBoard()[selectedX][selectedY].setBackground(Color.WHITE);
    }

    public void writeInBoard(String num){
        if (board.getBoard()[selectedX][selectedY].getBackground() == Color.lightGray){
            board.getBoard()[selectedX][selectedY].setText(num);
            board.getBoard()[selectedX][selectedY].repaint();
        }
    }

    public void writeOutSelected(){
        if (board.getBoard()[selectedX][selectedY].getBackground() == Color.lightGray){
            board.getBoard()[selectedX][selectedY].setText("");
        }
    }

    public void moveSelected(String keyMove){
        if (board.getBoard()[selectedX][selectedY].getBackground() == Color.lightGray){
            if ((keyMove.equals("W") || keyMove.equals("Up"))&&(selectedX>0)){
                board.getBoard()[selectedX][selectedY].setBackground(Color.WHITE);
                board.getBoard()[selectedX-1][selectedY].setBackground(Color.lightGray);
                selectedX -= 1;
            }

            else if ((keyMove.equals("S") || keyMove.equals("Down"))&&(selectedX+1<ROWS)){
                board.getBoard()[selectedX][selectedY].setBackground(Color.WHITE);
                board.getBoard()[selectedX+1][selectedY].setBackground(Color.lightGray);
                selectedX += 1;
            }

            else if ((keyMove.equals("A") || keyMove.equals("Left"))&&(selectedY>0)){
                board.getBoard()[selectedX][selectedY].setBackground(Color.WHITE);
                board.getBoard()[selectedX][selectedY-1].setBackground(Color.lightGray);
                selectedY -= 1;
            }

            else if ((keyMove.equals("D") || keyMove.equals("Right"))&&(selectedY+1<COLS)){
                board.getBoard()[selectedX][selectedY].setBackground(Color.WHITE);
                board.getBoard()[selectedX][selectedY+1].setBackground(Color.lightGray);
                selectedY += 1;
            }
        }
    }

    public void checkNeighbours(){
        Integer i = 1;
        for (JButton[] buttonRow: board.getBoard()){
            for (JButton button: buttonRow){
                button.setText(i.toString());
                i += 1;
            }
        }

        int xToCheck = 1;
        int yToCheck = 7;

        System.out.println("Button checking: " + board.getBoard()[xToCheck][yToCheck].getText());
        System.out.print("Row Neighbours: ");
        for (SudokuButton button: board.getBoard()[xToCheck][yToCheck].getRowNeighbours()){
            System.out.printf(button.getText() +", ");
        }
        System.out.println();

        System.out.print("Col Neighbours: ");
        for (SudokuButton button: board.getBoard()[xToCheck][yToCheck].getColNeighbours()){
            System.out.printf(button.getText() +", ");
        }
        System.out.println();

        System.out.print("Box Neighbours: ");
        for (SudokuButton button: board.getBoard()[xToCheck][yToCheck].getBoxNeighbours()){
            System.out.printf(button.getText() +", ");
        }
        System.out.println();
    }

    public Boolean checkFullGrid(){
        //checkNeighbours();
        for (int x = 0; x < ROWS; x++){
            for (SudokuButton button: board.getBoard()[x][0].getRowNeighbours()){
                for (int i = button.getYcoord()+1; i < COLS; i++){
                    if (!button.getText().isEmpty() && button.getText().equals(board.getBoard()[x][i].getText())){
                        return false;
                    }
                }
            }
        }
        for (int y = 0; y < COLS; y++){
            for (SudokuButton button: board.getBoard()[0][y].getColNeighbours()){
                for (int i = button.getXcoord()+1; i < ROWS; i++){
                    if (!button.getText().isEmpty() && button.getText().equals(board.getBoard()[i][y].getText())){
                        return false;
                    }
                }
            }
        }
        for (int x = 0; x < ROWS/3; x++){
            for (int y = 0; y < COLS/3; y++){
                ArrayList<SudokuButton> buttonNeighbours = board.getBoard()[x*3][y*3].getBoxNeighbours();
                for (SudokuButton button: buttonNeighbours){
                    for (int i =  buttonNeighbours.indexOf(button)+1 ; i < COLS-1; i++){
                        if (!button.getText().isEmpty() && button.getText().equals(buttonNeighbours.get(i).getText())){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public void solveAC3(){
        AC3 ac3 = new AC3(this);
        ac3.solveCSP(true);
    }

    public String solveDFS(boolean partial, boolean ordering, boolean inference){
        DFS dfs = new DFS(this, partial, ordering, inference);
        if (dfs.solveDFS()){
            if (!partial && !ordering && !inference) {
                return "DFS";
            }
            else if (partial && !ordering && !inference){
                return "DFS+Partial";
            }
            else if (partial && !ordering && inference){
                return "DFS+Partial+AC3-Inferencing";
            }
            else if (!partial && !ordering && inference){
                return "DFS+AC3-Inferencing";
            }
        }

        return "No solution found";
    }

    public String solveMinConflicts(int maxSteps){
        MinConflicts minCon = new MinConflicts(this, maxSteps);
        if (minCon.solveCSP()){
            return "Minimum Conflicts - (" + maxSteps + " steps)";
        }
        return "No solution found";
    }

    public void clearGrid(){
        for (JButton[] buttonRow: board.getBoard()){
            for (JButton button: buttonRow){
                button.setText("");
                button.setBackground(Color.WHITE);
            }
        }
    }

    public void clearSolution(){
        for (JButton[] buttonRow: board.getBoard()){
            for (JButton button: buttonRow){
                if (button.getBackground() == Color.cyan){
                    button.setText("");
                    button.setBackground(Color.WHITE);
                }
            }
        }
    }

    public boolean checkAssignment(ArrayList<String> assignment, boolean full, String value){
        int size = assignment.size();
        if (full && size != 81){
            return false;
        }
        if (!full){
            assignment.add(value);
        }
        for (int i = 0; i < size; i++){
            String iVal = assignment.get(i);
            for (int x = 0; x < assignment.size(); x++){
                String xVal = assignment.get(x);
                if (x != i && x/9 == i/9 && xVal.equals(iVal)){
                    if (!full){assignment.removeLast();}
                    return false;
                }
                if (x != i && x%9 == i%9 && xVal.equals(iVal)){
                    if (!full){assignment.removeLast();}
                    return false;
                }
                if ( x != i && (((x/9)/3)*3)+((x%9)/3) + 1 == (((i/9)/3)*3)+((i%9)/3) + 1 && xVal.equals(iVal)){
                    if (!full){assignment.removeLast();}
                    return false;
                }
            }
        }
        if (!full){assignment.removeLast();}
        return true;
    }

    public void setScreen(SudokuScreen screen){this.screen = screen;}

    public SudokuScreen getScreen(){return screen;}

    public void testFillInSudoku() {
        SudokuButton[] oRow = board.getBoard()[0];
        SudokuButton[] tRow = board.getBoard()[1];
        SudokuButton[] thRow = board.getBoard()[2];
        SudokuButton[] fRow = board.getBoard()[3];
        SudokuButton[] fiRow = board.getBoard()[4];
        SudokuButton[] sRow = board.getBoard()[5];
        SudokuButton[] seRow = board.getBoard()[6];
        SudokuButton[] eRow = board.getBoard()[7];
        SudokuButton[] nRow = board.getBoard()[8];
        oRow[0].setText("8");
        oRow[1].setText("2");
        oRow[2].setText("7");
        oRow[3].setText("1");
        oRow[4].setText("5");
        oRow[5].setText("4");
        oRow[6].setText("3");
        oRow[7].setText("9");
        oRow[8].setText("6");

        tRow[0].setText("9");
        tRow[1].setText("6");
        tRow[2].setText("5");
        tRow[3].setText("3");
        tRow[4].setText("2");
        tRow[5].setText("7");
        tRow[6].setText("1");
        tRow[7].setText("4");
        tRow[8].setText("8");

        thRow[0].setText("3");
        thRow[1].setText("4");
        thRow[2].setText("1");
        thRow[3].setText("6");
        thRow[4].setText("8");
        thRow[5].setText("9");
        thRow[6].setText("7");
        thRow[7].setText("5");
        thRow[8].setText("2");

        fRow[0].setText("5");
        fRow[1].setText("9");
        fRow[2].setText("3");
        fRow[3].setText("4");
        fRow[4].setText("6");
        fRow[5].setText("8");
        fRow[6].setText("2");
        fRow[7].setText("7");
        fRow[8].setText("1");

        fiRow[0].setText("4");
        fiRow[1].setText("7");
        fiRow[2].setText("2");
        fiRow[3].setText("5");
        fiRow[4].setText("1");
        fiRow[5].setText("3");
        fiRow[6].setText("6");
        fiRow[7].setText("8");
        fiRow[8].setText("9");

        sRow[0].setText("6");
        sRow[1].setText("1");
        sRow[2].setText("8");
        sRow[3].setText("9");
        sRow[4].setText("7");
        sRow[5].setText("2");
        sRow[6].setText("4");
        sRow[7].setText("3");
        sRow[8].setText("5");

        seRow[0].setText("7");
        seRow[1].setText("8");
        seRow[2].setText("6");
        seRow[3].setText("2");
        seRow[4].setText("3");
        seRow[5].setText("5");
        seRow[6].setText("9");
        seRow[7].setText("1");
        seRow[8].setText("4");

        eRow[0].setText("1");
        eRow[1].setText("5");
        eRow[2].setText("4");
        eRow[3].setText("7");
        eRow[4].setText("9");
        eRow[5].setText("6");
        eRow[6].setText("8");
        eRow[7].setText("2");
        eRow[8].setText("3");

        nRow[0].setText("2");
        nRow[1].setText("3");
        nRow[2].setText("9");
        nRow[3].setText("8");
        nRow[4].setText("4");
        nRow[5].setText("1");
        nRow[6].setText("5");
        nRow[7].setText("6");
        nRow[8].setText("7");
    }

}
