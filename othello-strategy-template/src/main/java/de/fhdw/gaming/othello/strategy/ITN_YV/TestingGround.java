package de.fhdw.gaming.othello.strategy.ITN_YV;

import java.util.List;
import java.util.ListIterator;

public class TestingGround {

    public static void main(final String[] args) {
        final Node<String> root = testgen();
        final List<List<Node<String>>> lNodes = root.getOrganizedLowestLayer();
        final ListIterator i = lNodes.listIterator();
        while (i.hasNext()) {
            final List<Node<String>> currentList = (List<Node<String>>) i.next();
            final ListIterator j = currentList.listIterator();
            while (j.hasNext()) {
                final Node<String> currentNode = (Node<String>) j.next();
                currentNode.deleteNode();
            }
        }

//        for (int i = 0; i < lNodes.size(); i++) {
//            for (int j = 0; j < lNodes.get(i).size(); j++) {
//                lNodes.get(i).get(j).deleteNode();
//            }
//        }
//        for (int i = 0; i < lNodes.size(); i++) {
//            for (int j = 0; j < lNodes.get(i).size(); j++) {
//                lNodes.get(i).get(j).deleteNode();
//            }
//        }
        System.out.println(root);
        // System.out.println(testgen().toString());

    }

    public static Node<String> testgen() {
        final Node<String> root = new Node<>("rootnode");
        final Node<String> childnode1 = root.addChild(new Node<>("childnode1"));
        childnode1.addChild(new Node<>("subchildnode1"));
        final Node<String> childnode2 = root.addChild(new Node<>("childnode2"));
        childnode2.addChild(new Node<>("subchildnode2"));
        final Node<String> childnode3 = root.addChild(new Node<>("childnode3"));
        childnode3.addChild(new Node<>("subchildnode3"));
        return root;
    }

}
