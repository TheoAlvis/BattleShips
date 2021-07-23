import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Scanner;

public class BSModel extends Observable {
    // Defines the game board this is stored within a 2d array of object type Cell
    private final Cell[][] lGrid = new Cell[10][10];
    // Defines the list of ships in the game stored in an array list of type BattleShip
    private ArrayList<BattleShip> lShips;
    // Defines the list of letters used for grid reference as an array list of Strings from A-J
    private final ArrayList<String> lLetters = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J"));
    // Defines the int for keeping track of total bombs fired
    private int nBombsFired = 0;
    // Defines the int size of a cell to the value of 50
    private final int nSize = 50;

    // Constructor for BSModel when defined the constructor will call initialise()
    public BSModel() {
        initialise();
    }

    // The initialise function is called called on the declaration of the model through a call in the constructor
    public void initialise() {
        assert invariantGridEmpty(lGrid) : "Array of cells already contains one or more values";
        assert(lShips == null) : "Array of ships already initialized";
        lShips = new ArrayList<>();
        /*
         Nested for loop used to loop through each element in the 2 dimensional array
         sets each element to have a new cell object with the corresponding cell name and
         as well as the correct x and y location.
        */
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                lGrid[x][y] = new Cell(lLetters.get(x) + (y+1), x, y);
            }
        }
        assert !invariantGridEmpty(lGrid) : "Array of cells initializing failed, array is empty";
        assert(lShips != null) : "Array of ships not initialized";
        notifyObservers();
    }

    // This function is used when the user wants to load a preset ship layout from a txt file
    public void loadShipsFile(){
        /*
         lShipConfig is an ArrayList of lists of string used to store each line of the config file as the set structure
         the first index being the size, second being the starting location and thirdly the direction the ship is facing.
        */
        ArrayList<String[]> lShipConfig = new ArrayList<>();
        /*
        Try and catch for loading the config from file, defines the path and a scanner linked to the path.
        While the file contains a next line the line is split where a ',' is present and stores the values in
        lShipConfig at a new index.
        expected input: size, starting coordinate, direction
        If the file loading fails then an error is displayed in console.
        */
        try {
            File fShipConfig = new File("C:\\Users\\theoa\\IdeaProjects\\AdvancedOOPCourseWork\\src\\shipConfig.txt");
            Scanner sReader = new Scanner(fShipConfig);
            while (sReader.hasNextLine()) {
                lShipConfig.add(sReader.nextLine().split(","));
            }
            sReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File Loading Error: File path may be incorrect!");
            e.printStackTrace();
        }
        /*
        for loop used to turn the instructions stored in the ArrayList of String[] into the parameters
        required for creating a new ship and then later on creates all of the corresponding segments
        for the ship object.
        */
        for(String[] lConfig : lShipConfig){
            // Defining the integers for x and y offset used to determine and alter direction of ship
            int nXOffset = 0;
            int nYOffset = 0;
            /*
            Switch statement takes in the element at index 2 of the lConfig array, if the correct structure
            has been followed this will be a string for direction either up, down, left or right.
            the offset for x and y are set accordingly
            */
            switch(lConfig[2]){
                case "up": nYOffset = -1;
                    break;
                case "down": nYOffset = 1;
                    break;
                case "left": nXOffset = -1;
                    break;
                case "right" : nXOffset = 1;
                    break;
            }
            // Setting starting row and column to that specified in the config file
            int nRow = getXIntValue(lConfig[1]);
            int nCol = Integer.parseInt(lConfig[1].substring(1));
            // Adding new ship defined
            lShips.add(new BattleShip());
            // creating each segment based off starting location and offset each iteration
            for(int i = 0; i < Integer.parseInt(lConfig[0]); i++){
                lShips.get(lShips.size()-1).setSegment(lLetters.get(nRow-1)+(nCol));
                nRow += nXOffset;
                nCol += nYOffset;
            }
        }
        positionShips();
    }

    // This function is used to load ships from the application rather than file all values are hard coded
    public void loadShipsComputer(){
        // Creating ship one length 5
        lShips.add(new BattleShip());
        lShips.get(0).setSegment("B2");
        lShips.get(0).setSegment("C2");
        lShips.get(0).setSegment("D2");
        lShips.get(0).setSegment("E2");
        lShips.get(0).setSegment("F2");
        // Creating ship two length 4
        lShips.add(new BattleShip());
        lShips.get(1).setSegment("I3");
        lShips.get(1).setSegment("I4");
        lShips.get(1).setSegment("I5");
        lShips.get(1).setSegment("I6");
        // Creating ship three length 3
        lShips.add(new BattleShip());
        lShips.get(2).setSegment("C8");
        lShips.get(2).setSegment("D8");
        lShips.get(2).setSegment("E8");
        // Creating ship four length 2
        lShips.add(new BattleShip());
        lShips.get(3).setSegment("B5");
        lShips.get(3).setSegment("B6");
        // Creating ship five length 2
        lShips.add(new BattleShip());
        lShips.get(4).setSegment("H9");
        lShips.get(4).setSegment("I9");

        positionShips();
    }

    // position ships function is to to iterate through each ship and their segements and
    // comparing their saved cell name to each cell in the grid and referencing the segment
    // object in the cell object when a match is found.
    public void positionShips(){
        assert invariantShipHasSegments(lShips) : "One or more ships has no assigned segments";
        assert !invariantGridEmpty(lGrid) : "Array of cells missing one or more objects";
        for(BattleShip ship : lShips){
            for(Segment segment: ship.getSegments()){
                for (Cell[] row : lGrid) {
                    for (Cell  cell : row) {
                        if(cell.getName().equals(segment.getName())){
                            assert cell.getSegment() == null : "Error loading Ships! Intersection in cell: " + cell.getName();
                            cell.setSegment(segment);
                        }
                    }
                }
            }
        }
    }

    // Getter for the game grid in form of a 2D array of Cell Objects
    public Cell[][] getGrid(){
        assert !invariantGridEmpty(lGrid) : "One or more cells in the grid is empty so cannot be returned";
        return lGrid;
    }

    // Getter for the list of ships in form of an arraylist of BattleShip Objects.
    public ArrayList<BattleShip> getShips(){
        assert invariantHasShips(lShips) : "5 Ships do not exist so ships cannot be returned";
        return lShips;
    }

    /*
     The purpose of this function is to determine when a game is end by checking if the
     sunk state of all ships on the board. if all sunk states are true it will return true
    */
    public boolean getGameOver(){
        assert invariantHasShips(lShips) : "List of ships empty unable to continue with game end algorithm";
        boolean bGameOver = true;
        for(BattleShip ship : lShips){
            assert ship != null : "Ship Does not exist!";
            if(!ship.getShipSunk()){
                bGameOver = false;
                break;
            }
        }
        return bGameOver;
    }

    /*
     This function calculates the total number of bombs fired by totalling up the number
     of cells with ture bombed states.
    */
    public void setBombsFired(){
        assert !invariantGridEmpty(lGrid) : "Array of cells missing one or more objects";
        nBombsFired = 0;
        for(Cell[] row : lGrid) {
            for(Cell cell : row) {
                if(cell.getBombedState()){
                    nBombsFired++;
                }
            }
        }
        assert invariantBombCount(nBombsFired) : "Bomb Count Bellow 0 - Invalid bomb count";
    }

    // Getter for the total number of bombs fired calculated in setBombsFired()
    public int getBombsFired(){
        assert invariantBombCount(nBombsFired) : "Bomb Count Bellow 0 - Invalid bomb count";
        return nBombsFired;
    }

    /*
     Function for converting the letter in the cell name to the
     Corresponding int Value representing the row
    */
    public int getXIntValue(String name){
        assert invariantHasLetter(name) : "The cell name entered does not have a valid letter! must be A-J";
        int nRow = 0;

        switch(name.charAt(0)){
            case 'A': nRow = 1;
                break;
            case 'B': nRow = 2;
                break;
            case 'C': nRow = 3;
                break;
            case 'D': nRow = 4;
                break;
            case 'E': nRow = 5;
                break;
            case 'F': nRow = 6;
                break;
            case 'G': nRow = 7;
                break;
            case 'H': nRow = 8;
                break;
            case 'I': nRow = 9;
                break;
            case 'J': nRow = 10;
                break;
        }
        assert nRow >= 1;
        return nRow;
    }

    // Getter for the final int SIZE
    public int getSIZE(){
        assert nSize == 50 : "nSize should be set to the value of 50";
        return nSize;
    }

    // Invariant for checking a cells name has a valid letter (A-J)
    public boolean invariantHasLetter(String name){
        boolean bHasLetter = false;
        for(String l: lLetters){
            if(name.startsWith(l)){
                bHasLetter = true;
                break;
            }
        }
        return bHasLetter;
    }

    // Invariant for checking the list of cells for the grid is full
    public boolean invariantGridEmpty(Cell[][] grid){
        boolean bEmpty = false;
        for(Cell[] row : grid){
            for(Cell cell : row){
                if(cell == null){
                    bEmpty = true;
                    break;
                }
            }
        }
        return bEmpty;
    }

    // Invariant for checking a ship has segments
    public boolean invariantShipHasSegments(ArrayList<BattleShip> ships){
        boolean bHasSegments = true;
        for(BattleShip ship : ships){
            if(ship.getSegments().isEmpty()){
                bHasSegments = false;
                break;
            }
        }
        return bHasSegments;
    }

    // Invariant for checking that there are 5 ships
    public boolean invariantHasShips(ArrayList<BattleShip> ships){
        boolean bHasShips = true;
        for(int i = 0; i < 5; i++){
            if(ships.get(i) == null){
                bHasShips = false;
                break;
            }
        }
        return bHasShips;
    }

    // Invariant for making sure bomb count is greater than or equal to 0
    public boolean invariantBombCount(int bombsFired){
        return bombsFired >= 0;
    }
}