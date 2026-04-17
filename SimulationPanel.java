import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SimulationPanel extends JPanel {
    ArrayList<Prey> preyPopulation = new ArrayList<>();
    ArrayList<Prey> newGen = new ArrayList<>();
    ArrayList<Double> longestTimesAlivePerGen = new ArrayList<>();
    Random rand = new Random();
    Boolean allDead = true;
    Boolean newGenStarted = true;
    int maxTurnsPerGen = 10 * 60;
    int currentTurn = 0;
    int currentGen = 1;

    public void update() {
        if (newGenStarted) {
            printStartingStats();
            newGenStarted = false;
        }
        updatePrey();
        if (allDead || currentTurn >= maxTurnsPerGen) {
            printCurrentGenResults();
            getNextGeneration();
        }
        if (preyPopulation.isEmpty()) {
            printOverallResults();
            System.exit(0);
        }

        currentTurn++;
    }

    private void updatePrey() {
        for (Prey p: preyPopulation) {

            if (!p.getIsAlive()) {
                continue;
            }

            updateAge(p);

            checkSurvival(p);

            updateSpeed(p);

            // check if going out of bounds
             move(p);

            checkBounds(p);

            p.setCurrentTurn(p.getCurrentTurn() + 1);
        }

        checkLiving();
    }

    private void move(Prey p) {
        p.setX(p.getX() + p.getXSpeed());
        p.setY(p.getY() + p.getYSpeed());
    }

    private void updateAge(Prey p) {
        p.setAge(p.getAge() + 1);
    }

    private void updateSpeed(Prey p) {
        if (p.getCurrentTurn() >= p.getTurnsToUpdate()) {
            p.setXSpeed(p.getXSpeed() + rand.nextDouble() * 4 - 2);
            p.setYSpeed(p.getYSpeed() + rand.nextDouble() * 4 - 2);

            p.setCurrentTurn(0);
        }
    }

    private void checkSurvival(Prey p) {
        if (rand.nextDouble() < 0.1 / (1 + p.getSpeed())) {
            p.setIsAlive(false);
        }
    }

    private void checkBounds(Prey p) {
        // X bounds
        if (p.getX() < 0) {
            p.setX(0);
            p.setXSpeed(Math.abs(p.getXSpeed()));
        } else if (p.getX() > 1920 - 40) {
            p.setX(1920 - 40);
            p.setXSpeed(-Math.abs(p.getXSpeed()));
        }

        // Y bounds
        if (p.getY() < 0) {
            p.setY(0);
            p.setYSpeed(Math.abs(p.getYSpeed()));
        } else if (p.getY() > 1080 - 60) {
            p.setY(1080 - 60);
            p.setYSpeed(-Math.abs(p.getYSpeed()));
        }
    }

    public void checkLiving() {
        allDead = true;

        for (Prey p : preyPopulation) {
            if (p.getIsAlive()) {
                allDead = false;
                break;
            }
        }
    }

    public void spawnPrey() {
        preyPopulation.clear();
        for (int i = 0; i < 50; i++) {
            preyPopulation.add(new Prey(5,
                                        5 * (rand.nextDouble() * 2 - 1) * (rand.nextBoolean() ? 1 : -1),
                                        5 * (rand.nextDouble() * 2 - 1) * (rand.nextBoolean() ? 1 : -1),
                                        5,
                                        rand.nextInt(getWidth()),
                                        rand.nextInt(getHeight()),
                                        rand.nextInt(60) * 3,
                                        rand.nextDouble() * 20));
        }
    }

    public void printStartingStats() {
        System.out.printf("Current Gen: %d\n", currentGen);
        System.out.printf("New Gen pop size: %d\n", preyPopulation.size());
    }

    public void printCurrentGenResults() {
        double longest_time_alive = 0;
        Prey lastPrey = preyPopulation.get(0);

        double fastestSpeed = 0;
        Prey fastestPrey = preyPopulation.get(0);

        double sumEnergy = 0;

        for (Prey p : preyPopulation) {
            sumEnergy += p.getEnergy();
            if (p.getAge() > longest_time_alive) {
                longest_time_alive = p.getAge();
                lastPrey = p;
            }
            if (p.getSpeed() > fastestSpeed) {
                fastestSpeed = p.getSpeed();
                fastestPrey = p;
            }
            //System.out.printf("Prey has been alive for %f\n", p.getAge() / 60);
        }


        longestTimesAlivePerGen.add(longest_time_alive);
        System.out.printf("Average Energy: %f\n", sumEnergy / preyPopulation.size());
        System.out.printf("Longest time alive: %f\nPrey speed: %f\n", longest_time_alive / 60, lastPrey.getSpeed());
        System.out.printf("Fastest Speed: %f\n", fastestSpeed);
        System.out.printf("%s\n", lastPrey.equals(fastestPrey) ? "SAME PREY" : "NOT THE SAME");

        System.out.println();
    }

    public void printOverallResults() {
        double bestTime = 0;

        for (Double d : longestTimesAlivePerGen) {
            if (d > bestTime) {
                bestTime = d;
            }
        }
        System.out.printf("Longest survival time: %f", bestTime / 60);
    }

    public void getNextGeneration(){
        for (Prey p : preyPopulation){
            // give a chance to randomly mate with another
            double chance = Math.min(0.30, p.getSpeed() * 0.07);
            int amountMaking = Math.max((int)(p.getEnergy() / 3), 1);

            if (rand.nextDouble() < chance) {
                Prey p2 = preyPopulation.get(rand.nextInt(preyPopulation.size()));
                if (p.equals(p2)) {
                    continue;
                }

                for (int i = 0; i < amountMaking; i++) {
                    newGen.add(new Prey(getIntAverage(p.getHealth(), p2.getHealth()) + rand.nextInt(3) - 2,
                                        getDoubleAverage(p.getXSpeed(), p2.getXSpeed()) * (rand.nextBoolean() ? 1 : -1),
                                        getDoubleAverage(p.getYSpeed(), p2.getYSpeed()) * (rand.nextBoolean() ? 1 : -1),
                                        5,
                                        rand.nextDouble(getWidth()),
                                        rand.nextDouble(getHeight()),
                                        rand.nextInt(60) * 3,
                                        rand.nextDouble() * 20 + rand.nextDouble(7) - 3));
                }
            }
        }
        
        allDead = false;

        preyPopulation.clear();
        preyPopulation.addAll(newGen);
        newGen.clear();

        if (preyPopulation.isEmpty()) {
            return;
        }
        
        newGenStarted = true;
        currentTurn = 0;
        currentGen++;
    }

    public int getIntAverage(int num1, int num2){
        return (Math.abs(num1) + Math.abs(num2)) / 2;
    }

    public double getDoubleAverage(double num1, double num2){
        return (Math.abs(num1) + Math.abs(num2)) / 2;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Prey p: preyPopulation) {
            if (p.getIsAlive()) {
                g.setColor(Color.BLUE);
            } else {
                g.setColor(Color.GRAY);
            }

            g.fillOval((int)p.getX(), (int)p.getY(), 20, 20);
        }
    }
}