public class Enemy {
    private final Environment environment;
    private int x;
    private int y;
    private boolean movingRight;
    private final int bulletVelocityX;
    private final int bulletVelocityY;

    public Enemy(Environment environment, int bulletVelocityX, int bulletVelocityY) {
        this.environment = environment;
        this.x = 0;
        this.y = environment.getCols() / 2;
        this.movingRight = true;
        this.bulletVelocityX = bulletVelocityX;
        this.bulletVelocityY = bulletVelocityY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void move() {
        if (movingRight) {
            y++;
            if (y >= environment.getCols() - 1) {
                movingRight = false;
            }
        } else {
            y--;
            if (y <= 0) {
                movingRight = true;
            }
        }

        if (Math.random() < 0.01) {
            environment.addBullet(x, y, bulletVelocityX, bulletVelocityY);
        }
    }
}
