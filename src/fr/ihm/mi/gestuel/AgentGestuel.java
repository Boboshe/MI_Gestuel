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
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author boulbamo
 */
public class AgentGestuel extends JFrame {

    private Ivy bus;
    private JPanel jp1 = new JPanel();
    private Color couleur;
    private int xDeb = 0, xFin = 0;
    private int yDeb = 0, yFin = 0;
    private boolean dragActived = false;
    private String nomObj = "no name";
    private ArrayList<String> listNomObj = new ArrayList<>();

    public AgentGestuel() throws IvyException {
        super();
//        this.setVisible(true);
// initialization, name and ready message
        bus = new Ivy("AgentGestuel", "Agent ready", null);
//        bus.bindMsg("^sra5 Parsed=Action:(.*) Confidence=(.*) NP=.*", new IvyMessageListener() {
//        bus.bindMsg("^sra5 Parsed=Action:(.*) Confidence=(.*) NP=.*", new IvyMessageListener() {

        palette(bus);
        geste(bus);
        voix(bus);

        // starts the bus on the default domain
        bus.start(null);

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
                float seuil = (float) 0.4;
                System.out.println("[IN] Confidence=" + confidence);
//                System.out.println("Confidence=" + args[1]);

                if (confidence < seuil) { //KO
                    System.out.println("Confidence:" + confidence + " < " + seuil);
                    System.out.println("Pas bien reconnu car confiance inférieure au seuil!\n");
                } else { //OK
                    System.out.println("Confidence:" + confidence + " >= 0.4");

                    //Placement
                    //contains > equals : permet d'ignorer le garbage
                    if (args[0].contains("ici")
                            || args[0].contains("la")
                            || args[0].contains("a cette position")
                            || args[0].contains("a cet endroit")) {
                        System.out.println("[RECONNU] Text=" + args[0]);
                        placer();
                    }

                    //Couleur
                    if (args[0].contains("noir")
                            || args[0].contains("blanc")
                            || args[0].contains("bleu")
                            || args[0].contains("rouge")
                            || args[0].contains("orange")
                            || args[0].contains("jaune")
                            || args[0].contains("vert")) {
                        System.out.println("[RECONNU] Text=" + args[0]);
                        colorer(args);
                    }

                    //Déplacer
                    if (args[0].contains("cet objet")
                            || args[0].contains("ce rectangle")
                            || args[0].contains("cette ellipse")) {
                        System.out.println("[RECONNU] Text=" + args[0]);
                        deplacer();
                    }

                    System.out.println("");
                }
            }

            private void placer() {
                //Analyse du placement
                //In: x, y
                //Dans la palette
                //Out: cmd de placement
            }

            private void colorer(String[] args) {
                //Analyse de la couleur
                //In: args
                //Out: cmd de coloration
                if (args[0].equals("noir")) {
                    couleur = Color.BLACK;
                }
                if (args[0].equals("bleu")) {
                    couleur = Color.BLUE;
                }
                if (args[0].equals("rouge")) {
                    couleur = Color.RED;
                }
                if (args[0].equals("vert")) {
                    couleur = Color.GREEN;
                }
                if (args[0].equals("jaune")) {
                    couleur = Color.YELLOW;
                }
                if (args[0].equals("orange")) {
                    couleur = Color.ORANGE;
                }
            }

            private void deplacer() {
                //Analyse du placement
                //In: x, y
                //Out: cmd de placement

            }
        });
    }

    private void palette(Ivy bus) throws IvyException {
        bus.bindMsg("^Palette:(.*) x=(.*) y=(.*) ", new IvyMessageListener() {

            @Override
            public void receive(IvyClient ic, String[] args) {
                String propriete = args[0];
                int x = new Integer(args[1]);
                int y = new Integer(args[2]);
                System.out.println("[IN] Propriété=" + propriete);

                if (propriete.equals("MousePressed")) {
                    xDeb = x;
                    yDeb = y;
                }
                if (propriete.equals("mouseDragged")) {
                    dragActived = true;
                    //Une fondtion su rle dragged
                }
                if (propriete.equals("MouseReleased")) {
                    xFin = x;
                    yFin = y;
                }
            }
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

    private void geste(Ivy bus) throws IvyException {
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
            Logger.getLogger(AgentGestuel.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(AgentGestuel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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
            bus.sendMsg("Palette:CreerRectangle x=" + x + " y=" + y + " longueur=" + longueur + " hauteur=" + hauteur + " couleurFond=" + colorFond + " couleurContour=" + colorContour);
        } catch (IvyException ex) {
            Logger.getLogger(AgentGestuel.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(AgentGestuel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) throws IvyException {
        new AgentGestuel();

    }

}
