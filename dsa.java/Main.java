
import java.util.*;
import java.io.*;

class Student {
    String name;
    int[] marks = new int[3];
    int total;
    double percentage;
    String grade;

    Student(String name, int m1, int m2, int m3) {
        this.name = name;
        marks[0] = m1;
        marks[1] = m2;
        marks[2] = m3;

        total = m1 + m2 + m3;
        percentage = total / 3.0;
        if (percentage >= 90)
            grade = "A+";
        else if (percentage >= 75)
            grade = "A";
        else if (percentage >= 60)
            grade = "B";
        else if (percentage >= 40)
            grade = "C";
        else
            grade = "F";
    }

    void display() {
        System.out.println(name + " | Total: " + total +
                " | " + String.format("%.1f", percentage) +
                "% | Grade " + grade);
    }
}

public class Main {

    static ArrayList<Student> students = new ArrayList<>();
    static HashMap<String, Student> studentMap = new HashMap<>();

    static final String FILE_NAME = "results.txt";

    // Load data from file
    static void loadData() {
        try (BufferedReader br = new BufferedReader(
                new FileReader(FILE_NAME))) {

            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length == 4) {
                    String name = data[0];
                    int m1 = Integer.parseInt(data[1]);
                    int m2 = Integer.parseInt(data[2]);
                    int m3 = Integer.parseInt(data[3]);

                    Student s = new Student(name, m1, m2, m3);

                    students.add(s);
                    studentMap.put(name.toLowerCase(), s);
                }
            }

        } catch (FileNotFoundException e) {
            // File will be created when saving data
        } catch (Exception e) {
            System.out.println("Error loading data.");
        }
    }

    // Add student result
    static void addResult(Scanner sc) {

        System.out.print("Enter student name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Invalid name.");
            return;
        }

        if (studentMap.containsKey(name.toLowerCase())) {
            System.out.println("Student already exists.");
            return;
        }

        int[] marks = new int[3];

        System.out.println("Enter marks of 3 subjects:");

        for (int i = 0; i < 3; i++) {
            System.out.print("Subject " + (i + 1) + ": ");

            try {
                marks[i] = Integer.parseInt(sc.nextLine());

                if (marks[i] < 0 || marks[i] > 100) {
                    System.out.println(
                        "Invalid marks. Enter marks between 0 and 100.");
                    return;
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter numeric marks.");
                return;
            }
        }

        Student s = new Student(name, marks[0], marks[1], marks[2]);

        students.add(s);
        studentMap.put(name.toLowerCase(), s);

        System.out.println("Result added.");
        s.display();
    }

    // Search student by name
    static void searchResult(Scanner sc) {

        System.out.print("Enter student name to search: ");
        String name = sc.nextLine().trim();

        Student s = studentMap.get(name.toLowerCase());

        if (s != null) {
            s.display();
        } else {
            System.out.println("Student not found.");
        }
    }

    // Display rank list
    static void rankList() {

        if (students.isEmpty()) {
            System.out.println("No results available.");
            return;
        }

        ArrayList<Student> sortedList = new ArrayList<>(students);

        sortedList.sort((s1, s2) ->
                Integer.compare(s2.total, s1.total));

        System.out.println("\n----- RANK LIST -----");

        int rank = 1;

        for (Student s : sortedList) {
            System.out.println(rank + ". " + s.name +
                    " | " + s.total +
                    " | " + String.format("%.1f", s.percentage) + "%");
            rank++;
        }
    }

    // Find topper and class average
    static void topperAndAverage() {

        if (students.isEmpty()) {
            System.out.println("No results available.");
            return;
        }

        Student topper = students.get(0);
        int totalMarks = 0;

        for (Student s : students) {

            if (s.total > topper.total) {
                topper = s;
            }

            totalMarks += s.total;
        }

        double average = (double) totalMarks /
                (students.size() * 3);

        System.out.println("Topper: " + topper.name +
                " (" + topper.total + ")");

        System.out.println("Class average: " +
                String.format("%.1f", average) + "%");
    }

    // Save data to file
    static void saveData() {

        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(FILE_NAME))) {

            for (Student s : students) {
                bw.write(s.name + "," +
                        s.marks[0] + "," +
                        s.marks[1] + "," +
                        s.marks[2]);

                bw.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving data.");
        }
    }

    // Main method
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        loadData();

        int choice;

        do {
            System.out.println("\n===== RESULT ANALYSIS SYSTEM =====");
            System.out.println("1. Add Result");
            System.out.println("2. Search by Name");
            System.out.println("3. Rank List");
            System.out.println("4. Topper and Class Average");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine());

            } catch (NumberFormatException e) {
                System.out.println("Invalid choice.");
                continue;
            }

            switch (choice) {

                case 1:
                    addResult(sc);
                    break;

                case 2:

                    searchResult(sc);
                    break;

                case 3:
                    rankList();
                    break;

                case 4:
                    topperAndAverage();
                    break;

                case 5:
                    saveData();
                    System.out.println("Data saved successfully.");
                    System.out.println("Exiting program...");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (true);

    }
}
