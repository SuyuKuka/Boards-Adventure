package misc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

class DrawingPanel extends JPanel {
    private List<Shape> shapes = new ArrayList<>();

    public DrawingPanel() {
        setPreferredSize(new Dimension(400, 400));
        setBackground(Color.WHITE);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Add a rectangle to the list of shapes when mouse is pressed
                shapes.add(new Rectangle(e.getX(), e.getY(), 50, 50));
                repaint(); // Repaint the panel
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw all shapes in the list
        for (Shape shape : shapes) {
            g2d.draw(shape);
        }
    }

    public void undo() {
        if (!shapes.isEmpty()) {
            shapes.remove(shapes.size() - 1); // Remove the last shape
            repaint(); // Repaint the panel after removing
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Drawing Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            DrawingPanel drawingPanel = new DrawingPanel();
            frame.getContentPane().add(drawingPanel, BorderLayout.CENTER);

            JButton undoButton = new JButton("Undo");
            undoButton.addActionListener(e -> drawingPanel.undo());
            frame.getContentPane().add(undoButton, BorderLayout.SOUTH);

            frame.pack();
            frame.setVisible(true);
        });
    }
}
