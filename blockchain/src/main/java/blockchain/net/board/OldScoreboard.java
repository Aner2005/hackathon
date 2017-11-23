package blockchain.net.board;


import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ListIterator;
import blockchain.net.impl.blockchain.*;

public class OldScoreboard extends JFrame {

	private MyPanel myPanel;


	/**
	 * Create the frame.
	 */
	public OldScoreboard(GUI gui){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		myPanel= new MyPanel(gui);
		add(myPanel);
	}

	public synchronized void update(GUI givenGui)
	{
		myPanel.gui = givenGui;
		myPanel.repaint();
	}


}
/*
class MyPanel extends JPanel implements MouseListener {
	int x,y;
	GUI gui;	
	public MyPanel(GUI gui){
		this.gui = gui;
		addMouseListener(this);
        
	}

	@Override
	protected void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		//Graphics2D g2d = (Graphics2D) g;
		g.setColor(Color.BLACK);
		g.setFont(new Font("TimesRoman", Font.BOLD, 30));
		try {
		Image img = ImageIO.read(new File("background.jpg"));
		g.drawImage(img,0, 0, getWidth(), getHeight(), this);
		}
		catch (IOException e){
			e.printStackTrace();
		}
		g.drawString(""+gui.difficulty,326,297);
		g.drawString(gui.challenge,311,124);

		String[][] scores = gui.score;
		int counter = 0;
		int xPlace = 914;
		int yPlace = 167;
		while (counter<scores.length&&counter<7&&!(scores[counter][1].equals("0"))) {
			g.drawString(scores[counter][0], xPlace, yPlace);
			g.drawString(scores[counter][1], xPlace+378, yPlace);
			yPlace = yPlace+50;
			counter++;
		}
		int start = (gui.blocks.size()-5<0) ? 0 : gui.blocks.size()-5;
		ListIterator li = gui.blocks.listIterator(start);
		xPlace = 367;
		yPlace = 570;
		g.setFont(new Font("TimesRoman", Font.BOLD, 16));
		while (li.hasNext()) {
			Block b = (Block)li.next();
			g.drawString(""+b.difficulty, xPlace, yPlace);
			g.drawString(b.solvingGroupName, xPlace, yPlace+50);
			String string1 = (b.string1.length()>21) ? b.string1.substring(0,22)+"...":b.string1;
			String string2 = (b.string2.length()>21) ? b.string2.substring(0,22)+"...":b.string2;
			g.drawString(string1, xPlace, yPlace+100);
			g.drawString(string2, xPlace, yPlace+150);
			xPlace = xPlace+233;	
		}
		//g.drawString("width " +getWidth() +"height "+getHeight(), 800, 800);	
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
		// TODO Auto-generated method stub
		//System.out.println(arg0.getX());
		x=arg0.getX();y=arg0.getY();
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}*/
