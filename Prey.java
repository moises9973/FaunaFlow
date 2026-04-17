public class Prey {
    private boolean is_alive;
    private int health;
    private int radius;
    private double x_speed;
    private double y_speed;
    private double age;
    private double x;
    private double y;
    private double energy;
    private int turns_to_update; // do not change after being set
    private int current_turn;
    
    public Prey(int health, double x_speed, double y_speed, int radius, double x, double y, int turns_to_update, double energy) { 
        this.health = health;
        this.turns_to_update = turns_to_update;
        this.current_turn = 0;
        this.is_alive = true;
        this.radius = radius;
        this.x_speed = x_speed;
        this.y_speed = y_speed;
        this.energy = energy;
        this.age = 0;
        this.x = x;
        this.y = y;
    }

    public boolean getIsAlive() {
        return this.is_alive;
    }

    public void setIsAlive(boolean is_alive) {
        this.is_alive = is_alive;
    }

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public double getEnergy() {
        return this.energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public double getAge() {
        return this.age;
    }

    public void setAge(double age) {
        this.age = age;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getXSpeed() {
        return this.x_speed;
    }

    public void setXSpeed(double x_speed) {
        this.x_speed = x_speed;
    }

    public double getYSpeed() {
        return this.y_speed;
    }

    public void setYSpeed(double y_speed) {
        this.y_speed = y_speed;
    }

    public double getSpeed() {
        return Math.sqrt(this.x_speed * this.x_speed + this.y_speed * this.y_speed);
    }

    public int getTurnsToUpdate() {
        return this.turns_to_update;
    }
    /* 
    public void setTurnsToUpdate(int turns_to_update) {
        this.turns_to_update = turns_to_update;
    }
    */

    public int getCurrentTurn() {
        return this.current_turn;
    }

    public void setCurrentTurn(int current_turn) {
        this.current_turn = current_turn;
    }
}
