package org.NixDB.Datastructures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Iterable<E> {
    Node<E> head;

    @Override
    public Iterator<E> iterator() {
        return new MyLinkedListIterator();
    }

    public void clear() {
        Node<E> current = head.next;
        while (current != null) {
            Node<E> next = current.next;
            current.next = null;
            current = next;
        }
        head.next = null;
    }



    static class Node<E> {
        E item;
        Node<E> next;

        Node(E item) {
            this.item = item;
            this.next = null;
        }

        Node(E item, Node next) {
            this.item = item;
            this.next = next;
        }
    }



    public void add(E item) {
        if (head==null) {
            head = new Node<>(item);
        } else {
            Node newNode = new Node(item, head);
            head = newNode;
        }
    }

    public E get(E item) {
        Node<E> current = head;
        while (!current.item.equals(item) && current.next != null) {
            current = current.next;
        }
        if (current.next == null) throw new NoSuchElementException();
        return current.item;
    }

    public E remove(E item) {
        if (head == null) {
            throw new NoSuchElementException();
        }

        if (head.item.equals(item)) {
            E itemCopy = head.item;
            head = head.next;
            return itemCopy;
        }

        Node<E> current = head;
        while (current.next != null && !current.next.item.equals(item)) {
            current = current.next;
        }

        if (current.next == null) {
            throw new NoSuchElementException();
        }

        E itemCopy = current.next.item;
        current.next = current.next.next;
        return itemCopy;
    }


    private class MyLinkedListIterator implements Iterator<E> {
        Node<E> current;


        MyLinkedListIterator() {
            current = head;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public E next() {
            E itemCopy = current.item;
            current = current.next;
            return itemCopy;
        }
    }


}
