import java.util.HashMap;
import java.util.Map;

/**
 * TreePath class.  A tree path representation for
 * chromosome minimax path
 * @author Michael Jonathan Halim 13521124
 * 
 */
public class TreePath {
    // Attributes
    private int value;
    Map<Integer, TreePath> children;

    /**
     * Default Constructor
     *
     */
    public TreePath(){
        this.value = 0;
        this.children = new HashMap<>();
    }

    /**
     * Custom Constructor
     * @param value The objective value of the node
     */
    public TreePath(int value){
        this.value = value;
        this.children = new HashMap<>();
    }

    // Setter
    public void setValue(int value){
        this.value = value;
    }

    // Getter
    public int getValue(){
        return this.value;
    }

    public Map<Integer, TreePath> getChildren() {
        return this.children;
    }

    public TreePath getChild(int position) {
        return this.children.get(position);
    }

    // Methods
    /**
     * Function to add child to tree
     * @param position The encode position
     * @param child Child that is going to be added to the tree
     *
     */
    public void addChild(int position, TreePath child){
        this.children.put(position, child);
    }

    /**
     * Function to check if tree has path to the encode position
     * @param position The encode position
     * @return boolean
     *
     */
    public boolean hasChild(int position) {
        return this.children.containsKey(position);
    }

    /**
     * Function to check if tree is a leaf node
     * @return boolean
     *
     */
    public boolean isLeaf(){
        return this.children.isEmpty();
    }
}