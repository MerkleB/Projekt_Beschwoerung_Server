package server;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import game.User;

public class Session {
	private UUID id;
	private User user;
	
	public Session(User user) throws UnsupportedEncodingException {
		this.user = user;
		String source = user.getUserName() + user.getEmail();
		byte[] sourceBytes = source.getBytes("UTF-8");
		id = UUID.nameUUIDFromBytes(sourceBytes);
	}
	
}
