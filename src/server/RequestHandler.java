package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class RequestHandler implements Runnable {
	private final Socket client;
	private final ServerSocket serverSocket;
	//For testing; tbr
	private static final String[][] users = new String[][] {
		{"BenJeth", "b_merkle-Benjamin@web.de", "Passwort"},
		{"TestUser1","test@user.com","einAnderesPasswort"},
		{"EinUserMitEinemSehrLangenNamenUndPasswort","einUserMitEinemSehrLangenNamen@undPasswort.uk","123&4einziemlichLangesPasswortHabenWirNunHierDasTippIchEinGanzUnverdrossenUndWarteHierAufPossen"},
	};
	
	public RequestHandler(Socket client, ServerSocket serverSocket) {
		this.client = client;
		this.serverSocket = serverSocket;
	}
	
	@Override
	public void run() {
		System.out.println("-=Process Request=-");
		StringBuffer sb = new StringBuffer();
		PrintWriter out = null;
		try {
			System.out.println("running service, " + Thread.currentThread());
			out = new PrintWriter(client.getOutputStream(), true);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			char[] buffer = new char[100];
			int numberOfChars = bufferedReader.read(buffer, 0, 100);
			String message = new String(buffer, 0, numberOfChars);
			String[] messageValues = message.split("\\s");
			if(messageValues[0].compareToIgnoreCase("Exit") == 0) {
				out.println("Server ended");
				if ( !serverSocket.isClosed() ) {
			          System.out.println("--- Ende Handler:ServerSocket close");
			          try {
			            serverSocket.close();
			          } catch ( IOException e ) { }
			        }
			}else {
				String returnMessage = this.processCommand(bufferedReader, messageValues);
				sb.append(returnMessage);
				sb.append("$END$");
			}
		} catch (IOException e) {
			System.out.println("IOException in Handler:");
			System.out.println(e.getMessage());
		}finally {
			out.println(sb);
			if ( !client.isClosed() ) {
		        System.out.println("****** Handler:Client close");
		        try {
		          client.close();
		        } catch ( IOException e ) { }
		      } 
		}
	}
	
	private String processCommand(BufferedReader reader, String[] startMessage) throws IOException {
		String returnMessage = "Session=NULL;Code=000;Message=Invalid Command";
		if(startMessage[0].equalsIgnoreCase("Login")) {
			String[] loginCommand = this.readCommand(reader, startMessage);
			for(int i=0; i<users.length; i++) {
				String userName = users[i][0];
				String email = users[i][1];
				String password = users[i][2];
				if(loginCommand[1].equals(userName)) {
					if(loginCommand[2].equals(password)) {
						Application application = GameServerApplication.GetInstance();
						SessionLike session = application.getSession(GameServerApplication.getUUID(userName, email));
						
						if(session == null) {
							session = application.createSession(userName, email);
							returnMessage = "Session=" + session.getUUID() + ";Code=100;Message=Login successfull";
						}else {
							returnMessage = "Session=" + session.getUUID() + ";Code=501;Message=Already logged in";
						}			
					}else returnMessage = "Session=NULL;Code=500;Message=User or password invalid";
				}else returnMessage = "Session=NULL;Code=500;Message=User or password invalid";
			}
		}
		return returnMessage;
	}
	
	private String[] readCommand(BufferedReader reader, String[] startMessage) throws IOException {
		String[] command;
		String commandString = startMessage[0];
		int lengthOfLastMessage = startMessage.length;
		String[] readMessage = startMessage;
		
		int currentIndex = 1;
		String nextString = readMessage[currentIndex];
		while(!nextString.equals("$END$")) {
			if(currentIndex >= readMessage.length) {
				char[] buffer = new char[1000];
				int numberOfChars = reader.read(buffer, lengthOfLastMessage, lengthOfLastMessage+1000); 
				String message = new String(buffer, 0, numberOfChars);
				readMessage = message.split("\\s");
				currentIndex = 0;
			}
			nextString = readMessage[currentIndex];
			commandString = commandString + ";" + nextString;
			currentIndex++;
		}
		
		command = commandString.split(";");
		return command;
	}
	
	

}
