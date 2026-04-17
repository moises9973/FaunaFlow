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
    int maxTurnsPerGen = 10 * 60;
    int currentTurn = 0;
    int currentGen = 1;

    public void update() {
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

            updateSpeed(p);

            checkSurvival(p);

            // check if going out of bounds
             move(p);

            checkBounds(p);

            p.setCurrentTrun(p.getCurrentTurn() + 1);
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

            p.setCurrentTrun(0);
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
                                        rand.nextDouble() * 2 - 1,
                                        rand.nextDouble() * 2 - 1,
                                        5,
                                        rand.nextInt(getWidth()),
                                        rand.nextInt(getHeight()),
                                        rand.nextInt(60) * 3,
                                        rand.nextDouble() * 20));
        }
    }

    public void printCurrentGenResults() {
        double longest_time_alive = 0;

        System.out.printf("Current Gen: %d\n", currentGen);
        for (Prey p : preyPopulation) {
            if (p.getAge() > longest_time_alive) {
                longest_time_alive = p.getAge();
            }
            //System.out.printf("Prey has been alive for %f\n", p.getAge() / 60);
        }

        longestTimesAlivePerGen.add(longest_time_alive);
        System.out.printf("Longest time alive: %f\n", longest_time_alive / 60);
    }

    public void printOverallResults() {
        double bestTime = 0;
        int i = 0;

        for (Prey p : preyPopulation) {
            System.out.printf("Prey %d has lived: %f\n", i++, p.getAge());
            if (p.getAge() > bestTime) {
                bestTime = p.getAge();
            }
        }
        System.out.printf("Longest survival time: %f", bestTime);
    }

    public void getNextGeneration(){
        for (Prey p : preyPopulation){
            // give a chance to randomly mate with another
            double chance = Math.min(0.30, p.getSpeed() * 0.07);

            if (rand.nextDouble() < chance) {
                Prey p2 = preyPopulation.get(rand.nextInt(preyPopulation.size() - 1));
                newGen.add(new Prey(getIntAverage(p.getHealth(), p2.getHealth()),
                                    getDoubleAverage(p.getXSpeed(), p2.getXSpeed()),
                                    getDoubleAverage(p.getYSpeed(), p2.getYSpeed()),
                                    5,
                                    rand.nextDouble(getWidth()),
                                    rand.nextDouble(getHeight()),
                                    rand.nextInt(60) * 3,
                                    rand.nextDouble() * 20));
            }
        }

        currentGen++;
        preyPopulation.clear();
        preyPopulation.addAll(newGen);
        newGen.clear();

        allDead = false;
    }

    public int getIntAverage(int num1, int num2){
        return (num1 + num2) / 2;
    }

    public double getDoubleAverage(double num1, double num2){
        return (num1 + num2) / 2;
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