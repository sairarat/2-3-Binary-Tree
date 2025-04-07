import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class TwoThreeTreeGUI extends JFrame {
    private TwoThreeTree tree;
    private JTextField inputField;
    private JButton insertButton, deleteButton, searchButton, clearButton;
    private TreePanel treePanel;

    public TwoThreeTreeGUI() {
        tree = new TwoThreeTree();

        // Set up the frame
        setTitle("2-3 Tree GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setSize(800, 600); // Increased size for better tree visualization

        // Input panel (top)
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        JLabel inputLabel = new JLabel("Enter value:");
        inputField = new JTextField(10);
        insertButton = new JButton("Insert");
        deleteButton = new JButton("Delete");
        searchButton = new JButton("Search");
        clearButton = new JButton("Clear");

        inputPanel.add(inputLabel);
        inputPanel.add(inputField);
        inputPanel.add(insertButton);
        inputPanel.add(deleteButton);
        inputPanel.add(searchButton);
        inputPanel.add(clearButton);

        // Tree panel (center)
        treePanel = new TreePanel(tree);
        JScrollPane scrollPane = new JScrollPane(treePanel);

        // Add components to frame
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Add action listeners
        insertButton.addActionListener(e -> handleInsert());
        deleteButton.addActionListener(e -> handleDelete());
        searchButton.addActionListener(e -> handleSearch());
        clearButton.addActionListener(e -> handleClear());


        // Make frame visible
        setLocationRelativeTo(null);
    }

    private void handleInsert() {
        try {
            int value = Integer.parseInt(inputField.getText().trim());
            tree.insert(value);
            treePanel.repaint(); // Redraw the tree
            inputField.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid integer",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete() {
        try {
            int value = Integer.parseInt(inputField.getText().trim());
            if (tree.search(value)) {
                tree.delete(value);
                treePanel.repaint(); // Redraw the tree
            } else {
                JOptionPane.showMessageDialog(this, "Value " + value + " not found in the tree",
                        "Not Found", JOptionPane.WARNING_MESSAGE);
            }
            inputField.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid integer",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSearch() {
        try {
            int value = Integer.parseInt(inputField.getText().trim());
            boolean found = tree.search(value);
            JOptionPane.showMessageDialog(this, "Value " + value + " found: " + found,
                    "Search Result", JOptionPane.INFORMATION_MESSAGE);
            inputField.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid integer",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void handleClear() {
        tree = new TwoThreeTree(); // Reset the tree to a new, empty instance
        treePanel.tree = tree;     // Update the tree reference in the TreePanel
        inputField.setText("");    // Clear the input field
        treePanel.repaint();       // Redraw the tree panel
    }

    // Custom panel to draw the tree graphically
    class TreePanel extends JPanel {
        private TwoThreeTree tree;
        private final int NODE_WIDTH = 60;
        private final int NODE_HEIGHT = 30;
        private final int LEVEL_HEIGHT = 80;
        private final int HORIZONTAL_GAP = 20;

        public TreePanel(TwoThreeTree tree) {
            this.tree = tree;
            setPreferredSize(new Dimension(800, 600)); // Default size
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (tree.getRoot() == null || tree.getRoot().getKeys().isEmpty()) {
                g2d.drawString("Tree is empty", 20, 20);
                return;
            }

            // Calculate the width needed to draw the tree
            int treeWidth = calculateTreeWidth(tree.getRoot(), 0);
            setPreferredSize(new Dimension(Math.max(treeWidth * (NODE_WIDTH + HORIZONTAL_GAP), 800),
                    calculateTreeHeight(tree.getRoot()) * LEVEL_HEIGHT + 50));
            revalidate(); // Adjust scroll pane

            // Draw the tree starting from the root
            drawTree(g2d, tree.getRoot(), getWidth() / 2, 30, getWidth() / 4);
        }

        private int calculateTreeWidth(TwoThreeTree.Node node, int level) {
            if (node == null || node.isLeaf()) {
                return 1;
            }
            int width = 0;
            for (TwoThreeTree.Node child : node.getChildren()) {
                width += calculateTreeWidth(child, level + 1);
            }
            return Math.max(width, 1);
        }

        private int calculateTreeHeight(TwoThreeTree.Node node) {
            if (node == null || node.isLeaf()) {
                return 1;
            }
            int maxHeight = 0;
            for (TwoThreeTree.Node child : node.getChildren()) {
                maxHeight = Math.max(maxHeight, calculateTreeHeight(child));
            }
            return maxHeight + 1;
        }

        private void drawTree(Graphics2D g2d, TwoThreeTree.Node node, int x, int y, int xOffset) {
            if (node == null) return;

            // Draw the node
            ArrayList<Integer> keys = node.getKeys();
            int nodeWidth = NODE_WIDTH * keys.size(); // Adjust width based on number of keys
            g2d.setColor(new Color(200, 255, 200)); // Light green background
            g2d.fillRect(x - nodeWidth / 2, y, nodeWidth, NODE_HEIGHT);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(x - nodeWidth / 2, y, nodeWidth, NODE_HEIGHT);

            // Draw keys inside the node
            for (int i = 0; i < keys.size(); i++) {
                g2d.drawString(keys.get(i).toString(),
                        x - nodeWidth / 2 + i * NODE_WIDTH + NODE_WIDTH / 4,
                        y + NODE_HEIGHT / 2 + 5);
            }

            // Draw vertices (small circles) for child connections
            int[] vertexX = new int[keys.size() + 1];
            for (int i = 0; i <= keys.size(); i++) {
                vertexX[i] = x - nodeWidth / 2 + i * (nodeWidth / keys.size());
                g2d.setColor(Color.BLACK);
                g2d.fillOval(vertexX[i] - 3, y + NODE_HEIGHT - 3, 6, 6);
            }

            // Draw children
            if (!node.isLeaf()) {
                ArrayList<TwoThreeTree.Node> children = node.getChildren();
                int childXOffset = xOffset / (children.size() > 1 ? children.size() : 1);
                int nextY = y + LEVEL_HEIGHT;

                for (int i = 0; i < children.size(); i++) {
                    int childX = x - xOffset + i * (2 * xOffset / (children.size() > 1 ? children.size() - 1 : 1));
                    // Draw edge from parent vertex to child
                    g2d.setColor(Color.BLACK);
                    g2d.drawLine(vertexX[i], y + NODE_HEIGHT, childX, nextY);
                    drawTree(g2d, children.get(i), childX, nextY, childXOffset);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TwoThreeTreeGUI gui = new TwoThreeTreeGUI();
            gui.setVisible(true);
        });
    }
}