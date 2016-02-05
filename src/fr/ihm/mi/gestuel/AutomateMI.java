/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ihm.mi.gestuel;

/**
 *
 * @author Boris
 */
public class AutomateMI {

    private Etat currentState;

    public enum Etat {

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

   public AutomateMI() {
        currentState = Etat.Idle;
    }

    public boolean changeState(Etat state) {

        boolean changeStateOK = false;
        
        System.out.println("\n> Mode Actuel :" + currentState);
        System.out.println("> Demande de changement d'état vers: " + state);
        switch (currentState) {
            case Idle:
                switch (state) {
                    case Idle:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case CreationRectangle:
                        System.out.println("Mode CreationRectangle\n");
                        currentState = state;
                        changeStateOK = true;
                        break;
                    case CreationEllipse:
                        System.out.println("Mode CreationEllipse\n");
                        currentState = state;
                        changeStateOK = true;
                        break;
                    case Deplacement:
                        System.out.println("Mode Deplacement\n");
                        currentState = state;
                        changeStateOK = true;
                        break;
                    case Suppression:
                        System.out.println("Mode Suppression\n");
                        currentState = state;
                        changeStateOK = true;
                        break;
                    case Positionnement:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Coloration:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Pointage:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Designation:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                }
                break; //FIN_Idle
            //----------------------------------------------------------------//
            case CreationRectangle:
                switch (state) {
                    case Idle:
                        System.out.println("Mode Idle\n");
                        currentState = state;
                        changeStateOK = true;
                        break;
                    case CreationRectangle:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case CreationEllipse:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Deplacement:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Suppression:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Positionnement:
                        System.out.println("Mode Positionnement\n");
                        currentState = state;
                        changeStateOK = true;
                        break;
                    case Coloration:
                        System.out.println("Mode Coloration\n");
                        currentState = state;
                        changeStateOK = true;
                        break;
                    case Pointage:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Designation:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                }
                break; //FIN_CreationRectangle
            //----------------------------------------------------------------//
            case CreationEllipse:
                switch (state) {
                    case Idle:
                        System.out.println("Mode Idle\n");
                        currentState = state;
                        changeStateOK = true;
                        break;
                    case CreationRectangle:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case CreationEllipse:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Deplacement:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Suppression:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Positionnement:
                        System.out.println("Mode Positionnement\n");
                        currentState = state;
                        changeStateOK = true;
                        break;
                    case Coloration:
                        System.out.println("Mode Coloration\n");
                        currentState = state;
                        changeStateOK = true;
                        break;
                    case Pointage:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Designation:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                }
                break; //FIN_CreationEllipse
            //----------------------------------------------------------------//   
            case Deplacement:
                switch (state) {
                    case Idle:
                        System.out.println("Mode Idle\n");
                        currentState = state;
                        changeStateOK = true;
                        break;
                    case CreationRectangle:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case CreationEllipse:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Deplacement:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Suppression:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Positionnement:
                        System.out.println("Mode Positionnement\n");
                        currentState = state;
                        changeStateOK = true;
                        break;
                    case Coloration:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Pointage:
                        System.out.println("Mode Pointage\n");
                        currentState = state;
                        changeStateOK = true;
                        break;
                    case Designation:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                }
                break; //FIN_Deplacement
            //----------------------------------------------------------------//
            case Suppression:
                switch (state) {
                    case Idle:
                        System.out.println("Mode Idle\n");
                        currentState = state;
                        changeStateOK = true;
                        break;
                    case CreationRectangle:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case CreationEllipse:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Deplacement:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Suppression:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Positionnement:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Coloration:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Pointage:
                        System.out.println("Mode Pointage\n");
                        currentState = state;
                        changeStateOK = true;
                        break;
                    case Designation:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                }
                break; //FIN_Suppression
            //----------------------------------------------------------------//
            case Positionnement:
                switch (state) {
                    case Idle:
                        System.out.println("Mode Idle\n");
                        currentState = state;
                        changeStateOK = true;
                        break;
                    case CreationRectangle:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case CreationEllipse:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Deplacement:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Suppression:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Positionnement:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Coloration:
                        System.out.println("Mode Coloration\n");
                        currentState = state;
                        changeStateOK = true;
                        break;
                    case Pointage:
                        System.out.println("Mode Pointage\n");
                        currentState = state;
                        changeStateOK = true;
                        break;
                    case Designation:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                }
                break; //FIN_Positionnement
            //----------------------------------------------------------------//    
            case Coloration:
                switch (state) {
                    case Idle:
                        System.out.println("Mode Idle\n");
                        currentState = state;
                        changeStateOK = true;
                        break;
                    case CreationRectangle:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case CreationEllipse:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Deplacement:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Suppression:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Positionnement:
                        System.out.println("Mode Positionnement\n");
                        currentState = state;
                        changeStateOK = true;
                        break;
                    case Coloration:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Pointage:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Designation:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                }
                break; //FIN_Coloration
            //----------------------------------------------------------------//    
            case Pointage:
                switch (state) {
                    case Idle:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case CreationRectangle:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case CreationEllipse:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Deplacement:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Suppression:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Positionnement:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Coloration:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Pointage:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Designation:
                        System.out.println("Mode Designation\n");
                        currentState = state;
                        changeStateOK = true;
                        break;
                }
                break; //FIN_Pointage
            //----------------------------------------------------------------//    
            case Designation:
                switch (state) {
                    case Idle:
                        System.out.println("Mode Idle\n");
                        currentState = state;
                        changeStateOK = true;
                        break;
                    case CreationRectangle:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case CreationEllipse:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Deplacement:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Suppression:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Positionnement:
                        System.out.println("Mode Positionnement\n");
                        currentState = state;
                        changeStateOK = true;
                        break;
                    case Coloration:
                        System.out.println("Mode Coloration\n");
                        currentState = state;
                        changeStateOK = true;
                        break;
                    case Pointage:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                    case Designation:
                        //INTERDIT
                        changeStateOK = false;
                        break;
                }
                break; //FIN_Designation
                //----------------------------------------------------------------//  
                
        }
        return changeStateOK;
    }
}
