package com.chess.ai;

import java.awt.Color;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import javax.swing.border.LineBorder;

import com.chess.board.ChessBoardBlock;
import com.chess.board.ChessPiece;
import com.chess.board.GameStatus;
import com.chess.board.MainFrame;

/**
 * This Class is all for Assignment 3
 */
public class AIRobot {

	public static final String AI_STATUS_HUMAN = "human";
	public static final String AI_STATUS_BLACK = "black";
	public static final String AI_STATUS_WHITE = "white";
	
	private static HashMap<String, ChessPiece> tmpPiece = null;
	private static ChessPiece tp = null;
	
	public AIRobot() {
		
	}
	
	/**
	 * Get all pieces of flag player
	 * @param m
	 * @param ps
	 * @param flag
	 * @return
	 */
	public static HashMap<String, ChessPiece> getPlayerPieces(MainFrame m, HashMap<String, ChessPiece> ps, String flag) {
		HashMap<String, ChessPiece> tmp = new HashMap<String, ChessPiece>();
		
		for (int i=0; i<ps.size(); i++) {
			ChessPiece tp = ps.get(m.order[i]);
			if (tp.chessPlayer.equals(flag)) {
				tmp.put(tp.position, tp);
			}
		}
		
		return tmp;
	}
	
	/**
	 * Once this method is called in a executing game, the robot will control a piece to move. The robot act the player
	 * that user identified in menu
	 * @param m
	 */
	public static void robotMove(MainFrame m) {
//		System.out.println("robot move");
		if (m.status.getStatus().equals(GameStatus.GAME_STATUS_EXEC) && m.activePlayer.equals(m.aiFlag)) {
			int level = 0;
			tmpPiece = m.piece;
			
			// Get all movable pieces
			tmpPiece = getMovablePieces(m, tmpPiece);
			level = 4;
			if (tmpPiece.size() == 0) {
				return;
			}
			
			// If robot can capture a piece, take them first
			if (getPiecesCanCapture(m, tmpPiece).size() != 0) {
				level = 1;
				System.out.println("level:" + level);
				tmpPiece = getPiecesCanCapture(m, tmpPiece);
				System.out.println("level:" + level);
			}
			
			// If there are pieces will be captured by player, take them first
			else if (getPiecesWillBeCaptured(m, getPlayerPieces(m, m.piece, m.aiFlag)).size() != 0) {
				level = 2;
				System.out.println("level:" + level);
				tmpPiece = getPiecesWillBeCaptured(m, getPlayerPieces(m, m.piece, m.aiFlag));
				System.out.println("level:" + level);
			}
			
			// If there are pieces will be a King, take them
			else if (getPiecesCanBeKing(m, tmpPiece).size() != 0) {
				level = 3;
				System.out.println("level:" + level);
				tmpPiece = getPiecesCanBeKing(m, tmpPiece);
				System.out.println("level:" + level);
			}
			
			// Randomly get a piece that can move (or capture a piece)
			Object[] pieces = tmpPiece.values().toArray();
			for (int i=0; i<pieces.length; i++) {
				System.out.print(((ChessPiece)pieces[i]).position + " ");
			}
			if (pieces.length == 0) {
				return;
			}
			Random random = new Random();
			int index = random.nextInt(pieces.length);
			tp = (ChessPiece)pieces[index];
			
			// Get the available position of the piece
			Vector<ChessBoardBlock> avaiBlocks = m.getMovablePosition(tp);
			
			if (level == 4) {
				System.out.println("level:" + level);
				changePiecePosition(m, tp, avaiBlocks.get(random.nextInt(avaiBlocks.size()-1)+1).position);
			} else if (level == 1) {
				System.out.println("level:" + level);
				// If there a position, the piece moved there and able to capture a opposite player's piece,
				// Record this position
				Vector<ChessBoardBlock> capturableBlocks = new Vector<ChessBoardBlock>();
				for (int i=0; i<avaiBlocks.size(); i++) {
					char fh = tp.position.charAt(0);
					int fv = Integer.parseInt(tp.position.substring(1));
					char th = avaiBlocks.get(i).position.charAt(0);
					int tv = Integer.parseInt(avaiBlocks.get(i).position.substring(1));
					if (Math.abs(fv-tv) == 2) {
						capturableBlocks.add(avaiBlocks.get(i));
					}
				}
				changePiecePosition(m, tp, capturableBlocks.get(random.nextInt(capturableBlocks.size())).position);
			} else if (level == 2) {
				System.out.println("level:" + level + "    " + tp.position);
				// Help a piece which will be captured
				char h = tp.position.charAt(0);
				int v = Integer.parseInt(tp.position.substring(1));
				
				ChessPiece t1 = m.piece.get(Character.toString((char)(h+1)) + (v+1));
				ChessPiece t2 = m.piece.get(Character.toString((char)(h-1)) + (v+1));
				ChessPiece t3 = m.piece.get(Character.toString((char)(h+1)) + (v-1));
				ChessPiece t4 = m.piece.get(Character.toString((char)(h-1)) + (v-1));
				
				if (tp.chessPlayer.equals("white")) {
					if (t1!=null && t1.chessPlayer.equals("black") && t4.chessPlayer.equals("empty") && getHelpPiece(m, tp, t4)!=null) {
						changePiecePosition(m, getHelpPiece(m, tp, t4), t4.position);
//						System.out.println(getHelpPiece(m, tp, t4).chessPlayer + " " + getHelpPiece(m, tp, t4).position + " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					} else if (t2!=null && t2.chessPlayer.equals("black") && t3.chessPlayer.equals("empty") && getHelpPiece(m, tp, t3)!=null) {
						changePiecePosition(m, getHelpPiece(m, tp, t3), t3.position);
//						System.out.println(getHelpPiece(m, tp, t3).chessPlayer + " " + getHelpPiece(m, tp, t3).position + " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					} else if (t3!=null && t3.chessPlayer.equals("black") && t3.getText()!=null && t2.chessPlayer.equals("empty") && getHelpPiece(m, tp, t2)!=null) {
						changePiecePosition(m, getHelpPiece(m, tp, t2), t2.position);
//						System.out.println(getHelpPiece(m, tp, t2).chessPlayer + " " + getHelpPiece(m, tp, t2).position + " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					} else if (t4!=null && t4.chessPlayer.equals("black") && t4.getText()!=null && t1.chessPlayer.equals("empty") && getHelpPiece(m, tp, t1)!=null) {
						changePiecePosition(m, getHelpPiece(m, tp, t1), t1.position);
//						System.out.println(getHelpPiece(m, tp, t1).chessPlayer + " " + getHelpPiece(m, tp, t1).position + " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					}
				} else if (tp.chessPlayer.equals("black")) {
					if (t1!=null && t1.chessPlayer.equals("white") && t1.getText()!=null && t4.chessPlayer.equals("empty") && getHelpPiece(m, tp, t4)!=null) {
						changePiecePosition(m, getHelpPiece(m, tp, t4), t4.position);
//						System.out.println(getHelpPiece(m, tp, t4).chessPlayer + " " + getHelpPiece(m, tp, t4).position + " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					} else if (t2!=null && t2.chessPlayer.equals("white") && t2.getText()!=null && t3.chessPlayer.equals("empty") && getHelpPiece(m, tp, t3)!=null) {
						changePiecePosition(m, getHelpPiece(m, tp, t3), t3.position);
//						System.out.println(getHelpPiece(m, tp, t3).chessPlayer + " " + getHelpPiece(m, tp, t3).position + " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					} else if (t3!=null && t3.chessPlayer.equals("white") && t2.chessPlayer.equals("empty") && getHelpPiece(m, tp, t2)!=null) {
						changePiecePosition(m, getHelpPiece(m, tp, t2), t2.position);
//						System.out.println(getHelpPiece(m, tp, t2).chessPlayer + " " + getHelpPiece(m, tp, t2).position + " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					} else if (t4!=null && t4.chessPlayer.equals("white") && t1.chessPlayer.equals("empty") && getHelpPiece(m, tp, t1)!=null) {
						changePiecePosition(m, getHelpPiece(m, tp, t1), t1.position);
//						System.out.println(getHelpPiece(m, tp, t1).chessPlayer + " " + getHelpPiece(m, tp, t1).position + " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					}
				}
				
			} 
			
			// If a piece can be a King, move it
			else if (level == 3) {
				System.out.println("level:" + level);
				changePiecePosition(m, tp, avaiBlocks.get(random.nextInt(avaiBlocks.size()-1)+1).position);
			}
		}
//		System.out.println();
	}
	
	/**
	 * Calculate what pieces are able to move by the given pieces collection
	 * @param m
	 * @param ps *Calculation depend on this pieces collection
	 * @return
	 */
	public static HashMap<String, ChessPiece> getMovablePieces(MainFrame m, HashMap<String, ChessPiece> ps) {
		HashMap<String, ChessPiece> tmp = new HashMap<String, ChessPiece>();

		Object[] pieces = ps.values().toArray();
		for (int i=0; i<pieces.length; i++) {
			ChessPiece tp = (ChessPiece)pieces[i];
			if (tp.chessPlayer.equals(m.aiFlag) && m.getMovablePosition(tp).size() != 1) {
				tmp.put(tp.position, tp);
			}
		}
		
		return tmp;
	}
	
	/**
	 * Calculate what pieces are able to move and capture a piece of opposite player by the given pieces collection
	 * @param m
	 * @param ps *Calculation depend on this pieces collection
	 * @return
	 */
	public static HashMap<String, ChessPiece> getPiecesCanCapture(MainFrame m, HashMap<String, ChessPiece> ps) {
		HashMap<String, ChessPiece> tmp = new HashMap<String, ChessPiece>();
		
		Object[] pieces = ps.values().toArray();
		for (int i=0; i<pieces.length; i++) {
			ChessPiece tp = (ChessPiece)pieces[i];
			
			Vector<ChessBoardBlock> avaiBlocks = m.getMovablePosition(tp);
			for (int j=0; j<avaiBlocks.size(); j++) {
				int a = Integer.parseInt(avaiBlocks.get(j).position.substring(1));
				int b = Integer.parseInt(tp.position.substring(1));
				if (Math.abs(a-b) == 2) {
					tmp.put(tp.position, tp);
					break;
				}
			}
		}
		
		return tmp;
	}
	
	/**
	 * Calculate what pieces are able to move and capture a piece of opposite player by the given pieces collection
	 * @param m
	 * @param ps *Calculation depend on this pieces collection
	 * @return
	 */
	public static HashMap<String, ChessPiece> getPiecesWillBeCaptured(MainFrame m, HashMap<String, ChessPiece> ps) {
		HashMap<String, ChessPiece> tmp = new HashMap<String, ChessPiece>();
		
		Object[] pieces = ps.values().toArray();
		for (int i=0; i<pieces.length; i++) {
			ChessPiece tp = (ChessPiece)pieces[i];
//			System.out.println("getPieceCanBeHelp:" + tp.chessPlayer + "  " + tp.position);
			ChessPiece hp = getPieceCanBeHelp(m, tp);
			if (hp!=null) {
				tmp.put(hp.position, hp);
			}
		}
		
		return tmp;
	}
	
	/**
	 * If there is not a piece that can help it, return null
	 * If there is, return the position
	 * @param m
	 * @param tp
	 * @return
	 */
	public static ChessPiece getPieceCanBeHelp(MainFrame m, ChessPiece tp) {
		char h = tp.position.charAt(0);
		int v = Integer.parseInt(tp.position.substring(1));
//		System.out.println("h:" + Character.toString((char)(h)));
//		System.out.println("v:" + v);
		
		ChessPiece t1 = m.piece.get(Character.toString((char)(h+1)) + (v+1));
		ChessPiece t2 = m.piece.get(Character.toString((char)(h-1)) + (v+1));
		ChessPiece t3 = m.piece.get(Character.toString((char)(h+1)) + (v-1));
		ChessPiece t4 = m.piece.get(Character.toString((char)(h-1)) + (v-1));
//		System.out.println("getPieceCanBeHelp:" + (t1==null?null:t1.position) + ":" + (t2==null?null:t2.position) + ":" + (t3==null?null:t3.position) + ":" + (t4==null?null:t4.position));
		
		if (tp.chessPlayer.equals("white")) {
			if (t1!=null && t4!=null && t1.chessPlayer.equals("black") && t4.chessPlayer.equals("empty") && getHelpPiece(m, tp, t4)!=null) {
				System.out.println(tp.chessPlayer + " " + tp.position + " is in danger1");
				return tp;
			} else if (t2!=null && t3!=null && t2.chessPlayer.equals("black") && t3.chessPlayer.equals("empty") && getHelpPiece(m, tp, t3)!=null) {
				System.out.println(tp.chessPlayer + " " + tp.position + " is in danger2");
				return tp;
			} else if (t3!=null && t2!=null && t3.chessPlayer.equals("black") && t3.getText()!=null && t2.chessPlayer.equals("empty") && getHelpPiece(m, tp, t2)!=null) {
				System.out.println(tp.chessPlayer + " " + tp.position + " is in danger3");
				return tp;
			} else if (t4!=null && t1!=null && t4.chessPlayer.equals("black") && t4.getText()!=null && t1.chessPlayer.equals("empty") && getHelpPiece(m, tp, t1)!=null) {
				System.out.println(tp.chessPlayer + " " + tp.position + " is in danger4");
				return tp;
			}
		} else if (tp.chessPlayer.equals("black")) {
			if (t1!=null && t4!=null && t1.chessPlayer.equals("white") && t1.getText()!=null && t4.chessPlayer.equals("empty") && getHelpPiece(m, tp, t4)!=null) {
				System.out.println(tp.chessPlayer + " " + tp.position + " is in danger5");
				return tp;
			} else if (t2!=null && t3!=null && t2.chessPlayer.equals("white") && t2.getText()!=null && t3.chessPlayer.equals("empty") && getHelpPiece(m, tp, t3)!=null) {
				System.out.println(tp.chessPlayer + " " + tp.position + " is in danger6");
				return tp;
			} else if (t3!=null && t2!=null && t3.chessPlayer.equals("white") && t2.chessPlayer.equals("empty") && getHelpPiece(m, tp, t2)!=null) {
				System.out.println(tp.chessPlayer + " " + tp.position + " is in danger7");
				return tp;
			} else if (t4!=null && t1!=null && t4.chessPlayer.equals("white") && t1.chessPlayer.equals("empty") && getHelpPiece(m, tp, t1)!=null) {
				System.out.println(tp.chessPlayer + " " + tp.position + " is in danger8");
				return tp;
			}
		}
		return null;
	}
	
	/**
	 * Get the piece that can help piece(p)
	 * @param m
	 * @param p
	 * @param helpP
	 * @return
	 */
	public static ChessPiece getHelpPiece(MainFrame m, ChessPiece p, ChessPiece helpP) {
		char ph = p.position.charAt(0);
		int pv = Integer.parseInt(p.position.substring(1));
		char hh = helpP.position.charAt(0);
		int hv = Integer.parseInt(helpP.position.substring(1));

		ChessPiece t1 = m.piece.get(Character.toString((char)(hh+1)) + (hv+1));
		ChessPiece t2 = m.piece.get(Character.toString((char)(hh-1)) + (hv+1));
		ChessPiece t3 = m.piece.get(Character.toString((char)(hh+1)) + (hv-1));
		ChessPiece t4 = m.piece.get(Character.toString((char)(hh-1)) + (hv-1));
//		System.out.println("getHelpPiece p:" + p.chessPlayer + "  " + p.position);
//		System.out.println("getHelpPiece helpP:" + helpP.chessPlayer + "  " + helpP.position);
		
		if (p.chessPlayer.equals("white") && t1!=null && p.position.equals(t1.position)) {
			System.out.println("getHelpPiece1:" + t1.chessPlayer + "  " + t1.position);
			if (t2!= null && t2.chessPlayer.equals("white") && t2.getText()!=null) return t2;
			if (t3!= null && t3.chessPlayer.equals("white")) return t3;
			if (t4!= null && t4.chessPlayer.equals("white")) return t4;
		} else if (p.chessPlayer.equals("white") && t2!=null && p.position.equals(t2.position)) {
			System.out.println("getHelpPiece2:" + t2.chessPlayer + "  " + t2.position);
			if (t1!= null && t1.chessPlayer.equals("white") && t1.getText()!=null) return t1;
			if (t3!= null && t3.chessPlayer.equals("white")) return t3;
			if (t4!= null && t4.chessPlayer.equals("white")) return t4;
		} else if (p.chessPlayer.equals("white") && t3!=null && p.position.equals(t3.position)) {
			System.out.println("getHelpPiece3:" + t3.chessPlayer + "  " + t3.position);
			if (t1!= null && t1.chessPlayer.equals("white") && t1.getText()!=null) return t1;
			if (t2!= null && t2.chessPlayer.equals("white") && t2.getText()!=null) return t2;
			if (t4!= null && t4.chessPlayer.equals("white")) return t4;
		} else if (p.chessPlayer.equals("white") && t4!=null && p.position.equals(t4.position)) {
			System.out.println("getHelpPiece4:" + t4.chessPlayer + "  " + t4.position);
			if (t1!= null && t1.chessPlayer.equals("white") && t1.getText()!=null) return t1;
			if (t2!= null && t2.chessPlayer.equals("white") && t2.getText()!=null) return t2;
			if (t3!= null && t3.chessPlayer.equals("white")) return t3;
		} 
		
		else if (p.chessPlayer.equals("black") && t1!=null && p.position.equals(t1.position)) {
			System.out.println("getHelpPiece5:" + t1.chessPlayer + "  " + t1.position);
			if (t2!= null && t2.chessPlayer.equals("black")) {System.out.println("t2");return t2;};
			if (t3!= null && t3.chessPlayer.equals("black") && t3.getText()!=null) {System.out.println("t3");return t3;};
			if (t4!= null && t4.chessPlayer.equals("black") && t4.getText()!=null) {System.out.println("t4");return t4;};
		} else if (p.chessPlayer.equals("black") && t2!=null && p.position.equals(t2.position)) {
			System.out.println("getHelpPiece6:" + t2.chessPlayer + "  " + t2.position);
			if (t1!= null && t1.chessPlayer.equals("black")) {System.out.println("t1");return t1;};
			if (t3!= null && t3.chessPlayer.equals("black") && t3.getText()!=null) {System.out.println("t3");return t3;};
			if (t4!= null && t4.chessPlayer.equals("black") && t4.getText()!=null) {System.out.println("t4");return t4;};
		} else if (p.chessPlayer.equals("black") && t3!=null && p.position.equals(t3.position)) {
			System.out.println("getHelpPiece7:" + t3.chessPlayer + "  " + t3.position);
			if (t1!= null && t1.chessPlayer.equals("black")) {System.out.println("t1");return t1;};
			if (t2!= null && t2.chessPlayer.equals("black")) {System.out.println("t2");return t2;};
			if (t4!= null && t4.chessPlayer.equals("black") && t4.getText()!=null) {System.out.println("t4");return t4;};//
		} else if (p.chessPlayer.equals("black") && t4!=null && p.position.equals(t4.position)) {
			System.out.println("getHelpPiece8:" + t4.chessPlayer + "  " + t4.position);
			if (t1!= null && t1.chessPlayer.equals("black")) {System.out.println("t1");return t1;};
			if (t2!= null && t2.chessPlayer.equals("black")) {System.out.println("t2");return t2;};
			if (t3!= null && t3.chessPlayer.equals("black") && t3.getText()!=null) {System.out.println("t3");return t3;};//
		} 
		
		else return null;
		return null;
	}
	
	/**
	 * Get the pieces that will be a King
	 * @param m
	 * @param ps
	 * @return
	 */
	public static HashMap<String, ChessPiece> getPiecesCanBeKing(MainFrame m, HashMap<String, ChessPiece> ps) {
		HashMap<String, ChessPiece> tmp = new HashMap<String, ChessPiece>();
		
		Object[] pieces = ps.values().toArray();
		for (int i=0; i<pieces.length; i++) {
			
			ChessPiece t1 = null;
			ChessPiece t2 = null;
			
			ChessPiece tp = (ChessPiece)pieces[i];
			char h = tp.position.charAt(0);
			int v = Integer.parseInt(tp.position.substring(1));
			if (tp.chessPlayer.equals("white") && tp.getText()==null && v==7) {
				String tmp1 = Character.toString((char)(h+1)) + (v+1);
				t1 = m.piece.get(tmp1);
				String tmp2 = Character.toString((char)(h-1)) + (v+1);
				t2 = m.piece.get(tmp2);
			} else if (tp.chessPlayer.equals("black") && tp.getText()==null && v==2) {
				String tmp1 = Character.toString((char)(h+1)) + (v-1);
				t1 = m.piece.get(tmp1);
				String tmp2 = Character.toString((char)(h-1)) + (v-1);
				t2 = m.piece.get(tmp2);
			}
			
			if ((t1 != null && t1.chessPlayer.equals("empty")) || (t2 != null && t2.chessPlayer.equals("empty"))) {
				tmp.put(tp.position, tp);
			}
		}
		
		return tmp;
	}
	
	/**
	 * Move a piece (p1) to new position (newP)
	 * @param m
	 * @param p1
	 * @param newP
	 */
	public static void changePiecePosition(MainFrame m, ChessPiece p1, String newP) {
		m.board.get(p1.position).setBorderPainted(true);
		m.board.get(newP).setBorderPainted(true);
		m.board.get(p1.position).setBorder(new LineBorder(Color.ORANGE, 4));
		m.board.get(newP).setBorder(new LineBorder(Color.ORANGE, 4));
		m.changePosition(m.piece.get(p1.position), newP);
	}

}
