package hse.edu.cs.fortuneAlg;

class Beachline {
    private Arc root;
    private  final Arc nullNode;

    Beachline() {
        this.nullNode = new Arc();
        this.root = this.nullNode;
    }

    boolean isEmpty() {
        return isNullNode(root);
    }

    boolean isNullNode(Arc node) {
        return node == nullNode;
    }

    Arc createArc(InitPoint initPoint) {
        return new Arc(initPoint, nullNode);
    }

    void setRoot(Arc node) {
        this.root = node;
        this.root.color = Arc.Color.BLACK;
    }

    Arc getLeastArc() {
        Arc node = root;
        while (!isNullNode(node.prev))
            node = node.prev;
        return node;
    }

    Arc findArcAbove(Point point, double line) {
        Arc node = root;
        boolean found = false;
        while (!found) {
            double leftBreakPoint = Double.NEGATIVE_INFINITY;
            double rightBreakPoint = Double.POSITIVE_INFINITY;
            if (!isNullNode(node.prev))
                leftBreakPoint = computeBreakPoint(node.prev.initPoint.getPoint(), node.initPoint.getPoint(), line);
            if (!isNullNode(node.next))
                rightBreakPoint = computeBreakPoint(node.initPoint.getPoint(), node.next.initPoint.getPoint(), line);
            if (point.x < leftBreakPoint)
                node = node.left;
            else if (point.x > rightBreakPoint)
                node = node.right;
            else
                found = true;
        }
        return node;
    }

    void insertBefore(Arc nextNode, Arc node) {
        // Find the right place
        if (isNullNode(nextNode.left)) {
            nextNode.left = node;
            node.parent = nextNode;
        } else {
            nextNode.prev.right = node;
            node.parent = nextNode.prev;
        }
        // Set the pointers
        node.prev = nextNode.prev;
        if (!isNullNode(node.prev))
            node.prev.next = node;
        node.next = nextNode;
        nextNode.prev = node;
        // Balance the tree
        insertFixup(node);
    }

    void insertAfter(Arc prevNode, Arc node)
    {
        // Find the right place
        if (isNullNode(prevNode.right)) {
            prevNode.right = node;
            node.parent = prevNode;
        }
        else {
            prevNode.next.left = node;
            node.parent = prevNode.next;
        }
        // Set the pointers
        node.next = prevNode.next;
        if (!isNullNode(node.next))
            node.next.prev = node;
        node.prev = prevNode;
        prevNode.next = node;
        // Balance the tree
        insertFixup(node);
    }

    void replace(Arc oldNode, Arc newNode) {
        transplant(oldNode, newNode);
        newNode.left = oldNode.left;
        newNode.right = oldNode.right;
        if (!isNullNode(newNode.left))
            newNode.left.parent = newNode;
        if (!isNullNode(newNode.right))
            newNode.right.parent = newNode;
        newNode.prev = oldNode.prev;
        newNode.next = oldNode.next;
        if (!isNullNode(newNode.prev))
            newNode.prev.next = newNode;
        if (!isNullNode(newNode.next))
            newNode.next.prev = newNode;
        newNode.color = oldNode.color;
    }

    void remove(Arc node) {
        Arc nodeCopy = node;
        Arc.Color nodeOriginalColor = nodeCopy.color;
        Arc x;
        if (isNullNode(node.left)) {
            x = node.right;
            transplant(node, node.right);
        } else if (isNullNode(node.right)) {
            x = node.left;
            transplant(node, node.left);
        } else {
            nodeCopy = minimum(node.right);
            nodeOriginalColor = nodeCopy.color;
            x = nodeCopy.right;
            if (nodeCopy.parent == node)
                x.parent = nodeCopy; // Because x could be Nil
            else {
                transplant(nodeCopy, nodeCopy.right);
                nodeCopy.right = node.right;
                nodeCopy.right.parent = nodeCopy;
            }
            transplant(node, nodeCopy);
            nodeCopy.left = node.left;
            nodeCopy.left.parent = nodeCopy;
            nodeCopy.color = node.color;
        }
        if (nodeOriginalColor == Arc.Color.BLACK)
            removeFixup(x);
        // Update next and prev
        if (!isNullNode(node.prev))
            node.prev.next = node.next;
        if (!isNullNode(node.next))
            node.next.prev = node.prev;
    }

    private double computeBreakPoint(Point point1, Point point2, double line) {
        double x1 = point1.x, y1 = point1.y, x2 = point2.x, y2 = point2.y;
        double d1 = 1.0 / (2.0 * (y1 - line));
        double d2 = 1.0 / (2.0 * (y2 - line));
        double a = d1 - d2;
        double b = 2.0 * (x2 * d2 - x1 * d1);
        double c = (y1 * y1 + x1 * x1 - line * line) * d1 - (y2 * y2 + x2 * x2 - line * line) * d2;
        double delta = b * b - 4.0 * a * c;
        return (-b + Math.sqrt(delta)) / (2.0 * a);
    }

