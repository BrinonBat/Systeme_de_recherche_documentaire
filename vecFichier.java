import java.io.File;
import java.util.Vector;

public class vecFichier {
    private File fichier;
    Vector<Reference> vec_refs;

    //constructeurs
    public vecFichier(File fic){
        fichier=fic;
        vec_refs=new Vector<Reference>();
    }
    public vecFichier(File fic,Reference ref){
        new vecFichier(fic);
        
    }
    public vecFichier(File fic,short numero){
        String num=Integer.toString(numero);
        switch(num.length()){
            case(1):{
                num="000"+num;
                break;
            }
            case(2):{
                num="00"+num;
                break;
            }
            case(3):{
                num="0"+num;
                break;
            }
            default: break;
        };
        fichier=new File(fic.getAbsolutePath()+"-"+num);
        vec_refs=new Vector<Reference>();
    }

    public float getPoids(int position){
        return vec_refs.get(position).getPoids();
    }

    public void setIDF(int position, double idf){
        vec_refs.get(position).setPoids(idf);
    }

    public File getFichier(){
        return fichier;
    }

    public void ajoutRef(Reference ref){
        vec_refs.add(ref);
    }

    public boolean contientMot(int position){
        return (vec_refs.get(position).getQuantite()>0);
    }
}