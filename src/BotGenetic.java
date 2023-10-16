import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import java.util.Random;

public class BotGenetic extends Bot {
    public int[] genetic_search(char[][] board){
        // Constants
        int population_size = 10;
        int cycles = 10;
        int[] roulette = new int[population_size];

        // Generate population
        Population[] pop = new Population[population_size];
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
            
        }
    }

    public int[] move(int playerXScore, int playerOScore, int roundsLeft, Button[][] buttons) {
        // Create temporary board array for search
        char[][] board = new char[buttons.length][buttons[0].length];
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                board[i][j] = buttons[i][j].getText();
            }
        }

        return genetic_search(board);
    }

    public int fitness_function(char[][] board, Population population){
        public Pair getCoordinate(char[][] board, int position){
            int x = 0;
            for(int i = 0; i < 8; i++){
                for(int j = 0; j < 8; j++){
                    if(board[i][j] == ''){
                        x++;
                    }
                    if(x == position){
                        return new Pair(i, j);
                    }
                }
            }
        }

        int[] encode = population.getEncode();
        // Simulate game based on population representation
        for(int i = 0; i < population.getSize(); i++){
            Pair currentCoor = getCoordinate(encode[i]);
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
}