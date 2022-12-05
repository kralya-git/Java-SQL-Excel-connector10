package com.test.idea.ten;

//импортируем библиотеки для работы с excel
import com.test.idea.EXL;
import com.test.idea.sql;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFRow;
//для работы с потоками (будем использовать в блоке с excel)
import  java.io.FileOutputStream;
//для работы с массивами данных
import java.io.FileWriter;
import java.util.Arrays;
//для считывания данных с клавиатуры
import java.util.List;
import java.util.Scanner;
//для работы с sql
import com.mysql.cj.jdbc.Driver;
//в особенности потом понадобятся Connection, ResultSet и Statement
import java.sql.*;


//главный КЛАСС
public class Students_database {

    //точка входа в программу + вывод информации об ошибках с бд
    //КЛАСС main()
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        //классу scanner присваиваем в качестве аргумента system.in
        Scanner scan = new Scanner(System.in);

        //начальное значение для switch case
        int x = 0;
        String s = "";

        //ввод названия таблицы с клавиатуры
        System.out.println("Введите название таблицы: ");
        String tablename = scan.nextLine();

        //цикл работает до тех пор, пока пользователь не введет 5
        while (!"7".equals(s)) {
            System.out.println();
            System.out.println("1. Вывести все таблицы из текущей БД.");
            System.out.println("2. Создать таблицу в БД.");
            System.out.println("3. Добавить данные в таблицу.");
            System.out.println("4. Сохранить данные в Excel.");
            System.out.println("5. Найти стдента по ID.");
            System.out.println("6. Удалить студента по ID.");
            System.out.println("7. Выйти из программы.");
            s = scan.next();

            //пробуем перевести пользовательский ввод в int
            try {
                x = Integer.parseInt(s);
            }
            //выдаем сообщение об ошибке ввода, и так до тех пор, пока пользователь не введет число
            catch (NumberFormatException e) {
                System.out.println("Неверный формат ввода.");
            }

            //оператор switch для множества развилок
            //эквивалентно оператору if
            switch (x) {

                //если пользователь ввел цифру 1, то...
                case 1 -> {
                    sql.TablesOutput();
                }

                //если пользователь ввел цифру 2, то...
                case 2 -> {
                    String query = " (ID int, program varchar(1001), student_name varchar(1001), student_group varchar(1001))";
                    sql.CreatingSQLTable(tablename, query);
                }

                //если пользователь ввел цифу 3, то...
                case 3 -> {

                    //регистрируем драйвер для дальнейшей работы (управление jdbc)
                    //КЛАСС DriverManager, МЕТОД registerDriver
                    DriverManager.registerDriver(new Driver());

                    //имя драйвера
                    //КЛАСС Class, МЕТОД forName
                    Class.forName("com.mysql.cj.jdbc.Driver");

                    //пытаемся установить соединение с заданным url бд
                    //ОБЪЕКТ Connection для работы с бд
                    //КЛАСС DriverManager, МЕТОД getConnection
                    Connection con2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql", "root", "кщще");
                    System.out.println("Успешно законнектились к БД!");

                    //МЕТОД nextLine() ОБЪЕКТА Scanner читает всю текущую строки и возвращает всё, что было в этой строке
                    scan.nextLine();

                    //вводим с клавиатуры
                    List<Students> stud = Students.input();
                    try {
                        for (Students st : stud) {
                            st.toMySQL(con2, tablename);
                        }
                    } catch (Exception e){
                        System.out.println(e);
                    }

                    System.out.println("Данные в MySQL успешно внесены!");

                    ///вывод количества строк в таблице
                    //создаем sql запрос
                    String query = "select count(*) from " + tablename;

                    //пробуем выполнить запрос через try - catch
                    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql", "root", "кщще");
                         Statement stmt = con.createStatement();
                         ResultSet rs = stmt.executeQuery(query)) {
                        while (rs.next()) {
                            int count = rs.getInt(1);
                            System.out.println("Всего внесено строк в таблицу " + tablename + " : " + count);
                        }
                    } catch (SQLException sqlEx) {
                        sqlEx.printStackTrace();
                    }
                }
                //если пользователь ввел цифру 4, то...
                case 4 -> {
                    EXL.ExcelConvector(tablename);
                }

                //для поиска по id
                case 5 -> {

                    try {
                        DriverManager.registerDriver(new Driver());
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection con5 = DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql", "root", "кщще");

                        System.out.println("\nВведите ID студента:");
                        int id = scan.nextInt();

                        Statement stmt = con5.createStatement();
                        String query = "SELECT * FROM " + tablename + " WHERE ID = " + id + "";
                        ResultSet rs = stmt.executeQuery(query);
                        ResultSetMetaData rsmd = rs.getMetaData();
                        int columnsNumber = rsmd.getColumnCount();

                        System.out.println("\nID\tНаправление\tИмя\tГруппа");

                        while (rs.next()) {
                            for(int i = 1 ; i <= columnsNumber; i++){
                                System.out.print(rs.getString(i) + "\t");
                            }
                            System.out.println();
                        }
                    } catch (Exception e){
                        System.out.println(e);
                    }
                }

                //для удаления по id
                case 6 -> {

                    try {
                        DriverManager.registerDriver(new Driver());
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection con6 = DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql", "root", "кщще");

                        System.out.println("\nВведите ID студента:");
                        int id = scan.nextInt();

                        Statement stmt = con6.createStatement();
                        String query = "DELETE FROM " + tablename + " WHERE ID = " + id + "";
                        stmt.executeUpdate(query);
                        System.out.println("Студент с указанным ID удален из таблицы.");
                    } catch (Exception e){
                        System.out.println(e);
                    }
                }

                //если пользователь введет 5, то выйдет из программы
                case 7 -> {
                    System.out.println("Вышли из нашей программы.");
                }
            }
        }
    }
}
