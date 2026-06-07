package io.github.maze.maze.loader;

package io.github.maze.maze.loader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/*
 * Load Map from TXT (~/maze/maps/mapX.txt)
 *
 */
public class MazeLoader {

    // Asumsi 'gp' itu parameter dari class GamePanel / Main class kamu
    public void loadMap(String mapPath, GamePanel gp) {
        try {
            // Ambil file txt (pake getResourceAsStream biar jalan walau udah di-build jadi .jar)
            InputStream is = getClass().getResourceAsStream(mapPath);
            
            if (is == null) {
                System.err.println("Waduh, file map di " + mapPath + " gak ketemu!");
                return;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            int row = 0;

            // Loop baca file txt baris per baris
            while ((line = br.readLine()) != null) {
                
                // Pisah karakter yang ada di baris itu pake spasi
                String[] numbers = line.split(" ");
                
                // Loop ke samping untuk ngambil col dan id-nya
                for (int col = 0; col < numbers.length; col++) {
                    
                    // Ambil id dari array string, ubah jadi integer
                    int id = Integer.parseInt(numbers[col]);
                    
                    // Execute method sesuai request kamu
                    gp.maze.addObject(id, col, row);
                }
                
                // Setelah selesai 1 baris (ke samping), baris (row) nambah 1 ke bawah
                row++;
            }
            
            br.close();
            System.out.println("Map berhasil di-load!");

        } catch (Exception e) {
            System.err.println("Ada error pas baca file map:");
            e.printStackTrace();
        }
    }
}
