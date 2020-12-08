import java.io.File;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
    //	Fenetre fen = new Fenetre();
    	String sources="tests/AP/";
        //cr�ation de l'indexer et v�rification de s'il est � jour
        Indexer indexer= new Indexer(sources);
        int non_indexe=indexer.nEstPasAJour();
        non_indexe=1;
        //s'il n'est pas un jour, averti l'utilisateur
        if(non_indexe>0){
            System.out.println("l'index n'est pas à jour de "+non_indexe+" fichiers, voulez-vous le mettre à jour ? y/n");
            char reponse='y'; // on remplacera par le retour de l'interface
            if(reponse=='y'){
                indexer.indexation();
            }
        
        //sinon, il se contente de charger
        }else{
           if(!indexer.chargerIndex()) System.out.println("erreur lors de la récupération de l'index");
            System.out.println(" l'index est à jour !");
        }

        // supposé afficher AP891216-0002 en premier.
        Requete saisie=new Requete(indexer.getIndex(),new File(sources),"second");
        ArrayList<File> resultats= saisie.documentsCorrespondants(indexer.getIndex());
    }

}
