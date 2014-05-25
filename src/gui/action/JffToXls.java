/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.action;

import gui.environment.Environment;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import Processor.ExcelProcessor;
import automata.Transition;
import automata.fsa.FiniteStateAutomaton;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author hoatrantrong
 */
public class JffToXls extends FSAAction {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new <CODE>NFAToDFAAction</CODE>.
     * 
     * @param automaton
     *            the automaton that input will be simulated on
     * @param environment
     *            the environment object that we shall add our simulator pane to
     */
    public JffToXls(FiniteStateAutomaton automaton, Environment environment) {
        super("Export to Excel", null);
        this.automaton = automaton;
        this.environment = environment;
        /*
         * putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke (KeyEvent.VK_R,
         * MAIN_MENU_MASK+InputEvent.SHIFT_MASK));
         */
    }

    /**
     * Puts the DFA form in another window.
     * 
     * @param e
     *            the action event
     */
    public void actionPerformed(ActionEvent e) {
        String filename = (String) JOptionPane.showInputDialog(
                null, "Please enter file name: ");
        System.out.println("Export to ------------- " + filename + " -------------");
        if (filename.equals("")) {
            JOptionPane.showConfirmDialog(environment, "Filename must not be empty",
                    "Filename must not be empty", 0);
            return;
        }
        
        ExcelProcessor wr = new ExcelProcessor(filename);
        wr.addCellInNumber(0, 0, automaton.getStates().length);
        wr.addCellInString(0, 1, "Transition");
        for (int i = 0; i < automaton.getStates().length; i++) {
        	
            System.out.println("state: " + i);
            
            //Get states to each row
            if (automaton.getStates()[i].getName() != "") {
                wr.addCellInString(0, i + 2,
                        automaton.getStates()[i].getName());
            }
            
            List<String> event_list = new ArrayList<String>();
            //List<String> condition_list_wr = new ArrayList<String>();
            //Get states to each column
            for (int j = 0; j < automaton.getStates().length; j++) {
            	
                if (automaton.getStates()[i].getName() != "") {
                    wr.addCellInString(j + 1, 1,
                            automaton.getStates()[j].getName());
                }
                
                Transition[] t = automaton.getTransitionsFromStateToState(
                        automaton.getStates()[i], automaton.getStates()[j]);
                if (t.length != 0) {
                    String result = "";
                    String label = t[t.length - 1].getDescription();
                    //String label = t.toString();
                    
                    event_list.add(label);
                    result = result + label;
                    wr.addCellInString(j + 1, i + 2, result);
                }
            }
            
        }
        wr.close();
        JOptionPane.showConfirmDialog(environment, "Export successfully",
                "Export successfully", 0);
    }
    /** The automaton. */
    private FiniteStateAutomaton automaton;
    /** The environment. */
    private Environment environment;
}
