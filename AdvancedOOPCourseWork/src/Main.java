import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Creating an input scanner for application
        Scanner sInput = new Scanner(System.in);
        // Declaration of model for the Main
        BSModel model = new BSModel();

        // Declaring booleans for input and mode selection varification
        boolean bModeSelected = false;
        boolean bValidInput;

        // Defining a string for bombing co-ords that will be entered by user
        String sBombingCoords;

        // defining a local variable for the grid within model
        Cell[][] lGrid = model.getGrid();

        // While loop for user to select if they want to load from file or use pre generated
        while (!bModeSelected){
            System.out.println("Chose Game Option (Computer Generated or From File)");
            System.out.print("Enter 'C' for computer & 'F' for file: ");

            String Choice = sInput.next();

            if(Choice.equals("C")){
                model.loadShipsComputer();
                bModeSelected = true;
            }
            else if(Choice.equals("F")){
                model.loadShipsFile();
                bModeSelected = true;
            }
            else System.out.println("Invalid game option!");
        }

        /*
         While loop for the main game, a player selects a cell every iteration and once all ships sunk
         the while loop will break out. this handles the input and output of the application via the CLI
        */
        while (!model.getGameOver()) {

            // Output - Printing of the grid
            System.out.println("    A  B  C  D  E  F  G  H  I  J ");
            for (int x = 0; x < 10; x++) {
                System.out.print(x == 9 ? (x + 1) + " " : " " + (x + 1) + " ");
                for (int y = 0; y < 10; y++) {
                    if (lGrid[y][x].getContainsSegment()) {
                        if (lGrid[y][x].getSegment().getShip().getShipSunk()) {
                            System.out.print(" S ");
                        } else if (lGrid[y][x].getBombedState()) {
                            System.out.print(" H ");
                        }
                        else System.out.print(" ~ ");
                    } else if (lGrid[y][x].getBombedState()) {
                        System.out.print(" M ");
                    } else System.out.print(" ~ ");
                }
                System.out.println();
            }
            System.out.println("Bombs Fired: " + model.getBombsFired());

            // Input - Handling user input and bombing relevant square
            System.out.print("Enter bombing Coordinates: ");
            sBombingCoords = sInput.next();
            bValidInput = false;

            for(Cell[] row: lGrid){
                for (Cell cell : row){
                    if (cell.getName().equals(sBombingCoords)) {
                        cell.setBombedState(true);
                        if(cell.getContainsSegment()) {
                            cell.getSegment().setBombedState(true);
                        }
                        bValidInput = true;
                        model.setBombsFired();
                    }
                }
            }
            for(BattleShip ship: model.getShips()){
                ship.setShipSunk();
            }
            if(!bValidInput){
                System.out.println("Invalid input!");
            }
        }
        // Once the while loop has broken out it will print the game won line including total bombs fired
        System.out.println("Game Won! - All Enemy Ships Successfully Sunk In " + model.getBombsFired() + " Bombs");
    }
}



