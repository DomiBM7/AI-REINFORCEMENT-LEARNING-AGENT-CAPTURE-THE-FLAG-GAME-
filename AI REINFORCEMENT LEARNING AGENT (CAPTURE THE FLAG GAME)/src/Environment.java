import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Environment {
    private final int rows;
    private final int cols;
    private final char[][] grid;
    private final Random random;
    private final List<Bullet> bullets;

    public Environment(int rows, int cols, int numEnemies) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new char[rows][cols];
        this.random = new Random();
        this.bullets = new ArrayList<>();
        initializeEnvironment();
    }

    void initializeEnvironment() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = ' ';
            }
        }

        grid[0][cols-1] = 'F';

        // Place traps at fixed positions
        grid[7][10] = 'T';
        grid[3][15] = 'T';
        grid[15][4] = 'T';
        // Add more trap positions as needed

        // Place lifelines at fixed positions
        grid[1][1] = 'L';
        grid[16][18] = 'L';
        grid[1][12] = 'L';
        grid[12][18] = 'L';
        

        // Place bullets at fixed positions
        grid[17][5] = 'B';
        grid[3][12] = 'B';
        // Add more bullet positions as needed

        grid[0][0] = 'S'; 
        grid[8][8] = 'S'; 
    }
    
    public void resetBullets() {
        bullets.clear();
    }

    public void placeFlag() {
        // Place the flag in a random location
        Random random = new Random();
        int flagX = 0;
        int flagY = cols - 1;
        
        //setCell(flagX, flagY, 'F');
    }
    
    public void setCell(int x, int y, char value) {
        grid[x][y] = value;
    }


    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public char getCell(int x, int y) {
        return grid[x][y];
    }

    public char[][] getGrid() {
        return grid;
    }

    public void moveBullet() {
        Iterator<Bullet> iterator = bullets.iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            bullet.move();

            if (bullet.getX() < 0 || bullet.getX() >= rows || bullet.getY() < 0 || bullet.getY() >= cols) {
                iterator.remove();
                continue;
            }

            char cell = getCell(bullet.getX(), bullet.getY());
            if (cell == 'E' || cell == 'A') {
                removeEntity(bullet.getX(), bullet.getY());
                iterator.remove();
            }
        }
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public void addBullet(int x, int y, int directionX, int directionY) {
        bullets.add(new Bullet(x, y, directionX, directionY));
    }

    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < rows && y >= 0 && y < cols;
    }

    public void removeEntity(int x, int y) {
        if (isValidPosition(x, y)) {
            grid[x][y] = ' ';
        }
    }
}
