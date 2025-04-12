import java.util.ArrayList;

/**
 * Represents a player in the game. Stores the player's current room and inventory.
 * 
 * @author Diogo
 * @version 2025.04.11
 */
public class Player
{
    private Room currentRoom;
    private ArrayList<Item> inventory;
    private int maxWeight = 10; 
    
    /**
     * Create a new player with a starting room.
     * @param startRoom The room the player starts in.
     */
    public Player(Room startRoom) {
        currentRoom = startRoom;
        inventory = new ArrayList<>();
    }

    /**
     * @return The room the player is currently in.
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Set the player's current room.
     * @param room The room to move the player to.
     */
    public void setCurrentRoom(Room room) {
        currentRoom = room;
    }

    public boolean hasItems() {
        return !inventory.isEmpty();
    }

    /**
     * Attempt to add an item to the player's inventory.
     * Only succeeds if the weight limit is not exceeded.
     * @param item The item to add
     * @return true if item was added, false if too heavy
     */
    public boolean addItem(Item item) {
        if (getTotalWeight() + item.getWeight() <= maxWeight) {
            inventory.add(item);
            return true;
        } else {
            return false;
        }
    }

    public boolean hasItem(String name) {
        for (Item item : inventory) {
            if (item.getDescription().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public Item removeItem(String name) {
        for (Item item : inventory) {
            if (item.getDescription().equalsIgnoreCase(name)) {
                inventory.remove(item);
                return item;
            }
        }
        return null;
    }

    public String getInventoryString() {
        if (inventory.isEmpty()) {
            return "You are not carrying anything.";
        }
        StringBuilder result = new StringBuilder("You are carrying:");
        for (Item item : inventory) {
            result.append(" ").append(item.toString()).append(";");
        }
        return result.toString();
    }

    /**
     * Calculate the total weight of items the player is carrying.
     */
    public int getTotalWeight() {
        int total = 0;
        for (Item item : inventory) {
            total += item.getWeight();
        }
        return total;
    }
    
    public void increaseMaxWeight(int amount) {
        maxWeight += amount;
    }
    
    public int getMaxWeight() {
        return maxWeight;
    }
}