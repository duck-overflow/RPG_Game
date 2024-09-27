package de.duckoverflow.rpg.mapDesigner;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

public class GridImageEditor extends JFrame {
    private final int gridSize = 50;  // 50x50 Grid
    private final JTable table;
    private final JPanel blockPanel;
    private final int blockSize = 16; // Jede Zelle 16x16 Pixel
    private ImageIcon selectedIcon = null;  // Für das ausgewählte Icon

    public GridImageEditor() {
        setTitle("Grid Image Editor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Tabelle mit 50x50 Feldern erstellen
        table = new JTable(gridSize, gridSize) {
            @Override
            public Dimension getPreferredScrollableViewportSize() {
                return new Dimension(800, 800); // 800x800 Pixel Tabelle
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Zellen nicht direkt editierbar
            }

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component cell = super.prepareRenderer(renderer, row, column);
                Object cellValue = getValueAt(row, column);

                // Wenn die Zelle ein Icon enthält, wird es gesetzt, sonst ist sie leer
                JLabel label = (JLabel) cell;
                if (cellValue instanceof Icon) {
                    label.setIcon((Icon) cellValue);
                    label.setText("");
                } else {
                    label.setIcon(null);
                    label.setText("");  // Kein Text anzeigen
                    label.setBackground(Color.WHITE);  // Standardhintergrund
                }
                return cell;
            }
        };

        // Jede Zelle 16x16 Pixel
        table.setRowHeight(blockSize);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Event-Listener zum Einfärben der Tabelle mit dem ausgewählten Icon
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (selectedIcon != null) {
                    Point point = e.getPoint();
                    int row = table.rowAtPoint(point);
                    int col = table.columnAtPoint(point);

                    // Markiere die Zelle mit dem ausgewählten Icon
                    table.setValueAt(selectedIcon, row, col);
                    table.repaint();
                }
            }
        });

        table.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedIcon != null) {
                    Point point = e.getPoint();
                    int row = table.rowAtPoint(point);
                    int col = table.columnAtPoint(point);

                    if (row >= 0 && col >= 0 && row < gridSize && col < gridSize) {
                        // Färbe die Zellen während des Ziehens mit dem Icon
                        table.setValueAt(selectedIcon, row, col);
                        table.repaint();
                    }
                }
            }
        });

        for (int i = 0; i < gridSize; i++) {
            TableColumn col = table.getColumnModel().getColumn(i);
            col.setPreferredWidth(blockSize);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Block Panel erstellen
        blockPanel = new JPanel();
        blockPanel.setLayout(new FlowLayout());

        // Icons dynamisch aus dem resources-Ordner laden und ins Panel einfügen
        // Array von Bildpfaden innerhalb des Ressourcenordners
        String[] iconPaths = {
                "/tiles/grass.png",
                "/tiles/wall.png",
                "/tiles/water.png",
                "/tiles/earth.png",
                "/tiles/tree.png",
                "/tiles/sand.png",
                "/tiles/half_water_down.png",
                "/tiles/half_water_up.png"
        };
        for (String iconPath : iconPaths) {
            try {
                JLabel block = createSelectableImageBlock(iconPath);
                blockPanel.add(block);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        add(blockPanel, BorderLayout.SOUTH);

        // Speichern-Button
        JButton saveButton = new JButton("Speichern als PNG");
        saveButton.addActionListener(e -> saveGridAsImage());
        add(saveButton, BorderLayout.NORTH);

        pack();
        setLocationRelativeTo(null);  // Fenster zentrieren
    }

    // Erstellen eines auswählbaren Blocks mit einem PNG-Bild aus dem resources-Ordner
    private JLabel createSelectableImageBlock(String resourcePath) throws IOException {
        // Lade das Bild aus dem resources-Ordner
        ImageIcon icon = loadImageIconFromResources(resourcePath);

        // Bild auf die Größe des Blocks skalieren
        assert icon != null;
        Image scaledImage = icon.getImage().getScaledInstance(blockSize, blockSize, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel block = new JLabel(scaledIcon);  // Setze das Icon in den Block
        block.setPreferredSize(new Dimension(blockSize, blockSize));
        block.setOpaque(true);

        // Auswahlfunktion für den Block
        block.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                selectedIcon = scaledIcon;  // Setze das aktuelle Icon als ausgewähltes
                block.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));  // Markiere den ausgewählten Block
                clearOtherSelections(block);  // Andere Block auswahlen löschen
            }
        });

        return block;
    }

    // Methode zum Löschen der Auswahl anderer Blöcke
    private void clearOtherSelections(JLabel selectedBlock) {
        Component[] blocks = blockPanel.getComponents();
        for (Component block : blocks) {
            if (block instanceof JLabel && block != selectedBlock) {
                ((JLabel) block).setBorder(BorderFactory.createEmptyBorder());
            }
        }
    }

    // Methode zum Laden von Images aus dem resources-Ordner
    private ImageIcon loadImageIconFromResources(String resourcePath) {
        try {
            // Lade das Bild mit dem ClassLoader
            return new ImageIcon(Objects.requireNonNull(getClass().getResource(resourcePath)));
        } catch (Exception e) {
            System.err.println("Bild nicht gefunden: " + resourcePath);
            return null;
        }
    }

    // Speichert die Tabelle als PNG Bild
    private void saveGridAsImage() {
        int width = table.getColumnModel().getTotalColumnWidth();
        int height = table.getRowHeight() * table.getRowCount();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();

        // Hintergrund setzen
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, width, height);

        // Entferne Gitternetz
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                Object cellValue = table.getValueAt(row, col);
                if (cellValue instanceof Icon icon) {
                    icon.paintIcon(table, g2, col * blockSize, row * blockSize);
                }
            }
        }

        g2.dispose();

        File outputFile = new File("src/res/worldimage/grid_image.png");
        outputFile.getParentFile().mkdirs();

        try {
            ImageIO.write(image, "png", outputFile);
            JOptionPane.showMessageDialog(this, "Bild gespeichert unter: " + outputFile.getAbsolutePath());
            ImageAnalyser.analyseImage();
        } catch (IOException e) {
            throw new RuntimeException(e);
    }


}
}
