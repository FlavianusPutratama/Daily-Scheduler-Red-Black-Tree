import java.util.Scanner;

enum Color {
    RED, BLACK
}

class Task {
    String description;
    int priority;

    public Task(String description, int priority) {
        this.description = description;
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Task: " + description + ", Priority: " + priority;
    }
}

class Node {
    Task task;
    Color color;
    Node parent;
    Node left;
    Node right;

    public Node(Task task, Color color) {
        this.task = task;
        this.color = color;
    }
}

public class DailySchedule {
    private Node root;
    private Node nil; // Sentinel node

    public DailySchedule() {
        nil = new Node(null, Color.BLACK);
        root = nil;
    }

    // Red-Black Tree operations

    private void leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != nil) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == nil) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    private void rightRotate(Node y) {
        Node x = y.left;
        y.left = x.right;
        if (x.right != nil) {
            x.right.parent = y;
        }
        x.parent = y.parent;
        if (y.parent == nil) {
            root = x;
        } else if (y == y.parent.left) {
            y.parent.left = x;
        } else {
            y.parent.right = x;
        }
        x.right = y;
        y.parent = x;
    }

    private void insertFixup(Node z) {
        while (z.parent.color == Color.RED) {
            if (z.parent == z.parent.parent.left) {
                Node y = z.parent.parent.right;
                if (y.color == Color.RED) {
                    z.parent.color = Color.BLACK;
                    y.color = Color.BLACK;
                    z.parent.parent.color = Color.RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.right) {
                        z = z.parent;
                        leftRotate(z);
                    }
                    z.parent.color = Color.BLACK;
                    z.parent.parent.color = Color.RED;
                    rightRotate(z.parent.parent);
                }
            } else {
                Node y = z.parent.parent.left;
                if (y.color == Color.RED) {
                    z.parent.color = Color.BLACK;
                    y.color = Color.BLACK;
                    z.parent.parent.color = Color.RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rightRotate(z);
                    }
                    z.parent.color = Color.BLACK;
                    z.parent.parent.color = Color.RED;
                    leftRotate(z.parent.parent);
                }
            }
        }
        root.color = Color.BLACK;
    }

    public void insert(Task task) {
        Node z = new Node(task, Color.RED);
        Node y = nil;
        Node x = root;
        while (x != nil) {
            y = x;
            if (z.task.priority < x.task.priority) {
                x = x.left;
            } else {
                x = x.right;
            }
        }
        z.parent = y;
        if (y == nil) {
            root = z;
        } else if (z.task.priority < y.task.priority) {
            y.left = z;
        } else {
            y.right = z;
        }
        z.left = nil;
        z.right = nil;
        z.color = Color.RED;
        insertFixup(z);
    }

    private void inOrderTraversal(Node x) {
        if (x != nil) {
            inOrderTraversal(x.left);
            System.out.println(x.task);
            inOrderTraversal(x.right);
        }
    }

    public void printSchedule() {
        System.out.println("Daily Schedule:");
        inOrderTraversal(root);
    }

    public static void main(String[] args) {
        DailySchedule dailySchedule = new DailySchedule();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Tambah Kegiatan\n2. Cetak Jadwal\n3. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    System.out.println("Isi kegiatan:");
                    String description = scanner.nextLine();
                    System.out.println("Prioritas Kegiatan (dalam angka):");
                    int priority = scanner.nextInt();
                    dailySchedule.insert(new Task(description, priority));
                    break;
                case 2:
                    dailySchedule.printSchedule();
                    break;
                case 3:
                    System.exit(0);
            }
        }
    }
}
