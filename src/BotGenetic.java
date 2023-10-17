import javafx.scene.control.Button;
import java.util.Random;
import java.util.Arrays;
import javafx.util.Pair;

/**
 * BotGenetic class.  It uses genetic algorithm to evaluate 
 * best movement for current state
 * @author Michael Jonathan Halim 13521124
 * 
 */
public class BotGenetic extends Bot {
    /**
     * Constructor
     * @param ownedSymbol Bot's symbol
     * @param enemySymbol Enemy's symbol
     *
     */
    public BotGenetic(char ownedSymbol, char enemySymbol) {
        this.ownedSymbol = ownedSymbol;
        this.enemySymbol = enemySymbol;
    }

    /**
     * Main function to find best movement with genetic algorithm
     * @param board Game's board containing X and O symbols
     * @param roundsLeft Remaining rounds
     * @return int[]
     * 
     */
    public int[] genetic_search(char[][] board, int roundsLeft){
        // Constants
        int population_size = 1000;
        int cycles = 1000;
        double[] roulette = new double[population_size];

        // Generate population
        Chromosome[] population = new Chromosome[population_size];
        for (int i = 0; i < population_size; i++) {
            population[i] = new Chromosome(roundsLeft);
        }
        for(int i = 0; i < population_size; i++){
            population[i].randomize();
        }

        // Main GA
        for (int i = 0; i < cycles; i++){
            // Calculate Fitness
            for(int j = 0; j < population_size; j++){
                population[j].setFitness(fitness_function(board, population[j]));
            }

            // Selection
            Chromosome[] newPop = new Chromosome[population_size];

            // Calculate total fitness
            double total_fitness = 0;
            for(int j = 0; j < population_size; j++){
                total_fitness += population[j].getFitness();
            }

            // Create roulette
            for(int j = 0; j < population_size; j++){
                roulette[j] = population[j].getFitness() / total_fitness;
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
                newPop[j] = population[choosenIdx];
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
            population = Arrays.copyOf(newPop, population_size);
        }

        // Calculate last fitness
        for(int j = 0; j < population_size; j++){
            population[j].setFitness(fitness_function(board, population[j]));
        }

        // Return optimal solution
        Chromosome solution = new Chromosome();
        int maxFitness = -64;
        for(int i = 0; i < population_size; i++){
            if(population[i].getFitness() > maxFitness){
                solution = population[i];
            }
        }

        int idxMove = solution.getEncode()[0];
        Pair result = this.getCoordinate(board, idxMove);
        int[] answer = {(int) result.getKey(), (int)result.getValue()};

        return answer;
    }

    /**
     * Function that returns the choosen move based on genetic algorithm
     * @param roundsLeft Remaining rounds
     * @param buttons Game board buttons containing O and X symbols
     * @return int[]
     *
     */
    public int[] move(int roundsLeft, Button[][] buttons) {
        // Create temporary board array for search
        char[][] board = createBoardFromButtons(buttons);

        return genetic_search(board, roundsLeft);
    }

    /**
     * Function that get coordinate of board game based on position (index of empty cells starting from 1)
     * @param board Game's board containing X and O symbols
     * @param position Index of empty cells in board starting from 1
     * @return Pair or null
     *
     */
    public Pair getCoordinate(char[][] board, int position){
        int x = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(board[i][j] == ' '){
                    x++;
                }
                if(x == position){
                    return new Pair(i, j);
                }
            }
        }

        return null;
    }

    /**
     * Function that calculate fitness value of a game state
     * @param board Game's board containing X and O symbols
     * @param chromosome A proposed solution of the problem
     * @return int
     *
     */
    public int fitness_function(char[][] board, Chromosome chromosome){
        int[] encode = chromosome.getEncode();
        char[][] tempBoard = copyBoard(board);

        // Simulate game based on chromosome representation
        for(int i = 0; i < chromosome.getSize(); i++){
            Pair currentCoor = this.getCoordinate(tempBoard, encode[i]);
            if(i % 2 == 0) {
                tempBoard[(int) currentCoor.getKey()][(int) currentCoor.getValue()] = this.ownedSymbol;
                checkAdjacency(tempBoard, currentCoor, "Bot");
            } else {
                tempBoard[(int) currentCoor.getKey()][(int) currentCoor.getValue()] = this.enemySymbol;
                checkAdjacency(tempBoard, currentCoor, "Enemy");
            }
        }

        // Calculate objective function
        return objective_value(tempBoard);
    }
}
