import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

public class SudokuBoard implements MouseListener {
    final int ROWS = 9;
    final int COLS = 9;
    final int DIM = 45;
    private SudokuButton[][] board;
    Map<String, Border> borderMap;
    private SudokuManager sudokuManager;

    public SudokuBoard(SudokuManager sudokuManager){
        this.sudokuManager = sudokuManager;
        board = new SudokuButton[ROWS][COLS];
        borderMap = createBordersMap(1);
        Dimension dim = new Dimension(DIM,DIM);

        for (int x = 0; x<ROWS; x++){
            for (int y = 0; y<COLS; y++){
                board[x][y] = new SudokuButton();
                board[x][y].setXcoord(x);
                board[x][y].setYcoord(y);
                board[x][y].setPreferredSize(dim);
                board[x][y].setText("");
                //board[x][y].setOpaque(true);
                board[x][y].setEnabled(false);
                board[x][y].setBackground(Color.WHITE);
                //board[x][y].setBorder(borderMap.get(""));
                board[x][y].addMouseListener(this);
                board[x][y].setFont(new Font("Arial", Font.BOLD, 20));
                board[x][y].setBoxVal(((x/3)*3) + ((y)/3) + 1);

                if (x==0 && y%3 == 0){board[x][y].setBorder(borderMap.get("TL"));}
                else if (x==0 && (y==8)){board[x][y].setBorder(borderMap.get("TR"));}
                else if (x==0){board[x][y].setBorder(borderMap.get("T"));}
                else if ((x%3==1 || x==3||x==6) && y%3 == 0){board[x][y].setBorder(borderMap.get("L"));}
                else if ((x%3==1 || x==3||x==6) && (y==8)){board[x][y].setBorder(borderMap.get("R"));}
                else if ((x==2||x==5||x==8) && y%3 == 0){board[x][y].setBorder(borderMap.get("LB"));}
                else if ((x==2||x==5||x==8) && (y==8)){board[x][y].setBorder(borderMap.get("BR"));}
                else if (x==2||x==5||x==8){board[x][y].setBorder(borderMap.get("B"));}

                //Commenting out fixes some line issues, consider fixing
                else{board[x][y].setBorder(borderMap.get(""));}
            }
        }
        for (SudokuButton[] buttonRow: board){
            for (SudokuButton button: buttonRow){
                for (int x = 0; x < ROWS;x++){
                    for (int y = 0; y < COLS; y++){
                        //if (board[x][y] != button && board[x][y].getBoxVal() == button.getBoxVal()){
                        if (board[x][y].getBoxVal() == button.getBoxVal()){
                            button.getBoxNeighbours().add(board[x][y]);
                        }
                        //if (x == button.getXcoord() && y != button.getYcoord()){
                        if (x == button.getXcoord()){
                            button.getRowNeighbours().add(board[x][y]);
                        }
                        //if (x != button.getXcoord() && y == button.getYcoord()){
                        if (y == button.getYcoord()){
                            button.getColNeighbours().add(board[x][y]);
                        }
                    }
                }
            }
        }

    }

    public SudokuButton[][] getBoard(){
        return board;
    }

    private static Map<String, Border> createBordersMap(int pixels) {
        Map<String, Border> result = new HashMap<>();
        //result.put("", BorderFactory.createEmptyBorder());
        result.put("", new MatteBorder(pixels, pixels, pixels, pixels, Color.LIGHT_GRAY));
        result.put("L", new CompoundBorder(
                BorderFactory.createMatteBorder(0, 2*pixels, 0, 0, Color.BLACK),
                BorderFactory.createMatteBorder(pixels, 0, pixels, pixels, Color.LIGHT_GRAY)));
        result.put("T", new CompoundBorder(
                BorderFactory.createMatteBorder(2*pixels, 0, 0, 0, Color.BLACK),
                BorderFactory.createMatteBorder(0, pixels, pixels, pixels, Color.LIGHT_GRAY)));
        result.put("R", new CompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 2*pixels, Color.BLACK),
                BorderFactory.createMatteBorder(pixels, pixels, pixels, 0, Color.LIGHT_GRAY)));
        result.put("B", new CompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2*pixels, 0, Color.BLACK),
                BorderFactory.createMatteBorder(pixels, pixels, 0, pixels, Color.LIGHT_GRAY)));
        result.put("TL", new CompoundBorder(
                BorderFactory.createMatteBorder(2*pixels, 2*pixels, 0, 0, Color.BLACK),
                BorderFactory.createMatteBorder(0, 0, pixels, pixels, Color.LIGHT_GRAY)));
        result.put("TB", new CompoundBorder(
                BorderFactory.createMatteBorder(2*pixels, 0, 2*pixels, 0, Color.BLACK),
                BorderFactory.createMatteBorder(0, pixels, 0, pixels, Color.LIGHT_GRAY)));
        result.put("TR", new CompoundBorder(
                BorderFactory.createMatteBorder(2*pixels, 0, 0, 2*pixels, Color.BLACK),
                BorderFactory.createMatteBorder(0, pixels, pixels, 0, Color.LIGHT_GRAY)));
        result.put("LB", new CompoundBorder(
                BorderFactory.createMatteBorder(0, 2*pixels, 2*pixels, 0, Color.BLACK),
                BorderFactory.createMatteBorder(pixels, 0, 0, pixels, Color.LIGHT_GRAY)));
        result.put("LR", new CompoundBorder(
                BorderFactory.createMatteBorder(0, 2*pixels, 0, 2*pixels, Color.BLACK),
                BorderFactory.createMatteBorder(pixels, 0, pixels, 0, Color.LIGHT_GRAY)));
        result.put("BR", new CompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2*pixels, 2*pixels, Color.BLACK),
                BorderFactory.createMatteBorder(pixels, pixels, 0, 0, Color.LIGHT_GRAY)));
        result.put("TLB", new CompoundBorder(
                BorderFactory.createMatteBorder(2*pixels, 2*pixels, 2*pixels, 0, Color.BLACK),
                BorderFactory.createMatteBorder(0,0,0, pixels, Color.LIGHT_GRAY)));
        result.put("TLR", new CompoundBorder(
                BorderFactory.createMatteBorder(2*pixels, 2*pixels, 0, 2*pixels, Color.BLACK),
                BorderFactory.createMatteBorder(0, 0, pixels, 0, Color.LIGHT_GRAY)));
        result.put("TBR", new CompoundBorder(
                BorderFactory.createMatteBorder(2*pixels, 0, 2*pixels, 2*pixels, Color.BLACK),
                BorderFactory.createMatteBorder(0, pixels, 0,0, Color.LIGHT_GRAY)));
        result.put("LBR", new CompoundBorder(
                BorderFactory.createMatteBorder(0, 2*pixels, 2*pixels, 2*pixels, Color.BLACK),
                BorderFactory.createMatteBorder(pixels, 0, 0,0, Color.LIGHT_GRAY)));
        result.put("TLBR", new CompoundBorder(
                BorderFactory.createMatteBorder(2*pixels, 2*pixels, 2*pixels, 2*pixels, Color.BLACK),
                BorderFactory.createMatteBorder(0,0,0,0, Color.LIGHT_GRAY)));
        return result;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object selected = e.getSource();

        if(e.getButton() == MouseEvent.BUTTON1) {
            //System.out.println("selected row: " + ((SudokuButton)selected).getXcoord() + ", selected col: " + ((SudokuButton)selected).getYcoord());
            sudokuManager.updateSelected(((SudokuButton)selected).getXcoord(), ((SudokuButton)selected).getYcoord());
            sudokuManager.getScreen().requestFocus();
        }
    }
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}
