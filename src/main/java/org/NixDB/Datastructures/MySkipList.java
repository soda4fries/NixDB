package org.NixDB.Datastructures;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MySkipList<V extends Comparable<V>> {

    private final Node<V> sentinel;

    private final int height;


    private final HeightStrategy heightStrategy;

    private int size;

    private static final int DEFAULT_CAPACITY = 100;

    public MySkipList() {
        this(DEFAULT_CAPACITY, new BernoulliHeightStrategy());
    }

    public MySkipList(int expectedCapacity, HeightStrategy heightStrategy) {
        this.heightStrategy = heightStrategy;
        this.height = heightStrategy.height(expectedCapacity);
        this.sentinel = new Node<>(null, this.height);
        this.size = 0;
    }

    public void add(V e) {
        Objects.requireNonNull(e);
        Node<V> current = sentinel;
        int layer = height;
        Node<V>[] toFix = new Node[height + 1];

        while (layer >= 0) {
            Node<V> next = current.next(layer);
            if (next == null || next.getValue().compareTo(e) > 0) {
                toFix[layer] = current;
                layer--;
            } else {
                current = next;
            }
        }
        int nodeHeight = heightStrategy.nodeHeight(height);
        Node<V> node = new Node<>(e, nodeHeight);
        for (int i = 0; i <= nodeHeight; i++) {
            if (toFix[i].next(i) != null) {
                node.setNext(i, toFix[i].next(i));
                toFix[i].next(i).setPrevious(i, node);
            }

            toFix[i].setNext(i, node);
            node.setPrevious(i, toFix[i]);
        }
        size++;
    }

    public V get(int index) {
        int counter = -1; // head index
        Node<V> current = sentinel;
        while (counter != index) {
            current = current.next(0);
            counter++;
        }
        return current.value;
    }

    public void remove(V e) {
        Objects.requireNonNull(e);
        Node<V> current = sentinel;
        int layer = height;

        while (layer >= 0) {
            Node<V> next = current.next(layer);
            if (e.equals(current.getValue())) {
                break;
            } else if (next == null || next.getValue().compareTo(e) > 0) {
                layer--;
            } else {
                current = next;
            }
        }
        for (int i = 0; i <= layer; i++) {
            current.previous(i).setNext(i, current.next(i));
            if (current.next(i) != null) {
                current.next(i).setPrevious(i, current.previous(i));
            }
        }
        size--;
    }

    public boolean contains(V e) {
        Objects.requireNonNull(e);
        Node<V> current = sentinel;
        int layer = height;

        while (layer >= 0) {
            Node<V> next = current.next(layer);
            if (e.equals(current.getValue())) {
                return true;
            } else if (next == null || next.getValue().compareTo(e) > 0) {
                layer--;
            } else {
                current = next;
            }
        }
        return false;
    }

    public int size() {
        return size;
    }
    @Override
    public String toString() {
        List<boolean[]> layers = new ArrayList<>();
        int sizeWithHeader = size + 1;
        for (int i = 0; i <= height; i++) {
            layers.add(new boolean[sizeWithHeader]);
        }

        Node<V> current = sentinel;
        int position = 0;
        while (current != null) {
            for (int i = 0; i <= current.height; i++) {
                layers.get(i)[position] = true;
            }
            current = current.next(0);
            position++;
        }

        Collections.reverse(layers);
        String result = layers.stream()
                .map(layer -> {
                    StringBuilder acc = new StringBuilder();
                    for (boolean b : layer) {
                        if (b) {
                            acc.append("[ ]");
                        } else {
                            acc.append("---");
                        }
                        acc.append(" ");
                    }
                    return acc.toString();
                })
                .collect(Collectors.joining("\n"));
        String positions = IntStream.range(0, sizeWithHeader - 1).mapToObj(i -> String.format("%3d", i)).collect(Collectors.joining(" "));

        return result + String.format("%n H %s%n", positions);
    }

    private static class Node<E> {

        private final E value;
        private final int height;
        private final List<Node<E>> forward;
        private final List<Node<E>> backward;

        @SuppressWarnings("unchecked")
        public Node(E value, int height) {
            this.value = value;
            this.height = height;

            // predefined size lists with null values in every cell
            this.forward = Arrays.asList(new Node[height + 1]);
            this.backward = Arrays.asList(new Node[height + 1]);
        }

        public Node<E> next(int layer) {
            checkLayer(layer);
            return forward.get(layer);
        }

        public void setNext(int layer, Node<E> node) {
            forward.set(layer, node);
        }

        public void setPrevious(int layer, Node<E> node) {
            backward.set(layer, node);
        }

        public Node<E> previous(int layer) {
            checkLayer(layer);
            return backward.get(layer);
        }

        public E getValue() {
            return value;
        }

        private void checkLayer(int layer) {
            if (layer < 0 || layer > height) {
                throw new IllegalArgumentException();
            }
        }
    }


    public interface HeightStrategy {
        int height(int expectedSize);
        int nodeHeight(int heightCap);
    }


    public static class BernoulliHeightStrategy implements HeightStrategy {

        private final double probability;

        private static final double DEFAULT_PROBABILITY = 0.5;
        private static final Random RANDOM = new Random();

        public BernoulliHeightStrategy() {
            this.probability = DEFAULT_PROBABILITY;
        }

        public BernoulliHeightStrategy(double probability) {
            if (probability <= 0 || probability >= 1) {
                throw new IllegalArgumentException("Probability should be from 0 to 1. But was: " + probability);
            }
            this.probability = probability;
        }

        @Override
        public int height(int expectedSize) {
            long height = Math.round(Math.log10(expectedSize) / Math.log10(1 / probability));
            if (height > Integer.MAX_VALUE) {
                throw new IllegalArgumentException();
            }
            return (int) height;
        }

        @Override
        public int nodeHeight(int heightCap) {
            int level = 0;
            double border = 100 * (1 - probability);
            while (((RANDOM.nextInt(Integer.MAX_VALUE) % 100) + 1) > border) {
                if (level + 1 >= heightCap) {
                    return level;
                }
                level++;
            }
            return level;
        }
    }
}