    private void insertFixup(Arc node) {
        while (node.parent.color == Arc.Color.RED) {
            if (node.parent == node.parent.parent.left) {
                Arc anotherNode = node.parent.parent.right;
                // Case 1
                if (anotherNode.color == Arc.Color.RED) {
                    node.parent.color = Arc.Color.BLACK;
                    anotherNode.color = Arc.Color.BLACK;
                    node.parent.parent.color = Arc.Color.RED;
                    node = node.parent.parent;
                } else {
                    // Case 2
                    if (node == node.parent.right)  {
                        node = node.parent;
                        leftRotate(node);
                    }
                    // Case 3
                    node.parent.color = Arc.Color.BLACK;
                    node.parent.parent.color = Arc.Color.RED;
                    rightRotate(node.parent.parent);
                }
            } else {
                Arc anotherNode = node.parent.parent.left;
                // Case 1
                if (anotherNode.color == Arc.Color.RED) {
                    node.parent.color = Arc.Color.BLACK;
                    anotherNode.color = Arc.Color.BLACK;
                    node.parent.parent.color = Arc.Color.RED;
                    node = node.parent.parent;
                } else {
                    // Case 2
                    if (node == node.parent.left) {
                        node = node.parent;
                        rightRotate(node);
                    }
                    // Case 3
                    node.parent.color = Arc.Color.BLACK;
                    node.parent.parent.color = Arc.Color.RED;
                    leftRotate(node.parent.parent);
                }
            }
        }
        this.root.color = Arc.Color.BLACK;
    }

    private void removeFixup(Arc node) {
        while (node != this.root && node.color == Arc.Color.BLACK)
        {
            Arc anotherNode;
            if (node == node.parent.left) {
                anotherNode = node.parent.right;
                // Case 1
                if (anotherNode.color == Arc.Color.RED) {
                    anotherNode.color = Arc.Color.BLACK;
                    node.parent.color = Arc.Color.RED;
                    leftRotate(node.parent);
                    anotherNode = node.parent.right;
                }
                // Case 2
                if (anotherNode.left.color == Arc.Color.BLACK && anotherNode.right.color == Arc.Color.BLACK) {
                    anotherNode.color = Arc.Color.RED;
                    node = node.parent;
                } else {
                    // Case 3
                    if (anotherNode.right.color == Arc.Color.BLACK) {
                        anotherNode.left.color = Arc.Color.BLACK;
                        anotherNode.color = Arc.Color.RED;
                        rightRotate(anotherNode);
                        anotherNode = node.parent.right;
                    }
                    // Case 4
                    anotherNode.color = node.parent.color;
                    node.parent.color = Arc.Color.BLACK;
                    anotherNode.right.color = Arc.Color.BLACK;
                    leftRotate(node.parent);
                    node = this.root;
                }
            } else {
                anotherNode = node.parent.left;
                // Case 1
                if (anotherNode.color == Arc.Color.RED) {
                    anotherNode.color = Arc.Color.BLACK;
                    node.parent.color = Arc.Color.RED;
                    rightRotate(node.parent);
                    anotherNode = node.parent.left;
                }
                // Case 2
                if (anotherNode.left.color == Arc.Color.BLACK && anotherNode.right.color == Arc.Color.BLACK) {
                    anotherNode.color = Arc.Color.RED;
                    node = node.parent;
                } else {
                    // Case 3
                    if (anotherNode.left.color == Arc.Color.BLACK) {
                        anotherNode.right.color = Arc.Color.BLACK;
                        anotherNode.color = Arc.Color.RED;
                        leftRotate(anotherNode);
                        anotherNode = node.parent.left;
                    }
                    // Case 4
                    anotherNode.color = node.parent.color;
                    node.parent.color = Arc.Color.BLACK;
                    anotherNode.left.color = Arc.Color.BLACK;
                    rightRotate(node.parent);
                    node = this.root;
                }
            }
        }
        node.color = Arc.Color.BLACK;
    }

    private void leftRotate(Arc node) {
        Arc rightNode = node.right;
        node.right = rightNode.left;
        if (!isNullNode(rightNode.left))
            rightNode.left.parent = node;
        rightNode.parent = node.parent;
        if (isNullNode(node.parent))
            this.root = rightNode;
        else if (node.parent.left == node)
            node.parent.left = rightNode;
        else
            node.parent.right = rightNode;
        rightNode.left = node;
        node.parent = rightNode;
    }

    private void rightRotate(Arc node) {
        Arc leftNode = node.left;
        node.left = leftNode.right;
        if (!isNullNode(leftNode.right))
            leftNode.right.parent = node;
        leftNode.parent = node.parent;
        if (isNullNode(node.parent))
            this.root = leftNode;
        else if (node.parent.left == node)
            node.parent.left = leftNode;
        else
            node.parent.right = leftNode;
        leftNode.right = node;
        node.parent = leftNode;
    }

    private void transplant(Arc oldNode, Arc newNode) {
        if (isNullNode(oldNode.parent))
            this.root = newNode;
        else if (oldNode == oldNode.parent.left)
            oldNode.parent.left = newNode;
        else
            oldNode.parent.right = newNode;
        newNode.parent = oldNode.parent;
    }

    private Arc minimum(Arc node) {
        while (!isNullNode(node.left))
            node = node.left;
        return node;
    }
}
