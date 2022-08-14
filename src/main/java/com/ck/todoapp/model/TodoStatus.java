package com.ck.todoapp.model;

public enum TodoStatus {

    NOT_DONE(1, "not dones"),
    DONE(2, "done"),
    PAST_DUE(3, "post due");

    private final Long id;
    private final String description;

    TodoStatus(int id, String description) {
        this.id = (long) id;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

}
