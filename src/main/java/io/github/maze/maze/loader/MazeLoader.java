package io.github.maze.maze.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.List;

/**
 * MapPanel
 * - Menampilkan satu atau beberapa layer map (List<int[][]>)
 * - Tile di-render dari image map atau fallback warna
 * - Mendukung zoom, pilih layer aktif, dan klik untuk membaca tile
 */
public class MapPanel extends JPanel {
    private final List<int[][]> layers;
    private final Map<Integer, BufferedImage> tileImages = new HashMap<>();
    private final Map<Integer, Color> tileColors = new HashMap<>();
    private int activeLayer = 0;
    private int tileSize = 32; // default pixel per tile
    private double zoom = 1.0;

    // optional pan
    private int offsetX = 0;
    private int offsetY = 0;
    private Point dragStart;

    public MapPanel(List<int[][]> layers) {
        this.layers = layers != null ? layers : Collections.emptyList();
        setBackground(Color.BLACK);
        setPreferredSize(calcPreferredSize());
        initDefaultColors();
        initMouseHandlers();
    }

    /** Inisialisasi warna default untuk beberapa tile id (fallback jika image tidak tersedia) */
    private void initDefaultColors() {
        tileColors.put(0, new Color(0x000000)); // empty
        tileColors.put(1, new Color(0xCCCCCC)); // floor
        tileColors.put(2, new Color(0x888888)); // wall
        tileColors.put(3, new Color(0xFFCC00)); // start
        tileColors.put(4, new Color(0x00CC66)); // special
        tileColors.put(5, new Color(0x0066CC)); // door
        tileColors.put(10, new Color(0xAA66FF));
        tileColors.put(11, new Color(0xFF6666));
        tileColors.put(12, new Color(0x66FFFF));
        tileColors.put(13, new Color(0x333333));
        tileColors.put(14, new Color(0x444444));
        tileColors.put(15, new Color(0xFFFFFF));
    }

    /** Optional: muat sprite dari folder resources (path relatif) */
    public void loadTileImage(int tileId, Path imagePath) throws IOException {
        BufferedImage img = javax.imageio.ImageIO.read(imagePath.toFile());
        tileImages.put(tileId, img);
    }

    /** Set ukuran tile (pixel) */
    public void setTileSize(int tileSize) {
        this.tileSize = Math.max(4, tileSize);
        revalidate();
        repaint();
    }

    /** Zoom (1.0 = 100%) */
    public void setZoom(double zoom) {
        this.zoom = Math.max(0.1, zoom);
        revalidate();
        repaint();
    }

    /** Pilih layer yang aktif (0 = base) */
    public void setActiveLayer(int layerIndex) {
        if (layerIndex >= 0 && layerIndex < layers.size()) {
            this.activeLayer = layerIndex;
            repaint();
        }
    }

    /** Hitung preferred size berdasarkan layer pertama */
    private Dimension calcPreferredSize() {
        if (layers.isEmpty()) return new Dimension(200, 200);
        int[][] base = layers.get(0);
        int w = base[0].length;
        int h = base.length;
        int size = (int) Math.ceil(tileSize * zoom);
        return new Dimension(w * size, h * size);
    }

    @Override
    public Dimension getPreferredSize() {
        return calcPreferredSize();
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0.create();
        g.translate(offsetX, offsetY);
        g.scale(zoom, zoom);
        if (layers.isEmpty()) {
            g.setColor(Color.DARK_GRAY);
            g.drawString("No map loaded", 10, 20);
            g.dispose();
            return;
        }

        // Render semua layer (atau hanya activeLayer jika Anda mau)
        for (int li = 0; li < layers.size(); li++) {
            int[][] map = layers.get(li);
            boolean isActive = (li == activeLayer);
            for (int r = 0; r < map.length; r++) {
                for (int c = 0; c < map[r].length; c++) {
                    int id = map[r][c];
                    int px = c * tileSize;
                    int py = r * tileSize;

                    // draw image if available
                    BufferedImage img = tileImages.get(id);
                    if (img != null) {
                        g.drawImage(img, px, py, tileSize, tileSize, null);
                    } else {
                        Color col = tileColors.getOrDefault(id, Color.MAGENTA);
                        g.setColor(col);
                        g.fillRect(px, py, tileSize, tileSize);
                    }

                    // optional grid / overlay
                    g.setColor(new Color(0,0,0,40));
                    g.drawRect(px, py, tileSize, tileSize);

                    // highlight active layer with slight overlay
                    if (!isActive) {
                        g.setColor(new Color(0,0,0,80));
                        g.fillRect(px, py, tileSize, tileSize);
                    }
                }
            }
        }
        g.dispose();
    }

    private void initMouseHandlers() {
        // click to get tile coordinates
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mx = (int) ((e.getX() - offsetX) / zoom);
                int my = (int) ((e.getY() - offsetY) / zoom);
                int col = mx / tileSize;
                int row = my / tileSize;
                if (!layers.isEmpty() && row >= 0 && row < layers.get(0).length
                        && col >= 0 && col < layers.get(0)[0].length) {
                    int id = layers.get(activeLayer)[row][col];
                    // contoh: tampilkan id di status bar atau console
                    System.out.printf("Clicked tile at row=%d col=%d id=%d (layer=%d)%n",
                            row, col, id, activeLayer);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                dragStart = e.getPoint();
                setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragStart = null;
                setCursor(Cursor.getDefaultCursor());
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragStart != null) {
                    Point p = e.getPoint();
                    offsetX += p.x - dragStart.x;
                    offsetY += p.y - dragStart.y;
                    dragStart = p;
                    repaint();
                }
            }
        });

        // wheel to zoom
        addMouseWheelListener(e -> {
            double delta = e.getPreciseWheelRotation();
            setZoom(Math.max(0.2, zoom - delta * 0.1));
            revalidate();
            repaint();
        });
    }
}


// buat manggil ke main
// // asumsi import io.github.maze.maze.loader.MazeLoader;
// List<int[][]> layers = MazeLoader.loadMap("map1.txt");
// MapPanel mapPanel = new MapPanel(layers);
// // optional: load tile images
// // mapPanel.loadTileImage(1, Paths.get("resources/tiles/floor.png"));

// JFrame frame = new JFrame("Maze Viewer");
// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
// frame.getContentPane().add(new JScrollPane(mapPanel));
// frame.pack();
// frame.setLocationRelativeTo(null);
// frame.setVisible(true);
