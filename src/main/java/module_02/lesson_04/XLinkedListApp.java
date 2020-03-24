package module_02.lesson_04;

public class XLinkedListApp {
  public static void main(String[] args) {
    XLinkedList xl = new XLinkedList();
    xl.prepend(1); // LL: 1
    xl.prepend(2); // LL: 2,1
    xl.prepend3(3); // LL: 3,2,1
    xl.append(10);   // LL: 3,2,1,10
    xl.append(20);   // LL: 3,2,1,10,20
    System.out.println(xl.represent());
    System.out.println(xl.represent2());
    System.out.println(xl.represent3r());
  }
}
