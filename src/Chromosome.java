import java.util.Random;
import java.util.List;

/**
 * Chromosome class.  A proposed solution of the problem 
 * for genetic algorithm
 * @author Michael Jonathan Halim 13521124
 * 
 */
public class Chromosome {
    // Attributes
    private int[] encode;
    private int size;
    private int fitness_value;
    private TreePath path;

    /**
     * Default Constructor
     *
     */
    public Chromosome() {
        this.size = 56;
        this.encode = new int[this.size];
    }

    /**
     * Custom Constructor
     * @param size Chromosome's size
     *
     */
    public Chromosome(int size){
        this.size = size;
        this.encode = new int[this.size];
    }

    // Methods
    /**
     * Function to initialize the encoding of chromosome
     * @param best List of best potential start move
     *
     */
    public void randomize(List<Integer> best) {
        Random random = new Random(System.currentTimeMillis());

        // Start with "best" move
        if(best.size() > 0){
            int randomIndex = random.nextInt(best.size());
            this.encode[0] = best.get(randomIndex);
        } else {
            this.encode[0] = random.nextInt(this.size) + 1;
        }

        // Random the remainings
        for(int i = 1; i < this.size; i++){
            this.encode[i] = random.nextInt(this.size - i) + 1;
        }
    }

    // Getter
    public int getSize(){
        return this.size;
    }

    public int[] getEncode(){
        return this.encode;
    }

    public int getFitness(){
        return this.fitness_value;
    }

    public TreePath getPath(){
        return this.path;
    }

    // Setter
    public void setFitness(int fitness_value){
        this.fitness_value = fitness_value;
    }

    public void setEncode(int action, int idx){
        this.encode[idx] = action;
    }

    public void setPath(TreePath path){
        this.path = path;
    }

    /**
     * Function to swap encode from other chromosome
     * @param other Other Chromosome
     * @param idx Index point of swapping
     *
     */
    public void swapEncode(Chromosome other, int idx){
        for(int i = idx; i < this.size; i++){
            int temp = this.encode[i];
            this.encode[i] = other.getEncode()[i];
            other.setEncode(temp, i);
        }
    }
}