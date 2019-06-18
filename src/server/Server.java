package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Server {
	
	private int status; // 0 = not started; 1 = started
	
	public Server() {
		this.status = 0;
	}
	
	public void start(String var) throws IOException {
		if(status == 0) {
			this.status = 1;
			final ExecutorService pool;
			final ServerSocket serverSocket;
			int port = 1227;
			if(var.equals("C")) {
				pool = Executors.newCachedThreadPool();
			}else {
				int poolSize = 4;
				pool = Executors.newFixedThreadPool(poolSize);
			}
			serverSocket = new ServerSocket(port);
			
			Thread t1 = new Thread(new NetworkService(pool, serverSocket));
			
			t1.start();
			
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					System.out.println("Strg+C, pool.shutdown");
					pool.shutdown();
					try {
						pool.awaitTermination(4L, TimeUnit.SECONDS);
						if(!serverSocket.isClosed()) {
							System.out.println("ServerSocket close");
							serverSocket.close();
						} 
					}catch (IOException e) { }
					catch (InterruptedException interupt) { }
				}
			});
		}else {
			System.out.println("Server was already started.");
		}
	}
	
	public static void main(String[] args) {
		Server server = new Server();
		String var = "C";
		if(args.length > 0) {
			var = args[0].toUpperCase();
		}
		try {
			server.start(var);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

}
