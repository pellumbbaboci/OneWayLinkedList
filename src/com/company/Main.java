package com.company;

import java.util.*;
interface IList<E> extends Iterable<E> {
    boolean add(E e); // qdd element to the end of list

    void add(int index, E element) throws NoSuchElementException; // add element on position index

    void clear(); // delete all elements

    boolean contains(E element); // is list containing an element (equals())

    E get(int index) throws NoSuchElementException; //get element from position

    E set(int index, E element) throws NoSuchElementException; // set new value on position

    int indexOf(E element); // where is element (equals())

    boolean isEmpty();

    Iterator<E> iterator();

    ListIterator<E> listIterator() throws UnsupportedOperationException; // for ListIterator

    E remove(int index) throws NoSuchElementException; // remove element from position index

    boolean remove(E e); // remove element

    int size();
}

class OneWayLinkedList<E> implements IList<E> {
    int size = 0;

    // Node
    private class Element {
        public Element(E e) {
            this.object = e;
        }

        E object;
        Element next = null;
    }

    Element sentinel;

    private class InnerIterator implements Iterator<E> {
        Element actElement;

        public InnerIterator() {
            actElement = sentinel.next;
        }

        @Override
        public boolean hasNext() {
            return actElement != null;
        }

        @Override
        public E next() {
            E value = actElement.object;
            actElement = actElement.next;
            return value;
        }
    }

    public OneWayLinkedList() {
        sentinel = new Element((E) "abc");
    }

    @Override
    public Iterator<E> iterator() {
        return new InnerIterator();
    }

    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(E e) {
        Element newElem = new Element(e);
        Element tail = sentinel;
        while (tail.next != null)
            tail = tail.next;
        tail.next = newElem;
        size = size + 1;
        return true;
    }

    @Override
    public void add(int index, E element) throws NoSuchElementException {
        set(index, element);
    }

    @Override
    public void clear() {
        Element e = sentinel;
        while (iterator().hasNext()) {
            e.next = null;
        }
    }

    @Override
    public boolean contains(E element) {
        while (iterator().hasNext()) {
            if (iterator().next() == element) {
                return true;
            }
        }
        return false;
    }

    @Override
    public E get(int index) throws NoSuchElementException {
        Element e = sentinel.next;
        if (e == null) {
            return null;
        }
        for (int i = 0; i < index; i++) {
            e = e.next;
        }
        return e.object;
    }

    @Override
    public E set(int index, E element) throws NoSuchElementException {
        Element newElem = new Element(element);
        Element e = sentinel.next;
        for (int i = 0; i < index; i++) {
            e = e.next;
        }
        e.next = newElem;
        newElem.next = e.next.next;
        return newElem.object;
    }

    @Override
    public int indexOf(E element) {

        int pos = 0;
        while(iterator().hasNext()){
            if(iterator().next().equals(element)) {
                return pos;
            }
            pos = pos + 1;
        }
        return -1;
    }

    @Override
    public boolean isEmpty() {
        if (sentinel.next == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public E remove(int index) throws NoSuchElementException {
        Element e = sentinel;
        Element deleted = null;
        Element prev = sentinel;
        if (isEmpty()) {
            return null;
        }
        for (int i = 0; i < index; i++) {
            prev = prev.next;
            e = e.next;
        }
        prev.next = e.next;
        deleted = e;
        e = null;
        return deleted.object;
    }

    @Override
    public boolean remove(E e) {
        int pos = 0;
        E deletedElm = null;
        while (iterator().hasNext()) {
            pos++;
            if (iterator().next() == e) {
                deletedElm = remove(pos);
                break;
            }
        }
        if (deletedElm == e) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int size() {
        int size = 0;
        Element e = sentinel.next;
        while (e != null) {
            size++;
            e = e.next;
        }
        return size;
    }
}


class Link {
    public String ref;

    public Link(String ref) {
        this.ref = ref;
    }

    public String getRef() {
        return ref;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Link))
            return false;
        Link temp = (Link) obj;
        return ref.equals(temp.ref);
    }

    public boolean equals(Link link) {
        return ref.equals(link.ref);
    }
    // in the future there will be more fields
}

class Document {
    public String name;
    public OneWayLinkedList<Link> links;

    public Document(String name, Scanner scan) {
        this.name = name;
        links = new OneWayLinkedList<Link>();

        load(scan);
    }

