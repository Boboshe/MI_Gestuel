/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ihm.mi.gestuel;

import static fr.ihm.mi.gestuel.AgentGestuel.DEFAULT_COLOR;
import static fr.ihm.mi.gestuel.AgentGestuel.DEFAULT_POSITION;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.TimerTask;

/**
 *
 * @author Boris
 */
public class CreateRectangleTask extends TimerTask {

    public static final Color DEFAULT_COLOR = Color.BLACK;
    public static final Point DEFAULT_POSITION = new Point(50, 50);
    private static final long DEFAULT_TIME = 10*1000; //temps initialisé à 10sec

    
    private AutomateMI myAutomate;
    private AgentGestuel agentGestuel;
    private long timer;

    //Variables qui doivent changer
    private Color myColorFond = DEFAULT_COLOR;
    private Color myColorContour = DEFAULT_COLOR;
    private Point myPosition = DEFAULT_POSITION;

    private boolean over;
    private boolean canStop;

    public boolean isOver() {
        return over;
    }

    public CreateRectangleTask(AutomateMI myAutomate, AgentGestuel agentGestuel) {
        this(myAutomate, agentGestuel, DEFAULT_TIME); 
    }

    public CreateRectangleTask(AutomateMI myAutomate, AgentGestuel agentGestuel, long timer) {
        super();
        this.myAutomate = myAutomate;
        this.agentGestuel = agentGestuel;
        this.timer = timer;
        //Variables obligatoirement initialisée à false lors de la création
        this.over = false;
        this.canStop = false;
    }

    @Override
    public void run() {
        System.out.println("****************  [Timer] DEBUT");
        System.out.println("Attente de position et de couleur...");
        try {
            Thread.sleep(timer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("****************  [Timer] FIN ");

        System.out.print("myColorFond: " + agentGestuel.recognizeColor(myColorFond));
        System.out.println(", myColorContour: " + agentGestuel.recognizeColor(myColorContour));
        agentGestuel.creerRectangle(this.myPosition.x, this.myPosition.y, 100, 100, this.myColorFond, this.myColorContour);
        System.out.println("**************** [CREER] Rectangle créée");
        
        System.out.println("****************  [Timer] Retour Idle");
        myAutomate.changeState(AutomateMI.Etat.Idle);
        over = false;
    }

    /**
     * Actualisation de la position (x,y) Si cette fonction n'est pas utilisée,
     * le rectangle est creer avec les valeurs par défaut
     *
     * @param myPosition
     */
    public void setMyPosition(Point myPosition) {
        this.myPosition = myPosition;
    }

    /**
     * Actualisation de la couleur du fond du rectangle Si cette fonction n'est
     * pas utilisée, le rectangle est creer avec la couleur du fond par défaut
     *
     * @param myColorFond
     */
    public void setMyColorFond(Color myColorFond) {
        this.myColorFond = myColorFond;
    }

    /**
     * Actualisation de la couleur du contour du rectangle Si cette fonction
     * n'est pas utilisée, le rectangle est creer avec la couleur du contour par
     * défaut
     *
     * @param myColorContour
     */
    public void setMyColorContour(Color myColorContour) {
        this.myColorContour = myColorContour;
    }
}
