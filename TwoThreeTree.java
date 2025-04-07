
public class TwoThreeTree {
    private Node root;

    // Node class for 2-3 tree
    private class Node {
        int leftValue, rightValue;
        Node left, middle, right;
        boolean isThreeNode;

        Node(int value) {
            leftValue = value;
            isThreeNode = false;
            left = middle = right = null;
        }
    }

    public TwoThreeTree() {
        root = null;
    }

    // Search operation
    public boolean search(int value) {
        return search(root, value);
    }

    private boolean search(Node node, int value) {
        if (node == null) return false;

        if (!node.isThreeNode) {
            if (node.leftValue == value) return true;
            if (value < node.leftValue) return search(node.left, value);
            return search(node.right, value);
        } else {
            if (node.leftValue == value || node.rightValue == value) return true;
            if (value < node.leftValue) return search(node.left, value);
            if (value < node.rightValue) return search(node.middle, value);
            return search(node.right, value);
        }
    }

    // Insert operation
    public void insert(int value) {
        if (root == null) {
            root = new Node(value);
            return;
        }
        root = insert(root, value);
    }

    private Node insert(Node node, int value) {
        // If we're at a leaf node
        if (node.left == null && node.right == null) {
            if (!node.isThreeNode) {
                if (value < node.leftValue) {
                    node.rightValue = node.leftValue;
                    node.leftValue = value;
                } else {
                    node.rightValue = value;
                }
                node.isThreeNode = true;
                return node;
            } else {
                // Split the node
                return splitNode(node, value);
            }
        }

        // Recurse to find insertion point
        if (!node.isThreeNode) {
            if (value < node.leftValue) {
                node.left = insert(node.left, value);
            } else {
                node.right = insert(node.right, value);
            }
        } else {
            if (value < node.leftValue) {
                node.left = insert(node.left, value);
            } else if (value < node.rightValue) {
                node.middle = insert(node.middle, value);
            } else {
                node.right = insert(node.right, value);
            }
        }
        return node;
    }

    private Node splitNode(Node node, int value) {
        int midValue;
        Node leftChild = new Node(Math.min(Math.min(node.leftValue, node.rightValue), value));
        Node rightChild = new Node(Math.max(Math.max(node.leftValue, node.rightValue), value));
        midValue = Math.min(Math.max(node.leftValue, node.rightValue),
                Math.max(Math.min(node.leftValue, node.rightValue), value));

        Node newParent = new Node(midValue);
        newParent.left = leftChild;
        newParent.right = rightChild;
        return newParent;
    }

    // Delete operation
    public void delete(int value) {
        if (root == null) return;
        root = delete(root, value);
    }

    private Node delete(Node node, int value) {
        if (node == null) return null;

        // Leaf node cases
        if (node.left == null && node.right == null) {
            if (!node.isThreeNode) {
                if (node.leftValue == value) return null;
                return node;
            } else {
                if (node.leftValue == value) {
                    node.leftValue = node.rightValue;
                    node.isThreeNode = false;
                    return node;
                } else if (node.rightValue == value) {
                    node.isThreeNode = false;
                    return node;
                }
                return node;
            }
        }

        // Internal node cases
        if (!node.isThreeNode) {
            if (value < node.leftValue) {
                node.left = delete(node.left, value);
            } else {
                node.right = delete(node.right, value);
            }
        } else {
            if (value < node.leftValue) {
                node.left = delete(node.left, value);
            } else if (value < node.rightValue) {
                node.middle = delete(node.middle, value);
            } else {
                node.right = delete(node.right, value);
            }
        }
        return balance(node);
    }

    private Node balance(Node node) {
        // Basic balancing - more complex cases would need additional logic
        if (node.left == null && node.right != null) {
            if (!node.isThreeNode) {
                node.leftValue = node.right.leftValue;
                node.left = node.right.left;
                node.right = node.right.right;
            }
        }
        return node;
    }

    // Method to print tree horizontally with a style similar to the image
    public void printTreeHorizontal() {
        if (root == null) {
            System.out.println("Tree is empty");
            return;
        }
        System.out.println("Horizontal 2-3 Tree (root on left):");
        printTreeHorizontal(root, "", true);
    }

    private void printTreeHorizontal(Node node, String prefix, boolean isRoot) {
        if (node == null) return;

        // Format the node as [value] for 2-node or [value1 value2] for 3-node
        String nodeStr = node.isThreeNode ?
                "[" + node.leftValue + " " + node.rightValue + "]" :
                "[" + node.leftValue + "]";

        // Print the current node
        if (isRoot) {
            System.out.println(nodeStr);
        } else {
            System.out.println(prefix + "└──>" + nodeStr);
        }

        // Prepare prefix for children
        String newPrefix = isRoot ? "" : prefix + "    ";

        // Print children with arrows
        if (node.left != null) {
            printTreeHorizontal(node.left, newPrefix, false);
        }
        if (node.isThreeNode && node.middle != null) {
            printTreeHorizontal(node.middle, newPrefix, false);
        }
        if (node.right != null) {
            printTreeHorizontal(node.right, newPrefix, false);
        }
    }
}