
public class Main {

    public static void main(String[] args) {
    	
    //	Fenetre fen = new Fenetre();
    	
        //cr�ation de l'indexer et v�rification de s'il est � jour
        Indexer indexer= new Indexer("Assets/AP/");
        int non_indexe=indexer.nEstPasAJour();
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
        
        //lancement de l'interface
            // interface lance traitement de requête
            // affiche le résultat
    }

}
