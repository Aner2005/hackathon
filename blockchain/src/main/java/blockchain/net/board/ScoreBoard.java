package blockchain.net.board;


import blockchain.net.impl.blockchain.*;
import java.awt.*;

import javax.swing.*;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;
import java.util.ListIterator;
import java.util.Map;

import java.util.Arrays;
import java.util.Random;

import java.io.FileWriter;



public class ScoreBoard extends JFrame {
	int counter;
	int numOfBlocks;
	//SecretHash sh;
	public MyPanel myPanel;
	//AllGroupData data;
	//BlockChainGUI blockChain;
	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ScoreBoard frame = new ScoreBoard();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the frame.
	 */
	public ScoreBoard(GUI gui){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH); 
		//setUndecorated(true);

		/*
		int numOfGroups=6;
		data = new AllGroupData(numOfGroups);
		Image logoImage=null;
		for (int i=0;i<numOfGroups;i++)
		{
		  try {
		  logoImage = ImageIO.read(new File("pics/logo"+(i+1)+".jpg"));
		  }
		  catch (IOException e){
			e.printStackTrace();
		  }
		  int r=0;
		  Boolean unique=false;
		  while(!unique)
		  {
		    r=(int)(Math.random()*1000);
		    unique=true;
		    for (int j=0;j<i;j++)
		    if (data.groupsData[j].groupID==r) unique=false;
		  }
		  data.groupsData[i] = new GroupData(r,"Team"+r,logoImage);
		
		}
		
		//setBounds(100, 100, 1000, 1000);
		blockChain = new BlockChainGUI();*/
		myPanel= new MyPanel(gui);
		add(myPanel);
		setVisible(true);
		
		counter=0;
		
		
	}
/*
	public void update(String s1, String s2)
	{
		  myPanel.test=0;
		  toFront();
		  setVisible(true);
		  toFront();
		  
		  
		  int r=(int)(Math.random()*data.groupsData.length);
		  data.groupsData[r].addScore(100);
		  //update blockChain
		  BlockGUI block = new BlockGUI(myPanel.challenge,data.groupsData[r].groupIDString,data.groupsData[r].groupName,s1,s2,data.groupsData[r].groupLogo);
		  block.next=blockChain.head;
		  blockChain.head=block;
		  
		  setChallenge(sh.secretHash(""+counter));//Integer.toHexString((int)(Math.random()*100000000)).toUpperCase()));
		  counter++;
		  if (counter%10==0) setDifficulty(myPanel.difficulty+1);
		  //setAlwaysOnTop(true);
		  myPanel.animation(r);
		  //setAlwaysOnTop(false);
		 
		
	}*/
	
	public synchronized void update(GUI givenGui)
	{
		int GuiNumOfBlocks= givenGui.blocks.size();
		if (GuiNumOfBlocks>=numOfBlocks)
		{
		myPanel.gui = givenGui;
		
		if (GuiNumOfBlocks == numOfBlocks+1)
		  myPanel.animation();//Add one block by animation
		else
		  myPanel.repaint();//Add multiple blocks or no blocks - no animation
		
		numOfBlocks=GuiNumOfBlocks;
		
		writeScoreToFile(givenGui.score);
		}
		
	}
	
	public void writeScoreToFile(String[][] array)
	{
	  try{
	  FileWriter br = new FileWriter("myfile.csv");
	  StringBuilder sb = new StringBuilder();
	  for (String[] row : array) 
	  {
	    for (String element : row) 
	    {
	      sb.append(element);
	      sb.append(",");
	    }
	    sb.append("\n");
	  }

	  br.write(sb.toString());
	  br.close();
	  } catch (IOException e)
	  {
	    System.out.println("Failed to write to file!");
	  }
	}
	
}



class MyPanel extends JPanel {
	final Boolean orange=true;
	final String IP="132.72.47.241";
	int test,test2;	
	Image scoreBoardImage;
	BufferedImage blockImage,blockChainImage;
	Image blockDescriptionImage;
	GUI gui;
	
	public MyPanel(GUI gui)
	{
		this.gui=gui;
		try {
		if (orange)
		{
		scoreBoardImage = ImageIO.read(new File("pics/ScoreBoardOrange.jpg"));
		blockChainImage = ImageIO.read(new File("pics/BlockChainOrange.png"));
		}
		else{
		scoreBoardImage = ImageIO.read(new File("pics/ScoreBoardBlue.jpg"));
		blockChainImage = ImageIO.read(new File("pics/BlockChainBlue.png"));
		}
		blockImage = blockChainImage.getSubimage(0, 0, 470, blockChainImage.getHeight());//ImageIO.read(new File("pics/Block2.png"));
		//blockDescriptionImage = ImageIO.read(new File("pics/Picture1.png"));
		}
		catch (IOException e){
			e.printStackTrace();
		}
		
		test=267;
		test2=99;
	}
	
