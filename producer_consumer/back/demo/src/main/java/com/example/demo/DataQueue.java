package com.example.demo;

import com.example.demo.observer.Observer;
import com.example.demo.observer.Subject;

import java.awt.*;
import java.util.*;

public class DataQueue implements Subject {
    int id;
    Queue<Product> products = new LinkedList<>();
    ArrayList<Observer> observers = new ArrayList<>();
    private boolean  empty = true;
    Random random = new Random();

    public DataQueue(int id) {
        this.id = id;
    }

    // set initial products
    public void SetProducts(int num) {
        for (int i = 0; i < num; i++){
            int red = random.nextInt(256); // Red component (0-255)
            int green = random.nextInt(256); // Green component (0-255)
            int blue = random.nextInt(256); // Blue component (0-255)
            Color color = new Color(red, green, blue);
            Product product = new Product(color);
            products.add(product);
        }
        if (num > 0)
            this.empty = false;
    }

    public void addProduct(){
        int red = random.nextInt(256); // Red component (0-255)
        int green = random.nextInt(256); // Green component (0-255)
        int blue = random.nextInt(256); // Blue component (0-255)
        Color color = new Color(red, green, blue);
        Product product = new Product(color);
        this.addProduct(product);
    }
    public synchronized void addProduct(Product product){ // ==>>>>>>>>>>>>>>>>>>>>>> Not finished yet
        products.add(product);
        setEmpty(false);
        notifyObservers();
    }
    public synchronized Product removeProduct(){
        Product product = this.products.poll();
        if (this.products.size() == 0){
            setEmpty(true);
        }
        return product;
    }

    @Override
    public void registerObserver(Observer observer){
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer){
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers(){
        System.out.println(observers.size());
        for (Observer observer: observers){
            observer.update();
            System.out.println("queue " + this.id + " observe " + observer);
            break;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Queue<Product> getProducts() {
        return products;
    }

    public void setProducts(Queue<Product> products) {
        this.products = products;
    }

    public ArrayList<Observer> getObservers() {
        return observers;
    }

    public void setObservers(ArrayList<Observer> observers) {
        this.observers = observers;
    }
    public boolean isEmpty() {

        return this.products.size() == 0;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }
}
