import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CaptureTheFlagGame extends JPanel {
    private static final int CELL_SIZE = 30;
    private final Environment environment;
    private final Agent agent;
    private final List<Enemy> enemies;
    private Set<Point> visitedCells = new HashSet<>(); // Track visited cells
    private static int TRAINING_STEPS = 10000; // Number of steps for training
    private static int EXECUTION_STEPS = 10000; // Number of steps for execution
    
    private Point flagPosition;

    public CaptureTheFlagGame(Environment environment, Agent agent, List<Enemy> enemies) {
        this.environment = environment;
        this.agent = agent;
       
        this.enemies = enemies;
        setPreferredSize(new Dimension(environment.getCols() * CELL_SIZE, environment.getRows() * CELL_SIZE));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawEnvironment(g);
        drawAgent(g);
        drawEnemies(g);
        drawBullets(g);
        agent.drawQValues();
    }

    private void drawEnvironment(Graphics g) {
        for (int i = 0; i < environment.getRows(); i++) {
            for (int j = 0; j < environment.getCols(); j++) {
                char cell = environment.getCell(i, j);
                switch (cell) {
                    case 'F' -> g.setColor(Color.GREEN);
                    case 'T' -> g.setColor(Color.RED);
                    case 'L' -> g.setColor(Color.ORANGE); // Lifeline color
                    case 'B' -> g.setColor(Color.BLACK); // Bullet color
                    case 'S' -> g.setColor(Color.PINK);
                    default -> g.setColor(Color.WHITE);
                }
                g.fillRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                g.setColor(Color.BLACK);
                g.drawRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    private void drawAgent(Graphics g) {
        g.setColor(Color.YELLOW);
        for (Point point : agent.getPath()) {
            g.fillRect(point.y * CELL_SIZE, point.x * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
        // Draw the current agent position
        g.setColor(Color.BLUE);
        g.fillOval(agent.getY() * CELL_SIZE, agent.getX() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }

    private void drawEnemies(Graphics g) {
        g.setColor(Color.RED);
        for (Enemy enemy : enemies) {
            g.fillOval(enemy.getY() * CELL_SIZE, enemy.getX() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
    }

    private void drawBullets(Graphics g) {
        g.setColor(Color.BLACK);
        for (Bullet bullet : environment.getBullets()) {
            g.fillOval(bullet.getY() * CELL_SIZE, bullet.getX() * CELL_SIZE, CELL_SIZE / 2, CELL_SIZE / 2);
        }
    }

    public void clearVisitedCells() {
        visitedCells.clear();
    }

    
}
