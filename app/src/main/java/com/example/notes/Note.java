package com.example.notes;


import java.io.Serializable;

public class Note implements Serializable { //Класс заметки
    private int id;
    private String name;
    private String text;

    Note(int id, String name, String text){ //Конструктор
        this.id = id;
        this.text = text;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return name; // Возвращаем заголовок заметки
    }
}
