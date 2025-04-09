public class Bullet {
    private int x;
    private int y;
    private final int velocityX;
    private final int velocityY;

    public Bullet(int startX, int startY, int velocityX, int velocityY) {
        this.x = startX;
        this.y = startY;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void move() {
        x += velocityX;
        y += velocityY;
    }
}
