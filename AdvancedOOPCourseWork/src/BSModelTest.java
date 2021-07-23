import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class BSModelTest {

    // Test against positionShips checks that ship segments are assigned to correct Cell
    @org.junit.jupiter.api.Test
    void positionShips() {
        BSModel model = new BSModel();
        model.loadShipsComputer();

        Cell[][] grid = model.getGrid();
        ArrayList<BattleShip> ships = model.getShips();

        // Checking Ship one of length 5 is plotted in correct cells
        assertEquals(ships.get(0).getSegments().get(0).getName(), grid[1][1].getName());
        assertEquals(ships.get(0).getSegments().get(1).getName(), grid[2][1].getName());
        assertEquals(ships.get(0).getSegments().get(2).getName(), grid[3][1].getName());
        assertEquals(ships.get(0).getSegments().get(3).getName(), grid[4][1].getName());
        assertEquals(ships.get(0).getSegments().get(4).getName(), grid[5][1].getName());
    }

    // Test against getGameOver checks that the state of game over is true when all ships sunk
    @org.junit.jupiter.api.Test
    void getGameOver() {
        BSModel model = new BSModel();
        model.loadShipsComputer();

        ArrayList<BattleShip> ships = model.getShips();

        for(BattleShip ship : ships){
            for(Segment segment : ship.getSegments()){
                segment.setBombedState(true);
                ship.setShipSunk();
            }
        }
        // Checking game over is true
        assertTrue(model.getGameOver());
    }

    // Test against getBombsFired fired to make sure the correct bomb count is returned
    @org.junit.jupiter.api.Test
    void getBombsFired() {
        BSModel model = new BSModel();
        model.loadShipsComputer();

        model.getGrid()[1][1].setBombedState(true);
        model.getGrid()[1][2].setBombedState(true);
        model.getGrid()[1][3].setBombedState(true);
        model.getGrid()[1][4].setBombedState(true);
        model.getGrid()[1][5].setBombedState(true);

        model.setBombsFired();

        // Checks that 5 bombs have been fired
        assertEquals(model.getBombsFired(), 5);
    }
}