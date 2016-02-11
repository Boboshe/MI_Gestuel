/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ihm.mi.gestuel;

import java.awt.Color;
import java.awt.Point;
import java.util.TimerTask;

/**
 *
 * @author Boris
 */
public class DeleteTask extends TimerTask {

    public static final Color DEFAULT_COLOR = Color.BLACK;
    private static final long DEFAULT_TIME = 5 * 1000; //temps initialisé à 10sec
    private static final int ERROR_NAME = 0;

    private AutomateMI myAutomate;
    private AgentGestuel agentGestuel;
    private long timer;

    //Variables qui doivent changer
    private Color myColorFond = DEFAULT_COLOR;
    private Color myColorContour = DEFAULT_COLOR;
    private String name = null;
    private String msgErreur = null;

    private boolean positionVoiceStated;
    private boolean nameStated;
    private boolean over;

    public boolean isOver() {
        return over;
    }

    public DeleteTask(AutomateMI myAutomate, AgentGestuel agentGestuel) {
        this(myAutomate, agentGestuel, DEFAULT_TIME);
    }

    public DeleteTask(AutomateMI myAutomate, AgentGestuel agentGestuel, long timer) {
        super();
        this.myAutomate = myAutomate;
        this.agentGestuel = agentGestuel;
        this.timer = timer;
        //Variable(s) obligatoirement initialisée(s) à false lors de la création
        this.over = false;
        this.positionVoiceStated = false;
        this.nameStated = false;
    }

    @Override
    public void run() {
        System.out.println("****************  [Timer Supprimer] DEBUT");
        System.out.println("Attente d'une désignation d'un objet...");
        try {
            Thread.sleep(timer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("****************  [Timer Supprimer] FIN ");

//        System.out.print("myColorFond: " + agentGestuel.recognizeColor(myColorFond));
//        System.out.println(", myColorContour: " + agentGestuel.recognizeColor(myColorContour));

        /* Gérer l'échec de la suppression */
        if (nameStated) {
            //Faire la suppression
            agentGestuel.supprimer(name);
            System.out.println("**************** [Timer Supprimer] ### Supprression effectué ###");

            myAutomate.changeState(AutomateMI.Etat.Idle);
            System.out.println("****************  [Timer Supprimer] Retour à Idle");
        } else { //Sinon msg erreur!  
            //On constitue le message d'erreur en fonction des différents 
            //paramètres qui n'ont pas été envoyés pendant la durée du timer
            if (nameStated) {
                constituerMsgErreur(ERROR_NAME);
            }
            
            System.out.println("**************** [Timer Supprimer] ### ECHEC de la suppression ###");
            myAutomate.changeState(AutomateMI.Etat.Idle);
            System.out.println("****************  [Timer Deplacer] Retour à Idle");
            System.out.println("" + msgErreur);
        }

        nameStated = false;
        over = true;
    }

    /**
     * Récupère le nom de l'objet à supprimer.
     * @param name
     */
    public void setName(String name) {
        this.name = name;
        nameStated = true;
    }

    /**
     * Récupère la couleur du fond
     * @param myColorFond
     */
    public void setMyColorFond(Color myColorFond) {
        this.myColorFond = myColorFond;
    }

    /**
     * Récupère la couleur du contour
     * @param myColorContour
     */
    public void setMyColorContour(Color myColorContour) {
        this.myColorContour = myColorContour;
    }

    private void constituerMsgErreur(int codeErrVerif) {
        msgErreur = "*[ERREUR] ";
        switch (codeErrVerif) {
            //Pour l'ajout :
            case ERROR_NAME:
                msgErreur += "Aucun nom détecté\n";
                break;
        }
    }

}
