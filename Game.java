import java.util.HashMap;  
import java.util.Set; 
import java.util.Stack;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class Game 
{
    private Parser parser;
    private Player player;
    private Stack<Room> roomHistory;
    
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        roomHistory = new Stack<>();
        parser = new Parser(); 
    }

    /**
     * Create all the rooms, link their exits, and place items.
     */
    private void createRooms()
    {
        Room outside, theater, pub, lab, office, cellar, attic, library;

        outside = new Room("outside the main entrance of the university");
        theater = new Room("in a lecture theater");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");
        cellar = new Room("in a dark cellar");
        attic = new Room("in a dusty attic");
        library = new Room("in the campus library");

        outside.setExit("east", theater);
        outside.setExit("south", lab);
        outside.setExit("west", pub);
        lab.setExit("north", outside);
        lab.setExit("east", office);
        office.setExit("west", lab);
        theater.setExit("west", outside);
        pub.setExit("east", outside);
        cellar.setExit("up", lab);
        attic.setExit("down", theater);
        library.setExit("south", pub);

        Item book = new Item("an ancient book", 3);
        Item beer = new Item("a pint of cold beer", 1);
        Item computer = new Item("a dusty computer", 10);
        Item scroll = new Item("a magical scroll", 2);

        library.addItem(book);
        pub.addItem(beer);
        lab.addItem(computer);
        attic.addItem(scroll);  

        player = new Player(outside);
    }
    
    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        boolean finished = false;
        while (!finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println(player.getCurrentRoom().getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
                System.out.println("I don't know what you mean...");
                break;

            case HELP:
                printHelp();
                break;

            case GO:
                goRoom(command);
                break;

            case QUIT:
                wantToQuit = quit(command);
                break;
                
            case BACK:
                goBack();
                break;
                
            case TAKE:
                takeItem(command);
                break;

            case DROP:
                dropItem(command);
                break;
        }
        return wantToQuit;
    }

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /**
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if (!command.hasSecondWord()) {
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();
        Room nextRoom = player.getCurrentRoom().getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        } else {
            roomHistory.push(player.getCurrentRoom());
            player.setCurrentRoom(nextRoom);
            System.out.println(player.getCurrentRoom().getLongDescription());
        }
    }

    /**
     * Go back to the previous room using room history stack.
     */
    private void goBack()
    {
        if (!roomHistory.isEmpty()) {
            player.setCurrentRoom(roomHistory.pop());
            System.out.println(player.getCurrentRoom().getLongDescription());
        } else {
            System.out.println("You can't go back any further.");
        }
    }

    /**
     * Try to take an item from the current room.
     */
    private void takeItem(Command command)
    {
        if (!command.hasSecondWord()) {
            System.out.println("Take what?");
            return;
    }

    String itemName = command.getSecondWord();
    Item item = player.getCurrentRoom().takeItem(itemName);

    if (item != null) {
        player.addItem(item);
        System.out.println("You picked up: " + item);
    } else {
        System.out.println("There is no item by that name here.");
    }
}

    /**
     * Try to drop the item the player is carrying.
     */
    private void dropItem(Command command)
    {
    if (!command.hasSecondWord()) {
        System.out.println("Drop what?");
        return;
    }

    String itemName = command.getSecondWord();
    if (!player.hasItem(itemName)) {
        System.out.println("You aren't carrying that.");
        return;
    }

    Item dropped = player.removeItem(itemName);
    player.getCurrentRoom().addItem(dropped);
    System.out.println("You dropped: " + dropped);
}
    
    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if (command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;
        }
    }
}