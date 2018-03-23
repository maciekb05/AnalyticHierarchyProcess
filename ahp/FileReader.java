import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;


public class FileReader {
//    public static Representation representation;

//    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
//        InputStreamReader rd = new InputStreamReader(System.in);
//        BufferedReader bfr = new BufferedReader(rd);
//
//        System.out.print("Podaj nazwe pliku z jakiego czytac: ");
//        String in = bfr.readLine();
//        parseFile(in);
//
//        Questions.prepareRepresentation(representation,bfr);
//        rd.close();
//        bfr.close();
//    }

    public static void parseFile(Representation representation, String fileName) throws ParserConfigurationException, IOException, SAXException{

        //representation = new Representation();

        representation.choices = new LinkedList<>();
        representation.root = new ElementAHP();
        representation.root.parent = representation.root;
        representation.root.children = new LinkedList<>();
        representation.root.level = 0;

        File inputFile = new File(fileName);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);

        doc.getDocumentElement().normalize();

        Element root = doc.getDocumentElement();

        if(!root.getNodeName().equals("ROOT")){
            System.out.println("Zly element ROOT w pliku xml");
        }
        else{
            NodeList choices = root.getElementsByTagName("CHOICE");
            for (int i = 0; i < choices.getLength(); i++) {
                Node choice = choices.item(i);
                representation.choices.add(choice.getTextContent());
            }
            NodeList listNodes = root.getChildNodes();
            for (int i = 0; i < listNodes.getLength(); i++) {
                Node current = listNodes.item(i);
                if(current.getNodeName().equals("CRITERION")){
                    Element el = (Element) current;
                    representation.root.name = el.getAttribute("name");
                    representation.root.level = 0;
                    representation.root.matrix = new LinkedList<>();
                    parseMatrix(el,representation.root);

                    Queue<ElementToParse> queue = new LinkedList<>();

                    NodeList list = el.getChildNodes();
                    for (int j = 0; j < list.getLength(); j++) {
                        ElementToParse elementToParse = new ElementToParse();
                        if(list.item(j).getNodeType() == Node.ELEMENT_NODE) {
                            elementToParse.element = (Element) list.item(j);
                            elementToParse.parent = representation.root;
                            queue.add(elementToParse);
                        }

                    }
                    ElementToParse currentEl;
                    while(true){
                        if(queue.isEmpty()){
                            break;
                        }
                        currentEl = queue.poll();
                        ElementAHP elementAHP = new ElementAHP();
                        currentEl.parent.children.add(elementAHP);
                        elementAHP.parent = currentEl.parent;
                        elementAHP.name = currentEl.element.getAttribute("name");
                        elementAHP.children = new LinkedList<>();
                        elementAHP.level = elementAHP.parent.level+1;
                        elementAHP.matrix = new LinkedList<>();
                        parseMatrix(currentEl.element,elementAHP);

                        NodeList listChild = currentEl.element.getChildNodes();
                        for (int j = 0; j < listChild.getLength(); j++) {
                            if(listChild.item(j).getNodeType() == Node.ELEMENT_NODE) {
                                ElementToParse elementToParse = new ElementToParse();
                                elementToParse.element = (Element) listChild.item(j);
                                elementToParse.parent = elementAHP;
                                queue.add(elementToParse);
                            }
                        }
                    }


                }
            }

        }

    }

    private static void parseMatrix(Element from, ElementAHP to){
        String matrix = from.getAttribute("m");
        String[] rows = matrix.split(";",0);
        Integer nrow = 0;
        for(String row : rows){
            String[] cels = row.split(" ",0);
            to.matrix.add(new LinkedList<>());
            for(String element : cels){
                Double value = Double.parseDouble(element);
                to.matrix.get(nrow).add(value);
            }
            nrow++;
        }
    }

}
