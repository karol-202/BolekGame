package pl.karol202.bolekgame.game.players;

import pl.karol202.bolekgame.server.RemoteUser;

class RemotePlayer extends Player<RemoteUser>
{
	RemotePlayer(RemoteUser remoteUser)
	{
		super(remoteUser);
	}
}
