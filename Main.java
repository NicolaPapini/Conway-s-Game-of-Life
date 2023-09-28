package application;
	
import java.util.Random;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Main extends Application {
	private static final int NUM_ROWS = 58;
	private static final int NUM_COLUMNS = 29;
	private static final int CELL_SIZE = 25;
	private static boolean isDrawing = true;
	private static boolean generate = true;
	private static boolean threadActive = false;
	private static Color[] colors = {Color.BLUE, Color.CRIMSON,Color.CHOCOLATE,
			Color.CORAL,Color.GREEN,Color.DEEPPINK,Color.PURPLE,Color.YELLOW};
	
	
	public static void main(String[] args) {
		launch(args);  
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setWidth(1524);
		primaryStage.setHeight(1000);
		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image("Logo.png"));
			
		Pane rootPane = new Pane();
        Scene scene = new Scene(rootPane);
		GridPane gridPane = new GridPane(); //root
		Rectangle[][] map = initializeGrid(gridPane);

		Button startButton = createNiceButton("Start",220,106,439,805);
		Button stopButton = createNiceButton("Stop",220,106,662,805);
		Button clearButton = createNiceButton("Clear",220,106,885,805);
		
        startButton.setOnAction(event -> startSimulation(map));
		stopButton.setOnAction(event-> stopSimulation());
		clearButton.setOnAction(event->clearBoard(map));	
		
        rootPane.getChildren().addAll(gridPane, startButton,stopButton,clearButton);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Conway's Game of Life DEMO");
		primaryStage.show();
	}

	
	private static Button createNiceButton(String name,int width, int height,int x, int y) {
		Button button = new Button(name);
		button.setMinWidth(width);
		button.setMinHeight(height);
		button.setLayoutX(x); 
		button.setLayoutY(y);
		button.setStyle("-fx-border-color: #000000; -fx-border-width: 1px;-fx-font-family: Arial;"
				+ " -fx-font-size: 50px;-fx-background-color: #FF0000; -fx-text-fill: #FFFFFF");
		
		return button;
		
	}	
		
	private static Rectangle[][] initializeGrid(GridPane gridPane) {
		Rectangle[][] map = new Rectangle[NUM_ROWS][NUM_COLUMNS];
		
		for (int i = 0; i < NUM_ROWS; i++) {
			for (int j = 0; j < NUM_COLUMNS; j++) {
				Rectangle current = new Rectangle(CELL_SIZE,CELL_SIZE);
				current.setFill(Color.WHITE);
				current.setStroke(Color.BLACK);
				current.setOnMousePressed(event->{
					if (isDrawing) {
						toggleColor(current);
					}
				});
				gridPane.add(current, i, j);
				map[i][j] = current;
			}
		}
		return map;
	}
	
	private static void getNextGen(Rectangle[][] map) {
		Boolean[][] colorMap = new Boolean[NUM_ROWS][NUM_COLUMNS];
		
		for (int i = 0; i < NUM_ROWS; i++) {
			for (int j = 0; j < NUM_COLUMNS; j++) {
				
				if (willBeAlive(map, i, j)) {
					colorMap[i][j] = true;
				} else {
					colorMap[i][j] = false;
				}
			}
		}
		
		for (int i = 0; i < NUM_ROWS; i++) {
			for (int j = 0; j < NUM_COLUMNS; j++) {
				if (colorMap[i][j]) {
					map[i][j].setFill(getRandomColor());
				} else {
					map[i][j].setFill(Color.WHITE);
				}
			}	
		}
	}
	

    
	private static boolean willBeAlive(Rectangle[][] map, int x, int y) {
		int aliveNeighbors = 0;
		int row,column;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				row = (x-1+i+NUM_ROWS)%NUM_ROWS;
				column = (y-1+j+NUM_COLUMNS)%NUM_COLUMNS;
				if (row==x && column==y) {
					continue;
				}
				if (map[row][column].getFill()!=Color.WHITE) {
					aliveNeighbors++;
				}
			}
		}
		
		return ( (map[x][y].getFill()!=Color.WHITE && (aliveNeighbors<2 || aliveNeighbors>3)) || 
				((map[x][y].getFill()==Color.WHITE) && (aliveNeighbors!=3)))? false: true;
	}
    
   	private void startSimulation(Rectangle[][] map) {
	        generate = true;
	        isDrawing = false;
	        if (!threadActive) {
	            threadActive = true;
	            Thread generationThread = new Thread(() -> {
	                while (generate) {
	                    Platform.runLater(() -> getNextGen(map));
	                    try {
	                        Thread.sleep(200);
	                    } catch (InterruptedException e) {
	                        Thread.currentThread().interrupt();
	                    }
	                }
	                isDrawing = true;
	            });
	            generationThread.start();
	        }
	    }
    
	private void stopSimulation() {
		generate = false;
		threadActive = false;
    	}
	    
	private void clearBoard(Rectangle[][] map) {
		for (int i = 0; i < NUM_ROWS; i++) {
			for (int j = 0; j < NUM_COLUMNS; j++) {
				map[i][j].setFill(Color.WHITE);;					
			}	
		}
	}
	
	private static void toggleColor(Rectangle cell) {
		if (cell.getFill() != Color.WHITE) {
			cell.setFill(Color.WHITE);
		} else {
			cell.setFill(getRandomColor());
		}
	}
	
	private static Color getRandomColor() {
		Random random = new Random();
		return colors[random.nextInt(colors.length)];  
    	}
}
