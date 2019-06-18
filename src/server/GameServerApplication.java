package server;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.UUID;

import game.User;

public class GameServerApplication implements Application {
	
	private static GameServerApplication instance = null;
	
	private Hashtable<UUID, SessionLike> sessions;
	
	public static Application GetInstance() {
		if(instance == null) {
			instance = new GameServerApplication();
			instance.sessions = new Hashtable<UUID, SessionLike>();
		}
		return instance;
	}
	
	public static UUID getUUID(String userName, String email) throws UnsupportedEncodingException {
		String source = userName + email;
		byte[] sourceBytes = source.getBytes("UTF-8");
		return UUID.nameUUIDFromBytes(sourceBytes);
	}
	
	@Override
	public SessionLike getSession(UUID sessionID) {
		return sessions.get(sessionID);
		
	}

	@Override
	public SessionLike createSession(String userName, String email) {
		game.User user = new User(userName, email);
		UUID sessionID = null;
		try {
			sessionID = GameServerApplication.getUUID(userName, email);
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
		}
		Session session = new Session(user, sessionID);
		sessions.put(sessionID, (SessionLike) session);
		return (SessionLike) session;
	}

}