    public void load(Scanner scan) {
        String input;
        while (!(input = scan.nextLine()).equalsIgnoreCase("eod")) {
            String[] arr = input.split("\\s+");
            for (String word : arr) {
                if (correctLink(word)) {
                    Link temp = new Link(word.substring(5).toLowerCase());
                    links.add(temp);
                }
            }
        }
    }

    // accepted only small letters, capitalic letter, digits nad '_' (but not on the begin)
    private static boolean correctLink(String link) {
        return link.toLowerCase().matches("link=[A-Za-z][0-9A-Za-z_]*$");
    }

    @Override
    public String toString() {
        StringBuffer strBuffer = new StringBuffer();
        Iterator<Link> it = links.iterator();
        strBuffer.append("Document: " + name);
        while (it.hasNext()) {
            strBuffer.append("\n" + it.next().getRef());
        }
        return strBuffer.toString();
    }

}

public class Main {

    static Scanner scan; // for input stream

    public static void main(String[] args) {


        System.out.println("START");
        scan = new Scanner(System.in);
        Document[] doc = null;
        int currentDocNo = 0;
        int maxNo = -1;
        boolean halt = false;
        while (!halt) {
            String line = scan.nextLine();
            // empty line and comment line - read next line
            if (line.length() == 0 || line.charAt(0) == '#')
                continue;
            // copy line to output (it is easier to find a place of a mistake)
            System.out.println("!" + line);
            String word[] = line.split(" ");
            // go n - start with array of the length n
            if (word[0].equalsIgnoreCase("go") && word.length == 2) {
                maxNo = Integer.parseInt(word[1]);
                doc = new Document[maxNo];
                continue;
            }
            //ch - change index
            if (word[0].equalsIgnoreCase("ch") && word.length == 2) {
                currentDocNo = Integer.parseInt(word[1]);
                continue;
            }
            // ld documentName
            if (word[0].equalsIgnoreCase("ld") && word.length == 2) {
                doc[currentDocNo] = new Document(word[1], scan);
                continue;
            }
            // ha
            if (word[0].equalsIgnoreCase("ha") && word.length == 1) {
                halt = true;
                continue;
            }
            // clear
            if (word[0].equalsIgnoreCase("clear") && word.length == 1) {
                doc[currentDocNo].links.clear();
                continue;
            }
            // show
            if (word[0].equalsIgnoreCase("show") && word.length == 1) {
                System.out.println(doc[currentDocNo].toString());
                continue;
            }
            // size
            if (word[0].equalsIgnoreCase("size") && word.length == 1) {
                System.out.println(doc[currentDocNo].links.size());
                continue;
            }
            // add str
            if (word[0].equalsIgnoreCase("add") && word.length == 2) {
                System.out.println(doc[currentDocNo].links.add(new Link(word[1])));
                continue;
            }
            // addi index str
            if (word[0].equalsIgnoreCase("addi") && word.length == 3) {
                int index = Integer.parseInt(word[1]);
                try {
                    doc[currentDocNo].links.add(index, new Link(word[2]));
                } catch (NoSuchElementException e) {
                    System.out.println("error");
                }
                continue;
            }
            // get index
            if (word[0].equalsIgnoreCase("get") && word.length == 2) {
                int index = Integer.parseInt(word[1]);
                try {
                    Link l = doc[currentDocNo].links.get(index);
                    System.out.println(l.ref);
                } catch (NoSuchElementException e) {
                    System.out.println("error");
                }
                continue;
            }
            // set index str
            if (word[0].equalsIgnoreCase("set") && word.length == 3) {
                int index = Integer.parseInt(word[1]);
                try {
                    Link l = doc[currentDocNo].links.set(index, new Link(word[2]));
                    System.out.println(l.ref);
                } catch (NoSuchElementException e) {
                    System.out.println("error");
                }

                continue;
            }
            // index str
            if (word[0].equalsIgnoreCase("index") && word.length == 2) {
                int index = doc[currentDocNo].links.indexOf(new Link(word[1]));
                System.out.println(index);
                continue;
            }
            // remi index
            if (word[0].equalsIgnoreCase("remi") && word.length == 2) {
                int index = Integer.parseInt(word[1]);
                try {
                    Link l = doc[currentDocNo].links.remove(index);
                    System.out.println(l.ref);
                } catch (NoSuchElementException e) {
                    System.out.println("error");
                }
                continue;
            }
            // rem str
            if (word[0].equalsIgnoreCase("rem") && word.length == 2) {
                System.out.println(doc[currentDocNo].links.remove(new Link(word[1])));
                continue;
            }
            System.out.println("Wrong command");
        }
        System.out.println("END OF EXECUTION");
        scan.close();
    }
}