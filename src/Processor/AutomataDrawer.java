package Processor;

import file.Codec;
import file.ParseException;
import gui.action.NewAction;
import gui.action.OpenAction;
import gui.environment.Profile;
import gui.environment.Universe;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class AutomataDrawer {

    /**
     * @param args
     */
    private boolean dontQuit; // Don't quit when Quit selected

    public boolean getDontQuit() {
        return dontQuit;
    }

    public void show() {
        dontQuit = false;
        // Make sure we're not some old version.
        try {
            String v = System.getProperty("java.specification.version");
            double version = Double.parseDouble(v) + 0.00001;
            if (version < 1.5) {
                javax.swing.JOptionPane.showMessageDialog(null,
                        "Java 1.5 or higher required to run JFLAP!\n"
                        + "You appear to be running Java " + v + ".\n"
                        + "This program will now exit.");
                System.exit(0);
            }
        } catch (SecurityException e) {
            // Eh, that shouldn't happen.
        }

        try {
            if (gui.ThrowableCatcher.class == null)
				;
            System.setProperty("sun.awt.exception.handler",
                    "gui.ThrowableCatcher");
        } catch (SecurityException e) {
            System.err.println("Warning: could not set the "
                    + "AWT exception handler.");
        }

        // Prompt the user for newness.
        NewAction.showNew();
        //loadPreferences();
    }

    public void execute() {

        dontQuit = false;
        // Make sure we're not some old version.
        try {
            String v = System.getProperty("java.specification.version");
            double version = Double.parseDouble(v) + 0.00001;
            if (version < 1.5) {
                javax.swing.JOptionPane.showMessageDialog(null,
                        "Java 1.5 or higher required to run JFLAP!\n"
                        + "You appear to be running Java " + v + ".\n"
                        + "This program will now exit.");
                System.exit(0);
            }
        } catch (SecurityException e) {
            // Eh, that shouldn't happen.
        }

        try {
            if (gui.ThrowableCatcher.class == null)
				;
            System.setProperty("sun.awt.exception.handler",
                    "gui.ThrowableCatcher");
        } catch (SecurityException e) {
            System.err.println("Warning: could not set the "
                    + "AWT exception handler.");
        }

        // Prompt the user for newness.
        NewAction.showNew();
        NewAction.closeNew();
        @SuppressWarnings("unchecked")
        Codec[] codecs = (Codec[]) Universe.CODEC_REGISTRY.getDecoders().toArray(new Codec[0]);
        try {
            OpenAction.openFile(new File("out.jff"), codecs);
        } catch (ParseException e) {
            System.out.println("no process");
        }

        try {
            ExcelProcessor wr = new ExcelProcessor("outfile");

            File fXmlFile = new File("out.jff");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList states = doc.getElementsByTagName("state");
            NodeList node_childs;
            for (int i = 0; i < states.getLength(); i++) {
                System.out.println(states.item(i).getNodeName());
                Element cur = (Element) states.item(i);
                System.out.println(cur.getAttribute("id"));
                node_childs = states.item(i).getChildNodes();
                String name = cur.getAttribute("name");

                for (int j = 0; j < node_childs.getLength(); j++) {
                    if (node_childs.item(j).getNodeName() == "label") {
                        System.out.println(node_childs.item(j).getTextContent());
                        name = node_childs.item(j).getTextContent();
                    }
                }

                wr.addCellInString(i + 1, 0, name);	//remove condition column
                wr.addCellInString(0, i + 1, name);
            }
            NodeList transitions = doc.getElementsByTagName("transition");
            for (int i = 0; i < transitions.getLength(); i++) {
                System.out.println(transitions.item(i).getNodeName());
                System.out.println(transitions.item(i).getNodeValue());
                
                //get list transition
                node_childs = transitions.item(i).getChildNodes();
                
                int row = 0;
                int column = 0;
                for (int j = 0; j < node_childs.getLength(); j++) {
                	
                    if (node_childs.item(j).getNodeName().equals("from")) {
                        row = Integer.parseInt(node_childs.item(j).getTextContent()) + 1;
                        System.out.println("row: " + row);
                    }
                    
                    if (node_childs.item(j).getNodeName().equals("to")) {
                        column = Integer.parseInt(node_childs.item(j).getTextContent()) + 2;
                        System.out.println("column:" + column);
                    }
                    
                    if (node_childs.item(j).getNodeName().equals("read")) {
                        System.out.println("column:" + column + "/" + "row: " + row);
                        String label = node_childs.item(j).getTextContent();
                        
                        wr.addCellInString(column, row, label);
                    }
                }
            }
            wr.close();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("exited");
        }
        loadPreferences();
    }

    /**
     * This method loads from the preferences file, if one exists.
     */
    private void loadPreferences() {
        Profile current = Universe.curProfile;
        String path = "";
        try {
            path = new File(".").getCanonicalPath();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        path = path + "/jflapPreferences.xml";
        current.pathToFile = path;

        if (new File(path).exists()) {
            File file = new File(path);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            try {
                builder = factory.newDocumentBuilder();
                Document doc;

                doc = builder.parse(file);

                // Set the empty string constant
                Node parent = doc.getDocumentElement().getElementsByTagName(current.EMPTY_STRING_NAME).item(0);
                if (parent != null) {
                    String empty = parent.getTextContent();
                    if (empty.equals(current.lambdaText)) {
                        current.setEmptyString(current.lambda);
                    } else if (empty.equals(current.epsilonText)) {
                        current.setEmptyString(current.epsilon);
                    }
                }

                // Then set the Turing final state constant
                parent = doc.getDocumentElement().getElementsByTagName(current.TURING_FINAL_NAME).item(0);
                if (parent != null) {
                    String turingFinal = parent.getTextContent();
                    if (turingFinal.equals("true")) {
                        current.setTransitionsFromTuringFinalStateAllowed(true);
                    } else {
                        current.setTransitionsFromTuringFinalStateAllowed(false);
                    }
                }

                // set the Turing Acceptance ways.
                parent = doc.getDocumentElement().getElementsByTagName(current.ACCEPT_FINAL_STATE).item(0);
                if (parent != null) {
                    String acceptFinal = parent.getTextContent();
                    if (acceptFinal.equals("true")) {
                        current.setAcceptByFinalState(true);
                    } else {
                        current.setAcceptByFinalState(false);
                    }
                }

                parent = doc.getDocumentElement().getElementsByTagName(current.ACCEPT_HALT).item(0);
                if (parent != null) {
                    String acceptHalt = parent.getTextContent();
                    if (acceptHalt.equals("true")) {
                        current.setAcceptByHalting(true);
                    } else {
                        current.setAcceptByHalting(false);
                    }

                }

                // set the AllowStay option
                parent = doc.getDocumentElement().getElementsByTagName(current.ALLOW_STAY).item(0);
                if (parent != null) {
                    String allowStay = parent.getTextContent();
                    if (allowStay.equals("true")) {
                        current.setAllowStay(true);
                    } else {
                        current.setAllowStay(false);
                    }
                }

                // Now set the Undo amount
                parent = doc.getDocumentElement().getElementsByTagName(current.UNDO_AMOUNT_NAME).item(0);
                if (parent != null) {
                    String number = parent.getTextContent();
                    current.setNumUndo(Integer.parseInt(number));
                }
            } catch (ParserConfigurationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SAXException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
}
