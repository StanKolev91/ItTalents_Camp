package diabloGame.worldPlaces;

import diabloGame.Game;
import diabloGame.rolePlayingChar.humanoids.Hero;

import java.util.Scanner;

public class Tavern implements Place {
    private double drinkPrice;
    private double eatPrice;
    private double feedPetPrice;
    private Hero player;
    Scanner sc = new Scanner(System.in);

    Tavern(Hero player) {
        this.drinkPrice = Game.getLevel() * 0.3;
        this.eatPrice = Game.getLevel() * 0.4;
        this.feedPetPrice = Game.getLevel() * 0.3;
        this.player = player;
    }

    private void drink(Hero player) {
        if (player.showGold() >= this.drinkPrice) {
            //TODO implement logic
            System.out.println("Drinking...");
            System.out.println("Your next attack within 1 min will do triple damage, but will cause 10% to you.");
            player.addMoney(- this.drinkPrice);
        } else this.notEnoughMoney();
    }

    private void notEnoughMoney() {
        System.out.println("<<not enough money>>>");
    }

    private void eat(Hero player) {
        if (player.showGold() >= this.eatPrice) {
            System.out.println("Eating...");
            System.out.println("You regained your strength, and have full health!");
            player.heal();
            player.addMoney(-this.eatPrice);
        } else this.notEnoughMoney();
    }

    private void feedPet(Hero player) {
        if (player.getPet() != null) {
            if (player.showGold() >= this.feedPetPrice) {
                //TODO implement logic
                System.out.println("Drinking...");
                System.out.println("You pet regained his strength, and have full health!");
                player.addMoney(- this.feedPetPrice);
            } else this.notEnoughMoney();
        } else System.out.println("<<you have no pet>>");
    }

    public void options() {
        System.out.println("Your gold: " + String.format("%.2f", this.player.showGold())+" gold");
        System.out.println("Greetings " + this.player.getName() + " you are welcome in my Tavern! I'm the Inkeeper. \nHow can i help you?");
        System.out.println("1. Drink some alcohol - " + String.format("%.2f", drinkPrice) +" gold" + "\n2. Eat some food - " + String.format("%.2f", eatPrice)+" gold"
                + "\n3. Feed your pet - " + String.format("%.2f", feedPetPrice)+" gold" + "\n4. Return");
        String nextMove;
        do {
            nextMove = sc.nextLine().trim();
            if (!nextMove.matches("[1-4]")) {
                System.out.println("<<invalid input>>");
            }
        } while (!nextMove.matches("[1-4]"));
        switch (nextMove) {
            case "1": {
                drink(player);
                break;
            }
            case "2": {
                eat(player);
                break;
            }
            case "3": {
                feedPet(player);
                break;
            }
            case "4": {
                returnToSquare(player);
            }
        }
        System.out.println();
        if (!Game.isGameOver()) {
            diabloGame.Game.currentPlace.options();
        }
    }
}
