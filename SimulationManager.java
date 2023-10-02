package application;

import java.util.Random;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SimulationManager {
    private boolean isColourable = true;
    private boolean generate;
    private boolean threadActive;
    
    private static Color[] colors = {Color.BLUE, Color.CRIMSON,Color.CHOCOLATE,
			Color.CORAL,Color.GREEN,Color.DEEPPINK,Color.PURPLE,Color.YELLOW};
    
    public void startSimulation(Rectangle[][] map) {
    	generate = true;
        isColourable = false;
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
                isColourable = true;
            });
            generationThread.start();
        }
    }

    public void stopSimulation() {
        generate = false;
        threadActive = false;
    }

    public void clearBoard(Rectangle[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j].setFill(Color.WHITE);
            }
        }
	isColourable = true;
        generate = false;
    }
    
    public boolean isColourable() {
    	return isColourable;
    }
    
    private void getNextGen(Rectangle[][] map) {
        Boolean[][] colorMap = new Boolean[map.length][map[0].length];

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (willBeAlive(map, i, j)) {
                    colorMap[i][j] = true;
                } else {
                    colorMap[i][j] = false;
                }
            }
        }

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
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
				row = (x-1+i+map.length)%map.length;
				column = (y-1+j+map[0].length)%map[0].length;
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
    
	public void toggleColor(Rectangle cell) {
		if (cell.getFill()==Color.WHITE) {
			cell.setFill(getRandomColor());
		} else {
			cell.setFill(Color.WHITE);
		}
	}
    
    private static Color getRandomColor() {
        Random random = new Random();
		return colors[random.nextInt(colors.length)];  
    }
}
