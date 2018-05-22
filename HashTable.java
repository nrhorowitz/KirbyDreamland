import java.io.Serializable;
public class HashTable<E> implements Serializable {
   private DLList<E>[] table;
   @SuppressWarnings("unchecked")
   public HashTable(int size) {
      table = new DLList[size];
      for(int i=0; i<table.length; i++) {
         table[i] = new DLList<E>();
      }
   }
   public void add(E myData) {
      table[myData.hashCode()].add(myData);
   }
   public void addToFront(E myData) {
      table[myData.hashCode()].addToFront(myData);
   }
   public void remove(E myData) {
      table[myData.hashCode()].remove(myData);
   }
   public E pop(int index) {
      E temp = get(index);
      remove(temp);
      return temp;
   }
   public DLList<E> getList(int index) {
      return table[index];
   }
   public E get(int index) {
      if((index >= 0) && (index < table.length)) {
         return table[index].get(0);
      }
	   return null;
   }
   public E get(int index, int listIndex) {
      return table[index].get(listIndex);
   }
   public int size() {
	   int size = 0;
	   for(int i=0; i<table.length; i++) {
		   if(table[i].size() > 0) {
			   size++;
		   }
	   }
	   return size;
   }
   public int rawSize() {
      return table.length;
   }
   public String toString() {
      String total = "";
      for(int i=0; i<table.length; i++) {
         if(table[i].size() > 0) {
            total += "\nbucket " + i;
            for(int j=0; j<table[i].size(); j++) {
               total += " " + table[i].get(j);
            }
         }
      }
      return total;
   }
}