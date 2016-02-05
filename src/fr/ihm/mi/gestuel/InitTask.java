/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ihm.mi.gestuel;

import java.util.TimerTask;

/**
 *
 * @author Boris
 */
public class InitTask extends TimerTask{

    private AutomateMI myAutomate;
    private boolean over; //Tache terminée

    public void setOver(boolean over) {
        this.over = over;
    }

    public boolean isOver() {
        return over;
    }

    public InitTask(AutomateMI myAutomate) {
        super();
        this.myAutomate = myAutomate;
        over = false;
    }
    
    @Override
    public void run() {
        over = true;
        System.out.println("Timer retour à Idle");
        myAutomate.changeState(AutomateMI.Etat.Idle);        
    }
    
    
}
