import java.util.ArrayList;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

  public static void main(String[] args) {
//    CircularLinkedList list = new CircularLinkedList(new User("1",1));
//    list.
//    list.add(new User("2",2));
//    list.add(new User("3",3));
//    list.add(new User("4",4));

    ArrayList<User> users = new ArrayList<>();
    users.add(new User("5",5));
    users.add(new User("6",6));
    users.add(new User("7",7));
    users.add(new User("8",8));

    UserCircularLinkedList circularLinkedList = new UserCircularLinkedList(users);

//    for (int i = 0; i < 4; i++) {
//      System.out.println(list.getNode(i).data + " " + list.getNode(i).next.data);
//    }

    for (int i = 0; i < 4; i++) {
      System.out.println(circularLinkedList.getNode(i).data + " " + circularLinkedList.getNode(i).next.data);
    }
    System.out.println(circularLinkedList.getNode(1).data);
    System.out.println(circularLinkedList.size());

//    circularLinkedList.removeData(users.get(1));
    circularLinkedList.removeByName("7");
    System.out.println();

    for (int i = 0; i < circularLinkedList.size(); i++) {
      System.out.println(circularLinkedList.getNode(i).data + " " + circularLinkedList.getNode(i).next.data);
    }
    System.out.println(circularLinkedList.getNode(1).data);
    System.out.println(circularLinkedList.size());

  }
}