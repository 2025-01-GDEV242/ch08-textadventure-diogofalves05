import java.util.HashMap;  
import java.util.Set; 
import java.util.Stack;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  
 *  Users can walk around some scenery.
 * 
 * @author  
 * @version 2016.02.29
 */

public class Game 
{
    private Parser parser;
    private Player player;
    private Stack<Room> roomHistory;
    
    public Game() 
    {
        createRooms();
        roomHistory = new Stack<>();
        parser = new Parser(); 
    }

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
        theater.setExit("up", attic);
        pub.setExit("east", outside);
        cellar.setExit("up", lab);
        attic.setExit("down", theater);
        library.setExit("south", pub);

        Item book = new Item("an ancient book", 3);
        Item beer = new Item("a pint of cold beer", 1);
        Item computer = new Item("a dusty computer", 10);
        Item scroll = new Item("a magical scroll", 2);
        Item cookie = new Item("a magic cookie", 1);

        library.addItem(book);
        pub.addItem(beer);
        lab.addItem(computer);
        attic.addItem(scroll);
        office.addItem(cookie);

        player = new Player(outside);
    }

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

    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println(player.getCurrentRoom().getLongDescription());
    }

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
            case INVENTORY:
                showInventory();
                break;
            case ITEMS:
                showItems();
                break;
            case EAT:
                eatItem(command);
                break;
        }
        return wantToQuit;
    }

    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println("Your command words are:");
        parser.showCommands();
    }

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

    private void goBack()
    {
        if (!roomHistory.isEmpty()) {
            player.setCurrentRoom(roomHistory.pop());
            System.out.println(player.getCurrentRoom().getLongDescription());
        } else {
            System.out.println("You can't go back any further.");
        }
    }

    private void takeItem(Command command)
    {
        if (!command.hasSecondWord()) {
            System.out.println("Take what?");
            return;
        }

        String itemName = command.getSecondWord();
        Item item = player.getCurrentRoom().takeItem(itemName);

        if (item != null) {
            if (player.addItem(item)) {
                System.out.println("You picked up: " + item);
            } else {
                System.out.println("That item is too heavy to carry.");
                player.getCurrentRoom().addItem(item);
            }
        } else {
            System.out.println("There is no item by that name here.");
        }
    }

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

    private void showInventory()
    {
        System.out.println(player.getInventoryString());
    }

    private void showItems()
    {
        System.out.println(player.getInventoryString());
        System.out.println("Total weight: " + player.getTotalWeight());
    }

    private void eatItem(Command command)
    {
        if (!command.hasSecondWord()) {
            System.out.println("Eat what?");
            return;
        }

        String itemName = command.getSecondWord();

        if (!player.hasItem(itemName)) {
            System.out.println("You are not carrying that.");
            return;
        }

        if (itemName.equalsIgnoreCase("cookie") || itemName.contains("cookie")) {
            Item cookie = player.removeItem(itemName);
            player.increaseMaxWeight(10);
            System.out.println("You ate the magic cookie! You feel stronger. Max weight is now " + player.getMaxWeight() + ".");
        } else {
            System.out.println("You can't eat that.");
        }
    }

    private boolean quit(Command command) 
    {
        if (command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        } else {
            return true;
        }
    }
}