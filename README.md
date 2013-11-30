#Assignment 2 (50%)

##B.Sc.* Year 2, Algorithms 2013

###2. Connect 4

Develop a java program that simulates the popular game Connect 4.  Connect 4 is a two player game in which the players take turns dropping coloured disks into a grid of seven columns by six rows. Please see here for more information and to understand fully the rules. Your program should have the following features:

Players will interact either via a console or graphical user interface (UI) that shows the current state of the grid after each go. See figures below for example UIs. The graphical based UI is more desirable.
For each turn, the current player selects the column they wish to place their disk. They can only put disks in a column that is not full. Disks will occupy next available place in the column.
The user(player 1) will take the first turn and the computer(player 2) will take the second turn.

After each turn, the program should check for the following:

*A winner (four of the current players discs next to each other vertically, horizontally, or diagonally).

*A draw (no winner and all columns full).

*The board must be represented as a 2D array. The computer player skeleton class is here. You must NOT change the interface of this class however you must include your student number in the ComputerPlayer class name.
