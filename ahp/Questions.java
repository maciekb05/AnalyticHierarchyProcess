import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Questions {
    private static Representation representation;
    private static InputStreamReader rd;
    private static BufferedReader bfr;

    public static void main(String[] args) throws IOException {
        rd = new InputStreamReader(System.in);
        bfr = new BufferedReader(rd);
        representation = new Representation();
        askChoices();
        askStructure();
        askMatrixes();
        prepareRepresentation();
    }

    private static void askChoices() throws IOException {
        String in;

        System.out.println("Alternatywy.");
        System.out.print("Podaj ilość alternatyw: ");

        in = bfr.readLine();
        Integer numberOfChoices = Integer.parseInt(in);

        representation.choices = new LinkedList<>();

        for(int i = 1; i <= numberOfChoices; i++){
            System.out.print("Podaj nazwe alternatywy ");
            System.out.print(i);
            System.out.print(": ");

            in = bfr.readLine();

            representation.choices.add(in);
        }
    }
    private static void askStructure() throws IOException {
        String in;
        Queue<Element> queue = new LinkedList<>();

        System.out.println("Struktura.");
        System.out.print("Podaj nazwe kryterium glownego: ");

        in = bfr.readLine();

        representation.root = new Element();
        representation.root.name = in;
        representation.root.parent = representation.root;
        representation.root.children = new LinkedList<>();
        representation.root.level = 0;

        askStructureElement(representation.root);
        Element parent = representation.root;
        Boolean end = false;
        while(!end){
            if(parent.children.size()!=0) { //Ma dzieci
                for (Element element : parent.children) {
                    askStructureElement(element);
                    queue.add(element);
                }
                parent = queue.poll();
            } else {
                end = true;
            }
        }
    }
    private static void askStructureElement(Element current) throws IOException {
        String in;

        while(true){
            System.out.print("\nCzy chcesz dodac podkryterium do ");
            System.out.print(current.name);
            System.out.print("? (t/n): ");
            in = bfr.readLine();
            if(in.charAt(0) == 'n') {
                System.out.println("Koniec");
                break;
            }
            else {
                System.out.print("Podaj nazwe: ");
                in = bfr.readLine();
                Element element = new Element();
                element.name = in;
                element.parent = current;
                element.children = new LinkedList<>();
                element.level = element.parent.level+1;
                current.children.add(element);
            }
        }
    }

    private static void askMatrixes() throws IOException {
        System.out.println("Porownania: ");
        Queue<Element> queue = new LinkedList<>();
        Element current = representation.root;
        askMatrixElement(current);
        Boolean end = false;
        while(!end){
            if(current.children.size()!=0) {
                for (Element element : current.children) {
                    askMatrixElement(element);
                    queue.add(element);
                }
                current = queue.poll();
            } else {
                end = true;
            }
        }
    }

    private static void askMatrixElement(Element element) throws IOException {
        initializeMatrix(element);
        String in;
        Integer size = element.children.size();
        if(size!=0){ //Ma podkategorie
            for(Integer i = 0; i < size; i++){
                for(Integer j = 0; j < size; j++){
                    if(j < i){
                        element.matrix.get(i).set(j,1/(element.matrix.get(j).get(i)));
                    }
                    else if(j > i) {
                        System.out.print("Ile razy kryterium ");
                        System.out.print(element.children.get(i).name);
                        System.out.print(" jest wazniejsze od kryterium ");
                        System.out.print(element.children.get(j).name);
                        System.out.print("?: ");
                        in = bfr.readLine();
                        element.matrix.get(i).set(j,Double.parseDouble(in));
                    }
                }
            }
        } else { //Jest ostatnia podkategoria
            size = representation.choices.size();
            for(Integer i = 0; i < size; i++){
                for(Integer j = 0; j < size; j++){
                    if(j < i){
                        element.matrix.get(i).set(j,1/(element.matrix.get(j).get(i)));
                    }
                    else if(j > i) {
                        System.out.print("Ile razy ");
                        System.out.print(representation.choices.get(i));
                        System.out.print(" jest lepsze od ");
                        System.out.print(representation.choices.get(j));
                        System.out.print(" pod wzgledem ");
                        System.out.print(element.name);
                        System.out.print("?: ");
                        in = bfr.readLine();
                        element.matrix.get(i).set(j,Double.parseDouble(in));
                    }
                }
            }
        }

    }
    private static void initializeMatrix(Element element) {
        Integer size = element.children.size();
        if(size == 0){
            size = representation.choices.size();
        }
        element.matrix = new LinkedList<>();
        for(Integer i = 0; i < size; i++){
            element.matrix.add(new LinkedList<>());
            for(Integer j = 0; j < size; j++){
                element.matrix.get(i).add(1.0);
            }
        }
    }

    private static void prepareRepresentation() throws IOException {
        String in;
        System.out.println("Zapisuje informacje");
        System.out.print("Jak chcialbys nazwac plik (UWAGA, " +
                "jesli nazwiesz plik tak jak istniejacy, " +
                "zostanie on nadpisany)?: ");
        in = bfr.readLine();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<ROOT>\n");

        for(String choice : representation.choices){
            stringBuilder.append("\t");
            stringBuilder.append("<CHOICE>");
            stringBuilder.append(choice);
            stringBuilder.append("</CHOICE>\n");
        }

        Stack<Element> stack = new Stack<>();
        Element current = representation.root;
        stringBuilder.append(getHeadingElement(current));

        Boolean end = false;
        Integer ending = 0;
        while(!end){
            if(current.children.size()!=0) { //Ma dzieci ?
                for (Integer i = current.children.size()-1; i >= 0; i--) {
                    stack.push(current.children.get(i));
                }
                ending++;
            }
            if(stack.empty()) //Pusto
                end = true;
            else{ //Kolejny ze stosu
                current = stack.pop();

                stringBuilder.append(getHeadingElement(current));

                //Czy jest ostatnim dzieckiem?
                if(current.parent.children.get(current.parent.children.size()-1).name.equals(current.name)){
                    //Nie mam dzieci?
                    if(current.children.size()==0){
                        ending--;
                        for (int i = 0; i < current.level-1; i++) {
                            stringBuilder.append("\t");
                        }
                        stringBuilder.append("\t</CRITERION>\n");
                        //Sprawdzanie czy domknac przodka?
                        Element checking = current.parent;
                        while(true){
                            if(checking == checking.parent){
                                break;
                            }
                            if(checking.parent.children.get(checking.parent.children.size()-1).name.equals(checking.name)){
                                ending--;
                                for (int i = 0; i < checking.level-1; i++) {
                                    stringBuilder.append("\t");
                                }
                                stringBuilder.append("\t</CRITERION>\n");
                            } else { break; }
                            checking = checking.parent;

                        }
                    }
                }
            }
        }

        for(Integer i = 0; i < ending; i++){
            stringBuilder.append("\t</CRITERION>\n");
        }
        stringBuilder.append("</ROOT>\n");
        System.out.println(stringBuilder);
        String mycontent = stringBuilder.toString();
        try {
            File file = new File("./"+in+".txt");
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(mycontent);
            System.out.println("Plik zapisano pomyslnie");
            bw.close();
            fw.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }
    private static String getHeadingElement(Element element) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int tab = 0; tab < element.level; tab++) {
            stringBuilder.append("\t");
        }
        stringBuilder.append("\t<CRITERION name=\"");
        stringBuilder.append(element.name);
        stringBuilder.append("\" m=\"");
        for(LinkedList<Double> row : element.matrix){
            for(Double value : row){
                stringBuilder.append(value);
                stringBuilder.append(" ");
            }
            stringBuilder.append(";");
        }
        stringBuilder.append("\"");
        if(element.children.size()==0){
            stringBuilder.append("/>\n");
        } else {
            stringBuilder.append(">\n");
        }

        return stringBuilder.toString();
    }
}
