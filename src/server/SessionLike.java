package server;

import java.util.UUID;
import game.User;

public interface SessionLike {
	public UUID getUUID();
	public User getUser();
}
