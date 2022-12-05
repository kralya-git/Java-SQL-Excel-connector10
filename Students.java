package com.test.idea.ten;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import java.sql.*;

public class Students {
    private final int id;
    private final String program;
    private final String name;
    private final String group;

    Students(int id, String program, String name, String group) {
        this.id = id;
        this.program = program;
        this.name = name;
        this.group = group;
    }

    @Override
    public String toString() {
        return String.format("| %20s | %20s | %20s | %20s \n", this.id, this.program, this.name, this.group);
    }

    public static Students insert() {
        Scanner s = new Scanner(System.in);

        System.out.print("Введите ID студента: ");
        int id = s.nextInt();
        System.out.print("Введите направление подготовки: ");
        String program = s.next();
        System.out.print("Введите имя: ");
        String name = s.next();
        System.out.print("Введите группу: ");
        String group = s.next();

        return new Students(id, program, name, group);
    }


    public static List<Students> input() {
        Scanner s = new Scanner(System.in);

        System.out.println("Введите количество студентов для внесения (не менее 5): ");
        int number = s.nextInt();
        if (number < 5){
            System.out.println("Количество меньше пяти.");
            return null;
        }

        List<Students> students = new ArrayList<Students>();

        for (int i = 0; i < number; i++) {
            students.add(Students.insert());
        }
        
        System.out.printf("| %20s | %20s | %20s | %20s \n", "ID", "Направление", "Имя", "Группа");
        for (Students st : students) {
            System.out.println(st.toString());
        }
        return students;
    }
    public void toMySQL(Connection con, String tablename) throws SQLException {
        PreparedStatement ps = con.prepareStatement("INSERT INTO " + tablename + " (ID, program, student_name, student_group) VALUES (?, ?, ?, ?)");
        ps.setInt(1, this.id);
        ps.setString(2, this.program);
        ps.setString(3, this.name);
        ps.setString(4, this.group);
        ps.executeUpdate();
    }
}
