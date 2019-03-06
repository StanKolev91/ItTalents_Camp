package diabloGame.worldPlaces;

import diabloGame.items.Consumables;
import diabloGame.items.IScrolls;
import diabloGame.items.Item;
import diabloGame.Game;
import diabloGame.rolePlayingChar.humanoids.Hero;

import java.util.Scanner;

public class InventoryRoom implements Place {
    private Hero player;
    private double bonusBuff;
    Scanner sc = new Scanner(System.in);

    InventoryRoom(Hero player) {
        this.player = player;
    }

    public void options() {
        System.out.println("You are in your inventory room.\nWhat do you wanna do?" +
                "\n1. Buff your weapon extra damage for the next fight.\n2. Show Character equipt gear.\n3. Show inventory.\n4. SpellBook \n5. Return");
        String nextMove;
        do {
            nextMove = sc.nextLine().trim();

        } while (!nextMove.matches("[1-5]"));
        switch (nextMove) {
            case "1": {
                System.out.println("Your weapon was buffed with " + bonusBuff + " extra damage");
                break;
            }
            case "2": {
                this.player.showGear();
                System.out.println("Do you wanna check your inventory to get put some gear on?");
                String answer = sc.nextLine().trim().toLowerCase();
                if (!(answer.contains("yes") || answer.contains("y"))) {
                    break;
                }
            }
            case "3": {
                int itemNmbr = this.player.chooseItemFromInventory();
                if (itemNmbr >= 0) {
                    Item chosenItem = this.player.getItemFromInventory(itemNmbr);
                    if (chosenItem != null) {
                        System.out.println("What do you wanna do with: ");
                        chosenItem.itemInfo();
                        System.out.println("\n1. Equipt\n2. Consume potion\n3. Learn Spell from Scroll\n4. Cancel");
                        String answer;
                        do {
                            answer = sc.nextLine().trim().toLowerCase();
                        } while (!answer.matches("[1-4]"));
                        switch (answer) {
                            case "1": {
                                this.player.equiptItem(itemNmbr);
                                break;
                            }
                            case "2": {
                                if (chosenItem instanceof Consumables) {
                                    ((Consumables) chosenItem).consume(this.player);
                                    this.player.removeItemFromInventory(itemNmbr);
                                }else {
                                    System.out.println("Not consumable Item!");
                                }
                                break;
                            }
                            case "3": {
                                if (chosenItem instanceof IScrolls) {
                                    ((IScrolls) chosenItem).learn(this.player);
                                    this.player.removeItemFromInventory(itemNmbr);
                                } else {
                                    System.out.println("This is not a Scroll!");
                                }break;

                            }
                            case "4": {
                                break;
                            }
                        }
                    }
                }
                break;
            }
            case "4":{
                //TODO activate/deactivate spell
                this.player.showSpells();
                System.out.println();
                break;
            }
            case "5": {
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
