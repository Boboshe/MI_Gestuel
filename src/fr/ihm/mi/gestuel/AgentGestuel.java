/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ihm.mi.gestuel;

import java.util.logging.Level;
import java.util.logging.Logger;
import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyException;
import fr.dgac.ivy.IvyMessageListener;
import fr.ihm.mi.gestuel.AutomateMI.Etat;
import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author boulbamo
 */
public class AgentGestuel extends JFrame {

    public static final Color DEFAULT_COLOR = Color.BLACK;
    public static final Point DEFAULT_POSITION = new Point(50, 50);

    private static Ivy bus;
    private JPanel jp1 = new JPanel();
    private Color couleurFond;
    private Color couleurContour;
    private int xDeb = 0, xFin = 0;
    private int yDeb = 0, yFin = 0;
    private int xCur = 0, yCur = 0;
    private int xClicked = 0, yClicked = 0;
    private boolean dragActived = false;
    private String nomObj = "no name";
    private ArrayList<String> listNomObj = new ArrayList<>();
    private Stroke stroke;
    private Template template;
    private ArrayList<Template> listeTemplate;
    private AutomateMI myAutomate;

    private boolean positionVoiceStated = false;
    private boolean positionCursorStated = false;
    private boolean colorStated = false;
    private boolean objStated = false;

    private Timer myTimer;
    private CreateRectangleTask rectangleTask;
    private CreateEllipseTask ellipseTask;
    private final int TYPE_RECTANGLE = 0;
    private final int TYPE_ELLIPSE = 1;

    private float seuil;

