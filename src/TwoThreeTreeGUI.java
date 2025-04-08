import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TwoThreeTreeGUI extends JFrame {
    private TwoThreeTree tree;
    private JTextField inputField;
    private JButton insertButton, deleteButton, searchButton, clearButton;
    private TreePanel treePanel;
    private DefaultListModel<Integer> insertedModel;
    private JList<Integer> insertedValueList;

    public TwoThreeTreeGUI() {
        tree = new TwoThreeTree();

        // Set up the frame
        setTitle("2-3 Tree GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setSize(800, 600);

        // Input panel (top)
        JPanel inputPanel = new JPanel(new FlowLayout());

        inputField = new JTextField(10);
        insertButton = new JButton("Insert");
        deleteButton = new JButton("Delete");
        searchButton = new JButton("Search");
        clearButton = new JButton("Clear");

        inputPanel.add(new JLabel("Enter value:"));
        inputPanel.add(inputField);
        inputPanel.add(insertButton);
        inputPanel.add(deleteButton);
        inputPanel.add(searchButton);
        inputPanel.add(clearButton);

        // Tree panel (center)
        treePanel = new TreePanel(tree);
        JScrollPane scrollPane = new JScrollPane(treePanel);

        // Values list (right side)
        insertedModel = new DefaultListModel<>();
        insertedValueList = new JList<>(insertedModel);
        insertedValueList.setBorder(BorderFactory.createTitledBorder("Values in Tree"));
        JScrollPane listScrollPane = new JScrollPane(insertedValueList);
        listScrollPane.setPreferredSize(new Dimension(150, 0));

        // Add components to frame
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(listScrollPane, BorderLayout.EAST);

        // Add action listeners
        insertButton.addActionListener(e -> handleInsert());
        deleteButton.addActionListener(e -> handleDelete());
        searchButton.addActionListener(e -> handleSearch());
        clearButton.addActionListener(e -> handleClear());

        setLocationRelativeTo(null);
    }

    private void handleInsert() {
        try {
            int value = Integer.parseInt(inputField.getText().trim());
            tree.insert(value);
            if (!insertedModel.contains(value)) {
                insertedModel.addElement(value);
            }
            treePanel.repaint();
            inputField.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid integer", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete() {
        try {
            int value = Integer.parseInt(inputField.getText().trim());
            if (tree.search(value)) {
                tree.delete(value);
                insertedModel.removeElement(value);
                treePanel.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Value " + value + " not found in the tree", "Not Found", JOptionPane.WARNING_MESSAGE);
            }
            inputField.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid integer", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSearch() {
        try {
            int value = Integer.parseInt(inputField.getText().trim());
            boolean found = tree.search(value);
            JOptionPane.showMessageDialog(this, "Value " + value + " found: " + found, "Search Result", JOptionPane.INFORMATION_MESSAGE);
            inputField.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid integer", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleClear() {
        tree = new TwoThreeTree();
        treePanel.tree = tree;
        insertedModel.clear();
        inputField.setText("");
        treePanel.repaint();
    }

    // TreePanel class stays the same...
    class TreePanel extends JPanel {
        private TwoThreeTree tree;
        private final int NODE_WIDTH = 60;
        private final int NODE_HEIGHT = 30;
        private final int LEVEL_HEIGHT = 80;
        private final int HORIZONTAL_GAP = 20;

        public TreePanel(TwoThreeTree tree) {
            this.tree = tree;
            setPreferredSize(new Dimension(800, 600));
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

            int treeWidth = calculateTreeWidth(tree.getRoot(), 0);
            setPreferredSize(new Dimension(Math.max(treeWidth * (NODE_WIDTH + HORIZONTAL_GAP), 800),
                    calculateTreeHeight(tree.getRoot()) * LEVEL_HEIGHT + 50));
            revalidate();

            drawTree(g2d, tree.getRoot(), getWidth() / 2, 30, getWidth() / 4);
        }

        private int calculateTreeWidth(TwoThreeTree.Node node, int level) {
            if (node == null || node.isLeaf()) return 1;
            int width = 0;
            for (TwoThreeTree.Node child : node.getChildren()) {
                width += calculateTreeWidth(child, level + 1);
            }
            return Math.max(width, 1);
        }

        private int calculateTreeHeight(TwoThreeTree.Node node) {
            if (node == null || node.isLeaf()) return 1;
            int maxHeight = 0;
            for (TwoThreeTree.Node child : node.getChildren()) {
                maxHeight = Math.max(maxHeight, calculateTreeHeight(child));
            }
            return maxHeight + 1;
        }

        private void drawTree(Graphics2D g2d, TwoThreeTree.Node node, int x, int y, int xOffset) {
            if (node == null) return;

            ArrayList<Integer> keys = node.getKeys();
            int nodeWidth = NODE_WIDTH * keys.size();
            g2d.setColor(new Color(200, 255, 200));
            g2d.fillRect(x - nodeWidth / 2, y, nodeWidth, NODE_HEIGHT);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(x - nodeWidth / 2, y, nodeWidth, NODE_HEIGHT);

            for (int i = 0; i < keys.size(); i++) {
                g2d.drawString(keys.get(i).toString(),
                        x - nodeWidth / 2 + i * NODE_WIDTH + NODE_WIDTH / 4,
                        y + NODE_HEIGHT / 2 + 5);
            }

            int[] vertexX = new int[keys.size() + 1];
            for (int i = 0; i <= keys.size(); i++) {
                vertexX[i] = x - nodeWidth / 2 + i * (nodeWidth / keys.size());
                g2d.fillOval(vertexX[i] - 3, y + NODE_HEIGHT - 3, 6, 6);
            }

            if (!node.isLeaf()) {
                ArrayList<TwoThreeTree.Node> children = node.getChildren();
                int childXOffset = xOffset / Math.max(1, children.size());
                int nextY = y + LEVEL_HEIGHT;

                for (int i = 0; i < children.size(); i++) {
                    int childX = x - xOffset + i * (2 * xOffset / Math.max(1, children.size() - 1));
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
