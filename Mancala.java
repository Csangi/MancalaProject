
import java.awt.event.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.util.Random;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.lang.*;
import java.awt.Font;
import javax.swing.*;

public class Mancala{                   //we start off with our main function where we just create a Manframe
    public static void main(String[] args) {
        ManFrame frame = new ManFrame();
    }
}

class ManFrame extends JFrame {                 //in our Manframe class first we extend Jframe we set up our frame and add our panel

    ManFrame() {
        ManPanel panel = new ManPanel();        //then we set up our frame and add our panel
        this.setTitle("Mancala");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.add(panel);
        this.pack();
        this.setResizable(true);
        this.setVisible(true);
    }
}

class ManPanel extends JPanel  {
    private int[][] boardValues = new int[2][6];            //integer array that holds all of out values of each cup
    private int leftGoal;                                   //integer for the left goal
    private int rightGoal;                                  //integer for the right goal
    private int turn;                                       //determines the player's turn (odd is player 2, even is player 1)
    private JButton[][] cups = new JButton[2][6];           //button array for all of the places on the board
    private JLabel leftGoalLabel = new JLabel("0");         //all of our different labels for the gui
    private JLabel rightGoalLabel = new JLabel("0");
    private JLabel playerTurn = new JLabel("Player 1's turn");
    private JLabel bottomLabel = new JLabel("Green is player 1's options, red is player 2");

    ManPanel() {                                                        //initializes all of our integers 
        this.setPreferredSize(new Dimension(1000, 400));                //and sets up our panel
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 6; j++)
                boardValues[i][j] = 4;

        turn = 0;
        leftGoal = 0;
        rightGoal = 0;
        
        //all of our gui set up
        leftGoalLabel.setPreferredSize(new Dimension(50, 50));
        rightGoalLabel.setPreferredSize(new Dimension(50, 50));
        leftGoalLabel.setFont(new Font("Verdana", Font.PLAIN, 30));
        rightGoalLabel.setFont(new Font("Verdana", Font.PLAIN, 30));
        playerTurn.setOpaque(true);
        this.setLayout(new BorderLayout());
        this.add(playerTurn, BorderLayout.PAGE_START);
        this.add(leftGoalLabel, BorderLayout.LINE_START);
        this.add(rightGoalLabel, BorderLayout.LINE_END);
        this.add(bottomLabel, BorderLayout.PAGE_END);

