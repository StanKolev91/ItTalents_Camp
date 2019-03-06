package diabloGame;

import diabloGame.Drop.IDropsGoldWhenKilled;
import diabloGame.items.Item;
import diabloGame.items.Trasch;
import diabloGame.rolePlayingChar.humanoids.Hero;
import diabloGame.rolePlayingChar.IGivesExperienceWhenKilled;
import diabloGame.rolePlayingChar.RolePlayingCharacter;
import java.util.Random;
import java.util.Scanner;

public class Fight {
    private Hero player;
    private RolePlayingCharacter oponent;
    Scanner sc = new Scanner(System.in);
    Random rand = new Random();

    public Fight(Hero player, RolePlayingCharacter oponent) {
        this.player = player;
        this.oponent = oponent;
        if (new Random().nextBoolean()) {
            while (!diabloGame.Game.isGameOver()) {
                player.attack(this.oponent);
                if (playerWonCheck()) {
                    break;
                }
                oponent.attack(this.player);
                if (diabloGame.Game.isGameOver()) break;
            }
        } else {
            while (!diabloGame.Game.isGameOver()) {
                oponent.attack(this.player);
                if (diabloGame.Game.isGameOver()) {
                    break;
                }
                player.attack(this.oponent);
                if (playerWonCheck()) {
                    break;
                }
            }
        }
    }

    private boolean playerWonCheck() {
        if (this.oponent.getHealth() <= 0) {
                System.out.println(this.player.getName() + " won!\n" );
                    if (this.oponent instanceof IGivesExperienceWhenKilled){
                        System.out.println(this.player.getName()+" receive "+IGivesExperienceWhenKilled.experienceGiven+" experience!");
                        this.player.addScExperience(IGivesExperienceWhenKilled.experienceGiven);
                        if (this.oponent instanceof IDropsGoldWhenKilled) {
                            double bonusMoney = rand.nextInt(this.oponent.getLevel()) + (rand.nextDouble());
                            player.addMoney(bonusMoney);
                            System.out.println("You pickpocket " + this.oponent.getName() + " and found " + String.format("%.2f", bonusMoney) + " gold.");
                        }
                    }
                    this.oponent.setDrop(new Trasch().generateDrop(this.oponent));
                    System.out.println("\nDo you want to add this Item to inventory - YES/NO");
                    String answer = sc.nextLine().trim().toLowerCase();
                    if (answer.contains("yes")||answer.contains("y")){
                        this.player.addToInventory((Item) oponent.getDrop());
                        System.out.println("<<"+this.oponent.getDrop().getItemType().substring(0,1).toUpperCase()+
                                this.oponent.getDrop().getItemType().substring(1)+" added to inventory>>");
                    }
                    this.player.stats();
            return true;
        } else return false;
    }
}
