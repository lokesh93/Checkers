package com.chess.board;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.border.LineBorder;

import com.chess.ai.AIRobot;
import com.chess.menu.MenuItems;

public class MainFrame extends JFrame implements ActionListener, MouseMotionListener, MouseListener, ItemListener {

	private JPanel boardBg;// Assign1, Board background layer
	private JLayeredPane lp;// Assign1, JFrame layering object
	private JPanel body;// Assign1, Pieces layer
	JLabel t;// Assign1, Message
	JButton switcher;// Assign1, switcher button
	JButton confirm;// Assign1, confirm button
	private GridLayout gridLayout;// Assign1, Board layout
	public GameStatus status = new GameStatus();  // Assign2, Game status
	public String activePlayer = new String();// Assign2
	public String aiFlag = AIRobot.AI_STATUS_HUMAN;// Assign3
	public AIRobot robot = new AIRobot();
	public String order[] = { "A8", "B8", "C8", "D8", "E8", "F8", "G8", "H8", 
							  "A7",	"B7", "C7", "D7", "E7", "F7", "G7", "H7", 
							  "A6", "B6", "C6", "D6", "E6", "F6", "G6", "H6", 
							  "A5", "B5", "C5", "D5", "E5", "F5", "G5",	"H5",
							  "A4", "B4", "C4", "D4", "E4", "F4", "G4", "H4",
							  "A3", "B3", "C3", "D3", "E3", "F3", "G3", "H3", 
							  "A2", "B2", "C2", "D2", "E2",	"F2", "G2", "H2", 
							  "A1", "B1", "C1", "D1", "E1", "F1", "G1", "H1" };// Assign1, board position names

	private ChessPiece blackPieces[] = new ChessPiece[12];// Assign1, Black pieces collection
	private ChessPiece whitePieces[] = new ChessPiece[12];// Assign1, White pieces collection
	private ChessPiece spacePieces[] = new ChessPiece[64];// Assign1, No pieces collection

	public HashMap<String, ChessPiece> piece = new HashMap<String, ChessPiece>();// Assign1, all pieces on board temporarily, Assign1 is Vector, Assign2 change to HashMap
	public HashMap<String, ChessBoardBlock> board = new HashMap<String, ChessBoardBlock>();// Assign1, board blocks
	
