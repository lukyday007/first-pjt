package com.boricori.util;

import java.util.List;

public abstract class CircularLinkedList<E> {

  public Node<E> tail;
  public int size;

  public CircularLinkedList(E e) {
    size = 1;
    tail = new Node(e);
    tail.next = tail;
  }

  public CircularLinkedList(List<E> list) {
    list.forEach(e -> {
      if (isEmpty()) {
        initCircularLinkedList(e);
      } else {
        this.add(e);
      }
    });
  }

  public void initCircularLinkedList(E e) {
    size = 1;
    tail = new Node(e);
    tail.next = tail;
  }

  public Node<E> getNode(int i) {
    if (i > size || i < -1) {
      throw new IllegalArgumentException();
    }
    Node<E> node = tail.next;
    for (int j = 0; j <= i; j++) {
      node = node.next;
    }
    return node;
  }

  public void add(Object x) {
    Node<E> prevNode = tail;
    Node<E> newNode = new Node(x, prevNode.next);
    tail.next = newNode;
    tail = newNode;
    size++;
  }

  public void remove(int i) {
    if (i > size - 1 || i < 0) {
      throw new IllegalArgumentException();
    }
    Node<E> prevNode = getNode(i - 1);
    prevNode.next = getNode(i + 1);
    size--;
  }

  public void removeData(E e) {
    Node<E> currNode = tail.next;
    Node<E> prevNode;

    for (int i = 0; i < size; i++) {
      prevNode = currNode;
      currNode = currNode.next;

      if (currNode.data.equals(e)) {
        prevNode.next = currNode.next;
        size--;
        return;
      }
    }
  }

  public int size() {
    return size;
  }

  public boolean isEmpty() {
    return size == 0;
  }

}
