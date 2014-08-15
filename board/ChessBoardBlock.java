package com.chess.board;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

public class ChessBoardBlock extends JButton {
	
	public String bgColor;
	public String position;
	
	public ChessBoardBlock(String color, String position) {
		super();
		this.bgColor = color;
		this.position = position;
	}

	@Override
	public void paint(Graphics g) {
		// Set color of the board
		this.setBackground(bgColor.equals("white")?Color.WHITE:Color.RED);
		super.paint(g);
	}

}
