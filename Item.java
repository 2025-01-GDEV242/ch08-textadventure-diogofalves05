/**
 * Class Item - represents an item in the game.
 * Each item has a description and a weight.
 * 
 * @author Diogo
 * @version 2025.04.11
 */
public class Item {
    private String description;
    private int weight;

    /**
     * Create a new item with a description and weight.
     * @param description A short description of the item.
     * @param weight The weight of the item in arbitrary units.
     */
    public Item(String description, int weight) {
        this.description = description;
        this.weight = weight;
    }

    /**
     * @return The item's description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return The item's weight.
     */
    public int getWeight() {
        return weight;
    }

    /**
     * @return A string representation of the item.
     */
    public String toString() {
        return description + " (Weight: " + weight + ")";
    }
}