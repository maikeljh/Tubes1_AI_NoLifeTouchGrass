import javafx.scene.control.Button;
import java.util.Random;
import java.util.Arrays;
import javafx.util.Pair;

public class BotGenetic extends Bot {
    public int[] genetic_search(char[][] board, int roundsLeft){
        // Constants
        int population_size = 1000;
        int cycles = 1000;
        double[] roulette = new double[population_size];

        // Generate population
        Population[] pop = new Population[population_size];
        for (int i = 0; i < population_size; i++) {
            pop[i] = new Population(roundsLeft);
        }
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
            for(int j = 0; j < population_size; j++){
                total_fitness += pop[j].getFitness();
            }

            // Create roulette
            for(int j = 0; j < population_size; j++){
                roulette[j] = pop[j].getFitness() / total_fitness;
            }
            Arrays.sort(roulette);
            for(int j = 1; j < population_size; j++){
                roulette[j] += roulette[j-1];
            }

            // Select parents
            for(int j = 0; j < population_size; j++){
                double random = Math.random();
                int choosenIdx = 0;
                while(random > roulette[choosenIdx]) {
                    choosenIdx++;
                }
                newPop[j] = pop[choosenIdx];
            }

            // Crossover
            Random random = new Random(System.currentTimeMillis());
            int idxCross = random.nextInt(roundsLeft);
            for(int j = 1; j < population_size; j+=2){
                newPop[j-1].swapEncode(newPop[j], idxCross);
            }

            // Mutation
            int idxMutation = random.nextInt(roundsLeft);
            for(int j = 0; j < population_size; j++){
                int randomNumber = random.nextInt(newPop[j].getSize() - idxMutation) + 1;
                newPop[j].setEncode(randomNumber, idxMutation);
            }

            // Set new population
            pop = Arrays.copyOf(newPop, population_size);
        }

        // Calculate last fitness
        for(int j = 0; j < population_size; j++){
            pop[j].setFitness(fitness_function(board, pop[j]));
        }

        // Return optimal solution
        Population solution = new Population();
        int maxFitness = -64;
        for(int i = 0; i < population_size; i++){
            if(pop[i].getFitness() > maxFitness){
                solution = pop[i];
            }
        }

        int idxMove = solution.getEncode()[0];
        Pair result = this.getCoordinate(board, idxMove);
        int[] answer = {(int) result.getKey(), (int)result.getValue()};

        return answer;
    }

    public int[] move(int playerXScore, int playerOScore, int roundsLeft, Button[][] buttons) {
        // Create temporary board array for search
        char[][] board = new char[buttons.length][buttons[0].length];
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                String buttonText = buttons[i][j].getText();
                if (buttonText.isEmpty()) {
                    board[i][j] = ' ';
                } else {
                    board[i][j] = buttonText.charAt(0);
                }
            }
        }

        return genetic_search(board, roundsLeft);
    }

    public Pair getCoordinate(char[][] boardCoor, int position){
        int x = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(boardCoor[i][j] == ' '){
                    x++;
                }
                if(x == position){
                    return new Pair(i, j);
                }
            }
        }

        return null;
    }

    public int fitness_function(char[][] board, Population population){
        int[] encode = population.getEncode();
        char[][] tempBoard = new char[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                tempBoard[i][j] = board[i][j];
            }
        }
        // Simulate game based on population representation
        for(int i = 0; i < population.getSize(); i++){
            Pair currentCoor = this.getCoordinate(tempBoard, encode[i]);
            if(i % 2 == 0) {
                tempBoard[(int) currentCoor.getKey()][(int) currentCoor.getValue()] = 'O';
                checkAdjacency(tempBoard, currentCoor, "Bot");
            } else {
                tempBoard[(int) currentCoor.getKey()][(int) currentCoor.getValue()] = 'X';
                checkAdjacency(tempBoard, currentCoor, "Player");
            }
        }

        // Calculate objective function
        int playerOScore = 0;
        int playerXScore = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(tempBoard[i][j] == 'O'){
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
        Random random = new Random(System.currentTimeMillis());

        for(int i = 0; i < this.size; i++){
            this.encode[i] = random.nextInt(this.size - i) + 1;
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