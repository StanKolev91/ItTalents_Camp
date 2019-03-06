package diabloGame.worldPlaces;

import diabloGame.Fight;
import diabloGame.Game;
import diabloGame.rolePlayingChar.Beast;
import diabloGame.rolePlayingChar.humanoids.Hero;
import diabloGame.rolePlayingChar.humanoids.Mage;
import diabloGame.rolePlayingChar.humanoids.Warrior;
import diabloGame.rolePlayingChar.RolePlayingCharacter;

import java.util.Random;
import java.util.Scanner;

public class Forest implements Place{
    private SpaskasLiar bossLiar;
    private RolePlayingCharacter enemy;
    private Hero player;
    Scanner sc = new Scanner(System.in);
    Random rand = new Random();

    Forest(Hero player) {
        this.player = player;
        this.bossLiar = new SpaskasLiar(player);
    }

    public SpaskasLiar getBossLiar() {
        return bossLiar;
    }

    private void fightSpaska() {
        new Fight(player, bossLiar.getSpaska());
    }

    private void fightRandomEnemy() {
        player.stats();
        new Fight(player, enemy);
    }

     public void options() {
        System.out.println("You are in the forest now.\nWhat do you wanna do:\n1. Search for Spaska's Liar\n2. Find enemy to fight\n3. Return");
        String nextMove;
            do {
                nextMove = sc.nextLine().trim();
            } while (!nextMove.matches("[1-3]"));
            switch (nextMove) {
                case "1": {
                    fightSpaska();
                    break;
                }
                case "2": {
                    this.enemy = rand.nextBoolean() ? ((rand.nextBoolean())?new Mage("Grand Wizard Pescho"):new Warrior("Joe The Fat One")) : new Beast();
                    fightRandomEnemy();
                    break;
                }
                case "3": {
                    returnToSquare(player);
                    break;
                }
            }
         System.out.println();
            if (!Game.isGameOver()) {
                diabloGame.Game.currentPlace.options();
            }
    }
}
