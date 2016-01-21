/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vocal;

import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyException;
import fr.dgac.ivy.IvyMessageListener;
import fr.ihm.mi.gestuel.AgentGestuel;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author terooabo
 */
public class vocal extends JFrame {

    private Ivy bus;
    private JPanel jp1 = new JPanel();

    public vocal() throws IvyException {
// initialization, name and ready message
        bus = new Ivy("AgentGestuel", "Agent ready", null);
        bus.bindMsg("^sra5 Parsed=Action:(.*) Confidence=(.*) NP=.*", new IvyMessageListener() {

            //Info regex: 
            //(.*) => ça c'est recuperer
            // .*  => ça c'est trash
            @Override
            public void receive(IvyClient ic, String[] args) {
                String c = args[1].replace(",", ".");
                float confidence = Float.parseFloat(c);
                System.out.println("Confidence=" + confidence);

                if (confidence < 0.8) { //KO
                    System.out.println("Confidence < 0.8");
                    System.out.println("Pas bien reconnu car confiance inférieure au seuil!");
                } else { //OK
                    System.out.println("Confidence >= 0.8");

                    //Placement vocal
                    if (args[0].equals("placement ici")
                            || args[0].equals("placement là")
                            || args[0].equals("placement à cette position")) {
                        gauche();
                        System.out.println("Gauche");
                    }

                    /* Centrer */
                    if (args[0].equals("centrage")) {
                        centrage();
                        System.out.println("Centrage effectué");
                    }
                    /**/
                    /* Déplacer */
                    if (args[0].equals("deplacement Position:en bas")) {
                        bas();
                        System.out.println("Bas");
                    }
                    if (args[0].equals("deplacement Position:en haut")) {
                        haut();
                        System.out.println("Haut");
                    }
                    if (args[0].equals("deplacement Position:a droite")) {
                        droite();
                        System.out.println("Droite");
                    }
                    if (args[0].equals("deplacement Position:a gauche")) {
                        gauche();
                        System.out.println("Gauche");
                    }
//               /**/

                }
                //"sra5 Text=initialiser Confidence=0,8728002"

            }
        });

        // starts the bus on the default domain
        bus.start(null);

    }

    private void centrage() {
        try {
            bus.sendMsg("ppilot5 Say=Center");
        } catch (IvyException ex) {
            Logger.getLogger(AgentGestuel.class.getName()).log(Level.SEVERE, null, ex);
        }
        jp1.setLocation(this.getWidth() / 2, this.getHeight() / 2);

    }

    private void bas() {
        try {
            bus.sendMsg("ppilot5 Say=Down");
        } catch (IvyException ex) {
            Logger.getLogger(AgentGestuel.class.getName()).log(Level.SEVERE, null, ex);
        }
        jp1.setLocation(jp1.getLocation().x, jp1.getLocation().y + 10);
    }

    private void haut() {
        try {
            bus.sendMsg("ppilot5 Say=Up");
        } catch (IvyException ex) {
            Logger.getLogger(AgentGestuel.class.getName()).log(Level.SEVERE, null, ex);
        }
        jp1.setLocation(jp1.getLocation().x, jp1.getLocation().y - 10);
    }

    private void droite() {
        try {
            bus.sendMsg("ppilot5 Say=Right");
        } catch (IvyException ex) {
            Logger.getLogger(AgentGestuel.class.getName()).log(Level.SEVERE, null, ex);
        }
        jp1.setLocation(jp1.getLocation().x + 10, jp1.getLocation().y);
    }

    private void gauche() {
        try {
            bus.sendMsg("ppilot5 Say=Left");
        } catch (IvyException ex) {
            Logger.getLogger(AgentGestuel.class.getName()).log(Level.SEVERE, null, ex);
        }
        jp1.setLocation(jp1.getLocation().x - 10, jp1.getLocation().y);
    }

}
