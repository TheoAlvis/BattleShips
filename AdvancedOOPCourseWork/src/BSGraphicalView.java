import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

public class BSGraphicalView extends Application implements Observer {

    // Declaration of all private variables used throughout BSGraphicalView
    private final ArrayList<String> lLetters = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J"));
    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 650;
    private static final int CELL_SIZE = 50;
    private BSModel model;
    private BSController controller;
    private GraphicsContext gc;

    /*
     Start Function for BSGraphicalView sets up the core scene and adds all of the appropriate content
     First loads up the selection page for a user to choose to play from file or pre set values
     Once a user has selected the update method is called and the game screen is displayed
    */
    @Override
    public void start(Stage primaryStage) {

        // Setting up the model and controller and adding the view references
        model = new BSModel();
        controller = new BSController(model);
        controller.setView(this);
        model.addObserver(this);

        // Creation of the root, canvas and gc
        Group root = new Group();
        Canvas canvas = new Canvas();
        canvas.setHeight(WINDOW_HEIGHT);
        canvas.setWidth(WINDOW_WIDTH);
        gc = canvas.getGraphicsContext2D();

        // Event handler for mouse click on the canvas
        EventHandler<MouseEvent> handler = event ->
                controller.mouseClick(event.getX(),event.getY());
        canvas.addEventFilter(MouseEvent.MOUSE_CLICKED, handler);

        // Making canvas a child of root
        root.getChildren().add(canvas);
        // Styling for scene and setting primary stage to the scene
        primaryStage.setTitle("Battle Ships");
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);

        // Styling the gc canvas
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(0,0,WINDOW_WIDTH,WINDOW_HEIGHT);

        // Title styling and initialization
        Text title = new Text("Battle Ships");
        title.setX(140);
        title.setY(100);
        title.setFont(new Font("Arial", 60));

        // File upload styling and initialization
        Button fileUpload = new Button("Load From File");
        fileUpload.setLayoutX(100);
        fileUpload.setLayoutY(300);
        fileUpload.setMinSize(400,50);
        fileUpload.setMaxSize(400,50);
        fileUpload.setFont(new Font("Arial", 30));

        // Computer generated styling and initialization
        Button computerGenerated = new Button("Computer Generated");
        computerGenerated.setLayoutX(100);
        computerGenerated.setLayoutY(200);
        computerGenerated.setMinSize(400,50);
        computerGenerated.setMaxSize(400,50);
        computerGenerated.setFont(new Font("Arial", 30));

        // File upload button action and triggers
        fileUpload.setOnAction(Event ->{
            controller.loadFile();
            update(null,null);
            fileUpload.setVisible(false);
            computerGenerated.setVisible(false);
            title.setVisible(false);
        });

        // Computer generated button action and triggers
        computerGenerated.setOnAction(event ->{
            controller.loadComputer();
            update(null,null );
            computerGenerated.setVisible(false);
            fileUpload.setVisible(false);
            title.setVisible(false);
        });

        // Adding the title, fileUpload and computerGenerated to the root
        root.getChildren().add(title);
        root.getChildren().add(fileUpload);
        root.getChildren().add(computerGenerated);

        // Launches the primary stage
        primaryStage.show();
    }

    // When called the initialize function sets the background colour and displays the cell axis labels
    public void initialize(){

        // Styling for gc canvas
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(0,0,WINDOW_WIDTH,WINDOW_HEIGHT);

        // Creating the row Axis
        for(int i = 1; i<=10; i++){
            gc.setFill(Color.BLACK);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setFont(new Font("Arial", 40));
            gc.fillText(Integer.toString(i), 25, i*50+40, 100);
        }

        // Creating the column Axis
        for(int i = 0; i<10; i++){
            gc.setFill(Color.BLACK);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setFont(new Font("Arial", 40));
            gc.fillText(lLetters.get(i), (i+1)*50+25, 40, 100);
        }
    }

    /*
     Update is called one the observer senses change in the Model and will in turn display
     the game grid and its current visual appearance depending on the state of each cell and ship
    */
    @Override
    public void update(Observable o, Object arg) {

        gc.clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        initialize();

        // Logic for displaying the grid and appropriate hit markers
        for(Cell[] row : model.getGrid()) {
            for(Cell cell : row){
                gc.setFill(Color.BLACK);
                gc.fillRect(cell.getRow()*CELL_SIZE+CELL_SIZE, cell.getCol()*CELL_SIZE+CELL_SIZE, CELL_SIZE, CELL_SIZE);

                if(cell.getContainsSegment() && cell.getSegment().getShip().getShipSunk()){
                        gc.setFill(Color.GREY);
                }
                else gc.setFill(Color.MEDIUMTURQUOISE);

                gc.fillRect(cell.getRow()*CELL_SIZE+CELL_SIZE+1, cell.getCol()*CELL_SIZE+CELL_SIZE+1, CELL_SIZE-2, CELL_SIZE-2);

                if(cell.getBombedState() && !cell.getContainsSegment()){
                    gc.setFill(Color.WHITE);
                }
                else if(cell.getContainsSegment() && cell.getBombedState()){
                    gc.setFill(Color.RED);
                }

                gc.fillOval(cell.getRow()*CELL_SIZE+CELL_SIZE+10, cell.getCol()*CELL_SIZE+CELL_SIZE+10, 30, 30);
            }
        }

        // Logic for when the game ends to display the game over banner
        if(model.getGameOver()){

            gc.setFill(Color.DARKGREY);
            gc.fillRect(50, 250, 500,100);

            gc.setFill(Color.BLACK);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setFont(new Font("Arial", 40));
            gc.fillText("Game Over! \n All Enemy Ships Successfully Cleared", 300, 290, 450);
        }

        // Logic for the bomb counter at bottom of game screen
        gc.setFill(Color.DARKRED);
        gc.setFont(new Font("Arial", 30));
        gc.fillText("Bombs Fired: " + model.getBombsFired() , 300, 600, 200);
    }

    // The main is used to launch the application
    public static void main(String[] args) {
        launch(args);
    }
}
