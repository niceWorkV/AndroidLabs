package ua.cn.stu.getvariant.lab4.Main.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import ua.cn.stu.getvariant.lab4.model.BookDocs;

@Entity(tableName = "books")
public class BookDbEntity {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "subjects")
    private String subjects;

    @ColumnInfo(name = "author")
    private String author;

    public BookDbEntity() {
    }

    public BookDbEntity(String author, BookDocs bookDocs) {
        this.id = bookDocs.getCover_i();
        this.title = bookDocs.getTitle();
        this.author = author;
        String text = "";
        for(String sub: bookDocs.getSubject()){
            text = text+" "+sub;
        }
        this.subjects = text;
    }




    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }
}
