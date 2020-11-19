
public class Main {

    public static void main(String[] args) {
        //indexation des sources AP si nécessaire
        Indexer indexer= new Indexer("Assets/AP/");
        int non_indexe=indexer.estAJour();
        if(non_indexe>0){
            System.out.println("l'index n'est pas à jour de"+non_indexe+", voulez-vous le mettre à jour ? y/n");
            char reponse='n'; // on remplacera par le retour de l'interface
            if(reponse=='y'){
                indexer.supprime_index();
                indexer.indexation();
            }
        }else{
            System.out.println(" l'index est à jour !");
        }
        //indexer.supprime_index();
       // indexer.indexation();
            // compare les fichiers source et les colonnes de l'index, s'il en manque une lance l'indexation

        //lancement de l'interface
            // interface lance traitement de requête
            // affiche le résultat
    }

}
