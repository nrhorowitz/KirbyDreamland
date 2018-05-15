import java.io.Serializable;
public class Node<E> implements Serializable {
  private E data;
  private Node<E> next;
  private Node<E> prev;
  
  public Node(E myData) {
    data = myData;
    next = null;
    prev = null;
  }
  
  public E get() {
   return data;
  }
  public Node<E> next() {
    return next;
  }
  public Node<E> prev() {
    return prev;
  }
  public void setNext(Node<E> myNext) {
    next = myNext;
  }
  public void setPrev(Node<E> myPrev) {
    prev = myPrev;
  }
  public void setData(E newData) {
    data = newData;
  }
}