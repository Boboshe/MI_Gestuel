// (#G) <==================================================================
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
public class MoveTask extends TimerTask {

    public static final Color DEFAULT_COLOR = Color.BLACK;
    public static final Point DEFAULT_POSITION = new Point(50, 50);
    private static final long DEFAULT_TIME = 10 * 1000; //temps initialisé à 10sec
    private static final int ERROR_NAME = 0;
    private static final int ERROR_POSITION = 1;

    private AutomateMI myAutomate;
    private AgentGestuel agentGestuel;
    private long timer;

    //Variables qui doivent changer
    private Color myColorFond = DEFAULT_COLOR;
    private Color myColorContour = DEFAULT_COLOR;
    private Point myPosition = DEFAULT_POSITION;
    private String name = null;
    private String msgErreur = null;

    private boolean positionVoiceStated;
    private boolean nameStated;
    private boolean over;

    public boolean isOver() {
        return over;
    }

    public MoveTask(AutomateMI myAutomate, AgentGestuel agentGestuel) {
        this(myAutomate, agentGestuel, DEFAULT_TIME);
    }

    public MoveTask(AutomateMI myAutomate, AgentGestuel agentGestuel, long timer) {
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
        System.out.println("****************  [Timer Deplacer] DEBUT");
        System.out.println("Attente d'une désignation d'un objet...");
        try {
            Thread.sleep(timer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("****************  [Timer Deplacer] FIN ");

//        System.out.print("myColorFond: " + agentGestuel.recognizeColor(myColorFond));
//        System.out.println(", myColorContour: " + agentGestuel.recognizeColor(myColorContour));

        /* Gérer l'échec du déplacement */
        if (nameStated && positionVoiceStated) {
            //Faire le déplacement
            agentGestuel.deplacer(name, myPosition.x, myPosition.y);
            System.out.println("**************** [Timer Deplacer] ### Déplacement effectué ###");

            myAutomate.changeState(AutomateMI.Etat.Idle);
            System.out.println("****************  [Timer Deplacer] Retour à Idle");
        } else { //Sinon msg erreur!  
            //On constitue le message d'erreur en fonction des différents 
            //paramètres qui n'ont pas été envoyés pendant la durée du timer
            if (nameStated) {
                constituerMsgErreur(ERROR_NAME);
            }
            if (positionVoiceStated) {
                constituerMsgErreur(ERROR_POSITION);
            }
            System.out.println("**************** [Timer Deplacer] ### ECHEC du déplacement ###");
            myAutomate.changeState(AutomateMI.Etat.Idle);
            System.out.println("****************  [Timer Deplacer] Retour à Idle");
            System.out.println("" + msgErreur);
        }

        nameStated = false;
        positionVoiceStated = false;
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
     * Récupère la position
     * @param myPosition
     */
    public void setMyPosition(Point myPosition) {
        this.myPosition = myPosition;
        positionVoiceStated = true;
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
            case ERROR_POSITION:
                msgErreur += "Aucune position détectée";
                break;
        }
    }

}
// (#G) END <==================================================================
