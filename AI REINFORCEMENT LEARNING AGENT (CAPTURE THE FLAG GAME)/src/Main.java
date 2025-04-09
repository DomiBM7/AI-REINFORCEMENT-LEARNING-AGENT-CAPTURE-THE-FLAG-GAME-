import java.util.ArrayList;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.Timer;




public class Main
{
	private static int TRAINING_STEPS = 100000; // Number of steps for training
    private static int EXECUTION_STEPS = 10000; // Number of steps for execution
    
    public static void main(String[] args) {
        Environment environment = new Environment(20, 20, 3); //  environment size and number 
        Agent agent = new Agent(environment, 0.8, null); //  agent with exploration rate 0.8
        List<Enemy> enemies = new ArrayList<>();
        for (int i = 0; i < 3; i++) { // 
            enemies.add(new Enemy(environment, 1, 0)); 
        }

        // Create and configure game panel
        CaptureTheFlagGame gamePanel = new CaptureTheFlagGame(environment, agent, enemies);
        agent.setGame(gamePanel); // Set the game reference in the agent
        JFrame frame = new JFrame("Capture the Flag Agent");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(gamePanel);
        frame.pack();
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true);

        // Define initial timer delay for training phase
        int timerDelay = 1;

        // Start the game loop using Swing Timer
        Timer timer = new Timer(timerDelay, e -> {
            if (TRAINING_STEPS > 0) {
                agent.setTrainingPhase(true);
                agent.setExecutionEpsilon(0.8); // Exploration rate during training phase
                TRAINING_STEPS--;
                System.out.println("---TRAINING AGENT PHASE---");
                System.out.println("Step: " + (10000 - TRAINING_STEPS));
                System.out.println();
            } else if (EXECUTION_STEPS > 0) {
                agent.setTrainingPhase(false);
                agent.setExecutionEpsilon(0.1); // Exploration rate during execution phase
                EXECUTION_STEPS--;
                System.out.println("---EXECUTING AGENT PHASE---");
                System.out.println("Step: " + (10000 - EXECUTION_STEPS));
                System.out.println();
                // Increase timer delay for slower execution
                ((Timer) e.getSource()).setDelay(100); // Adjust this delay as needed
            } else {
                ((Timer) e.getSource()).stop(); // Stop the timer when both training and execution steps are completed
                System.out.println("Training and execution completed. Game over.");
                return;
            }

            agent.move();
            for (Enemy enemy : enemies) {
                enemy.move();
            }
            environment.moveBullet();
            gamePanel.repaint();
            if (environment.getCell(agent.getX(), agent.getY()) == 'F') {
                ((Timer) e.getSource()).stop(); // Stop the timer when the flag is captured
                System.out.println("------Flag captured! Game over.---------");
            }
        });
        
        timer.start();
    }

}