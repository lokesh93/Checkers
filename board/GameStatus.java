package com.chess.board;

public class GameStatus {

	public static final String GAME_STATUS_INIT = "INIT";
	public static final String GAME_STATUS_EDIT = "EDIT";
	public static final String GAME_STATUS_EXEC = "EXEC";
	public static final String GAME_STATUS_OVER = "OVER";
	
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatusInit() {
		this.status = GAME_STATUS_INIT;
	}

	public void setStatusEdit() {
		this.status = GAME_STATUS_EDIT;
	}

	public void setStatusExec() {
		this.status = GAME_STATUS_EXEC;
	}

	public void setStatusOver() {
		this.status = GAME_STATUS_OVER;
	}

	public GameStatus() {
		this.status = GameStatus.GAME_STATUS_INIT;
	}
	
	public static void main(String a[]) {
		
	}

}
