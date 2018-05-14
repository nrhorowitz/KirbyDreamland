public class DLList<E> {
  private Node<E> head;
  private Node<E> tail;
  private int size;
  
  public DLList() {
    head = new Node(null);
    tail = new Node(null);
    head.setNext(tail);
    tail.setPrev(head);
    size = 0;
  }
  
  public void add(E myData) {
    Node<E> addNode = new Node(myData);
    tail.prev().setNext(addNode);
    addNode.setPrev(tail.prev());
    addNode.setNext(tail);
    tail.setPrev(addNode);
    size++;
  }
  public E get(int index) {
    Node<E> current = head.next();
    for(int i=0; i<index; i++) {
      current = current.next();
    }
    return current.get();
  }
  public Node<E> getNode(int index) {
     Node<E> current = head.next();
     for(int i=0; i<index; i++) {
       current = current.next();
     }
     return current;
   }
  public void setNode(int index, Node<E> myNode) {
     Node<E> current = head.next();
     for(int i=0; i<index; i++) {
       current = current.next();
     }
     current.prev().setNext(myNode);
     current.next().setPrev(myNode);
     myNode.setPrev(current.prev());
     myNode.setNext(current.next());
   }
  public void set(int index, E myData) {
     Node<E> current = head.next();
     for(int i=0; i<index; i++) {
       current = current.next();
     }
     current.setData(myData);
   }
  public void remove(E myData) {
     Node<E> current = head.next();
     while(current.next() != null) {
        if(current.get().equals(myData)) {
           current.prev().setNext(current.next());
           current.next().setPrev(current.prev());
           size--;
        }
        current = current.next();
     }
  }
  public String toString() {
    String total = "";
    Node<E> current = head.next();
    for(int i=0; i<size; i++) {
      total += current.get().toString() + "\n";
      current = current.next();
    }
    return total;
  }
  public int size() {
    return size;
  }
  
}