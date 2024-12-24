import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Pattern;

public class SudokuScreen extends JFrame implements MouseListener, ActionListener, KeyListener, ItemListener {
    private JPanel sudokuPanel, numberPanel, solvePanel, bottomPanel, topPanel;
    private SudokuManager sudokuManager;
    private JLabel topLabel, bottomLabel;
    private GridBagConstraints gbc = new GridBagConstraints();
    private JCheckBox dfs, partial, ordering, inference, minCon;
    private JTextField minConMaxSteps;

    public SudokuScreen(SudokuManager sudokuManager){
        this.sudokuManager = sudokuManager;
        sudokuManager.setScreen(this);
        addKeyListener(this);
        SudokuBoard board = sudokuManager.getBoard();
        getContentPane().setLayout(new BorderLayout(5,5));

        sudokuPanel = new JPanel(new GridBagLayout());
        for(int i = 0; i<board.getBoard().length;i++){
            for (int j=0;j<board.getBoard()[0].length;j++){
                gbc.gridx = j;
                gbc.gridy = i;
                sudokuPanel.add(board.getBoard()[i][j], gbc);
            }
        }

        Font font = new Font("Arial", Font.BOLD, 14);

        numberPanel = new JPanel();
        numberPanel.add(makeNumberPanel());
        numberPanel.addMouseListener(this);
        solvePanel = new JPanel();
        solvePanel.add(makeAlgoPanel());
        solvePanel.addMouseListener(this);
        bottomPanel = new JPanel(new GridLayout(1,0));
        bottomPanel.addMouseListener(this);
        bottomLabel = new JLabel("Time of Algorithm");
        bottomLabel.setFont(font);
        bottomPanel.add(bottomLabel);
        topPanel = new JPanel();
        topPanel.addMouseListener(this);
        topLabel = new JLabel("Sudoku");
        topLabel.setFont(font);
        topPanel.add(topLabel);

        getContentPane().add(sudokuPanel, BorderLayout.CENTER);
        getContentPane().add(numberPanel, BorderLayout.EAST);
        getContentPane().add(solvePanel, BorderLayout.WEST);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        getContentPane().add(topPanel, BorderLayout.NORTH);

        pack();

        // housekeeping : behaviour
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible( true);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object selected = e.getSource();
        if (Pattern.matches("[1-9]",((JButton)selected).getText())){
            //System.out.println(((JButton)selected).getText());
            sudokuManager.writeInBoard(((JButton)selected).getText());
        }
        else if (((JButton)selected).getText().equals("_")){
            sudokuManager.writeOutSelected();
        }
        else if (((JButton)selected).getText().equals("Manually Check Grid")){
            if (checkGrid()){
                topLabel.setText("Sudoku");
                long startTime = System.currentTimeMillis();
                if (sudokuManager.checkFullGrid()){
                    topLabel.setText("Valid Sudoku Grid");
                }else{topLabel.setText("Invalid Sudoku Grid");}
                long endTime = System.currentTimeMillis();
                bottomLabel.setText("Check Grid took: "+ (endTime - startTime) + " milliseconds");
            }
        }
        else if (((JButton)selected).getText().equals("Print AC-3 Inferences")){
            if (checkGrid()){
                topLabel.setText("Sudoku");
                long startTime = System.currentTimeMillis();
                sudokuManager.solveAC3();
                long endTime = System.currentTimeMillis();
                bottomLabel.setText("AC-3 took: "+ (endTime - startTime) + " milliseconds");
            }
        }

        else if (((JButton)selected).getText().equals("Fill With Valid Grid")){
            sudokuManager.testFillInSudoku();
        }

        else if (((JButton)selected).getText().equals("Clear Grid")){
            sudokuManager.clearGrid();
        }

        else if (((JButton)selected).getText().equals("Clear Solution (Blue)")){
            sudokuManager.clearSolution();
        }

        else if (((JButton)selected).getText().equals("Solve")){
            if (checkGrid()){
                if (dfs.isSelected()){
                    topLabel.setText("Sudoku");
                    long startTime = System.currentTimeMillis();
                    String returnMessage = sudokuManager.solveDFS(partial.isSelected(), ordering.isSelected(), inference.isSelected());
                    long endTime = System.currentTimeMillis();
                    if (!returnMessage.equals("No solution found")){
                        bottomLabel.setText(returnMessage + " took: "+ (endTime - startTime) + " milliseconds ("+(endTime - startTime)/1000 + " seconds)");
                    } else{bottomLabel.setText(returnMessage);}
                }
                else if (minCon.isSelected()){
                    if (!(Pattern.matches("^[1-9]\\d*$",minConMaxSteps.getText()))){
                        topLabel.setText("Max Steps for Minimum Conflicts not an int");
                        return;
                    }
                    topLabel.setText("Sudoku");
                    long startTime = System.currentTimeMillis();
                    String returnMessage = sudokuManager.solveMinConflicts(Integer.parseInt(minConMaxSteps.getText()));
                    long endTime = System.currentTimeMillis();
                    if (!returnMessage.equals("No solution found")){
                        bottomLabel.setText(returnMessage + " took: "+ (endTime - startTime) + " milliseconds ("+(endTime - startTime)/1000 + " seconds)");
                    } else{bottomLabel.setText(returnMessage);}
                }
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        Object selected = e.getSource();
        if (((JCheckBox)selected).getText().equals("DFS") && ((JCheckBox)selected).isSelected()){
            dfs.setEnabled(true);
            partial.setEnabled(true);
            ordering.setEnabled(true);
            inference.setEnabled(true);
            minCon.setEnabled(false);
            minConMaxSteps.setEnabled(false);
        }
        else if (((JCheckBox)selected).getText().equals("DFS") && !((JCheckBox)selected).isSelected()){
            partial.setEnabled(false);
            ordering.setEnabled(false);
            inference.setEnabled(false);
            minCon.setEnabled(true);
            minConMaxSteps.setEnabled(false);
        }
        else if (((JCheckBox)selected).getText().equals("Minimum Conflicts") && ((JCheckBox)selected).isSelected()){
            dfs.setEnabled(false);
            partial.setEnabled(false);
            ordering.setEnabled(false);
            inference.setEnabled(false);
            minConMaxSteps.setEnabled(true);
        }
        else if (((JCheckBox)selected).getText().equals("Minimum Conflicts") && !((JCheckBox)selected).isSelected()){
            dfs.setEnabled(true);
            partial.setEnabled(false);
            ordering.setEnabled(false);
            inference.setEnabled(false);
            minConMaxSteps.setEnabled(false);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object selected = e.getSource();
        if (selected instanceof  JPanel){
            if(e.getButton() == MouseEvent.BUTTON1){
                this.requestFocus();
                if ((JPanel)selected != sudokuPanel){
                    sudokuManager.wipeSelected();
                }
            }
        }
        else if (selected instanceof JTextField){
            if(e.getButton() == MouseEvent.BUTTON1){
                if ((JTextField) selected == minConMaxSteps){
                    minConMaxSteps.requestFocus();
                }
            }
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

    @Override
    public void keyReleased(KeyEvent e) {
        String input = KeyEvent.getKeyText(e.getKeyCode());

        if (Pattern.matches("NumPad-.", input)){
            input = input.substring(input.length()-1);
        }

        if (Pattern.matches("[1-9]", input)){
            sudokuManager.writeInBoard(input);
        }

        else if (input.equals("Backspace")){
            sudokuManager.writeOutSelected();
        }

        else if (Pattern.matches("[WASD]|Up|Left|Down|Right", input)){
            sudokuManager.moveSelected(input);
        }

    }
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {}

    private JPanel makeNumberPanel(){
        JPanel toReturn = new JPanel(new GridBagLayout());
        Dimension dim = new Dimension(45,45);
        Font font = new Font("Arial", Font.BOLD, 20);
        for (Integer i = 1; i < 10; i++){
            JButton toAdd = new JButton(i.toString());
            toAdd.setBackground(Color.WHITE);
            toAdd.setPreferredSize(dim);
            toAdd.addActionListener(this);
            toAdd.setFocusable(false);
            toAdd.setFont(font);
            gbc.gridx = (i-1)/3;
            gbc.gridy = ((i-1)%3);
            toReturn.add(toAdd, gbc);
        }
        JButton toAdd = new JButton("_");
        toAdd.setBackground(Color.WHITE);
        toAdd.setPreferredSize(dim);
        toAdd.addActionListener(this);
        toAdd.setFocusable(false);
        toAdd.setFont(font);
        gbc.gridx = 0;
        gbc.gridy = 4;
        toReturn.add(toAdd, gbc);
        return toReturn;
    }

    private JPanel makeAlgoPanel(){
        JPanel toReturn = new JPanel(new GridBagLayout());
        gbc.insets = new Insets(5, 0, 5, 0);

        dfs =  new JCheckBox("DFS");
        dfs.addItemListener(this);
        dfs.setFocusable(false);
        gbc.gridx = 0;
        gbc.gridy  = 0;
        toReturn.add(dfs, gbc);

        partial =  new JCheckBox("Partial Pruning");
        partial.setEnabled(false);
        partial.setFocusable(false);
        gbc.gridy  = 1;
        toReturn.add(partial, gbc);

        ordering =  new JCheckBox("Domain Value-Ordering");
//        ordering.setEnabled(false);
//        ordering.setFocusable(false);
//        gbc.gridy  = 2;
//        toReturn.add(ordering, gbc);

        inference =  new JCheckBox("AC-3 Inferencing");
        inference.setFocusable(false);
        inference.setEnabled(false);
        gbc.gridy  = 3;
        toReturn.add(inference, gbc);

        minCon =  new JCheckBox("Minimum Conflicts");
        minCon.addItemListener(this);
        minCon.setFocusable(false);
        gbc.gridy  = 4;
        toReturn.add(minCon, gbc);

        minConMaxSteps =  new JTextField("Max Steps", 6);
        minConMaxSteps.setEnabled(false);
        minConMaxSteps.addMouseListener(this);
        gbc.gridy  = 5;
        gbc.gridx = 0;
        toReturn.add(minConMaxSteps, gbc);
//        gbc.gridx = 2;
//        JLabel maxStepsLabel = new JLabel("Max Steps:");
//        toReturn.add(maxStepsLabel, gbc);
//        gbc.gridx=1;

        JButton toAdd = new JButton("Solve");
        toAdd.setBackground(Color.WHITE);
        toAdd.addActionListener(this);
        toAdd.setFocusable(false);
        gbc.gridy = 6;
        toReturn.add(toAdd, gbc);

        toAdd = new JButton("Manually Check Grid");
        toAdd.setBackground(Color.WHITE);
        toAdd.addActionListener(this);
        toAdd.setFocusable(false);
        gbc.gridy = 12;
        toReturn.add(toAdd, gbc);

        toAdd = new JButton("Print AC-3 Inferences");
        toAdd.setBackground(Color.WHITE);
        toAdd.addActionListener(this);
        toAdd.setFocusable(false);
        gbc.gridy = 11;
        toReturn.add(toAdd, gbc);

        toAdd = new JButton("Fill With Valid Grid");
        toAdd.setBackground(Color.WHITE);
        toAdd.addActionListener(this);
        toAdd.setFocusable(false);
        gbc.gridy = 9;
        toReturn.add(toAdd, gbc);

        toAdd = new JButton("Clear Grid");
        toAdd.setBackground(Color.WHITE);
        toAdd.addActionListener(this);
        toAdd.setFocusable(false);
        gbc.gridy = 8;
        toReturn.add(toAdd, gbc);

        toAdd = new JButton("Clear Solution (Blue)");
        toAdd.setBackground(Color.WHITE);
        toAdd.addActionListener(this);
        toAdd.setFocusable(false);
        gbc.gridy = 10;
        toReturn.add(toAdd, gbc);

        JButton tempButton = new JButton();
        tempButton.setPreferredSize(new Dimension(1,25));
        tempButton.setOpaque(true);
        tempButton.setEnabled(false);
        tempButton.setBorderPainted(false);
        gbc.gridy = 7;
        toReturn.add(tempButton, gbc);


        return toReturn;
    }

    private boolean checkGrid(){
        if (!sudokuManager.checkFullGrid()){
            topLabel.setText("Not valid sudoku grid");
            bottomLabel.setText("Time of Algorithm");
            return false;
        }
        return true;
    }
}

