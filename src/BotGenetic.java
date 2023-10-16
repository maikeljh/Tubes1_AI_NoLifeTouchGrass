import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import java.util.Random;
import java.util.Arrays;
import java.util.Math;

public class BotGenetic extends Bot {
    public int[] genetic_search(char[][] board, int roundsLeft){
        // Constants
        int population_size = 10;
        int cycles = 10;
        double[] roulette = new double[population_size];

        // Generate population
        Population[] pop = new Population[population_size](roundsLeft);
        for(int i = 0; i < population_size; i++){
            pop[i].randomize();
        }

        // Main GA
        for (int i = 0; i < cycles; i++){
            // Calculate Fitness
            for(int j = 0; j < population_size; j++){
                pop[j].setFitness(fitness_function(board, pop[j]));
            }

            // Selection
            Population[] newPop = new Population[population_size];

            // Calculate total fitness
            double total_fitness = 0;
            for(int i = 0; i < population_size; i++){
                total_fitness += pop[i].getFitness();
            }

            // Create roulette
            for(int i = 0; i < population_size; i++){
                roulette[i] = pop[i].getFitness() / total_fitness;
            }
            Arrays.sort(roulette);
            for(int i = 1; i < population_size; i++){
                roulette[i] += roulette[i-1];
            }

            // Select parents
            for(int i = 0; i < population_size; i++){
                double random = Math.random();
                int choosenIdx = 0;
                while(random > roulette[choosenIdx]) {
                    choosenIdx++;
                }
                newPop[i] = pop[choosenIdx];
            }

            // Crossover
            Random random = new Random();
            int idxCross = random.nextInt(roundsLeft);
            for(int i = 1; i < population_size; i+=2){
                swapEncode(newPop[i-1], newPop[i]);
            }

            // Mutation
            int idxMutation = random.nextInt(roundsLeft);
            for(int i = 0; i < population_size; i++){
                int randomNumber = random.nextInt(newPop.getSize() - idxMutation);
                newPop.setEncode(randomNumber, idxMutation);
            }

            // Set new population
            pop = Arrays.copyof(newPop, population_size);
        }

        // Calculate last fitness
        for(int j = 0; j < population_size; j++){
            pop[j].setFitness(fitness_function(board, pop[j]));
        }

        // Return optimal solution
        Population solution;
        int maxFitness = -64;
        for(int i = 0; i < population_size; i++){
            if(pop[i].getFitness() > maxFitness){
                solution = pop[i];
            }
        }

        int idxMove = solution.getEncode()[0];
        return this.getCoordinate(board, idxMove);
    }

    public int[] move(int playerXScore, int playerOScore, int roundsLeft, Button[][] buttons) {
        // Create temporary board array for search
        char[][] board = new char[buttons.length][buttons[0].length];
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                board[i][j] = buttons[i][j].getText();
            }
        }

        return genetic_search(board, roundsLeft);
    }

    public Pair getCoordinate(char[][] boardCoor, int position){
        int x = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(boardCoor[i][j] == ''){
                    x++;
                }
                if(x == position){
                    return new Pair(i, j);
                }
            }
        }
    }

    public int fitness_function(char[][] board, Population population){
        int[] encode = population.getEncode();
        char[][] tempBoard = Arrays.copyof()
        // Simulate game based on population representation
        for(int i = 0; i < population.getSize(); i++){
            Pair currentCoor = this.getCoordinate(board, encode[i]);
            if(i % 2 == 0) {
                board[currentCoor.getKey()][currentCoor.getValue()] = 'O';
                checkAdjacency(board, currentCoor, "Bot");
            } else {
                board[currentCoor.getKey()][currentCoor.getValue()] = 'X';
                checkAdjacency(board, currentCoor, "Player");
            }
        }

        // Calculate objective function
        int playerOScore = 0;
        int playerXScore = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(board[i][j] == 'O'){
                    playerOScore++;
                } else {
                    playerXScore++;
                }
            }
        }

        return objective_function(playerXScore, playerOScore);
    }
}

class Population {
    // Attributes
    private int[] encode;
    private int size;
    private int fitness_value;

    // Constructor
    public Population() {
        this.size = 56;
        this.encode = new int[this.size];
    }

    public Population(int size){
        this.size = size;
        this.encode = new int[this.size];
    }

    // Methods
    public void randomize() {
        Random random = new Random();

        for(int i = 0; i < this.size; i++){
            this.encode[i] = random.nextInt(size - i);
        }
    }

    public int getSize(){
        return this.size;
    }

    public int[] getEncode(){
        return this.encode;
    }

    public int getFitness(){
        return this.fitness_value;
    }

    public void setFitness(int fitness_value){
        this.fitness_value = fitness_value;
    }

    public void setEncode(int action, int idx){
        this.encode[idx] = action;
    }

    public void swapEncode(Population other, int idx){
        for(int i = idx; i < this.size; i++){
            int temp = this.encode[i];
            this.encode[i] = other.getEncode()[i];
            other.setEncode(temp, i);
        }
    }
}