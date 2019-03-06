package diabloGame.worldPlaces;

import diabloGame.items.Upgradable;
import diabloGame.Game;
import diabloGame.rolePlayingChar.humanoids.Hero;

import java.util.Scanner;

public class Shop implements Place{
    private double price;
    private Hero player;

    Scanner sc = new Scanner(System.in);

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    Shop(Hero player) {
        this.player = player;
        this.price = 10 + Game.getLevel() * 5;
    }

    private void upgradeItem() {
        int itemNumber = this.player.chooseItemFromInventory();
        if (itemNumber>=0) {
            if (player.showGold() > this.price) {
                if (player.getItemFromInventory(itemNumber) instanceof Upgradable) {
                    ((Upgradable) player.getItemFromInventory(itemNumber)).upgrade();
                    pay();
                    player.getItemFromInventory(itemNumber).itemInfo();
                }
            } else System.out.println("<<not enough money>>");
        }else Game.currentPlace.options();
    }

    private void pay(){
        this.player.addMoney(- this.price);
    }

    private void sellItem() {
        int itemNumber = this.player.chooseItemFromInventory();
        if (itemNumber>=0 && this.player.getItemFromInventory(itemNumber)!=null) {
            this.player.addMoney(this.player.getItemFromInventory(itemNumber).getSellPrice());
            System.out.println("You sold " + this.player.getItemFromInventory(itemNumber).getItemType() + " for " +
                    String.format("%.2f",this.player.getItemFromInventory(itemNumber).getSellPrice()));
            this.player.removeItemFromInventory(itemNumber);
        }else Game.currentPlace.options();
    }

    public void options(){
        System.out.println("Your gold: " +String.format("%.2f", this.player.showGold())+" gold"+
                "\nWelcome to Shop, what do you wanna do:\n1. Upgrade equipment  \n2. Sell item" +
                "\n3. Check the merchandise for suitable item\n4. Return");
        String nextMove;
            do {
                nextMove = sc.nextLine().trim();
            } while (!nextMove.matches("[1-4]"));
            switch (nextMove) {
                case "1": {
                    upgradeItem();
                    break;
                }
                case "2": {
                    sellItem();
                    break;
                }
                case "3":{
                    System.out.println("No available merchandise!");
                    break;
                }
                case "4": {
                    returnToSquare(player);
                    break;
                }
            }
        System.out.println();
        if (!Game.isGameOver()) {
            diabloGame.Game.currentPlace.options();
        }    }
}