        //call different function to initialize the buttons
        initializeButtons();

    }

    public void initializeButtons() {
        JPanel buttons = new JPanel(new GridLayout(2, 6));              //we used GridLayout to allow our game to be resizable and for easy set up
        for (int i = 0; i < 2; i++) {                                   //here we loop through both rows
            if (i == 1) {                                               //if it is the second player then we add the buttons from left to right
                for (int j = 0; j < 6; j++) {                           //we loop through all 6 buttons in the row
                    cups[i][j] = new JButton();
                    cups[i][j].setPreferredSize(new Dimension(75, 75));
                    cups[i][j].setFocusable(false);
                    cups[i][j].setBackground(Color.RED);                //player 2's color is red 
                    buttons.add(cups[i][j]);                            //we add the button to the Gridlayout
                }
            } else if (i == 0) {                                        //if it is the first player then we add the buttons from right to left
                for (int j = 5; j >= 0; j--) {
                    cups[i][j] = new JButton();
                    cups[i][j].setPreferredSize(new Dimension(75, 75));
                    cups[i][j].setFocusable(false);
                    cups[i][j].setBackground(Color.GREEN);
                    buttons.add(cups[i][j]);
                }
            }
        }
        this.add(buttons, BorderLayout.CENTER);                         //then we add the panel to this panel

        cups[0][0].addActionListener(e -> cupIsPressed(0, 0));          //here we are setting every action listener for our buttons through a lambda expression
        cups[0][1].addActionListener(e -> cupIsPressed(0, 1));          //and all we send is the index of the button
        cups[0][2].addActionListener(e -> cupIsPressed(0, 2));
        cups[0][3].addActionListener(e -> cupIsPressed(0, 3));
        cups[0][4].addActionListener(e -> cupIsPressed(0, 4));
        cups[0][5].addActionListener(e -> cupIsPressed(0, 5));
        cups[1][0].addActionListener(e -> cupIsPressed(1, 0));
        cups[1][1].addActionListener(e -> cupIsPressed(1, 1));
        cups[1][2].addActionListener(e -> cupIsPressed(1, 2));
        cups[1][3].addActionListener(e -> cupIsPressed(1, 3));
        cups[1][4].addActionListener(e -> cupIsPressed(1, 4));
        cups[1][5].addActionListener(e -> cupIsPressed(1, 5));
        reSetNames();
    }

    public void reSetNames() {                  //this function just resets the name of the button for every turn and changes the color of the turn
        StringBuilder sb = new StringBuilder();
        String value = new String();
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 6; j++) {
                sb.setLength(0);
                value = Integer.toString(boardValues[i][j]);
                sb.append(value);
                cups[i][j].setText(sb.toString());
                cups[i][j].setFont(new Font("Verdana", Font.PLAIN, 30));
            }
        playerTurn.setText(turn % 2 == 0 ? "Player 1's turn" : "Player 2's turn");
        playerTurn.setBackground(turn % 2 == 0 ? Color.GREEN : Color.RED);
        leftGoalLabel.setText(Integer.toString(leftGoal));             //change the goals to the new values
        rightGoalLabel.setText(Integer.toString(rightGoal));
    }

    public void cupIsPressed(int x, int y) {        //this is our function for when a button is actually pressed
        boolean gameEnded = false;                  //this function is mostly for testing to see if the button was a valid input
        if (x != (turn % 2)) {                      //if the x coordinate does not line up with the turn 
            JFrame frame = new JFrame();            //we give them an error in the form of a jframe with a label
            frame.setSize(new Dimension(300, 100));
            frame.setName("Error");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setVisible(true);
            int player = x + 1;
            JLabel label = new JLabel("It is not Player " + player + "'s turn");
            frame.add(label);
            turn--;                                 //and we decrement the turn
        }
        else if (boardValues[x][y] == 0) {          //if the space doesn't have any values
            JFrame frame = new JFrame();            //we give another error in the form of the jframe
            frame.setSize(new Dimension(300, 100));
            frame.setName("Error");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setVisible(true);
            JLabel label = new JLabel("Cup does not have any marbles");
            frame.add(label);
            turn--;                             //and again decrement the turn
        } else {                                //if the move is valid 
            moveMarbles(x, y);                  //we send them to the move function where the actual gameplay happens
        }
        if (endOfGameChecker() && !gameEnded) {     //after this we test to see if the game has ended 
            String Winner = "";
            gameEnded = true;                       //if so we add up the totals 
            int playerOneTotal = 0, playerTwoTotal = 0;
            for (int j = 0; j < 6; j++)
                playerOneTotal += boardValues[0][j];

            for (int j = 0; j < 6; j++)
                playerTwoTotal += boardValues[1][j];

            for (int i = 0; i < 2; i++)             //set the board values to 0
                for (int j = 0; j < 6; j++)
                    boardValues[i][j] = 0;

            leftGoal += playerOneTotal;             //add the totals to the players goal
            rightGoal += playerTwoTotal;           

            if (leftGoal > rightGoal) {             //and test to see who won
                Winner = "Player 1 wins!";
            } else if (rightGoal > leftGoal) {
                Winner = "Player 2 wins!";
            } else if (rightGoal == leftGoal) {
                Winner = "Tie Game";
            }

            JFrame frame = new JFrame();            //we then give a jframe to give them the news
            JPanel panel = new JPanel();            //make a panel because we will be adding more than just a label
            frame.setSize(new Dimension(150, 100));
            frame.setName("Game Over");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setVisible(true);
            JLabel label = new JLabel(Winner);
            JButton playagainButton =new JButton("Play again?");                //create a button to reset the board
            playagainButton.addActionListener(e -> resetGameBoard(frame));      //set up the action listener
            frame.add(panel);               //add the panel to the frame 
            panel.add(label);               //then add the components to the panel 
            panel.add(playagainButton);
        }
        turn++;                         //we then increment the turn
        reSetNames();                   //reset the button text
    }

    public void resetGameBoard(JFrame frame)        //function for resetting the game board
    {
    for (int i = 0; i < 2; i++)                     //set all board values to 4
        for (int j = 0; j < 6; j++)
            boardValues[i][j] = 4;

    turn = 0;                                       //set goals and turn to 0
    leftGoal = 0;
    rightGoal = 0;
        reSetNames();                               //set the new names
        frame.dispose();                            //dispose of the reset frame
    }

    public boolean endOfGameChecker() {             //this is a function to test for the end of the game and all it does is return a boolean
        int playerOneTotal = 0, playerTwoTotal = 0;
        for (int j = 0; j < 6; j++)
            playerOneTotal += boardValues[0][j];

        for (int j = 0; j < 6; j++)
            playerTwoTotal += boardValues[1][j];

        return (playerOneTotal == 0 || playerTwoTotal == 0);
    }

    public void moveMarbles(int x, int y) {         //this is our main gameplay function where all the movement, capturing, and extra turns happen
        int currentX = x;                           //we have functions for where we are currently on the board as we add marbles to each cup
        int currentY = y + 1;                       //we start off by incrementing the Y incase it is directly next to a goal 
        while (boardValues[x][y] > 0) {             //while the starting goal still have marbles
            if (currentY == 6 && boardValues[x][y] != 0) {          //if the y is 6 then we need to move down/up the board and possibly add to the goal
                if (x == 0 && currentX == 0) {      //if both Xs are 0 (meaning it is player 1's turn) then we can add to the goal
                    leftGoal++;
                    boardValues[x][y]--;            //we decrement the starting cup
                    currentY = 0;                   //and set the current index to the bottom left most cup
                    currentX = 1;
                    if (boardValues[x][y] == 0) {   //and if it was the last marble we give player 1 an extra turn
                        turn--;
                    }
                } else if (x == 0 && currentX == 1) {       //if it is player 1's turn but we are not next to their goal we just set the index up to the top right
                    currentY = 0;
                    currentX = 0;
                } else if (x == 1 && currentX == 1) {       //same algorithm as player 1's
                    rightGoal++;
                    boardValues[x][y]--;
                    currentY = 0;
                    currentX = 0;
                    if (boardValues[x][y] == 0) {
                        turn--;
                    }
                } else if (x == 1 && currentX == 0) {
                    currentY = 0;
                    currentX = 1;
                }
            }
            if (boardValues[x][y] != 0) {           //if after the last if statement the starting cup still have marbles
                boardValues[x][y]--;                //we take one from the starting cup
                boardValues[currentX][currentY]++;  //add it to the current cup
                if (boardValues[x][y] == 0 && boardValues[currentX][currentY] == 1 && currentX == x) {      //if it was the last marble from the starting position 
                    int oppositeX = 0, total = 0, oppositeY = 0;        //and the current cup was empty before, and we are currently in the player's own cups then we possibly can capture
                    if (currentX == 0)              //if it was player one then the opposite x index is player 2's
                        oppositeX = 1;
                    else if (currentX == 1)
                        oppositeX = 0;

                    switch (currentY) {             // a switch for the Y coordinates because I dont think there is an algorithm to come up with them mathmatically
                    case 0:
                        oppositeY = 5;
                        break;
                    case 1:
                        oppositeY = 4;
                        break;
                    case 2:
                        oppositeY = 3;
                        break;
                    case 3:
                        oppositeY = 2;
                        break;
                    case 4:
                        oppositeY = 1;
                        break;
                    case 5:
                        oppositeY = 0;
                        break;
                    }
                    if (boardValues[oppositeX][oppositeY] != 0) {           //if the opposite cup has marbles then we can capture
                        total = boardValues[oppositeX][oppositeY] + boardValues[currentX][currentY];        //add up the total
                        boardValues[oppositeX][oppositeY] = 0;          //set the current and opposite cup to 0
                        boardValues[currentX][currentY] = 0;
                        if (turn % 2 == 0) {                    //determine the player and add to their goal
                            leftGoal += total;
                        } else if (turn % 2 == 1) {
                            rightGoal += total;
                        }
                    }
                }
                currentY++;                 //increment the current Y index
            }
        }
    }
}