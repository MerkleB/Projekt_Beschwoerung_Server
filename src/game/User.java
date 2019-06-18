package game;

public class User {
	private String userName;
	private String email;
	
	public User(String name, String email) {
		this.userName = name;
		this.email = email;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public String getEmail() {
		return email;
	}
}
