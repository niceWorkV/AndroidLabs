package ua.cn.stu.getvariant.lab4.model;

import java.util.List;

import ua.cn.stu.getvariant.lab4.model.network.BookNetworkEntity;

public class BookDocs {

    public String getTitle() {
        return title;
    }

    public String[] getSubject() {
        return subject;
    }

    public int getCover_i() {
        return cover_i;
    }

    private String  title;
    private String[] subject;
    private int cover_i;


}
