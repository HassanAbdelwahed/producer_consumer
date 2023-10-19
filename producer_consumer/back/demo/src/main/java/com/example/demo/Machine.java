package com.example.demo;

import com.example.demo.controller.Controller;
import com.example.demo.memento.Caretaker;
import com.example.demo.memento.Originator;
import com.example.demo.observer.Observer;
import java.time.Duration;
import java.time.Instant;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Machine implements Runnable, Observer {  // consumer && producer
    private int id;
    private ArrayList<DataQueue> inQueues = new ArrayList<>();
    private static Instant startTime = Instant.ofEpochSecond(0);
    private DataQueue outQueue;
    private int serviceTime;

    private boolean isRunning;
    private Random random = new Random();

    private Color color = null;
    private  String hexValue;

    private int inIndex;
    private int inID = -1;

    public Machine(int id) {
        this.id = id;
        this.color = new Color(0, 128,0);
        this.hexValue = String.format("#%06X", (0xFFFFFF & this.color.getRGB()));
        this.serviceTime = (random.nextInt(7) + 4) * 1000;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<DataQueue> getInQueues() {
        return inQueues;
    }
    public void setInQueue(DataQueue queue){
        this.inQueues.add(queue);
    }

    public void setInQueues(ArrayList<DataQueue> inQueues) {
        this.inQueues = inQueues;
    }

    public DataQueue getOutQueue() {
        return outQueue;
    }

    public void setOutQueue(DataQueue outQueue) {
        this.outQueue = outQueue;
    }

    @Override
    public void run() {
        this.isRunning = true;
        try {
            consume();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void consume() throws InterruptedException {
        while (isRunning){
            synchronized (this)
            {
                if (existQueueNotEmpty()){
                    Product product = getProduct();
                    this.setColor(product.getColor());
                    unObserve();
                    String[] str = {String.valueOf(this.getId()), this.hexValue, String.valueOf(this.outQueue.getId()), String.valueOf(this.outQueue.getProducts().size()),
                            String.valueOf(this.inID), String.valueOf(inQueues.get(this.inIndex).getProducts().size())};
                    saveCurrentState();
                    Controller.disPatch(str); // send changes to front
                    Thread.sleep(this.serviceTime);
                    this.outQueue.addProduct(product);
                }else {
                    this.setColor(new Color(0, 128, 0));
                    if (this.inID != -1){  // send changes
                        String[] str = {String.valueOf(this.getId()), this.hexValue, String.valueOf(this.outQueue.getId()), String.valueOf(this.outQueue.getProducts().size()),
                                String.valueOf(this.inID), String.valueOf(inQueues.get(this.inIndex).getProducts().size())};
                        saveCurrentState();
                        Controller.disPatch(str);
                    }
                    observe();
                    wait();
                }
            }
        }
    }

    public synchronized Product getProduct(){
        for (DataQueue qu: inQueues) {
            if (!qu.isEmpty()){
                this.inIndex = inQueues.indexOf(qu);
                this.inID = qu.getId();
                return qu.removeProduct();
            }
        }
        return null;
    }
    public void observe(){
        for (DataQueue qu: inQueues) {
            qu.registerObserver(this);
        }
    }

    public void unObserve(){
        for (DataQueue qu: inQueues) {
            qu.removeObserver(this);
        }
    }

    public synchronized boolean existQueueNotEmpty(){
        for (DataQueue qu: inQueues) {
            if (!qu.isEmpty()){
                return true;
            }
        }
        return false;
    }

    @Override
    public void update() {
        synchronized(this){
            this.notify();
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        System.out.println("M" + this.id + " " + this.hexValue);
        this.color = color;
        this.hexValue = String.format("#%06X", (0xFFFFFF & this.color.getRGB()));
        System.out.println("Inqueue " + inQueues.get(0).getProducts().size());
        System.out.println("outQueue" + outQueue.getProducts().size());
    }

    public String getHexValue() {
        return hexValue;
    }

    public synchronized void saveCurrentState(){
        Instant endTime = Instant.now();
        Duration timeElapsed = Duration.between(startTime, endTime);
        if (startTime == Instant.ofEpochSecond(0))
            timeElapsed = Duration.between(Instant.ofEpochSecond(0), Instant.ofEpochSecond(0));
        startTime = endTime;
        String[] state = {String.valueOf(this.getId()), this.hexValue, String.valueOf(this.outQueue.getId()), String.valueOf(this.outQueue.getProducts().size()),
                String.valueOf(this.inID), String.valueOf(inQueues.get(this.inIndex).getProducts().size()), String.valueOf(timeElapsed.toMillis())};
        Caretaker caretaker = Caretaker.getInstance();
        Originator originator = new Originator();
        originator.setState(state);
        caretaker.add(originator.createMemento());
    }
//    @Override
//    public String toString()
//    {
//        return String.format("Machine [id=%s, color=%s]", this.getId(), this.getHexValue());
//    }
}
