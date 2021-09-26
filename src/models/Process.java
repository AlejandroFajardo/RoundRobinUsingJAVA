/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.ArrayList;

/**
 *
 * @author jhona
 */
public class Process {

    public String id;
    public String color;
    public char state;
    public int timeProcess;
    public int timeArrival;
    public int priority;

    public Process(int timeProcess, int timeArrival, int priority) {
        this.timeProcess = timeProcess;
        this.timeArrival = timeArrival;
        this.id = "process" + (int) (Math.random() * (255 - 0 + 1) + 0);
        this.color = "" + (int) (Math.random() * (255 - 0 + 1) + 0) + "," + (int) (Math.random() * (255 - 0 + 1) + 0) + "," + (int) (Math.random() * (255 - 0 + 1) + 0);
        this.state = 'W';
        this.priority = priority;
    }

    public void changeState(char newState) {    
        this.state = newState;
    }

}
