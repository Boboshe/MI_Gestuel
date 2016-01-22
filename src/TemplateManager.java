import fr.ihm.mi.gestuel.Template;
import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyException;
import fr.dgac.ivy.IvyMessageListener;
import fr.ihm.mi.gestuel.Stroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.Action;
import javax.swing.JButton;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.

 Ajouter le bouton  supprimer les templates


 4 Templates:
 - CreerRectangle
 - CreerEllipse
 - Déplacer (trait)
 - Supprimer (x|)

 */
/**
 *
 * @author terooabo
 */
public class TemplateManager extends javax.swing.JFrame {

    private Ivy bus;
    private int xDeb = 0, xFin = 0;
    private int yDeb = 0, yFin = 0;
    private int xCur = 0, yCur = 0;
    private boolean dragActived = false;
    private String nomObj = "no name";
    private Stroke stroke;
    private File fichier;
    private ArrayList<Template> listeTemplate;
    private ArrayList<Template> listeTemplate2;
    private int i = 1;

    JButton saveFile;

    /**
     * Creates new form InitTemplate
     */
    public TemplateManager() throws IvyException {
        initComponents();

        //How it works:
        //bus = new Ivy("nomAgent", "1ermessage", null);
        bus = new Ivy("TemplateManager", "Agent ready", null);
        bus.start(null);
        stroke = new Stroke();
        listeTemplate = new ArrayList<>();

        //(.*) correspond a une valeur
        //le 1er (.*) est stocké dans le 1er argument du tableau de String du receive
        //le 2eme (.*) est stocké dans le 2eme argument du tableau de String du receive
        //etc...
        bus.bindMsg("^Palette:(.*) x=(.*) y=(.*)", new IvyMessageListener() {

            @Override
            public void receive(IvyClient ic, String[] args) {
                System.out.println("args[0]" + args[0]);
                String propriete = args[0];
                int x = new Integer(args[1]);
                int y = new Integer(args[2]);
//                stroke = new Stroke();
                System.out.println("[IN] Propriété=" + propriete);

                if (propriete.equals("MousePressed")) {
                    System.out.println("Pressed -  x:" + x + ", y:" + y);
                    xDeb = x;
                    yDeb = y;
                }
                if (propriete.equals("mouseDragged")) {
                    xCur = x;
                    yCur = y;
                    stockPointsInStroke(xCur, yCur);
                    dragActived = true;
                }
                if (propriete.equals("MouseReleased")) {
                    System.out.println("Released -  x:" + x + ", y:" + y);
                    xFin = x;
                    yFin = y;
                    if (dragActived) {
                        System.out.println("Ajout du template");
                        stroke.normalize();
                        listeTemplate.add(new Template("Template" + (i++), stroke));
                        stroke = new Stroke();
                        dragActived = false;
                    }
                }
            }//Fin_received

        });
    }

//    public TemplateManager(GraphicsConfiguration gc) {
//        super(gc);
//    }
    public void saveTemplate() throws IOException {
        System.out.println("saveTemplate");
        try {
            PrintWriter out = new PrintWriter(new FileWriter("templates.txt"));
            for (int i = 0; i < listeTemplate.size(); i++) {
                listeTemplate.get(i).write(out);
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadTemplate() throws IOException {
        System.out.println("loadTemplate");
        listeTemplate2 = new ArrayList<Template>();
        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader("templates.txt"));
            String str;
            while ((str = in.readLine()) != null) {
                if (!str.trim().equals("")) {
                    listeTemplate2.add(Template.read(str));
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dessin();
    }

    public void stockPointsInStroke(int x, int y) {
        // TODO
        stroke.addPoint(new Point2D.Double(x, y));
        System.out.println("added x:" + x + ", y:" + y);
    }

    private void dessin() {
        Iterator<Template> iterator = listeTemplate2.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Template nextTemp = iterator.next();
            Graphics2D g = (Graphics2D) this.getGraphics();
            // Dessine la liste de points de chaque stroke

            nextTemp.dessinerStroke(g, i);
            i++;
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonSave = new javax.swing.JButton();
        buttonLoad = new javax.swing.JButton();
        panel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        buttonSave.setText("Save Template");
        buttonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSaveActionPerformed(evt);
            }
        });

        buttonLoad.setText("Load Template");
        buttonLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonLoadActionPerformed(evt);
            }
        });

        panel.setName(""); // NOI18N

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 216, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonLoad)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonSave)
                    .addComponent(buttonLoad))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSaveActionPerformed
        try {
            // TODO add your handling code here:
            saveTemplate();
        } catch (IOException ex) {
            Logger.getLogger(TemplateManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_buttonSaveActionPerformed

    private void buttonLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonLoadActionPerformed
        try {
            loadTemplate();
        } catch (IOException ex) {
            Logger.getLogger(TemplateManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_buttonLoadActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TemplateManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TemplateManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TemplateManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TemplateManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new TemplateManager().setVisible(true);
                } catch (IvyException ex) {
                    Logger.getLogger(TemplateManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonLoad;
    private javax.swing.JButton buttonSave;
    private javax.swing.JPanel panel;
    // End of variables declaration//GEN-END:variables

}

/*

 FileOutputStream fop = null;
 String contenu = "This is the text content";

 try {

 fichier = new File("c:/newfile.txt");
 fop = new FileOutputStream(fichier);

 // if file doesnt exists, then create it
 if (!fichier.exists()) {
 fichier.createNewFile();
 }

 // get the content in bytes
 byte[] contentInBytes = contenu.getBytes();

 fop.write(contentInBytes);
 fop.flush();
 fop.close();

 System.out.println("Done");

 } catch (IOException e) {
 e.printStackTrace();
 } finally {
 try {
 if (fop != null) {
 fop.close();
 }
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
 */

/*
 setLayout(null);
 saveFile = new JButton("Save FIle");
 saveFile.setVisible(true);
 saveFile.addActionListener(new ActionListener() {

 @Override
 public void actionPerformed(ActionEvent e) {
 try {
 saveTemplate();
 } catch (IOException ex) {
 Logger.getLogger(TemplateManager.class.getName()).log(Level.SEVERE, null, ex);
 }
 }
 });
        
 this.add(saveFile);
 */
/*
 private void saveTemplate2() throws IOException {
 stroke.normalize();
 System.out.println("saveTemplates");
 byte[] data;
 FileOutputStream out = new FileOutputStream("./res/Templates.txt");

 ObjectOutputStream object = new ObjectOutputStream(out);
 object.writeObject(stroke);

 out.close();
 System.out.println("Done");

 dragActived = false;
 }//Fin_saveTemplates

 */
