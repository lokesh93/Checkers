package com.chess.board;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

public class ChessPiece extends JButton {

	public String chessPlayer;// white or black, or empty
	public String position;
	public volatile int draggedAtX, draggedAtY;

	public ChessPiece(String player, String position) {
		super();
		this.chessPlayer = player;
		this.position = position;
		
		// This call causes the JButton not to paint
		// the background.
		// This allows us to paint a round background.
		setContentAreaFilled(false);
		
//		addMouseListener(new MouseAdapter(){
//            public void mousePressed(MouseEvent e){
//                draggedAtX = e.getX();
//                draggedAtY = e.getY();
//            }
//        });

//        addMouseMotionListener(new MouseMotionAdapter(){
//            public void mouseDragged(MouseEvent e){
//                setLocation(e.getX() - draggedAtX + getLocation().x,
//                        e.getY() - draggedAtY + getLocation().y);
//            }
//        });
	}

	@Override
	public void paintComponent(Graphics g) {
		if (getModel().isArmed()) {
			// When mouse pressed, change the color
			g.setColor(getBackground());
		} else {
			g.setColor(getBackground());
		}
		g.fillOval(0, 0, getSize().width - 1, getSize().height - 1);
		super.paintComponent(g);
	}

	// Paint the border of the button using a simple stroke.
	protected void paintBorder(Graphics g) {
		g.setColor(getForeground());
		g.drawOval(0, 0, getSize().width - 1, getSize().height - 1);
	}
	
}