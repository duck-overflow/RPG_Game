package de.duckoverflow.rpg.mapDesigner;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GridImageEditor editor = new GridImageEditor();
            editor.setVisible(true);
        });
    }

}
