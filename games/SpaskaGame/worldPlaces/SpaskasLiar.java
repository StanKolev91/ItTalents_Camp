package diabloGame.worldPlaces;

import diabloGame.Fight;
import diabloGame.rolePlayingChar.Beast;
import diabloGame.rolePlayingChar.humanoids.Hero;

import java.util.Scanner;

public class SpaskasLiar implements Place {
    private Beast spaska;
    private Hero player;
    Scanner sc = new Scanner(System.in);

    SpaskasLiar(Hero player) {
        this.spaska = new Beast();
        this.spaska.setLevel(100);
        this.spaska.updateStats();
        this.spaska.setName("Lamqta Spaska");
        this.player = player;
    }

    Beast getSpaska() {
        return spaska;
    }

    public void options() {
        System.out.println("You see the mighty Spaska, what do you wanna do:\n1. Flee in fear cuz you are a chicken\n2. Die ahahaha");
        this.spaska.stats();
        String input;
        do {
            input = sc.nextLine().trim();
        } while (!input.matches("[1-2]"));
        switch (input) {
            case "1": {
                returnToSquare(this.player);
                diabloGame.Game.currentPlace.options();
            }
            case "2": {
                new Fight(player, spaska);
                break;
            }
        }
    }
}
