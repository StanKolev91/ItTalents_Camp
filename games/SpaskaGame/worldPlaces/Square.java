package diabloGame.worldPlaces;

import diabloGame.Game;
import diabloGame.rolePlayingChar.humanoids.Hero;

import java.util.Scanner;

public class Square implements Place {
    private Hero player;
    Scanner sc = new Scanner(System.in);

    public Square(Hero player) {
        this.player = player;
    }

    public void options() {
        String nextMove;
        this.player.stats();
        System.out.println("\nMain Square\nWhere do you wanna go now?\n1. Go to your Inventory room\n2. Go in the " +
                "Tavern, no place like the bar\n3. Go to the Shop to buy some items\n" +
                "4. Go in the Forest to get some training before facing Spaska\n5. Go in Spaska's Liar and face fearsome Spaska");
        do {
            nextMove = sc.nextLine().trim();
        } while (!nextMove.matches("[1-6]"));
        switch (nextMove) {
            case "1": {
                System.out.println("Inventory Room");
                diabloGame.Game.currentPlace = new InventoryRoom(player);
                break;
            }
            case "2": {
                System.out.println("Tavern");
                diabloGame.Game.currentPlace = new Tavern(this.player);
                break;
            }
            case "3": {
                System.out.println("Weapon Shop");
                diabloGame.Game.currentPlace = new Shop(this.player);
                break;
            }
            case "4": {
                System.out.println("Dark Forest");
                diabloGame.Game.currentPlace = new Forest(this.player);
                break;
            }
            case "5": {
                System.out.println("Spaska's Liar");
                diabloGame.Game.currentPlace = new Forest(this.player).getBossLiar();
                break;
            }
            default: {
                returnToSquare(player);
            }
        }
        if (!Game.isGameOver()){
            diabloGame.Game.currentPlace.options();
        }
    }
}
