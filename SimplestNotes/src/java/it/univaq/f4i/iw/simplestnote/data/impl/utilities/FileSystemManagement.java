package it.univaq.f4i.iw.simplestnote.data.impl.utilities;

import java.io.File;

/**
 *
 * @author lorenzoaddazi
 */
public class FileSystemManagement {
    
    public static boolean delFileRecursively(File root) {
            if (root != null && root.exists()) {
                if (root.isDirectory()) {
                    File[] childFiles = root.listFiles();
                    if (childFiles != null) {
                        for (File childFile : childFiles) {
                            delFileRecursively(childFile);
                        }
                    }
                }
            return root.delete();
            }
        return false;
        }
    
    
    
    public static int levenshteinDistance(String a, String b){
        int[][] distance = new  int[a.length() + 1][b.length() + 1];
        for (int i = 0; i <= a.length(); i++)
			distance[i][0] = i;
		for (int j = 1; j <= b.length(); j++)
			distance[0][j] = j;
 
		for (int i = 1; i <= a.length(); i++)
			for (int j = 1; j <= b.length(); j++)
				distance[i][j] = min(
						distance[i - 1][j] + 1,
						distance[i][j - 1] + 1,
						distance[i - 1][j - 1]+ ((a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1));
 
		return distance[a.length()][b.length()];   
    }
    
    private static int min(int a, int b, int c){
        return Math.min(Math.min(a, b), c);
    }
    
}
