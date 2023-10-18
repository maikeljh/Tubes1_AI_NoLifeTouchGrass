import javafx.scene.control.Button;
import java.util.Random;
import java.util.Arrays;
import javafx.util.Pair;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

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
        int population_size = 2000;
        int cycles = 1000;
        int maxDepth;
        double[] roulette = new double[population_size];
        long startTime = System.currentTimeMillis();

        // Set max depth
        if(roundsLeft > 5) {
            maxDepth = 5;
        } else {
            maxDepth = roundsLeft;
        }

        // Generate population
        Chromosome[] population = new Chromosome[population_size];
        for (int i = 0; i < population_size; i++) {
            population[i] = new Chromosome(maxDepth);
        }

        // Get best candidates
        List<Integer> bestCandidates = new ArrayList<Integer>();
        int idx = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == ' ') {
                    idx++;
                    if(
                        (borderCheck(i-1, j) && board[i-1][j] == enemySymbol) ||
                        (borderCheck(i+1, j) && board[i+1][j] == enemySymbol) ||
                        (borderCheck(i, j+1) && board[i][j+1] == enemySymbol) ||
                        (borderCheck(i, j-1) && board[i][j-1] == enemySymbol)
                    ){
                        bestCandidates.add(idx);
                    }
                }
            }
        }

        // Initialize chromosomes
        for(int i = 0; i < population_size; i++){
            population[i].randomize(bestCandidates);
        }

        // Main GA
        for (int i = 0; i < cycles; i++){
            // Check for break
            if(System.currentTimeMillis() - startTime >= 4000){
                break;
            }

            // Create Path
            for(int j = 0; j < population_size; j++){
                this.createPath(population[j], board);
            }

            // Calculate Fitness
            this.calculateFitness(population);

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
            int idxCross = random.nextInt(maxDepth);
            for(int j = 1; j < population_size; j+=2){
                newPop[j-1].swapEncode(newPop[j], idxCross);
            }

            // Mutation
            int idxMutation = random.nextInt(maxDepth);
            for(int j = 0; j < population_size; j++){
                int randomNumber = random.nextInt(newPop[j].getSize() - idxMutation) + 1;
                newPop[j].setEncode(randomNumber, idxMutation);
            }

            // Set new population
            population = Arrays.copyOf(newPop, population_size);
        }

        // Calculate last fitness
        for(int j = 0; j < population_size; j++){
            this.createPath(population[j], board);
        }

        this.calculateFitness(population);

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
     * Function to create tree path for a chromosome
     * @param chromosome The chromosome
     * @param board The game's board containing X and O symbols
     *
     */
    public void createPath(Chromosome chromosome, char[][] board){
        // Create temporary board
        char[][] tempBoard = this.copyBoard(board);

        // Create tree
        TreePath root = new TreePath();
        TreePath currentNode = root;

        // Get encoding
        int[] encode = chromosome.getEncode();

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

            // Update tree
            TreePath child = new TreePath(this.objective_value(tempBoard));
            currentNode.addChild(encode[i], child);
            currentNode = child;
        }

        chromosome.setPath(root);
    }

    /**
     * Function to create reservation tree by merging all paths into a main tree
     * @param population Population containing list of chromosomes
     * @return TreePath
     *
     */
    public TreePath createReservationTree(Chromosome[] population){
        TreePath root = population[0].getPath();

        for(int i = 1; i < population.length; i++){
            TreePath startNode = root;
            TreePath chromosomeNode = population[i].getPath();
            int[] encode = population[i].getEncode();
            int idx = 0;

            while(!startNode.isLeaf()){
                if(startNode.hasChild(encode[idx])){
                    startNode = startNode.getChild(encode[idx]);
                    chromosomeNode = chromosomeNode.getChild(encode[idx]);
                    idx++;
                } else {
                    startNode.addChild(encode[idx], chromosomeNode.getChild(encode[idx]));
                    break;
                }
            }
        }

        return root;
    }

    /**
     * Function to calculate fitness value for each chromosome in population
     * @param population Population containing list of chromosomes
     *
     */
    public void calculateFitness(Chromosome[] population){
        // Create reservation tree
        TreePath reservationTree = createReservationTree(population);

        // Perform minimax to tree
        for (Map.Entry<Integer, TreePath> entry : reservationTree.getChildren().entrySet()) {
            TreePath child = entry.getValue();
            updateTreeWithMinimax(child, true);
        }

        // Calculate fitness for every chromosome
        for(int i = 0; i < population.length; i++){
            int[] encode = population[i].getEncode();
            List<Integer> objValues = new ArrayList<Integer>(population[i].getSize());

            // Get minimax path values
            List<Integer> minimaxValues = findMinimaxValues(reservationTree.getChild(encode[0]), true);

            TreePath startNode = reservationTree;
            int idx = 0;
            while(!startNode.isLeaf()){
                startNode = startNode.getChild(encode[idx]);
                objValues.add(startNode.getValue());
                idx++;
            }

            int commonValuesCount = 0;
            int x = objValues.size() - 1;
            int y = minimaxValues.size() - 1;

            while (x >= 0 && y >= 0 && objValues.get(x).equals(minimaxValues.get(y))) {
                commonValuesCount++;
                x--;
                y--;
            }

            // Set fitness value
            population[i].setFitness(commonValuesCount + 1);
        }
    }

    /**
     * Function to update reservation tree values with minimax
     * @param node The current node
     * @param isMaximizer True if current turn is maximizer else False
     * @return int
     */
    public int updateTreeWithMinimax(TreePath node, boolean isMaximizer){
        if (node.isLeaf()) {
            return node.getValue();
        } else {
            if (isMaximizer) {
                int maxEval = Integer.MIN_VALUE;
                for (TreePath child : node.getChildren().values()) {
                    int eval = updateTreeWithMinimax(child, false);
                    maxEval = Math.max(maxEval, eval);
                }
                node.setValue(maxEval);
                return maxEval;
            } else {
                int minEval = Integer.MAX_VALUE;
                for (TreePath child : node.getChildren().values()) {
                    int eval = updateTreeWithMinimax(child, true);
                    minEval = Math.min(minEval, eval);
                }
                node.setValue(minEval);
                return minEval;
            }
        }
    }

    /**
     * Function to find the minimax path for initial bot's move
     * @param node The current node
     * @param isMaximizer True if current turn is maximizer else False
     * @return List<Integer>
     *
     */
    public List<Integer> findMinimaxValues(TreePath node, boolean isMaximizer) {
        List<Integer> values = new ArrayList<>();

        if (node.isLeaf()) {
            values.add(node.getValue());
            return values;
        }

        if (isMaximizer) {
            int maxEval = Integer.MIN_VALUE;
            for (TreePath child : node.getChildren().values()) {
                List<Integer> childValues = findMinimaxValues(child, false);
                int eval = child.getValue();
                maxEval = Math.max(maxEval, eval);
                values = childValues;
            }
            values.add(0, maxEval);
            return values;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (TreePath child : node.getChildren().values()) {
                List<Integer> childValues = findMinimaxValues(child, true);
                int eval = child.getValue();
                minEval = Math.min(minEval, eval);
                values = childValues;
            }
            values.add(0, minEval);
            return values;
        }
    }
}
