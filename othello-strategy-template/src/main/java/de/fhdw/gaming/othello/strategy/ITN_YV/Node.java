package de.fhdw.gaming.othello.strategy.ITN_YV;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents Nodes organized in a Tree structure.
 *
 * @author outeh
 *
 * @param <DATATYPE> some kind of data a Node holds
 */
public class Node<DATATYPE> {
    /**
     * The place to hold the nodes data.
     */
    private DATATYPE data = null;

    /**
     * The place to store a nodes children.
     */
    private final List<Node<DATATYPE>> children = new ArrayList<>();

    /**
     * The place to store the parent of a Node.
     */
    private Node<DATATYPE> parent = null;

    /**
     * The Constructor for Node.
     *
     * @param data data to store in this first node
     */
    public Node(final DATATYPE data) {
        this.data = data;
    }

    /**
     * Adds a single child to the node.
     *
     * @param child child to add
     * @return @TODO
     */
    public Node<DATATYPE> addChild(final Node<DATATYPE> child) {
        child.setParent(this);
        this.children.add(child);
        return child;
    }

    /**
     * Adds multiple children to the node.
     *
     * @param children
     */
    public void addChildren(final List<Node<DATATYPE>> children) {
        children.parallelStream().forEach(each -> each.setParent(this));
        this.children.addAll(children);
    }

    /**
     * Returns the direct children of the node.
     *
     * @return
     */
    public List<Node<DATATYPE>> getChildren() {
        return this.children;
    }

    /**
     * Returns the Lowest Layer of the Tree (the nodes without further children) in a flat List.(recursiv)
     *
     * @return
     */
    public List<Node<DATATYPE>> getLowestLayerrec() {
        final List<Node<DATATYPE>> outNodes = new ArrayList<>();
        final List<Node<DATATYPE>> children = this.getChildren();

        if (children.isEmpty()) {

            outNodes.add(this);
            return outNodes;
        } else {
            for (final Node<DATATYPE> currNode : children) {
                outNodes.addAll(currNode.getLowestLayer());
            }
            return outNodes;
        }
    }

    /**
     * Returns the Lowest Layer of the Tree (the nodes without further children) in a flat List.
     *
     * @return
     */
    public List<Node<DATATYPE>> getLowestLayer() {
        final List<Node<DATATYPE>> outNodes = new ArrayList<>();
        List<Node<DATATYPE>> currentLayer = new ArrayList<>();
        List<Node<DATATYPE>> nextLayer = new ArrayList<>();
        currentLayer = this.getChildren();
        while (true) {
            for (final Node<DATATYPE> node : currentLayer) {
                nextLayer.addAll(node.getChildren());
                if (nextLayer.isEmpty()) {
                    return currentLayer;
                }
            }
            currentLayer = nextLayer;
            nextLayer = new ArrayList<>();
        }
    }

    /**
     * Returns the Lowest Layer of the Tree in Lists separated by parents within a List.
     *
     * @Requires balanced Tree!!!
     *
     * @return
     */
    public List<List<Node<DATATYPE>>> getOrganizedLowestLayer() {
        final List<Node<DATATYPE>> parents = new ArrayList<>();
        final List<Node<DATATYPE>> flatLowestLayer = this.getLowestLayer();
        List<Node<DATATYPE>> group = new ArrayList<>();
        for (final Node<DATATYPE> node : flatLowestLayer) {
            if (!parents.contains(node.getParent())) {
                parents.add(node.getParent());
            }
        }
        final List<List<Node<DATATYPE>>> outNodes = new ArrayList<>();
        for (final Node<DATATYPE> parent : parents) {
            if (parent != null) {
//                outNodes.add(parent.getChildren());
                for (final Node<DATATYPE> node : parent.getChildren()) {
                    if (flatLowestLayer.contains(node)) {
                        group.add(node);
                    }
                }
                outNodes.add(group);
                group = new ArrayList<>();
            }
        }
        // outNodes.add(parents);
        return outNodes;
    }

    public void deleteNode() {
        if (this.parent != null) {
            final int index = this.parent.getChildren().indexOf(this);
            this.parent.getChildren().remove(this);
            this.getChildren().parallelStream().forEach(each -> each.setParent(this.parent));
            this.parent.getChildren().addAll(index, this.getChildren());
        } else {
            this.deleteRootNode();
        }
        this.getChildren().clear();
    }

    public Node<DATATYPE> deleteRootNode() {
        if (this.parent != null) {
            throw new IllegalStateException("deleteRootNode not called on root");
        }
        Node<DATATYPE> newParent = null;
        if (!this.getChildren().isEmpty()) {
            newParent = this.getChildren().get(0);
            newParent.setParent(null);
            this.getChildren().remove(0);
            for (final Node<DATATYPE> each : this.getChildren()) {
                each.setParent(newParent);
            }
            newParent.getChildren().addAll(this.getChildren());
        }
        this.getChildren().clear();
        return newParent;
    }

    /**
     * Returns the data stored in the node.
     *
     * @return
     */
    public DATATYPE getData() {
        return this.data;
    }

    /**
     * Sets the Data stored in the node.
     *
     * @param data
     */
    public void setData(final DATATYPE data) {
        this.data = data;
    }

    /**
     * Sets the parent of the node.
     *
     * @param parent
     */
    private void setParent(final Node<DATATYPE> parent) {
        this.parent = parent;
    }

    /**
     * returns the parent of the node.
     *
     * @return
     */
    public Node<DATATYPE> getParent() {
        return this.parent;
    }

    @Override
    public String toString() {
        return this.getData().toString();
    }

}
