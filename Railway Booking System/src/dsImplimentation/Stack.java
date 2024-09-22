package dsImplimentation;

public class Stack<T> {
    private T[] elements;
    private int top;

    @SuppressWarnings("unchecked")
    public Stack(int capacity) {
        elements = (T[]) new Object[capacity];
        top = -1;
    }

    public void push(T element) {
        if (top >= elements.length - 1) {
            System.out.println("Stack overflow.");
        } else {
            elements[++top] = element;
        }
    }

    public T pop() {
        if (top == -1) {
            System.out.println("Stack underflow.");
            return null;
        } else {
            T element = elements[top];
            top--;
            return element;
        }
    }

    public T peek() {
        if (top == -1) {
            System.out.println("Stack is empty.");
            return null;
        } else {
            return elements[top];
        }
    }

    public boolean isEmpty() {
        return top == -1;
    }
}
