/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ihm.mi.gestuel.trash;

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
        Deplacement,
        Suppression,
        Positionnement,
        Coloration,
        Pointage,
        Designation;
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
                switch (state) {
                    case Idle:
                        System.out.println("Mode Idle");
                        break;
                    case CreationRectangle:
                        System.out.println("Mode CreationRectangle");
                        break;
                    case CreationEllipse:
                        System.out.println("Mode CreationEllipse");
                        break;
                    case Deplacement:
                        System.out.println("Mode Deplacement");
                        break;
                    case Suppression:
                        System.out.println("Mode Suppression");
                        break;
                    case Positionnement:
                        System.out.println("Mode Positionnement");
                        break;
                    case Coloration:
                        System.out.println("Mode Coloration");
                        break;
                    case Pointage:
                        System.out.println("Mode Pointage");
                        break;
                    case Designation:
                        System.out.println("Mode Designation");
                        break;
                }
            } //Fin_IDLE

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
    
    
    
    
    /**
     * 
     * System.out.println("Creation du Rectancle\n");
                //On vérifie que le changement d'Etat est possible, et
                //On fait le changement d'Etat
                //On execute l'action
                //Puis on reviens dans l'Etat Idle
                if (Etat.CreationRectangle == myAutomate.changeState(Etat.CreationRectangle)) {

//                    if (positionStated && colorStated) {
//                        creerRectangle(xCur, yCur, 100, 200, couleurFond, couleurContour);
//                    }
                    startForPosition = System.currentTimeMillis();
                    endForPosition = startForPosition + (10 * 1000); //10 sec
                    System.out.println("1er while");
                    while (System.currentTimeMillis() < endForPosition) {
                        System.out.print("-");
                        if (positionStated) {
                            if (Etat.Positionnement == myAutomate.changeState(Etat.Positionnement)) {
                                myPosition.setLocation(xCur, yCur);
//                                myPosition.x = xCur;
//                                myPosition.y = yCur;
                            }
                        }
                    }
                    
                    System.out.println("2eme while");
                    startForColor = System.currentTimeMillis();
                    endForColor = startForColor + (10 * 1000); //10 sec
                    while (System.currentTimeMillis() < endForColor) {
                        if (colorStated) {
                            if (Etat.Coloration == myAutomate.changeState(Etat.Coloration)) {
                                //creerRectangle(xCur, yCur, 100, 200, couleurFond, couleurContour);
                                myColorContour = couleurContour;
                                myColorFond = couleurFond;
                            }
                        }
                    }
                    System.out.println("**************** Rectangle créée");
                    creerRectangle(myPosition.x, myPosition.y, 100, 100, myColorFond, myColorContour);
                    myAutomate.setToIdle();
                }
     * 
     * 
     * 
     * 
     * 
     * if (Etat.CreationEllipse == myAutomate.changeState(Etat.CreationEllipse)) {
                    if (positionStated && colorStated) {
                        creerEllipse(xCur, yCur, 100, 200, couleurFond, couleurContour);
                    } else if (positionStated) {
                        creerEllipse(xCur, yCur, 100, 100);
                    }
                    myAutomate.setToIdle();
                }
     */

}
