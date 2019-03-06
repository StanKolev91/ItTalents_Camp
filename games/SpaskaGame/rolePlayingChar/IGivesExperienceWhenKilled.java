package diabloGame.rolePlayingChar;

import diabloGame.Game;

public interface IGivesExperienceWhenKilled {
    double experienceGiven = (60 + Game.getLevel()*10);
}
