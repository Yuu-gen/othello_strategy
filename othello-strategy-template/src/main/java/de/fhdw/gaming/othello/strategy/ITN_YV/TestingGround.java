package de.fhdw.gaming.othello.strategy.ITN_YV;

import java.util.List;

public class TestingGround {

    public static void main(final String[] args) {
        final Node<String> root = testgen();
        final List<List<Node<String>>> lNodes = root.getOrganizedLowestLayer();

        for (int i = 0; i < lNodes.size(); i++) {
            for (int j = 0; j < lNodes.get(i).size(); j++) {
                lNodes.get(i).get(j).deleteNode();
            }
        }
//        for (int i = 0; i < lNodes.size(); i++) {
//            for (int j = 0; j < lNodes.get(i).size(); j++) {
//                lNodes.get(i).get(j).deleteNode();
//            }
//        }
        System.out.println(lNodes);
        // System.out.println(testgen().toString());d

    }

    public static Node<String> testgen() {
        final Node<String> root = new Node<>("rootnode");
        final Node<String> childnode1 = root.addChild(new Node<>("childnode1"));
        childnode1.addChild(new Node<>("subchildnode1 1"));
        childnode1.addChild(new Node<>("subchildnode1 2"));
        childnode1.addChild(new Node<>("subchildnode1 3"));
        final Node<String> childnode2 = root.addChild(new Node<>("childnode2"));
        childnode2.addChild(new Node<>("subchildnode2 1"));
        final Node<String> childnode3 = root.addChild(new Node<>("childnode3"));
        childnode3.addChild(new Node<>("subchildnode3 1"));
        childnode3.addChild(new Node<>("subchildnode3 2"));
        return root;
    }

}
