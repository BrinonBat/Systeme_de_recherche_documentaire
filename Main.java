
public class Main {

    public static void main(String[] args) {
        //création de l'indexer et vérification de s'il est à jour
        Indexer indexer= new Indexer("Assets/AP/");
        int non_indexe=indexer.nEstPasAJour();
        //s'il n'est pas un jour, averti l'utilisateur
        if(non_indexe>0){
            System.out.println("l'index n'est pas à jour de "+non_indexe+" fichiers, voulez-vous le mettre à jour ? y/n");
            char reponse='n'; // on remplacera par le retour de l'interface
            if(reponse=='y'){
                indexer.supprime_index();
                indexer.indexation();
            }
        
        //sinon, il se content de charger
        }else{
            System.out.println(" l'index est à jour !");
        }
        //indexer.chargerIndex();
        indexer.afficherIndex();
        indexer.supprime_index();
        //lancement de l'interface
            // interface lance traitement de requête
            // affiche le résultat
    }

}