    /* Constructeur */
    public AgentGestuel() throws IvyException {
        super();
//        this.setVisible(true);
        //Initialization, Name and Ready message
        bus = new Ivy("AgentGestuel", "Agent ready", null);
        stroke = new Stroke();
        myAutomate = new AutomateMI();

        palette(bus);
//        voix(bus); // dans => compareToExistingTemplate

        bus.start(null);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /* sra => Voix */
    private void voix(Ivy bus) throws IvyException {

        bus.bindMsg("^sra5 Text=(.*) Confidence=(.*)", new IvyMessageListener() {
            //Info regex: 
            //(.*) => ça c'est recuperer
            // .*  => ça c'est trash
            @Override
            public void receive(IvyClient ic, String[] args) {

//                System.out.println("[IN] Text=" + args[0]);
                String c = args[1].replace(",", ".");
                float confidence = Float.parseFloat(c);
                seuil = (float) 0.7;
//                System.out.println("[IN] Confidence=" + confidence);
//                System.out.println("[IN] Confidence=" + args[1]);

                if (confidence < seuil) { //KO
                    System.out.println("Pas bien reconnu car confiance inférieure au seuil!\n");
                } else { //OK

                    //Position
                    //contains > equals : permet d'ignorer le garbage
                    if (args[0].contains("ici")
                            || args[0].contains("la")
                            || args[0].contains("a cette position")
                            || args[0].contains("a cet endroit")) {
                        System.out.println("[POSITION RECONNU] Text=" + args[0]);
                        positionFind();

                    }

                    //Couleur
                    if (args[0].contains("noir")
                            || args[0].contains("blanc")
                            || args[0].contains("bleu")
                            || args[0].contains("rouge")
                            || args[0].contains("orange")
                            || args[0].contains("jaune")
                            || args[0].contains("vert")) {
                        System.out.println("[COULEUR RECONNU] Text=" + args[0]);
                        colorsFind(args);
                    }

                    //<============================================ ICI LE PLUS DUR
                    if (args[0].contains("de cette couleur")) {
                        //Il faut capter la couleur de l'objet désigné
                    }

                    //Objet
                    if (args[0].contains("cet objet")
                            || args[0].contains("ce rectangle")
                            || args[0].contains("cette ellipse")) {
                        System.out.println("[OBJET RECONNU] Text=" + args[0]);
                        objectFind();
                    }

                    System.out.println("");
                }

            }

            private void positionFind() {
                positionVoiceStated = true;
//                System.out.println("Position find, positionStated=" + positionVoiceStated);
            }

            private void colorsFind(String[] args) {
                String[] splitArray = null;
                splitArray = args[0].split(" ");

                String couleurFondArg = splitArray[0];
                String couleurContourArg = splitArray[1];

                couleurFond = recognizeStringColor(couleurFondArg);
                couleurContour = recognizeStringColor(couleurContourArg);
                colorStated = true;

                if (rectangleTask != null) {
                    if (!rectangleTask.isOver()) {
                        rectangleTask.setMyColorFond(couleurFond);
                        rectangleTask.setMyColorContour(couleurContour);
                        System.out.println("**************** [Couleur Rectangle]  CouleurFond: " + recognizeColor(couleurFond) + ", CouleurContour: " + recognizeColor(couleurContour));
                        colorStated = false;
                    }
                }
                if (ellipseTask != null) {
                    if (!ellipseTask.isOver()) {
                        ellipseTask.setMyColorFond(couleurFond);
                        ellipseTask.setMyColorContour(couleurContour);
                        System.out.println("**************** [Couleur Ellipse]  CouleurFond: " + recognizeColor(couleurFond) + ", CouleurContour: " + recognizeColor(couleurContour));
                        colorStated = false;
                    }
                }

//                System.out.println("Color find, colorStated=" + colorStated);
            }

            private void objectFind() {
                objStated = true;
            }

            private Color recognizeStringColor(String couleur) {
                //Couleur par défaut
                Color color = Color.BLACK;

                if (couleur.equals("noir")) {
                    color = Color.BLACK;
                }
                if (couleur.equals("bleu")) {
                    color = Color.BLUE;
                }
                if (couleur.equals("rouge")) {
                    color = Color.RED;
                }
                if (couleur.equals("vert")) {
                    color = Color.GREEN;
                }
                if (couleur.equals("jaune")) {
                    color = Color.YELLOW;
                }
                if (couleur.equals("orange")) {
                    color = Color.ORANGE;
                }
                //de cette couleur
                return color;
            }

        }
        );
    }

    public String recognizeColor(Color c) {
        return convertToPaletteColor(c);
    }

    private String convertToPaletteColor(Color c) {
        String colorName = "No color found";
        if (Color.BLACK.equals(c)) {
            colorName = "BLACK";
        }
        if (Color.BLUE.equals(c)) {
            colorName = "BLUE";
        }
        if (Color.RED.equals(c)) {
            colorName = "RED";
        }
        if (Color.GREEN.equals(c)) {
            colorName = "GREEN";
        }
        if (Color.YELLOW.equals(c)) {
            colorName = "YELLOW";
        }
        if (Color.ORANGE.equals(c)) {
            colorName = "ORANGE";
        }
        return colorName;
    }

    /* Palette => EvtSouris */
    private void palette(Ivy bus) throws IvyException {
        bus.bindMsg("^Palette:(.*) x=(.*) y=(.*)", new IvyMessageListener() {

            @Override
            public void receive(IvyClient ic, String[] args) {
//                System.out.println("args[0]" + args[0]);
                String propriete = args[0];
                int x = new Integer(args[1]);
                int y = new Integer(args[2]);
//                System.out.println("[IN] Propriété=" + propriete);

                if (propriete.equals("MouseClicked")) {
                    //Si on est dans la tâche de récupération de la position...

                    // (#G) <==================================================================
                    /* RECTANGLE */
                    if (rectangleTask != null) {
                        if (!rectangleTask.isOver()) {
                            System.out.println("positionVoiceStated=" + positionVoiceStated);
                            //On vérifie que la position à été validée par la voix
                            //si ce n'est pas le cas, on précise à l'utilisateur qu'il doit le faire
                            if (!positionVoiceStated) {
                                System.out.print("**************** [Pointeur]");
                                System.out.print("Vous devez indiquer la position à la voix, ");
                                System.out.println("avant de cliquer, pour valider la position.");
                            } else { //Sinon c'est bon on stocke la position
                                System.out.print("**************** [Pointeur] Position stockée");
                                rectangleTask.setMyPosition(new Point(x, y));
                                positionVoiceStated = false;
                            }//Fin_if_positionVoiceStated?
                        }//fin_rectangleTask.isOver?
                    }//fin_rectangleTask-null?

                    /* ELLIPSE */
                    if (ellipseTask != null) {
                        if (!ellipseTask.isOver()) {
                            System.out.println("positionVoiceStated=" + positionVoiceStated);
                            //On vérifie que la position à été validée par la voix
                            //si ce n'est pas le cas, on précise à l'utilisateur qu'il doit le faire
                            if (!positionVoiceStated) {
                                System.out.print("**************** [Pointeur]");
                                System.out.print("Vous devez indiquer la position à la voix, ");
                                System.out.println("avant de cliquer, pour valider la position.");
                            } else { //Sinon c'est bon on stocke la position
                                System.out.print("**************** [Pointeur] Position stockée");
                                ellipseTask.setMyPosition(new Point(x, y));
                                positionVoiceStated = false;
                            }//Fin_if_positionVoiceStated?
                        }//fin_ellipseTask.isOver?
                    }//fin_ellipseTask-null?
                    // (#G) END <===============================================================
                }//Fin_MouseClicked

                if (propriete.equals("MousePressed")) {
                    xDeb = x;
                    yDeb = y;
                }
                if (propriete.equals("MouseDragged")) {
                    xCur = x;
                    yCur = y;
                    stockPointsInStroke(xCur, yCur);
                    dragActived = true;
                }

                if (propriete.equals("MouseReleased")) {
                    xFin = x;
                    yFin = y;
                    if (dragActived) {
//                        System.out.println("Ajout du template");
                        stroke.normalize();
                        try {
                            compareToExistingTemplate(stroke);

                        } catch (IOException ex) {
                            Logger.getLogger(AgentGestuel.class
                                    .getName()).log(Level.SEVERE, null, ex);
                        } catch (IvyException ex) {
                            Logger.getLogger(AgentGestuel.class
                                    .getName()).log(Level.SEVERE, null, ex);
                        }
                        stroke = new Stroke();
                        dragActived = false;

                    }
                }

            }//Fin_received

        }
        );

        bus.bindMsg(
                "^Palette:(.*) x=(.*) y=(.*) nom=(.*)", new IvyMessageListener() {

                    @Override
                    public void receive(IvyClient ic, String[] args
                    ) {
                        String propriete = args[0];
                        int x = new Integer(args[1]);
                        int y = new Integer(args[2]);
                        String nom = args[3];
                        if (propriete.equals("ResultatTesterPoint")) {
                            xFin = x;
                            yFin = y;
                            nomObj = nom;
                        }
                    }
                }
        );
    }

    public void loadTemplate() throws IOException {
        System.out.println("loadTemplate");
        listeTemplate = new ArrayList<Template>();
        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader("templates.txt"));
            String str;
            while ((str = in.readLine()) != null) {
                if (!str.trim().equals("")) {
                    listeTemplate.add(Template.read(str));
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * * Stockage des strokes **
     */
    public void stockPointsInStroke(int x, int y) {
        stroke.addPoint(new Point2D.Double(x, y));
//        System.out.println("Point added x:" + x + ", y:" + y);
    }

    /* Fonction Principale */
    /**
     * Fonction permettant de reconnaître le geste et de le comparer au Template
     * le plus proche
     *
     * @param stroke
     * @throws IOException
     * @throws IvyException
     */
    private void compareToExistingTemplate(Stroke stroke) throws IOException, IvyException {
        /* Traitement données fichier */
        //Récup données du fichier
        loadTemplate();

        /* Comparaison avec Templates */
        //Calcul distance
        int i = 0, index = 0;
        double min = 0;

        min = listeTemplate.get(i).getDistance(stroke);

        for (i = 1; i < listeTemplate.size(); i++) {
            if (min > listeTemplate.get(i).getDistance(stroke)) {
                min = listeTemplate.get(i).getDistance(stroke);
                //i = récupération du i correspondant au template le plus proche
                index = i;
            }
        }
        voix(bus);

        /**
         * * ACTIONS **
         */
        myTimer = new Timer();
        switch (index) {
            case TYPE_RECTANGLE:
                System.out.println("Geste CreationRectancle\n");

                if (myAutomate.changeState(Etat.CreationRectangle)) {
                    //Activation Timer
                    initTimer(TYPE_RECTANGLE); //TimerCreerRectangle
                }
                break;
            case TYPE_ELLIPSE:
                System.out.println("Geste CreationEllipse\n");

                if (myAutomate.changeState(Etat.CreationEllipse)) {
                    //Activation Timer
                    initTimer(TYPE_ELLIPSE); //TimerCreationEllipse
                }
                break;
            case 2:
                System.out.println("Geste Déplacer\n");
                break;
            case 3:
                System.out.println("Geste Supprimer\n");
                break;
            default:
                System.out.println("Commande non reconnue\n");
        }
    }

    /* TIMER & TASK */
    private void initRectangleTask() {
        System.out.println("******** Init rectangleTask");
        rectangleTask = new CreateRectangleTask(myAutomate, this); //<===================================== ICI
    }

    private void initEllipseTask() {
        System.out.println("******** Init ellipseTask");
        ellipseTask = new CreateEllipseTask(myAutomate, this); //<===================================== ICI
    }

    private void initTimer(int type) {
        reinitTimer(); // <=============================== Peut être pas utile...
        System.out.println("Init myTimer");
        if (type == TYPE_RECTANGLE) {
            initRectangleTask();
            myTimer.schedule(rectangleTask, 0); //0 = On execute le run imédiatement
        }
        if (type == TYPE_ELLIPSE) {
            initEllipseTask();
            myTimer.schedule(ellipseTask, 0); //0 = On execute le run imédiatement
        }

    }

    private void reinitTimer() {
        if (myTimer != null) {
            System.out.print("******** Re");
            myTimer = new Timer();
        }
    }

    private void stopTimer() {
        myTimer.cancel();
    }

    /* NOT USED */
//    private void reinitRectangleTask() {
//        if (rectangleTask != null) {
//            System.out.println("******** Restart rectangleTask");
//            rectangleTask = new CreateRectangleTask(myAutomate, this); //<===================================== ICI
//        }
//    }
    /* CREATION */
    /**
     * Dessine une rectangle avec les couleurs de fond et de contour
     *
     * @param x
     * @param y
     * @param longueur
     * @param hauteur
     * @param colorFond
     * @param colorContour
     */
    public void creerRectangle(int x, int y, int longueur, int hauteur, Color colorFond, Color colorContour) {
        try {
            System.out.println("Palette:CreerRectangle x=" + x + " y=" + y + " longueur=" + longueur + " hauteur=" + hauteur + " couleurFond=" + convertToPaletteColor(colorFond) + " couleurContour=" + convertToPaletteColor(colorContour));
            bus.sendMsg("Palette:CreerRectangle x=" + x + " y=" + y + " longueur=" + longueur + " hauteur=" + hauteur + " couleurFond=" + convertToPaletteColor(colorFond) + " couleurContour=" + convertToPaletteColor(colorContour));
            //"Palette:CreerRectangle x=" + myPosition.x + " y=" + myPosition.y + " longueur=" + 100 + " hauteur=" + 100 + " couleurFond=" + myColorFond + " couleurContour=" + myColorContour"
            //Palette:CreerRectangle x=100 y=100 longueur=100 hauteur=100 couleurFond=Color.BLACK couleurContour=Color.BLACK
        } catch (IvyException ex) {
            Logger.getLogger(AgentGestuel.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Dessine une ellipse avec les couleurs de fond et de contour
     *
     * @param x
     * @param y
     * @param longueur
     * @param hauteur
     * @param colorFond
     * @param colorContour
     *
     * //Ex: //Palette:CreerEllipse x=100 y=100 longueur=100 hauteur=100
     * couleurFond=blue couleurContour=red
     */
    public void creerEllipse(int x, int y, int longueur, int hauteur, Color colorFond, Color colorContour) {
        try {
            System.out.println("Palette:CreerEllipse x=" + x + " y=" + y + " longueur=" + longueur + " hauteur=" + hauteur + " couleurFond=" + convertToPaletteColor(colorFond) + " couleurContour=" + convertToPaletteColor(colorContour));
            bus.sendMsg("Palette:CreerEllipse x=" + x + " y=" + y + " longueur=" + longueur + " hauteur=" + hauteur + " couleurFond=" + convertToPaletteColor(colorFond)  + " couleurContour=" + convertToPaletteColor(colorContour));

        } catch (IvyException ex) {
            Logger.getLogger(AgentGestuel.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /* DEPLACEMENT */
    /**
     * * DEPLACEMENT **
     */
    public void deplacer(String nom, int x, int y) {
        try {
            bus.sendMsg("Palette:DeplacerObjet nom=" + nom + " x=" + x + " y=" + y);

        } catch (IvyException ex) {
            Logger.getLogger(AgentGestuel.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /* SUPPRESSION */
    /**
     * Supprime l'objet désigné
     */
    public void supprimer(String nom, int x, int y) {
        try {
            bus.sendMsg("Palette:SupprimerObjet nom=" + nom + " x=" + x + " y=" + y);
        } catch (IvyException ex) {
            Logger.getLogger(AgentGestuel.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*  METHODES SUPLEMENTAIRES */
    /**
     * Renvoi le nom et la position de l'objet à supprimer
     *
     * @param x
     * @param y
     * @param nom
     */
    public void resultatTesterPoint(int x, int y, String nom) {
        try {
            bus.sendMsg("Palette:ResultatTesterPoint x=" + x + " y=" + y + " nom=" + nom);
        } catch (IvyException ex) {
            Logger.getLogger(AgentGestuel.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /* MAIN */
    /**
     * * MAIN **
     */
    public static void main(String[] args) throws IvyException {
        new AgentGestuel();
    }
}
