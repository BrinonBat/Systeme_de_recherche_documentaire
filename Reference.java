import java.util.Vector;

public class Reference {
    private float poids;
    private Vector<Integer> liste_positions;

    //constructeur 
    Reference(int nouv_poids,int position){
        poids=nouv_poids;
        liste_positions= new Vector<Integer>();
        liste_positions.add(position);
    }

    //accesseurs

}
