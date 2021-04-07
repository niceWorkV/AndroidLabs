package ua.cn.stu.getvariant.lab4.model;

import ua.cn.stu.getvariant.lab4.Main.db.BookDbEntity;
import ua.cn.stu.getvariant.lab4.model.network.BookNetworkEntity;

public class Book {

    private int id;
    private String name;
    private String[] subjects;

    public Book(int id, String name, String[] subjects) {
        this.id = id;
        this.name = name;
        this.subjects = subjects;
    }

    public Book(BookDbEntity entity){
        this(
            entity.getId(),
            entity.getTitle(),
                entity.getSubjects().split("2space2")
        );
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String[] getSubjects() {
        return subjects;
    }
}
