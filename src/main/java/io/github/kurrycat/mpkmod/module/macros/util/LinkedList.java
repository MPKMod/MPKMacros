package io.github.kurrycat.mpkmod.module.macros.util;

import java.util.Iterator;

public class LinkedList<T> implements Iterable<T> {
    protected int size = 0;
    public Node first;
    public Node last;

    public Itr iterator() {
        return new Itr();
    }

    public NodeItr nodeIterator() {
        return new NodeItr();
    }

    public class Itr implements Iterator<T> {
        protected Node curr = first;

        @Override
        public boolean hasNext() {
            return curr != null;
        }

        @Override
        public T next() {
            T n = curr.item;
            curr = curr.next;
            return n;
        }
    }

    public class NodeItr implements Iterator<Node> {
        protected Node curr = first;

        @Override
        public boolean hasNext() {
            return curr != null;
        }

        @Override
        public Node next() {
            Node n = curr;
            curr = curr.next;
            return n;
        }
    }

    public int getSize() {
        return size;
    }

    public T getFirst() {
        return first == null ? null : first.item;
    }

    public T getLast() {
        return last == null ? null : last.item;
    }

    public void clear() {
        first = null;
        last = null;
        size = 0;
    }

    public Node addFirst(T ele) {
        if (first == null) {
            first = new Node(null, ele, null);
            last = first;
            size = 1;
            return first;
        } else {
            return first.addBefore(ele);
        }
    }

    public Node addLast(T ele) {
        if (last == null) {
            last = new Node(null, ele, null);
            first = last;
            size = 1;
            return last;
        } else {
            return last.addAfter(ele);
        }
    }

    public class Node {
        public T item;
        public Node next;
        public Node prev;

        public Node(Node prev, T element, Node next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }

        public Node addBefore(T ele) {
            Node p = prev;
            Node c = new Node(p, ele, this);
            prev = c;
            if (p != null) p.next = c;
            else first = c;
            size++;
            return c;
        }

        public Node addAfter(T ele) {
            Node n = next;
            Node c = new Node(this, ele, n);
            next = c;
            if (n != null) n.prev = c;
            else last = c;
            size++;
            return c;
        }

        public T remove() {
            if (prev != null) prev.next = next;
            else first = next;
            if (next != null) next.prev = prev;
            else last = prev;
            size--;
            return item;
        }
    }
}
