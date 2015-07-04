package org.geohunt.service.game.rest.exceptions;

public enum CustomError {
	NOT_AUTHORIZED(4010, "Not Authorized"), FORBIDDEN(4030, "Forbidden"), NOT_FOUND(4040,
			"Server is not found. Please connect the support for details."), SERVER_ERROR(5001,
					"Something goes wrong. Please connect the support for details."), OLD_VERSION_ERROR(6002,
							"The version of game client is incorrect. Please update the client to play."), GAME_EXISTS(
									6003, "The game with those name is already exists. Please enter other name.");

	private final int code;
	private final String description;

	private CustomError(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public int getCode() {
		return code;
	}

	@Override
	public String toString() {
		return code + ": " + description;
	}
}
