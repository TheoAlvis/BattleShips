public class Cell {

    // Declaration of all variables stored within a Cell Object
    private boolean bBombedState;
    private Segment oSegment = null;
    private final String sName;
    private final int nRow;
    private final int nCol;

    // Constructor for Cell takes in name, row and col
    public Cell(String name, int row, int col){
        bBombedState = false;
        sName = name;
        nRow = row;
        nCol = col;
    }

    // Getter and setter for bBombedState
    public boolean getBombedState() {
        return bBombedState;
    }

    public void setBombedState(boolean bombed) {
        bBombedState = bombed;
    }

    // Getter and setter for Segment
    public Segment getSegment(){
        return oSegment;
    }

    public void setSegment(Segment segment){
        oSegment = segment;
    }

    // Getter for name
    public String getName(){
        return sName;
    }

    // Getters for row and col
    public int getRow(){
        return nRow;
    }

    public int getCol(){
        return nCol;
    }

    // Getter for checking a cell contains a segment
    public boolean getContainsSegment(){
        boolean bContainsSegment;
        bContainsSegment = getSegment() != null;
        return  bContainsSegment;
    }
}
