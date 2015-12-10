package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.screens.LoadScreen;
import com.mygdx.game.screens.MainMenuScreen;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.screens.TransScreen;
import com.mygdx.game.ui.UI;
import com.mygdx.game.utils.Assets;

public class Joc extends Game {
	public LoadScreen load;
    public MainMenuScreen menu;
    public PlayScreen play;
    public TransScreen transition;

	@Override
	public void create() {
	    load= new LoadScreen(this);
        menu= new MainMenuScreen(this);
        play= new PlayScreen(this) ;
        transition= new TransScreen(this);
        setScreen(load);
	}

    @Override
    public void dispose() {
        play.dispose();
        menu.dispose();
        load.dispose();
        transition.dispose();
        Assets.man.dispose();
    }

}
