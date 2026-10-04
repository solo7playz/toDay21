package com.example;

public class Task {
    private int id;
    private String name;
    private String info;
    private boolean done;

    public Task() {
    }

    public Task(String name, String info, boolean done) {
        this.name = name;
        this.info = info;
        this.done = done;
    }

    public Task(int id, String name, String info, boolean done) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.done = done;
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public String toString() {
        String status = done ? "✓ Выполнено" : "✗ Не выполнено";
        return String.format("[%s] %s", status, name);
    }
}
