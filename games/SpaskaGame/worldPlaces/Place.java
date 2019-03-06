package diabloGame.worldPlaces;

import diabloGame.rolePlayingChar.humanoids.Hero;

public interface Place {


     void options();
     default void returnToSquare(Hero player){
            diabloGame.Game.currentPlace = new Square(player);
    }
}
