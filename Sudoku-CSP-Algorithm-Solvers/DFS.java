import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class DFS {
    private SudokuBoard board;
    private SudokuManager sudokuManager;
    Boolean[][] markBoard;
    private boolean partial, ordering, inference;
    private AC3 ac3;
    private Map<SudokuButton, ArrayList<String>> varDomains;

    public DFS(SudokuManager sudokuManager, boolean partial, boolean ordering, boolean inference){
        this.sudokuManager = sudokuManager;
        board = sudokuManager.getBoard();
        this.partial = partial;
        this.ordering = ordering;
        this.inference = inference;
        ac3 = new AC3(sudokuManager);
        varDomains = ac3.getVarDomains();
        markBoard = new Boolean[9][9];

        for (int x = 0; x < 9; x++){
            for (int y = 0; y<9; y++){
                if (board.getBoard()[x][y].getText().isEmpty()){
                    markBoard[x][y] = true;
                }
                else{
                    markBoard[x][y] = false;
                }
            }
        }

    }

    public boolean solveDFS(){
        ArrayList<String> assignment = new ArrayList<>();
        int depth = 0;
        if (!partial && !ordering && !inference){
            assignment = searchDFS(assignment, depth, board);
            return drawSolution(assignment);
        }
        else if (partial && !ordering && !inference){
            assignment = searchDFSp(assignment, depth, board);
            return drawSolution(assignment);
        }
        else if (partial && !ordering && inference){
            ac3.solveCSP(false);
            varDomains = ac3.getVarDomains();
            assignment = searchDFSpi(assignment, depth, board);
            return drawSolution(assignment);
        }
        else if (!partial && !ordering && inference){
            ac3.solveCSP(false);
            varDomains = ac3.getVarDomains();
            assignment = searchDFSi(assignment, depth, board);
            return drawSolution(assignment);
        }
        return false;
    }

    private ArrayList<String> searchDFS(ArrayList<String> assignment, int depth, SudokuBoard board){
        if (depth ==  81){
            return assignment;
        }
        else{
            SudokuButton var = board.getBoard()[depth/9][depth%9];
            for (String value : varDomains.get(var)) {
                assignment.add(value);
                ArrayList<String> result = searchDFS(assignment, depth + 1, board);
                if (sudokuManager.checkAssignment(result, true, "")) {
                    return result;
                }
                assignment.removeLast();
            }
        }
        return assignment;
    }

    private ArrayList<String> searchDFSp(ArrayList<String> assignment, int depth, SudokuBoard board){
        if (depth ==  81){
            return assignment;
        }
        else{
            SudokuButton var = board.getBoard()[depth/9][depth%9];
            for (String value : varDomains.get(var)) {
                if (sudokuManager.checkAssignment(assignment, false, value)) {
                    assignment.add(value);
                    ArrayList<String> result = searchDFSp(assignment, depth + 1, board);
                    if (sudokuManager.checkAssignment(result, true, "")) {
                        return result;
                    }
                    assignment.removeLast();
                }
            }
        }
        return assignment;
    }

    private ArrayList<String> searchDFSpi(ArrayList<String> assignment, int depth, SudokuBoard board){
        if (depth ==  81){
            return assignment;
        }
        else{
            SudokuButton var = board.getBoard()[depth/9][depth%9];
            for (String value : varDomains.get(var)) {
                if (sudokuManager.checkAssignment(assignment, false, value)) {
                    assignment.add(value);
                    ArrayList<String> result = searchDFSp(assignment, depth + 1, board);
                    if (sudokuManager.checkAssignment(result, true, "")) {
                        return result;
                    }
                    assignment.removeLast();
                }
            }
        }
        return assignment;
    }

    private ArrayList<String> searchDFSi(ArrayList<String> assignment, int depth, SudokuBoard board){
        if (depth ==  81){
            return assignment;
        }
        else{
            SudokuButton var = board.getBoard()[depth/9][depth%9];
            for (String value : varDomains.get(var)) {
                assignment.add(value);
                ArrayList<String> result = searchDFS(assignment, depth + 1, board);
                if (sudokuManager.checkAssignment(result, true, "")) {
                    return result;
                }
                assignment.removeLast();
            }
        }
        return assignment;
    }

    private boolean drawSolution(ArrayList<String> assignment){
        if (assignment.size() == 81){
            for (int i = 0; i < 81; i++){
                if (markBoard[i/9][i%9]){
                    board.getBoard()[i/9][i%9].setText(assignment.get(i));
                    board.getBoard()[i/9][i%9].setBackground(Color.cyan);
                }
            }
            return true;
        }
        return false;
    }



}
