import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Agent {
    private final Environment environment;
    public final int startX;
    private final int startY;
    public int x;
    public int y;
    public final Random random;
    public final Map<Point, Double[]> qTable;
    public boolean trainingPhase;
    private List<Point> path = new ArrayList<>();
    private boolean flagCaptured = false;
    private CaptureTheFlagGame game; // Reference to the game instance
    
    //Agent state sensors
    public boolean hasBoollets = true;
    public int numBullets = 100;
    public int health = 500;
    public boolean hasDied = false;
    public int hasDiedCount = 0;
    public int reachFlag = 0;

    private double epsilon = 0.5;
    private double executionEpsilon = 0.5;

    private final double alpha = 0.6; // Learning rate
    private final double gamma = 0.3; // Discount factor

    public Agent(Environment environment, double trainingEpsilon, CaptureTheFlagGame game) {
        this.environment = environment;
        this.startX = environment.getRows() - 1;
        this.startY = 0;
        this.x = startX;
        this.y = startY;
        this.random = new Random();
        this.qTable = new HashMap<>();
        this.trainingPhase = true;
        this.epsilon = trainingEpsilon;
        this.game = game; // Store the reference to the game instance
    }

    public List<Point> getPath() {
        return path;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isTrainingPhase() {
        return trainingPhase;
    }

    private void initializeState(Point state) {
        if (!qTable.containsKey(state)) {
            qTable.put(state, new Double[]{0.0, 0.0, 0.0, 0.0, 0.0});
        }
    }
    
    //agent sensors using state of agent
    public boolean AgentIsDead()
    {
    	if(health == 0)
    	{
    		hasDiedCount++;
    		return true;
    		
    	}
    	else 
    		return hasDied;
    }
    
    public void punishAgent(int intReward)
    {
    	if(AgentIsDead())
    	{
    		intReward -= 50;
    	}
    }

    //agent actuators
    public void move() {
        Point currentState = new Point(x, y);
        initializeState(currentState);

        int action;
        if ((trainingPhase && random.nextDouble() < epsilon) || (!trainingPhase && random.nextDouble() < executionEpsilon)) {
            action = random.nextInt(5);
        } else {
            action = getMaxQAction(currentState);
        }

        int newX = x;
        int newY = y;

        switch (action) {
        	//agent actuators
            case 0 -> newX--;
            case 1 -> newX++;
            case 2 -> newY--;
            case 3 -> newY++;
            //agent actuators
            case 4 -> shoot();
        }

        // Ensure the new position is within bounds
        if (newX < 0 || newX >= environment.getRows() || newY < 0 || newY >= environment.getCols()) {
            return;
        }

        double reward;
        //agent sensors
        char cell = environment.getCell(newX, newY);
        switch (cell) {
            case 'F' -> { // flag
                reward = 1000;
                flagCaptured = true;
                updateQValue(currentState, action, reward, new Point(newX, newY));
                if (trainingPhase) {
                    resetEnvironmentAndAgent();
                    return;
                }
                reachFlag++;
                System.out.println("Agent reached flag");
                System.out.println("Times Reached Flag: "+reachFlag);
            }
            case 'T' -> { // trap
                reward = -10;
                //agent actuators
                environment.setCell(newX, newY, ' ');
            }
            case 'L' -> { // life line
                reward = 1;
                health += 500;
                //agent actuators
                environment.setCell(newX, newY, ' ');
            }
            case 'S' -> { // weapon boost
                reward = 1;
                numBullets += 100;
                environment.setCell(newX, newY, ' ');
            }
            case 'B' -> { // bullet
                reward = -5;
                if (trainingPhase) {
                    game.clearVisitedCells();
                    environment.resetBullets();
                    environment.placeFlag();
                    health -= 50;
                    game.repaint();
                    return;
                }
            }
            default -> { // default
                reward = -1;
                health -= 1;
            }
        }

        Point newState = new Point(newX, newY);
        initializeState(newState);
        updateQValue(currentState, action, reward, newState);

        x = newX;
        y = newY;
        path.add(new Point(x, y));

        if (!trainingPhase) {
            flagCaptured = false;
        }

        // Log Q-value update for debugging
        System.out.printf("Updated Q-value for state (%d, %d) action %d: %.2f\n", currentState.x, currentState.y, action, qTable.get(currentState)[action]);
        System.out.println("Agent has died count: "+ hasDiedCount);
        System.out.println("Flag captured: "+ reachFlag);
    }

    private void resetEnvironmentAndAgent() {
        game.clearVisitedCells();
        environment.resetBullets();
        environment.placeFlag();
        game.repaint();
        resetPosition();
        environment.initializeEnvironment();
        path.clear();
    }

    private void updateQValue(Point currentState, int action, double reward, Point newState) {
        initializeState(newState);  // Ensure newState Q-values are initialized
        double oldQValue = qTable.get(currentState)[action];
        double maxFutureQValue = qTable.get(newState)[getMaxQAction(newState)];
        double newQValue = oldQValue + alpha * (reward + gamma * maxFutureQValue - oldQValue);
        qTable.get(currentState)[action] = newQValue;
    }

    

   

    private void shoot() {
    	if(numBullets > 0)
    	{
    		for (int i = 0; i < environment.getRows(); i++) {
            	environment.addBullet(this.getX(), this.getY(), -1, 0);
                    if (environment.getCell(i, y) == 'E') {
                        environment.removeEntity(i, y);
                        break;
                    }
                
                
            }
    		numBullets--;
    	}
        
    }

    private int getMaxQAction(Point state) {
        initializeState(state);  // Ensure state Q-values are initialized
        Double[] qValues = qTable.get(state);
        double maxQValue = qValues[0];
        int maxQAction = 0;
        for (int i = 1; i < qValues.length; i++) {
            if (qValues[i] > maxQValue) {
                maxQValue = qValues[i];
                maxQAction = i;
            }
        }
        return maxQAction;
    }

    public void setGame(CaptureTheFlagGame game) {
        this.game = game;
    }

    public void resetPosition() {
        x = startX;
      
        y = startY;
    }

    public void setTrainingPhase(boolean trainingPhase) {
        this.trainingPhase = trainingPhase;
    }

    public void setExecutionEpsilon(double executionEpsilon) {
        this.executionEpsilon = executionEpsilon;
    }
    
    public void drawQValues() {
        // make a 2D array to hold the maximum Qvalues for each state
        Double[][] qValueGrid = new Double[20][20];

        // Initialize each cell with null to distinguish unvisited states
        for (int i = 0; i < qValueGrid.length; i++) {
            for (int j = 0; j < qValueGrid[i].length; j++) {
                qValueGrid[i][j] = null;
            }
        }

        // put the qValueGrid with the maximum Qvalues from the qTable
        for (Map.Entry<Point, Double[]> entry : qTable.entrySet()) {
            Point state = entry.getKey();
            Double[] qValues = entry.getValue();
            double maxQValue = qValues[0];
            for (int i = 1; i < qValues.length; i++) {
                if (qValues[i] > maxQValue) {
                    maxQValue = qValues[i];
                }
            }
            qValueGrid[state.x][state.y] = maxQValue;
        }

        // Print the grid to the console
        for (int i = 0; i < qValueGrid.length; i++) {
            for (int j = 0; j < qValueGrid[i].length; j++) {
                if (qValueGrid[i][j] != null) {
                    System.out.printf("%6.1f", qValueGrid[i][j]);
                } else {
                    System.out.print("  N/A ");
                }
            }
            System.out.println();
        }
    }
    
}
