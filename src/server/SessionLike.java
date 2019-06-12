package server;

import java.util.UUID;
import game.User;

public interface SessionLike extends Runnable{
	public UUID getUUID();
	public User getUser();
}
