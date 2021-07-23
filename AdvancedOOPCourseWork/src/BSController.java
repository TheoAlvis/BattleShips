public class BSController {
    // Declarations of BSModel and BSGraphicalView
    private final BSModel model;
    private BSGraphicalView view;

    // Constructor for BSController, sets the model through the parameter
    public BSController(BSModel model) {
        this.model = model;
    }

    /*
     Function for handling mouse click and bombing the appropriate cell based on mouse
     X and Y position. updates the view with the changed values
    */
    public void mouseClick(double posX, double posY){
        Cell[][] lGrid = model.getGrid();

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {

                if(locateCell(posX,posY, lGrid[x][y].getRow(), lGrid[x][y].getCol())) {
                    lGrid[x][y].setBombedState(true);
                    if (lGrid[x][y].getContainsSegment()) {
                        lGrid[x][y].getSegment().setBombedState(true);
                    }
                }
            }
        }
        model.setBombsFired();

        for(BattleShip ship: model.getShips()){
            ship.setShipSunk();
        }

        view.update(null,null);
    }

    // function for locating the cell that the Mouse X and Y position are within
    public boolean locateCell(double mouseX, double mouseY, int cellX, int cellY){
        boolean bCellLocated;

        cellX = cellX * model.getSIZE() + model.getSIZE();
        cellY = cellY * model.getSIZE() + model.getSIZE();

        bCellLocated = (mouseX >= cellX && mouseX <= cellX + model.getSIZE()) &&
                (mouseY >= cellY && mouseY <= cellY + model.getSIZE());

        return  bCellLocated;
    }

    // Communication between view and model for loading ships from file
    public void loadFile(){
        model.loadShipsFile();
    }

    // Communication between view and model for loading ships from pre set
    public void loadComputer(){
        model.loadShipsComputer();
    }

    // Function for setting the view within the controller to be the main graphical view
    public void setView(BSGraphicalView view) {
        this.view = view;
    }
}