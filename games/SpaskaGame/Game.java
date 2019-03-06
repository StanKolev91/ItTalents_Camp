package diabloGame;

import diabloGame.rolePlayingChar.humanoids.Hero;
import diabloGame.worldPlaces.Place;
import diabloGame.worldPlaces.Square;

import java.util.Scanner;

public class Game {
    public static Place currentPlace;
    private Hero player;
    private static int level;
    private static boolean isGameOver;
    private static double expTillNextLevel;
    Scanner sc = new Scanner(System.in);
    Game() {
        Game.level = 1;
        Game.expTillNextLevel =Game.getLevel() * 100;
        System.out.println("Welcome in Lamqta Spaska game :)");
        System.out.println("Enter player's name:\n");
        String playersName = sc.nextLine().trim();
        this.player = new Hero(playersName);
        Game.isGameOver = false;
    }
    public static double getExpTillNextLevel() {
        return expTillNextLevel;
    }

    public static void setExpTillNextLevel(double expTillNextLevel) {
        Game.expTillNextLevel = expTillNextLevel;
    }

    public static boolean isGameOver() {
        return Game.isGameOver;
    }

    public static void setIsGameOver(boolean isGameOver) {
        System.out.println("\nGame Over!\n");
        Game.isGameOver = isGameOver;
        playAgain();
    }

    public static void setLevel(int level) {
        diabloGame.Game.level = level;
    }

    public static int getLevel() {
        return level;
    }

    void play() {
        diabloGame.Game.currentPlace = new Square(this.player);
        diabloGame.Game.currentPlace.options();
    }
    private static void playAgain(){
        System.out.println("Do you wanna play again?\n");
        String input;
        input = new Scanner(System.in).nextLine().trim().toLowerCase();
        if (input.contains("yes")||input.contains("y")){
            new Game().play();
        }
    }
}
