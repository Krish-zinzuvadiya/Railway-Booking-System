package dsImplimentation;

import booking.*;

public class Queue {
    private Node front;
    private Node rear;
    private int size;

    public Queue() {
        front = null;
        rear = null;
        size = 0;
    }

    private class Node {
        User data;
        Node next;

        public Node(User data) {
            this.data = data;
            this.next = null;
        }
    }

    public void enqueue(User user) {
        Node node = new Node(user);
        if (rear == null) {
            front = node;
            rear = node;
        } else {
            rear.next = node;
            rear = node;
        }
        size++;
    }

    public User dequeue() {
        if (front == null) {
            return null;
        }
        User data = front.data;
        front = front.next;
        if (front == null) {
            rear = null;
        }
        size--;
        return data;
    }

    public User peek() {
        if (front == null) {
            return null;
        }
        return front.data;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

}
