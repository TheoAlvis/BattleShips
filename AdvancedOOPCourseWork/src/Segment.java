public class Segment {
    // declaration for all variables associated with a segment object
    private boolean bBombedState;
    private final String sName;
    private final BattleShip oShip;

    // Constructor for Segment sets name and battleship
    public Segment(String name, BattleShip ship){
        sName = name;
        oShip = ship;
    }
    // Getter for segment name
    public String getName(){
        return sName;
    }

    // Getter and setter for bombed state
    public boolean getBombedState(){
        return bBombedState;
    }

    public void setBombedState(boolean state){
        bBombedState = state;
    }

    // Getter for Battleship object the segment belongs too
    public BattleShip getShip(){
        return oShip;
    }
}
