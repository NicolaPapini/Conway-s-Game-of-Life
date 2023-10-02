package application;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class UIManager {
	private Pane rootPane;
	SimulationManager sim = new SimulationManager();
	
	public UIManager(Pane root) {
		this.rootPane = root;
	}
	
	public Pane getRootPane() {
		return rootPane;
	}
	
	public void createUI(int rows, int columns, int cellSize) {
		GridPane gridPane = new GridPane(); 
		
		Rectangle[][] map = initializeGrid(gridPane,sim,rows,columns,cellSize);
		
		Button startButton = createNiceButton("Start",220,106,439,805);
		Button stopButton = createNiceButton("Stop",220,106,662,805);
		Button clearButton = createNiceButton("Clear",220,106,885,805);
		
        startButton.setOnAction(event -> sim.startSimulation(map));
        stopButton.setOnAction(event -> sim.stopSimulation());
        clearButton.setOnAction(event -> sim.clearBoard(map));	
		
        rootPane.getChildren().addAll(gridPane, startButton,stopButton,clearButton);
	}
	
    private Rectangle[][] initializeGrid(GridPane gridPane, SimulationManager simulationManager,int rows, int columns, int cellSize) {
    	Rectangle[][] cellGrid = new Rectangle[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
            	Rectangle current = new Rectangle(cellSize,cellSize);
                current.setFill(Color.WHITE);
                current.setStroke(Color.BLACK);   
                current.setOnMousePressed(event -> {
                    if (simulationManager.isColourable()) {
                    	sim.toggleColor(current);
                    }
                });
                gridPane.add(current, i, j);
                cellGrid[i][j] = current;
            }
        }
        return cellGrid;
    }
	
	private static Button createNiceButton(String name,int width, int height,int x, int y) {
		Button button = new Button(name);
		button.setMinWidth(width);
		button.setMinHeight(height);
		button.setLayoutX(x); 
		button.setLayoutY(y);
	    button.setStyle(
	            "-fx-border-color: #000000; " +
	            "-fx-border-width: 1px; " +
	            "-fx-font-family: Arial; " +
	            "-fx-font-size: 50px; " +
	            "-fx-background-color: #FF0000; " +
	            "-fx-text-fill: #FFFFFF;"
	        );
		return button;
	}
	

	

}
