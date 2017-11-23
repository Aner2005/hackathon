package blockchain.net.impl.blockchain;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import blockchain.net.impl.blockchain.*;

public class runClients {
	public static void main(String[] args) {
		try {
			String line;
			BufferedReader br = new BufferedReader(new FileReader("users.csv"));
			while ((line = br.readLine())!=null) {
				String[] groups = line.split(",");
				AutomatedClient ac = new AutomatedClient(groups[0], groups[1]);
				new Thread(ac).start();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}/*
		HashMap<String, Integer> score = new HashMap<String, Integer>();
		score.put("123", 100);
		FileInputStream loadFile;
		try {
			/*FileOutputStream saveFile = new FileOutputStream("backup/1.sav");
			ObjectOutputStream save = new ObjectOutputStream(saveFile);
			save.writeObject(score);

			loadFile = new FileInputStream("backup/2.sav");
			ObjectInputStream save = new ObjectInputStream(loadFile);
			int d = save.readInt();
			String c = (String)save.readObject();
			HashMap<String, Integer> score2 = (HashMap<String, Integer>)save.readObject();
			System.out.println(d+c+score2.get("123"));


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//conn2.loadBackup("1.sav");*/
	}
}
