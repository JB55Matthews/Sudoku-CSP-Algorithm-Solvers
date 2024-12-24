import java.util.*;

public class AC3 {
    private SudokuBoard board;
    private ArcQueue arcQueue;
    private LinkedList<SudokuButton[]> queue;
    private Map<SudokuButton, ArrayList<String>> varDomains;

    public AC3(SudokuManager sudokuManager){
        board = sudokuManager.getBoard();
        arcQueue = new ArcQueue(board);
        queue = arcQueue.getArcQueue();
        varDomains = createVarDomains();
        updateVarDomains();
    }

    public boolean solveCSP(Boolean toPrint){
        if (mainAC3()){
            if (toPrint) {
                for (SudokuButton[] buttonRow: board.getBoard()) {
                    for (SudokuButton button : buttonRow) {
                        System.out.println("x: " + button.getXcoord() + ", y: " + button.getYcoord() + ", Inferences: " + varDomains.get(button));
                    }
                }
            }
            return true;
        }
        return false;
    }

    private boolean mainAC3(){
        while (!queue.isEmpty()){
            SudokuButton[] arc = queue.pop();
            if (revise(arc[0], arc[1])){
                if(varDomains.get(arc[0]).isEmpty()){return false;}
                //System.out.println("Before: " + queue.size());
                arcQueue.updateQueue(arc[0],arc[1]);
                //System.out.println("After :"+queue.size());
            }
        }
        return true;
    }
    private boolean revise(SudokuButton Xi, SudokuButton Xj){
        boolean revised = false;
        Iterator<String> it = varDomains.get(Xi).iterator();
        while (it.hasNext()){
            String vi = it.next();
            boolean canAssign = false;
            for (String vj: varDomains.get(Xj)){
                if (!vi.equals(vj)) {
                    canAssign = true;
                    break;
                }
            }
            if (!canAssign){
                it.remove();
                revised = true;
            }
        }
        return revised;
    }

    private Map<SudokuButton, ArrayList<String>> createVarDomains(){
        Map<SudokuButton,ArrayList<String>> result = new HashMap<>();
        for (SudokuButton[] buttonRow: board.getBoard()){
            for (SudokuButton button: buttonRow){
                ArrayList<String> varDomain = new ArrayList<>(Arrays.asList("1","2","3","4","5","6","7","8","9"));
                result.put(button, varDomain);
            }
        }
        return result;
    }

    public void updateVarDomains(){
        for (SudokuButton[] buttonRow: board.getBoard()){
            for (SudokuButton button: buttonRow){
                if (!button.getText().isEmpty()){
                    ArrayList<String> varDomain = new ArrayList<>(Arrays.asList(button.getText()));
                    varDomains.replace(button, varDomain);
                }
            }
        }
    }

    public Map<SudokuButton, ArrayList<String>> getVarDomains(){
        return varDomains;
    }

}
