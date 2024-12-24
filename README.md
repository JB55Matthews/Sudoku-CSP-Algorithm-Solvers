# Sudoku-CSP-Algorithm-Solvers

This app allows users to input there own sudoku puzzles and solve them using different seaarch starategies, being
able to time them and compare. This was inspired by MUN's Comp 3200, algorithm techniques for AI, where different CSP 
algorithms were taught and explored. This is a fun side-project implementing these aglorithms into a useful, user-friendly
solver.

![SudokuCSPSolver](https://github.com/user-attachments/assets/f9633092-6c21-4b3b-9eb6-d40ec0edb3a5)

There are 5 main algorithms implemented which can be used:
* Basic Depth-First Search (DFS)
* DFS + Partial Assignment Prunning
* DFS + AC-3 Inferencing
* DFS + Partial Assignment Pruning + AC-3 Inferencing
* Minimum Conflicts (Local Search aglorithm)

Note that DFS + Partial Assignment Pruning + AC-3 Inferencing functions as an almost instantaneous sudoku solver
for virtually ever puzzle possible (no examples have yet been found where this is not a near-perfect solver)

The grid can be naviagted with mouse clicks and the given numberpad, or with WASD/Arrow keys and Number keys/NumPad. A given full
valid board can automatically be filled in for testing purposes. The inferences given by AC-3 can also be calculated by itself.
Below is what a solved grid for a given puzzle would look like. The solution can quickly be cleared, or the whole grid itself can be cleared.
![SudokuCSPSolver-Solved](https://github.com/user-attachments/assets/f8229120-dc4a-4710-9fd0-59bd780fee3b)