	public MainFrame(String arg0) {

		// Assign1, Generate menu bar and menu items, and set listener for them
		JMenuBar menuBar = new JMenuBar();
		
		// Assign2, Save and Cancel in menu
		JMenu file = new JMenu("File");
		JMenuItem f1 = new JMenuItem(MenuItems.items[4]);
		JMenuItem f2 = new JMenuItem(MenuItems.items[5]);
		f1.addActionListener(this);
		f2.addActionListener(this);
		file.add(f1);
		file.add(f2);
		menuBar.add(file);
		
		// Assign1, generate start menu 
		JMenu start = new JMenu("start");
		JMenuItem i1 = new JMenuItem(MenuItems.items[0]);
		JMenuItem i2 = new JMenuItem(MenuItems.items[1]);
		JMenuItem i3 = new JMenuItem(MenuItems.items[2]);
		JMenuItem i4 = new JMenuItem(MenuItems.items[3]);
		i1.addActionListener(this);
		i2.addActionListener(this);
		i3.addActionListener(this);
		i4.addActionListener(this);
		start.add(i1);
		start.add(i2);
		start.add(i3);
		start.add(i4);
		menuBar.add(start);
		
		// Assign3, generate AI menu
		JMenu AI = new JMenu("AI");
		JRadioButtonMenuItem playWithHuman = new JRadioButtonMenuItem(MenuItems.items[6]);
		playWithHuman.setSelected(true);
		JRadioButtonMenuItem playWithBlack = new JRadioButtonMenuItem(MenuItems.items[7]);
		JRadioButtonMenuItem playWithWhite = new JRadioButtonMenuItem(MenuItems.items[8]);
		ButtonGroup aiItems = new ButtonGroup();
		aiItems.add(playWithHuman);
		aiItems.add(playWithBlack);
		aiItems.add(playWithWhite);
		playWithHuman.addItemListener(this);
		playWithBlack.addItemListener(this);
		playWithWhite.addItemListener(this);
		AI.add(playWithHuman);
		AI.add(playWithBlack);
		AI.add(playWithWhite);
		menuBar.add(AI);
		
		this.setJMenuBar(menuBar);

		// Assign1, Initialize pieces collection
		initPieces();

		// Assign1, Initialize board
		initialize();

		this.setSize(515, 615);
		this.setDefaultCloseOperation(MainFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	/**
	 * Assign1, Initialize pieces collection
	 */
	public void initPieces() {
		for (int i = 0; i < 64; i++) {
			if (i < 12) {
				blackPieces[i] = new ChessPiece("black", "");
				blackPieces[i].setBackground(Color.BLACK);
				blackPieces[i].addMouseMotionListener(this);
				blackPieces[i].addMouseListener(this);
				blackPieces[i].setText(null);
				whitePieces[i] = new ChessPiece("white", "");
				whitePieces[i].setBackground(Color.WHITE);
				whitePieces[i].addMouseMotionListener(this);
				whitePieces[i].addMouseListener(this);
				whitePieces[i].setText(null);
			}
			spacePieces[i] = new ChessPiece("empty", "");
			spacePieces[i].setEnabled(false);
			spacePieces[i].setVisible(false);
			spacePieces[i].setText(null);
		}
	}

	/**
	 * Assign1, Initialize the chess board, all pieces are on initialized position
	 */
	public void initialize() {

		// Assign1, Add control button
		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		this.setLayout(fl);
		switcher = new JButton("Switch Player:black");
		switcher.setEnabled(false);
		switcher.addActionListener(this);
		confirm = new JButton("Confirm");
		confirm.setEnabled(false);
		confirm.addActionListener(this);
		this.add(switcher);
		this.add(confirm);
		switcher.setBounds(520, 200, 50, 20);
		confirm.setBounds(520, 400, 50, 20);

		// Assign1, Add message upper from the board
		t = new JLabel();
		t.setVerticalAlignment(JLabel.TOP);
		this.add(t);
		t.setVisible(true);

		// Assign1, Get layer object, which in order to enable layering of JFrame
		lp = this.getLayeredPane();

		// Define board by JPanel with a 8*8 GridLayout
		// Put red and white board
		boardBg = new JPanel();
		boardBg.setLayout(new GridLayout(8, 8));
		for (int i = 0; i < 8 * 8; i++) {
			// Assign1, Put red board
			if ((i % 2 == 0 && (i / 8) % 2 == 1)
					|| (i % 2 == 1 && (i / 8) % 2 == 0)) {
				ChessBoardBlock block = new ChessBoardBlock("red", order[i]);
				block.addActionListener(this);
				block.addMouseListener(this);
				board.put(order[i], block);
				block.setEnabled(false);
				block.setBorderPainted(false);
				boardBg.add(block);
			}
			// Assign1, Put white board
			else {
				ChessBoardBlock block = new ChessBoardBlock("white", order[i]);
				block.addActionListener(this);
				block.addMouseListener(this);
				board.put(order[i], block);
				block.setEnabled(false);
				block.setBorderPainted(false);
				boardBg.add(block);
			}
		}

		// Assign1, Put pieces on the board, on another same JPanel with 8*8 GridLayout
		body = new JPanel();
		body.setLayout(new GridLayout(8, 8));

		// Assign1, Put board panel and piece panel in different layer by the Integer parameter
		lp.add(boardBg, new Integer(1));
		lp.add(body, new Integer(2));
		boardBg.setBounds(0, 70, 500, 500);
		boardBg.setVisible(true);
		body.setBounds(0, 70, 500, 500);
		body.setOpaque(false);// Assign1, Make the upper layer panel transparent so that lower layer can be seen
		body.setVisible(true);
		
		status.setStatusInit();

	}

	/**
	 * Assign1, Remove all pieces from board and clear template data
	 */
	public void clearBoard() {
		body.removeAll();
		piece.clear();
		
		// Assign1, Remove 'K' label of all pieces
		for (int i=0; i<blackPieces.length; i++) {
			blackPieces[i].setText(null);
		}
		for (int i=0; i<whitePieces.length; i++) {
			whitePieces[i].setText(null);
		}
		body.repaint();
	}

	/**
	 * Assign1, Initialize all pieces to default position
	 */
	public void initChessBoardAutomaticly() {
		this.initPieces();
		this.clearBoard();
		for (int i=0; i<board.size(); i++) {
			ChessBoardBlock b = board.get(order[i]);
			b.setBorderPainted(false);
		}
		for (int i = 0, w = 0, b = 0, s = 0; i < 8 * 8; i++) {

			// Assign1, Disable board clickable
			ChessBoardBlock block = board.get(order[i]);
			block.setEnabled(false);

			// Assign1, Put black pieces and record the position
			if (i % 2 != 1 && i >= 8 && i < 16) {
				ChessPiece blackPiece = blackPieces[b];
				blackPiece.position = order[i];
				piece.put(order[i], blackPiece);
				body.add(blackPiece);
				b++;
			} else if (i % 2 != 0 && (i < 8 || (i > 16 && i < 24))) {
				ChessPiece blackPiece = blackPieces[b];
				blackPiece.position = order[i];

				piece.put(order[i], blackPiece);
				body.add(blackPiece);
				b++;
			}
			
			// Assign1, Put white pieces and record the position
			else if (i % 2 != 0 && i > 48 && i < 56) {
				ChessPiece whitePiece = whitePieces[w];
				whitePiece.position = order[i];
				piece.put(order[i], whitePiece);
				body.add(whitePiece);
				w++;
			} else if (i % 2 != 1
					&& ((i >= 40 && i < 48) || (i >= 56 && i < 64))) {
				ChessPiece whitePiece = whitePieces[w];
				whitePiece.position = order[i];
				
				piece.put(order[i], whitePiece);
				body.add(whitePiece);
				w++;
			}

			// Assign1, Put empty pieces on the board
			// Actually, empty pieces will not display on the board, they are
			// not existing
			// to chess players, just for calculation
			else {
				ChessPiece spacePiece = spacePieces[s];
				spacePiece.position = order[i];
				body.add(spacePiece);
				piece.put(order[i], spacePiece);
				spacePiece.setVisible(false);
				s++;
			}
		}
		t.setText("Chess Board has been initialized automatically");
		this.startGame();
	}
	
	/**
	 * Assign1, Set the board clickable or not
	 * @param flag
	 */
	public void setBoardEnabled(boolean flag) {
		for (int i = 0; i < order.length; i++) {
			ChessBoardBlock block = board.get(order[i]);
			block.setEnabled(flag);
//			block.setBorderPainted(flag);
		}
	}

	/**
	 * Assign1, Remove all pieces from board and user able to set pieces position
	 * manually
	 */
	public void initChessBoardManually() {
		status.setStatusEdit();
		clearBoard();
		this.setBoardEnabled(true);
		for (int i=0; i<8*8; i++) {
			ChessBoardBlock block = board.get(order[i]);
			block.setBorderPainted(false);
		}
		switcher.setEnabled(true);
		confirm.setEnabled(true);
		t.setText("<html>Please choose position to put the pieces.<br>Right click to set/cancel King flag</html>");
	}

	/**
	 * Assign1, Invoked when user put one piece on the board when manually set the board
	 * 
	 * @param b
	 */
	public void putPieceOnBoard(ChessBoardBlock b, String player) {
		int blackCount = getBlackCountInVector();
		int whiteCount = getWhiteCountInVector();
		if (blackCount == 12 && whiteCount == 12) {
			t.setText("<html>black:" + getBlackCountInVector() + "  white:" + getWhiteCountInVector() + "<br>Cannot put any more pieces on</html>");
			return;
		}
		if (blackCount == 12 && player.equals("black")) {
			t.setText("<html>black:" + getBlackCountInVector() + "  white:" + getWhiteCountInVector() + "<br>Black pieces are maximum</html>");
			return;
		}
		if (whiteCount == 12 && player.equals("white")) {
			t.setText("<html>black:" + getBlackCountInVector() + "  white:" + getWhiteCountInVector() + "<br>White pieces are maximum</html>");
			return;
		}
		String position = b.position;
		ChessPiece tmpPiece;
		if (player.equals("black")) {
			tmpPiece = blackPieces[blackCount];
			System.out.println(player);

			// Set 'K' label
			if (position.equals("A1")||position.equals("B1")||position.equals("C1")||position.equals("D1")||
				position.equals("E1")||position.equals("F1")||position.equals("G1")||position.equals("H1")) {
				tmpPiece.setText("<html><font color=\"white\">K</font></html>");
			}
		} else {
			tmpPiece = whitePieces[whiteCount];

			// Set 'K' label
			if (position.equals("A8")||position.equals("B8")||position.equals("C8")||position.equals("D8")||
				position.equals("E8")||position.equals("F8")||position.equals("G8")||position.equals("H8")) {
				tmpPiece.setText("<html><font color=\"black\">K</font></html>");
			}
		}
		tmpPiece.position = position;
		piece.put(position, tmpPiece);
		body.add(tmpPiece);
		reFormatPieceLayer();

		t.setText("<html>black:" + getBlackCountInVector() + "  white:" + getWhiteCountInVector() + "</html>");
	}
	
	/**
	 * Assign1, Refresh the pieces according to this.piece record
	 */
	public void reFormatPieceLayer() {
		body.removeAll();
		for (int i = 0; i < 8 * 8; i++) {
			ChessPiece tmpPiece = piece.get(order[i]);
			if (tmpPiece == null) {
				spacePieces[i].position = order[i];
				piece.put(order[i], spacePieces[i]);
				body.add(spacePieces[i]);
			} else {
				piece.put(order[i], tmpPiece);
				body.add(tmpPiece);
			}
		}
		body.repaint();
	}
	
	/**
	 * Assign1, Get the black piece on the board
	 * @return
	 */
	public int getBlackCountInVector() {
		int count = 0;
		Object tmp[] = piece.values().toArray();
		for (int i = 0; i < tmp.length; i++) {
			ChessPiece p = (ChessPiece)tmp[i];
			if (p.chessPlayer.equals("black")) {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * Assign1, Get the white piece on the board
	 * @return
	 */
	public int getWhiteCountInVector() {
		int count = 0;
		Object tmp[] = piece.values().toArray();
		for (int i = 0; i < tmp.length; i++) {
			ChessPiece p = (ChessPiece)tmp[i];
			if (p.chessPlayer.equals("white")) {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * Assign1, Print piece for testing
	 */
	public void print() {
		Object tmp[] = piece.values().toArray();
		for (int i = 0; i < piece.size(); i++) {
			ChessPiece p = (ChessPiece)tmp[i];
			System.out.println(i + " " + p.chessPlayer + ":" + p.position + " " + p.isEnabled());
		}
		System.out.println();
		System.out.println(body.countComponents());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Initial ChessBoard automatically")) {
			
			this.initPieces();
			this.initChessBoardAutomaticly();

		} else if (e.getActionCommand().equals("Initial ChessBoard manually")) {

			this.initPieces();
			this.initChessBoardManually();

		} else if (e.getActionCommand().equals("Print out ChessBoard")) {

			this.print();

		} else if (e.getActionCommand().equals("Exit")) {

			System.exit(0);

		} 
		
		// Assign1, invoke when manually put piece on board
		else if (e.getSource() instanceof ChessBoardBlock) {

			ChessBoardBlock b = (ChessBoardBlock) e.getSource();
			if (!b.bgColor.equals("white")) {
				String flag = switcher.getText();
				this.putPieceOnBoard(b, (flag.equals("Switch Player:black")) ? "black":"white");
			}

		} else if (e.getActionCommand().equals("Switch Player:black")) {

			switcher.setText("Switch Player:white");

		} else if (e.getActionCommand().equals("Switch Player:white")) {

			switcher.setText("Switch Player:black");

		} 
		
		// Assign2, start game from manually putting pieces
		else if (e.getActionCommand().equals("Confirm")) {
			
			this.setBoardEnabled(false);
			switcher.setEnabled(false);
			confirm.setEnabled(false);
			
			String winner = this.win();
			if (winner != null && status.getStatus().equals(GameStatus.GAME_STATUS_EXEC)) {
				t.setText("Winner is:" + winner);
				return;
			}
			
			this.startGame();

		} 
		
		// Assign2, Generate game information and write to "save.txt" file
		else if (e.getActionCommand().equals("Save")) {
			FileOutputStream out = null;
			String buffer = t.getText().replaceAll("null", "") + "\r\n";
			try {
				File save = new File("save.txt");
				if (!save.exists()) {
					save.createNewFile();
				}
				out = new FileOutputStream(save);
				
				for (int i = 0; i < piece.size(); i++) {
					ChessPiece p = (ChessPiece)piece.get(order[i]);
					buffer += p.chessPlayer + ":" + order[i] + ":" + p.getText() + "\r\n";
				}
				buffer += status.getStatus() + "\r\n";
				buffer += this.activePlayer;
				out.write(buffer.getBytes());
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				try {
					out.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		} 
		
		// Assign2, Load "save.txt" file, and rebuild the game by information in the file
		else if (e.getActionCommand().equals("Load")) {
			File f = new File("save.txt");
			BufferedReader input = null;
			try {
				input = new BufferedReader(new FileReader(f));
			    String s = "";
			    String s1 = "";
			    
			    // Read from file
			    while((s = input.readLine())!=null){
			    	s1 += s+"\r\n";
			    }
			    
			    // Generate objects structure for the game
			    int blackCount = 0;
			    int whiteCount = 0;
			    int spaceCount = 0;
			    String[] ps = s1.split("\r\n");
			    t.setText(ps[0]);
			    for (int i=1; i<ps.length-2; i++) {
			    	System.out.println(ps[i]);
				    String[] node = ps[i].split(":");
				    ChessPiece p;
				    if (node[0].equals("black")) {
				    	p = blackPieces[blackCount];
				    	p.setText(node[2].equals("null")?null:"<html><font color=\"white\">K</font></html>");
				    	blackCount++;
				    } else if (node[0].equals("white")) {
				    	p = whitePieces[whiteCount];
				    	p.setText(node[2].equals("null")?null:"<html><font color=\"black\">K</font></html>");
				    	whiteCount++;
				    } else {
				    	p = spacePieces[spaceCount];
				    	spaceCount++;
				    }
			    	p.position = node[1];
				    piece.put(node[1], p);
			    }
			    
			    // Re-put the pieces and reset game status
			    this.reFormatPieceLayer();
			    String st = ps[ps.length-2];
			    if (st.equals(GameStatus.GAME_STATUS_EXEC)) {
			    	status.setStatusExec();
			    	switcher.setEnabled(false);
			    	confirm.setEnabled(false);
			    	if (ps[0].startsWith("white")) {
			    		this.disablePlayer("black");
			    	} else {
			    		this.disablePlayer("white");
			    	}
			    } else if (st.equals(GameStatus.GAME_STATUS_EDIT)) {
			    	status.setStatusEdit();
			    	switcher.setEnabled(true);
			    	confirm.setEnabled(true);
			    }
			    activePlayer = ps[ps.length-1];
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				try {
					input.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} else {
			System.out.println("else:");
		}
	}
	
	// Assign1, Return the blocks that can be put on while manually putting pieces
	public Vector<ChessBoardBlock> getReputablePosition() {
		Vector<ChessBoardBlock> tmp = new Vector<ChessBoardBlock>();
		
		for (int i=0; i<8*8; i++) {
			if (piece.get(order[i]).chessPlayer.equals("empty") && board.get(order[i]).bgColor.equals("red")) {
				board.get(order[i]).setBorder(null);
				tmp.add(board.get(order[i]));
			} else {
				board.get(order[i]).setEnabled(false);
			}
		}
		
		return tmp;
	}
	
	// Assign1, Return the blocks that a piece is able to move while playing game
	public Vector<ChessBoardBlock> getMovablePosition(ChessPiece p) {
		String player = p.chessPlayer;
		String position = p.position;
		char h = position.charAt(0);
		int v = Integer.parseInt(position.substring(1));
		Vector<ChessBoardBlock> tmp = new Vector<ChessBoardBlock>();
		tmp.add(board.get(position));
		
		// Condition for white piece
		if (player.equals("white")) {
			
			// Condition for not King piece
			if (p.getText() == null) {
				ChessBoardBlock br1 = board.get(Character.toString((char)(h+1)) + (v+1));
				ChessBoardBlock br2 = board.get(Character.toString((char)(h+2)) + (v+2));
				// Condition for upper right to the piece
				if (br1 != null && piece.get(br1.position).chessPlayer.equals("empty")) {
					tmp.add(board.get(br1.position));
				} else if (br2 != null && piece.get(br1.position).chessPlayer.equals("black") && piece.get(br2.position).chessPlayer.equals("empty")) {
					tmp.add(board.get(br2.position));
				}
				
				ChessBoardBlock bl1 = board.get(Character.toString((char)(h-1)) + (v+1));
				ChessBoardBlock bl2 = board.get(Character.toString((char)(h-2)) + (v+2));
				// Condition for upper left to the piece
				if (bl1 != null && piece.get(bl1.position).chessPlayer.equals("empty")) {
					tmp.add(board.get(bl1.position));
				} else if (bl2 != null && piece.get(bl1.position).chessPlayer.equals("black") && piece.get(bl2.position).chessPlayer.equals("empty")) {
					tmp.add(board.get(bl2.position));
				}
				
			} 

			// Condition for King piece
			else {
				ChessBoardBlock bur1 = board.get(Character.toString((char)(h+1)) + (v+1));
				ChessBoardBlock bur2 = board.get(Character.toString((char)(h+2)) + (v+2));
				// Condition for upper right to the piece
				if (bur1 != null && piece.get(bur1.position).chessPlayer.equals("empty")) {
					tmp.add(board.get(bur1.position));
				} else if (bur2 != null && piece.get(bur1.position).chessPlayer.equals("black") && piece.get(bur2.position).chessPlayer.equals("empty")) {
					tmp.add(board.get(bur2.position));
				}
				
				ChessBoardBlock bul1 = board.get(Character.toString((char)(h-1)) + (v+1));
				ChessBoardBlock bul2 = board.get(Character.toString((char)(h-2)) + (v+2));
				// Condition for upper left to the piece
				if (bul1 != null && piece.get(bul1.position).chessPlayer.equals("empty")) {
					tmp.add(board.get(bul1.position));
				} else if (bul2 != null && piece.get(bul1.position).chessPlayer.equals("black") && piece.get(bul2.position).chessPlayer.equals("empty")) {
					tmp.add(board.get(bul2.position));
				}
				
				ChessBoardBlock bdr1 = board.get(Character.toString((char)(h+1)) + (v-1));
				ChessBoardBlock bdr2 = board.get(Character.toString((char)(h+2)) + (v-2));
				// Condition for lower right to the piece
				if (bdr1 != null && piece.get(bdr1.position).chessPlayer.equals("empty")) {
					tmp.add(board.get(bdr1.position));
				} else if (bdr2 != null && piece.get(bdr1.position).chessPlayer.equals("black") && piece.get(bdr2.position).chessPlayer.equals("empty")) {
					tmp.add(board.get(bdr2.position));
				}
				
				ChessBoardBlock bdl1 = board.get(Character.toString((char)(h-1)) + (v-1));
				ChessBoardBlock bdl2 = board.get(Character.toString((char)(h-2)) + (v-2));
				// Condition for lower left to the piece
				if (bdl1 != null && piece.get(bdl1.position).chessPlayer.equals("empty")) {
					tmp.add(board.get(bdl1.position));
				} else if (bdl2 != null && piece.get(bdl1.position).chessPlayer.equals("black") && piece.get(bdl2.position).chessPlayer.equals("empty")) {
					tmp.add(board.get(bdl2.position));
				}
				
			}
		}
		
		// Condition for black piece
		else {
			
			// Condition for not King piece
			if (p.getText() == null) {
				ChessBoardBlock br1 = board.get(Character.toString((char)(h+1)) + (v-1));
				ChessBoardBlock br2 = board.get(Character.toString((char)(h+2)) + (v-2));
				// Condition for upper right to the piece
				if (br1 != null && piece.get(br1.position).chessPlayer.equals("empty")) {
					tmp.add(board.get(br1.position));
				} else if (br2 != null && piece.get(br1.position).chessPlayer.equals("white") && piece.get(br2.position).chessPlayer.equals("empty")) {
					tmp.add(board.get(br2.position));
				}
				
				ChessBoardBlock bl1 = board.get(Character.toString((char)(h-1)) + (v-1));
				ChessBoardBlock bl2 = board.get(Character.toString((char)(h-2)) + (v-2));
				// Condition for upper left to the piece
				if (bl1 != null && piece.get(bl1.position).chessPlayer.equals("empty")) {
					tmp.add(board.get(bl1.position));
				} else if (bl2 != null && piece.get(bl1.position).chessPlayer.equals("white") && piece.get(bl2.position).chessPlayer.equals("empty")) {
					tmp.add(board.get(bl2.position));
				}
			} else {
				ChessBoardBlock bur1 = board.get(Character.toString((char)(h+1)) + (v+1));
				ChessBoardBlock bur2 = board.get(Character.toString((char)(h+2)) + (v+2));
				// Condition for upper right to the piece
				if (bur1 != null && piece.get(bur1.position).chessPlayer.equals("empty")) {
					tmp.add(board.get(bur1.position));
				} else if (bur2 != null && piece.get(bur1.position).chessPlayer.equals("white") && piece.get(bur2.position).chessPlayer.equals("empty")) {
					tmp.add(board.get(bur2.position));
				}
				
				ChessBoardBlock bul1 = board.get(Character.toString((char)(h-1)) + (v+1));
				ChessBoardBlock bul2 = board.get(Character.toString((char)(h-2)) + (v+2));
				// Condition for upper left to the piece
				if (bul1 != null && piece.get(bul1.position).chessPlayer.equals("empty")) {
					tmp.add(board.get(bul1.position));
				} else if (bul2 != null && piece.get(bul1.position).chessPlayer.equals("white") && piece.get(bul2.position).chessPlayer.equals("empty")) {
					tmp.add(board.get(bul2.position));
				}
				
				ChessBoardBlock bdr1 = board.get(Character.toString((char)(h+1)) + (v-1));
				ChessBoardBlock bdr2 = board.get(Character.toString((char)(h+2)) + (v-2));
				// Condition for lower right to the piece
				if (bdr1 != null && piece.get(bdr1.position).chessPlayer.equals("empty")) {
					tmp.add(board.get(bdr1.position));
				} else if (bdr2 != null && piece.get(bdr1.position).chessPlayer.equals("white") && piece.get(bdr2.position).chessPlayer.equals("empty")) {
					tmp.add(board.get(bdr2.position));
				}
				
				ChessBoardBlock bdl1 = board.get(Character.toString((char)(h-1)) + (v-1));
				ChessBoardBlock bdl2 = board.get(Character.toString((char)(h-2)) + (v-2));
				// Condition for lower left to the piece
				if (bdl1 != null && piece.get(bdl1.position).chessPlayer.equals("empty")) {
					tmp.add(board.get(bdl1.position));
				} else if (bdl2 != null && piece.get(bdl1.position).chessPlayer.equals("white") && piece.get(bdl2.position).chessPlayer.equals("empty")) {
					tmp.add(board.get(bdl2.position));
				}
			}
		}
		return tmp;
	}

	// Test program
	public static void main(String[] args) {
		new MainFrame("Chess");
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// Assign1, Reset location of piece while dragged the piece moving
		ChessPiece p = (ChessPiece)e.getSource();
		int newX = e.getX() - p.draggedAtX + p.getLocation().x;
		int newY = e.getY() - p.draggedAtY + p.getLocation().y;
		p.setLocation(newX, newY);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() instanceof ChessBoardBlock) {
//			System.out.println("mouseMoved");
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// Assign1, Set / Cancel King flag when right click
		if (arg0.getSource() instanceof ChessPiece && status.getStatus().equals(GameStatus.GAME_STATUS_EDIT) && arg0.getButton() == MouseEvent.BUTTON3) {
			ChessPiece p = (ChessPiece)arg0.getSource();
			if (p.chessPlayer.equals("black") && p.position.charAt(1) != '1') {
				if (p.getText() == null) {
					p.setText("<html><font color=\"white\">K</font></html>");
				} else {
					p.setText(null);
				}
			} else if (p.chessPlayer.equals("white") && p.position.charAt(1) != '8'){
				if (p.getText() == null) {
					p.setText("<html><font color=\"black\">K</font></html>");
				} else {
					p.setText(null);
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getSource() instanceof ChessBoardBlock) {
//			System.out.println("mouseEntered");
		}
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getSource() instanceof ChessBoardBlock) {
//			System.out.println("mouseExited");
		}
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// Invoke when mouse pressed
		
		// Assign2, when press on a piece while gaming
		if (status.getStatus().equals(GameStatus.GAME_STATUS_EXEC) && arg0.getSource() instanceof ChessPiece) {
			ChessPiece p = (ChessPiece)arg0.getSource();
			Vector<ChessBoardBlock> blocks = this.getMovablePosition(p);
			for (int i=0; i<blocks.size(); i++) {
				ChessBoardBlock b = blocks.elementAt(i);
				b.setEnabled(true);
				b.setBorderPainted(true);
				if (p.position.equals(b.position)) {
					b.setBorder(new LineBorder(Color.BLUE, 4));
				} else {
					b.setBorder(new LineBorder(Color.GREEN, 4));
				}
			}
			p.draggedAtX = arg0.getX();
			p.draggedAtY = arg0.getY();
			body.repaint();
		}

		// Assign1, when press on a piece while gaming
		if (status.getStatus().equals(GameStatus.GAME_STATUS_EDIT) && arg0.getSource() instanceof ChessPiece) {
			ChessPiece p = (ChessPiece)arg0.getSource();
			Vector<ChessBoardBlock> blocks = this.getReputablePosition();
			for (int i=0; i<blocks.size(); i++) {
				ChessBoardBlock b = blocks.elementAt(i);
				b.setEnabled(true);
				b.setBorderPainted(true);
				b.setBorder(new LineBorder(Color.GREEN, 0));
			}
			p.draggedAtX = arg0.getX();
			p.draggedAtY = arg0.getY();
			body.repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// Assign1&2, Invoke when mouse released
		if (arg0.getSource() instanceof ChessPiece) {
			ChessPiece p = (ChessPiece)arg0.getSource();
			ChessBoardBlock block = findMovedBlock(p.getLocation().x + arg0.getX(), p.getLocation().y + arg0.getY());
			if (block == null) {
				changePosition(p, board.get(p.position).position);
			} else
			if (block.isBorderPainted() && ((LineBorder)block.getBorder()).getLineColor().equals(Color.GREEN)) {
				changePosition(p, block.position);
			} else {
				changePosition(p, board.get(p.position).position);
			}
			for (int i=0; i<board.size(); i++) {
				ChessBoardBlock b = board.get(order[i]);
				b.setBorder(null);
				b.setBorderPainted(false);
			}
			AIRobot.robotMove(this);
			
			if (this.isPeace()) {
				t.setText("This game is peace, restart a new game");
			}
			
			String winner = this.win();
			if (winner != null) {
				t.setText("Winner is:" + winner);
			}
		}
	}
	
	/**
	 * Assign2, Get the block after a piece move
	 * @param x
	 * @param y
	 * @return
	 */
	public ChessBoardBlock findMovedBlock(int x, int y) {
		for (int i=0; i<board.size(); i++) {
			ChessBoardBlock b = board.get(order[i]);
			if ((x>=b.getX() && x<b.getX()+b.getWidth()) && (y>=b.getY() && y<b.getY()+b.getHeight())) {
				return b;
			}
		}
		return null;
	}
	
	/**
	 * Assign2, Move the piece(p1) to new position(newP)
	 * @param p1
	 * @param newP
	 */
	public void changePosition(ChessPiece p1, String newP) {
		if (!p1.position.equals(newP)) {
			if (status.getStatus().equals(GameStatus.GAME_STATUS_EXEC)) {
				this.clearCapturedPiece(p1, newP);
				this.exchangeActivePlayer();
			}
			ChessPiece p2 = piece.get(newP);
			p2.position = p1.position;
			piece.put(p1.position, p2);
			p1.position = newP;
			piece.put(newP, p1);
			if (newP.charAt(1) == '8' && p1.chessPlayer.equals("white")) {
				piece.get(newP).setText("<html><font color=\"black\">K</font></html>");
			}
			if (newP.charAt(1) == '1' && p1.chessPlayer.equals("black")) {
				piece.get(newP).setText("<html><font color=\"white\">K</font></html>");
			}
		}
		if (status.getStatus().equals(GameStatus.GAME_STATUS_EXEC)) {
			this.setBoardEnabled(false);
		}
		this.reFormatPieceLayer();
	}
	
	/**
	 * Assign2, Clear the piece if it was captured
	 * @param p1
	 * @param newP
	 */
	public void clearCapturedPiece(ChessPiece p1, String newP) {
		
		char lh = p1.position.charAt(0);
		int lv = Integer.parseInt(p1.position.substring(1));
		char rh = newP.charAt(0);
		int rv = Integer.parseInt(newP.substring(1));
		
		String midPosition;
		
		if (Math.abs(lv-rv) == 1) {
			midPosition = "no";
		} else {
			midPosition = Character.toString((char)((lh+rh)/2)) + (lv+rv)/2;
			piece.get(midPosition).chessPlayer = "empty";
			piece.get(midPosition).setText(null);;
			piece.get(midPosition).setVisible(false);
			this.reFormatPieceLayer();
		}
		
		System.out.println(p1.position+" "+newP+" insert:"+midPosition);
		
	}
	
	/**
	 * Assign2, Set parameter player cannot move
	 * @param player
	 */
	public void disablePlayer(String player) {
		Object[] o = piece.values().toArray();
		for (int i=0; i<o.length; i++) {
			ChessPiece p = (ChessPiece)o[i];
			if (p.chessPlayer.equals(player)) {
				p.removeMouseListener(this);
				p.removeMouseMotionListener(this);
			}
		}
	}
	
	/**
	 * Assign2, Set parameter player able to move
	 * @param player
	 */
	public void enablePlayer(String player) {
		Object[] o = piece.values().toArray();
		for (int i=0; i<o.length; i++) {
			ChessPiece p = (ChessPiece)o[i];
			if (p.chessPlayer.equals(player)) {
				p.addMouseListener(this);
				p.addMouseMotionListener(this);
			}
		}
		activePlayer = player;
	}
	
	/**
	 * Assign2, Change another player to go
	 */
	public void exchangeActivePlayer() {
		if (activePlayer.equals("black")) {
			disablePlayer("black");
			enablePlayer("white");
			t.setText("white turn");
		} else {
			disablePlayer("white");
			enablePlayer("black");
			t.setText("black turn");
		}
	}
	
	/**
	 * Assign2, Check if the game is peace or not
	 * @return
	 */
	public boolean isPeace() {
		for (int i=0; i<piece.size(); i++) {
			ChessPiece p = piece.get(order[i]);
			if (p.chessPlayer.equals("black") || p.chessPlayer.equals("white")) {
				Vector<ChessBoardBlock> blocks = this.getMovablePosition(p);
				if (blocks.size()>1) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Assign2, Check if the game is over and return who is the winner
	 * @return
	 */
	public String win() {
		int blackCount = 0;
		int whiteCount = 0;
		for (int i=0; i<piece.size(); i++) {
			if (piece.get(order[i]).chessPlayer.equals("black")) {
				blackCount++;
			} else if (piece.get(order[i]).chessPlayer.equals("white")) {
				whiteCount++;
			} else continue;
		}
		if (blackCount == 0) {
			return "white";
		} else if (whiteCount == 0) {
			return "black";
		} else return null;
	}
	
	/**
	 * Assign2, Start Game
	 */
	public void startGame() {
		status.setStatusExec();
		if (t.getText().startsWith("Winner")) {
			disablePlayer("black");
			disablePlayer("white");
			return;
		}
		disablePlayer("black");
		activePlayer = "white";
		if (this.activePlayer.equals("white") && this.aiFlag.equals("white")) {
			AIRobot.robotMove(this);
		}
		t.setText("white turn");
		this.setBoardEnabled(false);
	}

	@Override
	/**
	 * Assign3, invoke if AI items were selected on menu
	 */
	public void itemStateChanged(ItemEvent ie) {
		JRadioButtonMenuItem item = (JRadioButtonMenuItem)ie.getSource();
		if (item.getText().equals(MenuItems.items[6]) && item.isSelected()) {
			for (int i=0; i<board.size(); i++) {
				ChessBoardBlock b = board.get(order[i]);
				b.setBorder(null);
			}
			aiFlag = AIRobot.AI_STATUS_HUMAN;
			AIRobot.robotMove(this);
		} else if (item.getText().equals(MenuItems.items[7]) && item.isSelected()) {
			for (int i=0; i<board.size(); i++) {
				ChessBoardBlock b = board.get(order[i]);
				b.setBorder(null);
			}
			aiFlag = AIRobot.AI_STATUS_BLACK;
			AIRobot.robotMove(this);
		} else if (item.getText().equals(MenuItems.items[8]) && item.isSelected()) {
			for (int i=0; i<board.size(); i++) {
				ChessBoardBlock b = board.get(order[i]);
				b.setBorder(null);
			}
			aiFlag = AIRobot.AI_STATUS_WHITE;
			AIRobot.robotMove(this);
		} else {
			
		}
	}

}
