package main.game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Game {
	
	private int width, height;
	
	private int turn = 1;
	
	private int[] board, prevBoard;
	
	public Game(int width, int height) {
		this.width = width;
		this.height = height;
		
		board = new int[9];
	}

	public void update() {
		
	}
	
	public void render(Graphics g) {
		for(int i = 0;i < board.length;i++) {
			int x = i%3;
			int y = (int)(i/3);
			switch(board[i]) {
			case(-1):
				g.setColor(new Color(255, 0, 0));
				g.fillOval(x*width/3, y*height/3, width/3, height/3);
				break;
			case(0):
				break;
			case(1):
				g.setColor(new Color(0, 0, 255));
				g.fillOval(x*width/3, y*height/3, width/3, height/3);
				break;
			}
			g.setColor(new Color(0, 0, 0));
			g.drawRect(x*width/3, y*height/3, width/3, height/3);
		}
	}
	
	public ArrayList<int[]> getActions() {
		ArrayList<int[]> actions = new ArrayList<int[]>();
		for(int i = 0;i < board.length;i++) {
			if(board[i] == 0) {
				int[] action = {i};
				actions.add(action);
			}
		}
		return actions;
	}
	
	public int hasWon() {
		int player = 0;
		for(int i = 0;i < 3;i++) if(Math.abs(board[i]+board[i+3]+board[i+6]) == 3) player = (int) Math.signum(board[0*i]+board[1*i]+board[2*i]);
		for(int i = 0;i < 3;i++) if(Math.abs(board[i*3]+board[i*3+1]+board[i*3+2]) == 3)  player = (int) Math.signum(board[i*3]+board[i*3+1]+board[i*3+2]);
		if(Math.abs(board[0]+board[4]+board[8]) == 3)  player = (int) Math.signum(board[0]+board[4]+board[8]);
		if(Math.abs(board[2]+board[4]+board[6]) == 3)  player = (int) Math.signum(board[2]+board[4]+board[6]);
		return player;
	}
	
	public void makeMove(int i) {
		prevBoard = board;
		board[i] = turn;
		turn*=-1;
	}
	
	public void undoMove() {
		if(prevBoard != null)board = prevBoard;
		else return;
		prevBoard = null;
	}
	
	public int getTurn() {
		return turn;
	}

	public int[] getBoard() {
		return board;
	}
	
	public void reset() {
		board = new int[9];
		prevBoard = null;
		turn = 1;
	}
	
}
