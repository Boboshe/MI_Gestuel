/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ihm.mi.gestuel;

/**
 *
 * @author terooabo
 */

public class Automate {
 
    private Etat currentState;
    
   enum Etat {
       Idle,
       CreationRectangle,
       CreationEllipse,
       Suppression,
       Deplacement;
   }
  enum Evenement {
       ;
   }
   
   public Automate() {
       currentState = Etat.Idle;
   }
   
   public void setToIdle() {
       currentState = Etat.Idle;
   }
   
   public Etat changeState(Etat state) {
       
       System.out.println("> Mode Actuel :"+ currentState);
       
       switch (currentState)
       {
           case Idle : 
           {
               currentState = state;
               break;
           }
           
           case CreationRectangle: 
           {
               if(state == Etat.Idle)
               {
                   System.out.println("Mode Créationn Rectangle");
                   currentState = state;
               }
               else
                   System.out.println("Rectangle : Changement de mode Impossible");
               break;
           } 
           
           case CreationEllipse: 
           {
               if(state == Etat.Idle)
               {
                   System.out.println("Mode Création Ellipse");
                   currentState = state;
               }
               else
                   System.out.println("Ellipse : Changement de mode Impossible");
               break;
           } 
           
           case Suppression: 
           {
               if(state == Etat.Idle)
               {
                   System.out.println("Mode suppression");
                   currentState = state;
               }
               else
                   System.out.println("suppression : Changement de mode Impossible");
               break;
           } 
           
           case Deplacement: 
           {
               if(state == Etat.Idle)
               {
                   System.out.println("Mode déplacement");
                   currentState = state;
               }
               else
                   System.out.println("Déplacement : Changement de mode Impossible");
               break;
           } 
          
       }
       return currentState;
   }
   
    
}
