import java.io.File;
import java.io.FilenameFilter;


public class FiltreSrc implements FilenameFilter {
        // Refuse les noms qui terminent par '.txt' et '.Z'
        @Override
        public boolean accept(File dir, String name) {
     
            if (name.endsWith(".txt") || name.endsWith(".Z") || name.endsWith("README")) {
                return false;
            }
     
            return true;
        }
}