	public void animation()
	{
		//repaint();
	
		
		
	  test=0;
	  test2=0;
	  for (int i=0;i<89;i++)
		  {
		  test+=3;
		  try{
		  Thread.sleep(10);}catch(Exception e){e.printStackTrace();}
		  repaint();//getWidth()/10,getHeight()/2,getWidth(),getHeight());
	    }
	   //test2=0;
	   for (int i=0;i<33;i++)
	   {
		test2+=3;
		try{
		Thread.sleep(10);}catch(Exception e){e.printStackTrace();}
		repaint();//getWidth()/10,getHeight()/2,getWidth(),getHeight());
	   }
	}
	

	@Override
	protected void paintComponent(Graphics g) 
	{
		try {
		if (orange)
		scoreBoardImage = ImageIO.read(new File("pics/ScoreBoardOrange.jpg"));
		else
		scoreBoardImage = ImageIO.read(new File("pics/ScoreBoardBlue.jpg"));
		}
		catch (IOException e){
			e.printStackTrace();
		}
		//if (test%10==0)
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.scale(getWidth()/1920.0, getHeight()/1045.0);
		
		g2d.drawImage(scoreBoardImage,0, 0, 1920, 1045, this);
		
		
		g2d.setColor(Color.BLACK);
		g2d.setFont(new Font("SansSerif.bold", Font.PLAIN, 56));
		g2d.drawString(IP,80,130);//IP
		g2d.drawString(gui.challenge,80,300);//challenge
		g2d.setFont(new Font("SansSerif.bold", Font.PLAIN, 80));
		g2d.drawString(""+gui.difficulty,290,490);
		
		///

		
		String[][] scores = gui.score;
		int i=0;
		while (i<scores.length&&i<5&&!(scores[i][2].equals("0"))) {
		
		    g2d.setColor(Color.BLACK);
		    g2d.setFont(new Font("SansSerif.bold", Font.PLAIN, 50));
		    //g2d.drawImage(data.groupsData[i].groupLogo,925,110+i*90, 280, 70, this);
		    g2d.drawString(limit(scores[i][0]),890,162+i*90);//name
		    g2d.drawString(scores[i][1],1365,162+i*90);//ID
		    g2d.drawString(""+scores[i][2],1650,162+i*90);//score
		    i++;
		}
		
		///
		
		//Draw new block
		float opacity = (float)test2/100;
		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font("SansSerif.italic", Font.PLAIN, 25));
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
		g2d.drawImage(blockImage,325, 700, 350, 350, this);
		g2d.drawString(gui.challenge,405, 800);	
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		//Draw the blockchain
		
		//g2d.drawImage(blockDescriptionImage,0,765,265,265,this);
		g2d.drawImage(blockChainImage,340+test,700, 1600, 350, this);
		
		
		g2d.setFont(new Font("SansSerif.bold", Font.BOLD, 30));
		g2d.setColor(Color.BLACK);
		g2d.drawString("Solving Team",5, 740);
		g2d.drawString("Challenge",5, 800);
		g2d.drawString("Solving Team ID",5, 860);
		g2d.drawString("String 1",5, 920);
		g2d.drawString("String 2",5, 980);
		g2d.setColor(Color.WHITE);		
		int t0=305,t1=307,t2=311,t3=316,t4=322;
		ListIterator li = gui.blocks.listIterator(gui.blocks.size());
		String ss1,ss2;int max;String dots="...";
		i = 0;
		
		while (li.hasPrevious()&&i<5)
		{
			Block block = (Block)li.previous();
		  g2d.setFont(new Font("SansSerif.italic", Font.PLAIN, 25));
		  g2d.drawString(block.challenge,425+i*t1+test, 800);
		  g2d.setFont(new Font("SansSerif.italic", Font.ITALIC, 25));
		  g2d.setColor(Color.BLACK);
		  g2d.drawString(limit(block.solvingGroupName),430+i*t0+test, 740);
		  g2d.setColor(Color.WHITE);		  
		  g2d.setFont(new Font("SansSerif.italic", Font.PLAIN, 36));
		  g2d.drawString(block.groupId,415+i*t2+test, 860);
		  //calculate length to print
		  max=10;//calcMax(s1);
		  ss1=block.string1.substring(0,Math.min(max,block.string1.length()));
		  if (block.string1.length()>max) ss1+=dots;
		  ss2=block.string2.substring(0,Math.min(max,block.string2.length()));
		  if (block.string2.length()>max) ss2+=dots;
		  g2d.setFont(new Font("SansSerif.italic", Font.PLAIN, 21));
		  g2d.drawString(ss1,405+i*t3+test, 920);
		  g2d.drawString(ss2,398+i*t4+test, 980);
			
		  i++;
		}
	}
	
	private String limit(String s)
	{
	  String ans;
	  
	  if (s.length()>10) ans=s.substring(0,10)+"...";
	  else ans=s;
	  return ans;
	}
}