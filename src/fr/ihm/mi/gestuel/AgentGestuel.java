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
    private boolean dragActived = false;
    private String nomObj = "no name";
    private ArrayList<String> listNomObj = new ArrayList<>();
    private Stroke stroke;
    private Template template;
    private ArrayList<Template> listeTemplate;
    private AutomateMI myAutomate;
    private Timer timer;

    private boolean positionStated = false;
    private boolean colorStated = false;
    private boolean objStated = false;

    private long timerColorStart, timerPositionStart;
    private long timerColorStop, timerPositionStop;

    private Timer myTimer;
    private InitTask idleTask;

    public AgentGestuel() throws IvyException {
        super();
//        this.setVisible(true);
        //Initialization, Name and Ready message
        bus = new Ivy("AgentGestuel", "Agent ready", null);
        stroke = new Stroke();
        myAutomate = new AutomateMI();
        myTimer = new Timer();
        idleTask = new InitTask(myAutomate);

        palette(bus);
//        voix(bus); // dans => compareToExistingTemplate

        bus.start(null);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void voix(Ivy bus) throws IvyException {

        bus.bindMsg("^sra5 Text=(.*) Confidence=(.*)", new IvyMessageListener() {
            //Info regex: 
            //(.*) => ça c'est recuperer
            // .*  => ça c'est trash
            @Override
            public void receive(IvyClient ic, String[] args) {
                System.out.println("[IN] Text=" + args[0]);
                String c = args[1].replace(",", ".");
                float confidence = Float.parseFloat(c);
                float seuil = (float) 0.7;
//                System.out.println("[IN] Confidence=" + confidence);
                System.out.println("[IN] Confidence=" + args[1]);

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
                positionStated = true;
                System.out.println("Position find, positionStated=" + positionStated);
            }

            private void colorsFind(String[] args) {
                String[] splitArray = null;
                splitArray = args[0].split(" ");

                String couleurFondArg = splitArray[0];
                String couleurContourArg = splitArray[1];
//                System.out.println("CouleurFond: " + couleurFondArg + ", CouleurContour: " + couleurContourArg);

                couleurFond = recognizeStringColor(couleurFondArg);
                couleurContour = recognizeStringColor(couleurContourArg);
//                System.out.println("CouleurFond: " + recognizeColor(couleurFond) + ", CouleurContour: " + recognizeColor(couleurContour));

                colorStated = true;
                System.out.println("Color find, colorStated=" + colorStated);
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

    private String recognizeColor(Color c) {
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

    private void palette(Ivy bus) throws IvyException {
        bus.bindMsg("^Palette:(.*) x=(.*) y=(.*)", new IvyMessageListener() {

            @Override
            public void receive(IvyClient ic, String[] args) {
//                System.out.println("args[0]" + args[0]);
                String propriete = args[0];
                int x = new Integer(args[1]);
                int y = new Integer(args[2]);
//                System.out.println("[IN] Propriété=" + propriete);

                if (propriete.equals("MousePressed")) {
                    System.out.println("Pressed -  x:" + x + ", y:" + y);
                    xDeb = x;
                    yDeb = y;
                }
                if (propriete.equals("MouseDragged")) {
                    xCur = x;
                    yCur = y;
                    stockPointsInStroke(xCur, yCur);
                    dragActived = true;
                }
                if (propriete.equals("MouseClicked")) {
//                    if (positionStated) {
                        xCur = x;
                        yCur = y;
//                    }
                }

                if (propriete.equals("MouseMoved")) {
                    //Ne récupère la position que si un prédicat vocal a été émis
//                    if (positionStated) {
//                        System.out.print("MouseMoved");
                    xCur = x;
                    yCur = y;
//                    }
                }
                if (propriete.equals("MouseReleased")) {
                    System.out.println("Released -  x:" + x + ", y:" + y);
                    xFin = x;
                    yFin = y;
                    if (dragActived) {
                        System.out.println("Ajout du template");
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

        });

        bus.bindMsg("^Palette:(.*) x=(.*) y=(.*) nom=(.*)", new IvyMessageListener() {

            @Override
            public void receive(IvyClient ic, String[] args) {
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
        });
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
     * * Fonction Principale **
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
        Color myColorFond = DEFAULT_COLOR;
        Color myColorContour = DEFAULT_COLOR;
        Point myPosition = DEFAULT_POSITION;

        switch (index) {
            case 0:
                System.out.println("Creation du Rectancle\n");

                if (myAutomate.changeState(Etat.CreationRectangle)) {
                    //Activation Timer
                    restartTimer();
                    restartTache();
                    initTache(); //TimerCreerRectangle
                    System.out.println("timerCR");

                    //Tant que le timer n'a pas fini...
                    System.out.println("Attente d'une information vocale...[POSITION]ou[COULEUR]\n");
                    while (!idleTask.isOver()) {
//                        System.out.print(".");
                        System.out.print("positionStated=" + positionStated + ", ");
                        //Si une position ou une couleur à été dites...
                        if (positionStated) { //|| colorStated
                            myPosition.setLocation(xCur, yCur);
                            //on arrête le timer et on fini la tâche
                            System.out.println("Une Position ou une Couleur a été trouvée!");
                            stopTimer();
                            idleTask.setOver(false);

                            //On a trouvé une position
                            if (positionStated) {
                                System.out.println("=> Position trouvée");
                                //On passe dans l'état Positionnement
                                if (myAutomate.changeState(Etat.Positionnement)) {
                                    myPosition = new Point(xCur, yCur);

                                    restartTimer();
                                    restartTache();
                                    initTache(); //TimerPos
                                    System.out.println("xCur: " + xCur + ", yCur: " + yCur);

                                    System.out.println("\ntimerColor");
                                    System.out.println("Attente d'une information vocale [COULEUR]...\n");
                                    while (!idleTask.isOver()) {
                                        System.out.print("colorStated=" + colorStated + ", ");
                                        if (colorStated) {
                                            System.out.println("=> Couleur trouvée");
                                            if (myAutomate.changeState(Etat.Coloration)) {
                                                myColorContour = couleurContour;
                                                myColorFond = couleurFond;
//                                                System.out.print("couleurContour: " + recognizeColor(couleurContour));
//                                                System.out.println(", couleurFond: " + recognizeColor(couleurFond));
//                                                System.out.print("myColorContour: " + recognizeColor(myColorContour));
//                                                System.out.println(", myColorFond: " + recognizeColor(myColorFond));
                                                stopTimer();
                                                idleTask.setOver(true);
                                            }
                                            colorStated = false;
                                        }
                                    }//Fin_while
                                }
                                positionStated = false;
                            }

                            //Faire la même chose pour la Couleur avant le Positionnement
                        } //Fin_if_pos/color
                    }//Fin_while
                    idleTask.setOver(false);
                    System.out.println("**************** Rectangle créée");
                    System.out.print("myColorFond: " + recognizeColor(myColorFond));
                    System.out.println(", myColorContour: " + recognizeColor(myColorContour));
                    creerRectangle(myPosition.x, myPosition.y, 100, 100, myColorFond, myColorContour);

                    System.out.println("Retour à Idle");
                    myAutomate.changeState(AutomateMI.Etat.Idle);
                }
                break;
            case 1:
                System.out.println("Creer Ellipse\n");
                break;
            case 2:
                System.out.println("Déplacer\n");
                break;
            case 3:
                System.out.println("Supprimer\n");
                break;
            default:
                System.out.println("Commande non reconnue\n");
        }
    }

    private void restartTimer() {
        if (myTimer != null) {
            System.out.println("******** Restart myTimer");
            myTimer = new Timer();
        }
    }

    private void stopTimer() {
        myTimer.cancel();
    }

    private void restartTache() {
        if (idleTask != null) {
            System.out.println("******** Restart idleTask");
            idleTask = new InitTask();
        }
    }

    private void initTache() {
        myTimer.schedule(idleTask, 10 * 1000);
    }

    /**
     * * Stockage des strokes **
     */
    public void stockPointsInStroke(int x, int y) {
        stroke.addPoint(new Point2D.Double(x, y));
//        System.out.println("Point added x:" + x + ", y:" + y);
    }

    /**
     * CREATION *
     */
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
     * Dessine une rectangle
     *
     * @param x
     * @param y
     * @param longueur
     * @param hauteur
     */
    public void creerRectangle(int x, int y, int longueur, int hauteur) {
        try {
            bus.sendMsg("Palette:CreerRectangle x=" + x + " y=" + y + " longueur=" + longueur + " hauteur=" + hauteur);

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
            bus.sendMsg("Palette:CreerEllipse x=" + x + " y=" + y + " longueur=" + longueur + " hauteur=" + hauteur + " couleurFond=" + colorFond + " couleurContour=" + colorContour);

        } catch (IvyException ex) {
            Logger.getLogger(AgentGestuel.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Dessine une ellipse
     *
     * @param x
     * @param y
     * @param longueur
     * @param hauteur
     */
    public void creerEllipse(int x, int y, int longueur, int hauteur) {
        try {
            bus.sendMsg("Palette:CreerEllipse x=" + x + " y=" + y + " longueur=" + longueur + " hauteur=" + hauteur);

        } catch (IvyException ex) {
            Logger.getLogger(AgentGestuel.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

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

    /**
     * * SUPPRESSION **
     */
    public void supprimer(String nom, int x, int y) {
        try {
            bus.sendMsg("Palette:DeplacerObjet nom=" + nom + " x=" + x + " y=" + y);

        } catch (IvyException ex) {
            Logger.getLogger(AgentGestuel.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * * METHODES SUPLEMENTAIRES **
     */
    public void resultatTesterPoint(int x, int y, String nom) {
        try {
            bus.sendMsg("Palette:DeplacerObjet x=" + x + " y=" + y + " nom=" + nom);

        } catch (IvyException ex) {
            Logger.getLogger(AgentGestuel.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * * MAIN **
     */
    public static void main(String[] args) throws IvyException {
        new AgentGestuel();
    }

    /*
     private void startTimerPosition() {
     timerPositionStart = System.currentTimeMillis();
     timerPositionStop = timerPositionStart + (10 * 1000); //10 sec
     }

     private void startTimerCouleur() {
     timerColorStart = System.currentTimeMillis();
     timerColorStop = timerColorStart + (10 * 1000); //10 sec
     }

     private void startTimerObjet() {
     throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
     }
     */
}
