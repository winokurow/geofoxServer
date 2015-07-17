package org.geohunt.service.game.entities;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "game")
public class Game {
	public String getTyp() {
		return typ;
	}

	public void setTyp(String typ) {
		this.typ = typ;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	private String typ;
	private String name;
	private String password;
	private String user;
}
