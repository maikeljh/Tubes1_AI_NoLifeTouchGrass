import java.util.Random;

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
    public void randomize() {
        Random random = new Random(System.currentTimeMillis());

        for(int i = 0; i < this.size; i++){
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

    // Setter
    public void setFitness(int fitness_value){
        this.fitness_value = fitness_value;
    }

    public void setEncode(int action, int idx){
        this.encode[idx] = action;
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