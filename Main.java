import java.io.File;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
    //	Fenetre fen = new Fenetre();
    	String sources="Assets/AP/";
        //cr�ation de l'indexer et v�rification de s'il est � jour
        Indexer indexer= new Indexer(sources);
        int non_indexe=indexer.nEstPasAJour();
        //s'il n'est pas un jour, averti l'utilisateur
        if(non_indexe>0){
            System.out.println("l'index n'est pas à jour de "+non_indexe+" fichiers, voulez-vous le mettre à jour ? y/n");
            char reponse='n'; // on remplacera par le retour de l'interface
            if(reponse=='y'){
                indexer.indexation();
            }
        
        //sinon, il se contente de charger
        }else{
           if(!indexer.chargerIndex()) System.out.println("erreur lors de la récupération de l'index");
            System.out.println(" l'index est à jour !");
        }
        Requete saisie=new Requete(new File(sources),"constructive relationship with the United States.");
        ArrayList<File> resultats= saisie.documentsCorrespondants(indexer.getIndex());
    }

}
