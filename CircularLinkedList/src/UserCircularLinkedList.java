import java.util.List;

public class UserCircularLinkedList extends CircularLinkedList {

  public UserCircularLinkedList(Object o) {
    super(o);
  }

  public UserCircularLinkedList(List list) {
    super(list);
  }

  public void removeByName(String name) {
    Node<User> currNode = tail.next;
    Node<User> prevNode;

    for (int i = 0; i < size; i++) {
      prevNode = currNode;
      currNode = currNode.next;

      if (currNode.data.getName().equals(name)) {
        prevNode.next = currNode.next;
        size--;
        return;
      }
    }
  }
}
