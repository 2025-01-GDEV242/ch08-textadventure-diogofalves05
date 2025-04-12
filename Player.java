/**
 * Represents a player in the game. Stores the player's current room.
 * 
 * @author Diogo
 * @version 2025.04.11
 */
public class Player
{
    private Room currentRoom;

    /**
     * Create a new player with a starting room.
     * @param startRoom The room the player starts in.
     */
    public Player(Room startRoom) {
        currentRoom = startRoom;
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
}