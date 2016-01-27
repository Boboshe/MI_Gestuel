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
        Deplacement,
        Positionnement,
        Coloration;
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

        System.out.println("> Mode Actuel :" + currentState);

        switch (currentState) {
            case Idle: {
                currentState = state;
                break;
            }

            case CreationRectangle: {
//                if (state == Etat.Idle) {
//                    System.out.println("Mode créationn rectangle");
//                    currentState = state;
//                } else {
//                    System.out.println("Creer rectangle : Changement de mode Impossible");
//                }

                switch (state) {
                    case Idle:
                        System.out.println("Mode Idle");//Retour 
                        currentState = state;
                        break;
                    case Positionnement:
                        System.out.println("Mode Positionnement");
                        currentState = state;
                        break;
                    case Coloration:
                        System.out.println("Mode Coloration");
                        currentState = state;
                        break;
                    default:
                        System.out.println("Creer rectangle Impossible");
                }
            }

            case CreationEllipse: {
                if (state == Etat.Idle) {
                    System.out.println("Mode création ellipse");
                    currentState = state;
                } else {
                    System.out.println("Creer ellipse Impossible");
                }
                break;
            }

            case Suppression: {
                if (state == Etat.Idle) {
                    System.out.println("Mode suppression");
                    currentState = state;
                } else {
                    System.out.println("Suppression Impossible");
                }
                break;
            }

            case Deplacement: {
                if (state == Etat.Idle) {
                    System.out.println("Mode déplacement");
                    currentState = state;
                } else {
                    System.out.println("Déplacement Impossible");
                }
                break;
            }

            case Positionnement: {
                switch (state) {
                    case Idle:
                        System.out.println("Mode Idle");//Retour 
                        currentState = state;
                        break;
                    case Coloration:
                        System.out.println("Mode Coloration");
                        currentState = state;
                        break;
                    default:
                        System.out.println("Positionnement Impossible");
                }
            }
            
            case Coloration: { //TODO => passage à Position => exe en parallèle les timers
                switch (state) {
                    case Idle:
                        System.out.println("Mode Idle");//Retour 
                        currentState = state;
                        break;
                    default:
                        System.out.println("Coloration Impossible");
                }
            }

        }
        return currentState;
    }

}
