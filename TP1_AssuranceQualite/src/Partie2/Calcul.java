package Partie2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Calcul {
	
	private ArrayList<String> obj = new ArrayList<String>();
	private ArrayList<String> sub = new ArrayList<String>();
	
	public String[] clients ;
	private String[][] plats ;
	private String[][] commandes ;
	
	public Calcul() {
		
		// Appeler methode pour ouvrir le fichier .txt
		ouvertureFichier();
		
		//
		int i = 0;
		String temp;
		do{
			temp = obj.remove(0);
			
			// Depose les donnees accumuler quand l'iteration rencontre :
			if(temp.indexOf(':') >= 0 || temp.equals("Fin")) {
				// Decide dans quelle tableau il depose les donnees
				switch(i){
				case 0: break;
				// Tableau de client
				case 1:
					clients = new String[sub.size()];
					clients = sub.toArray(clients);
					break;
				// Tableau de plats
				case 2:
						assignationPlats();
					break;
				// Tableau de commandes
				case 3:
					
						assignationCommande();
					
					
					break;
				}
				
				i++;
				// Vide le array sub
				sub.clear();
			}else{
				// Ajoute la donn�e actuel a sub
				sub.add(temp);
			}
		} while (!temp.equals("Fin"));		
		faireCalcul();
	}
	
	private void ouvertureFichier() {
		File file = new File("Facture.txt");
		try {
			
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;			
			
			while ((line = br.readLine()) != null) {
				obj.add(line);
			}
			
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void assignationPlats() {
		plats = new String[sub.size()][2];
		String[] tempPlat;
		
		for(int j = 0; j < plats.length; j++) {
			tempPlat = sub.get(j).split(" ");
			
			for(int k = 0; k < plats[0].length; k++) {
				plats[j][k] = tempPlat[k];
			}					
		}
	}
	
	private void assignationCommande() {
		try {
			commandes = new String[sub.size()][3];
			String[] tempCommande;
			
			// Separe les lignes en trois partie / Client / Commande / nb Commandes
			for(int j = 0; j < commandes.length; j++) {
				tempCommande = sub.get(j).split(" ");
				
				for(int k = 0; k < commandes[0].length; k++) {
					boolean trouve = false;
					
					for(int l = 0; l < clients.length; l++) {
						
						if(tempCommande[0].equals(clients[l]) &&  tempCommande[1].equals(plats[k][0])) {
							commandes[j] = tempCommande;
							trouve = true;
							break;		
							
						}else if(tempCommande[0].equals(clients[l])) {
							break;		
							
						}
					}
					if(trouve) {
						break;				
					}
				}					
			}
		}catch (Exception e) {
			System.out.println("Le fichier ne respecte pas le format demand� !");
		}
	};

	/// ------------------------------------------------------------------------------------------------
	private void faireCalcul() {
		double[] prix = new double[clients.length];
		double resultatPrix = 0;
		
		for(int i = 0; i < clients.length; i++) {
			String client = clients[i];
			
			for(int j = 0; j < commandes.length; j++) {
				
				if(client.equals(commandes[j][0])) {
					String plat = commandes[j][1];
					
					for(int k = 0; k < plats.length; k++) {
						
						if(plat.equals(plats[k][0])) {
							
							try {
								double nb = Double.parseDouble(commandes[j][2]);
								double prixUnit = Double.parseDouble(plats[k][1]);
								resultatPrix = nb * prixUnit;
								
								prix[i] += resultatPrix;
								resultatPrix = 0;
							}catch(NumberFormatException e) {
								System.out.print("Erreur calcul");
							}				
						}
					}
				}
			}			
		}
		new Facture(clients, prix);
	}
	
	public static void main(String[] args) {
		
		
		new Calcul();
	}
}
