package pl.karol202.bolekgame.game.main.actions;

import pl.karol202.bolekgame.game.main.ActionManager;

public interface UpdatingAction extends Action
{
	void setUpdateCallback(ActionManager.ActionUpdateCallback callback);
}
