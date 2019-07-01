package server;
import java.util.UUID;

import game.User;

public class Session  implements Runnable, SessionLike{
	private UUID id;
	private User user;
	
	public Session(User user, UUID id){
		this.user = user;
		this.id = id;
	}
	
	public User getUser(){
			return this.user;
	}
	

	@Override
	public void run() {
//		try {
//			while(true) {
//				Socket clientSocket = serverSocket.accept();
//								
//			}
//		}catch (IOException e) {
//			System.out.println("-=Session interupted=-");
//		}
	}

	@Override
	public UUID getUUID() {
		return this.id;
	}
	
}
