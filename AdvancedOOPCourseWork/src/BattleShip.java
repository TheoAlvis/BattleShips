import java.util.ArrayList;

public class BattleShip {
    // Declaring the list of segments a ship is made up of
    private final ArrayList<Segment> lSegments;
    // Declaring the boolean bSunk to determine if a ship is sunk or not
    private boolean bSunk;

    // Constructor for a Battleship object sets sunk state to false and initializes lSegments
    public BattleShip( ){
        bSunk = false;
        lSegments = new ArrayList<>();
    }

    // Function for setting a Segment of the ship based of the cell name entered in as a parameter
    public void setSegment(String name){
        lSegments.add(new Segment(name, this));
    }

    // Getter for the ships list of Segments
    public ArrayList<Segment> getSegments() {
        return lSegments;
    }

    // Setter for determining if the ship is sunk or not
    public void setShipSunk(){
        bSunk = true;
        for(Segment segment: lSegments){
            if(!segment.getBombedState()){
                bSunk = false;
                break;
            }
        }
    }

    // Getter for bSunk the sunk state of the ship
    public boolean getShipSunk(){
        return bSunk;
    }

}
