package com.example.demo.memento;

import com.example.demo.Machine;

public class Memento {
    private String[] state;

    public Memento(String[] state) {
        this.state = state;
    }
    public String[] getState(){
        return state;
    }
}
