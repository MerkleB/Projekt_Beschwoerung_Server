package server;

import java.util.UUID;

public interface Application {
	public SessionLike getSession(UUID sessionID);
	public SessionLike createSession(String userName, String email);
	public void endSession(UUID sessionID);
}
