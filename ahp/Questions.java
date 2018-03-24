import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Questions {
//    private static Representation representation;
//    private static InputStreamReader rd;
//    private static BufferedReader bfr;

//    public static void main(String[] args) throws IOException {
//        rd = new InputStreamReader(System.in);
//        bfr = new BufferedReader(rd);
//        representation = new Representation();
//        askChoices();
//        askStructure();
//        askMatrixes();
//        prepareRepresentation(representation,bfr);
//        rd.close();
//        bfr.close();
//    }

    public static void askChoices(Representation representation, BufferedReader bfr) throws IOException {
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
    public static void askStructure(Representation representation, BufferedReader bfr) throws IOException {
        String in;
        Queue<ElementAHP> queue = new LinkedList<>();

        System.out.println("Struktura.");
        System.out.print("Podaj nazwe kryterium glownego: ");

        in = bfr.readLine();

        representation.root = new ElementAHP();
        representation.root.name = in;
        representation.root.parent = representation.root;
        representation.root.children = new LinkedList<>();
        representation.root.level = 0;

        askStructureElement(representation.root, bfr);
        ElementAHP parent = representation.root;
        Boolean end = false;
        while(!end){
            if(parent.children.size()!=0) { //Ma dzieci
                for (ElementAHP elementAHP : parent.children) {
                    askStructureElement(elementAHP, bfr);
                    queue.add(elementAHP);
                }
                parent = queue.poll();
            } else {
                end = true;
            }
        }
    }
    private static void askStructureElement(ElementAHP current, BufferedReader bfr) throws IOException {
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
                ElementAHP elementAHP = new ElementAHP();
                elementAHP.name = in;
                elementAHP.parent = current;
                elementAHP.children = new LinkedList<>();
                elementAHP.level = elementAHP.parent.level+1;
                current.children.add(elementAHP);
            }
        }
    }

    public static void askMatrixes(Representation representation, BufferedReader bfr) throws IOException {
        System.out.println("Porownania: ");
        Queue<ElementAHP> queue = new LinkedList<>();
        ElementAHP current = representation.root;
        askMatrixElement(representation,current,bfr);
        Boolean end = false;
        while(!end){
            if(current.children.size()!=0) {
                for (ElementAHP elementAHP : current.children) {
                    askMatrixElement(representation,elementAHP, bfr);
                    queue.add(elementAHP);
                }
                current = queue.poll();
            } else {
                end = true;
            }
        }
    }

    private static void askMatrixElement(Representation representation, ElementAHP elementAHP, BufferedReader bfr) throws IOException {
        initializeMatrix(representation, elementAHP);
        String in;
        Integer size = elementAHP.children.size();
        if(size!=0){ //Ma podkategorie
            for(Integer i = 0; i < size; i++){
                for(Integer j = 0; j < size; j++){
                    if(j < i){
                        Double value = 1/(elementAHP.matrix.get(j).get(i));
                        String str = value.toString();
                        String[] splitted = str.split("\\.",2);
                        if(splitted[1].length()>12){
                            str = splitted[0]+"."+splitted[1].substring(0,12);
                        }
                        elementAHP.matrix.get(i).set(j, Double.parseDouble(str));
                    }
                    else if(j > i) {
                        System.out.print("Ile razy kryterium ");
                        System.out.print(elementAHP.children.get(i).name);
                        System.out.print(" jest wazniejsze od kryterium ");
                        System.out.print(elementAHP.children.get(j).name);
                        System.out.print("?: ");
                        in = bfr.readLine();
                        if(in.contains(".")){
                            String[] splitted = in.split("\\.",2);
                            if(splitted[1].length()>12){
                                in = splitted[0]+"."+splitted[1].substring(0,12);
                            }
                        }
                        elementAHP.matrix.get(i).set(j,Double.parseDouble(in));

                    }
                }
            }
        } else { //Jest ostatnia podkategoria
            size = representation.choices.size();
            for(Integer i = 0; i < size; i++){
                for(Integer j = 0; j < size; j++){
                    if(j < i){
                        Double value = 1/(elementAHP.matrix.get(j).get(i));
                        String str = value.toString();
                        String[] splitted = str.split("\\.",2);
                        if(splitted[1].length()>12){
                            str = splitted[0]+"."+splitted[1].substring(0,12);
                        }
                        elementAHP.matrix.get(i).set(j, Double.parseDouble(str));
                    }
                    else if(j > i) {
                        System.out.print("Ile razy ");
                        System.out.print(representation.choices.get(i));
                        System.out.print(" jest lepsze od ");
                        System.out.print(representation.choices.get(j));
                        System.out.print(" pod wzgledem ");
                        System.out.print(elementAHP.name);
                        System.out.print("?: ");
                        in = bfr.readLine();
                        if(in.contains(".")){
                            String[] splitted = in.split("\\.",2);
                            if(splitted[1].length()>12){
                                in = splitted[0]+"."+splitted[1].substring(0,12);
                            }
                        }
                        elementAHP.matrix.get(i).set(j,Double.parseDouble(in));
                    }
                }
            }
        }

    }
    private static void initializeMatrix(Representation representation, ElementAHP elementAHP) {
        Integer size = elementAHP.children.size();
        if(size == 0){
            size = representation.choices.size();
        }
        elementAHP.matrix = new LinkedList<>();
        for(Integer i = 0; i < size; i++){
            elementAHP.matrix.add(new LinkedList<>());
            for(Integer j = 0; j < size; j++){
                elementAHP.matrix.get(i).add(1.0);
            }
        }
    }

    public static void prepareRepresentation(Representation representation, BufferedReader bfr) throws IOException {
        String in;
        System.out.println("Zapisuje informacje");
        System.out.print("Jak chcialbys nazwac plik (UWAGA, " +
                "jesli nazwiesz plik tak jak istniejacy, " +
                "zostanie on nadpisany)?: ");
        in = bfr.readLine();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<ROOT>\n");

        for(String choice : representation.choices){
            System.out.println(choice);
            stringBuilder.append("\t");
            stringBuilder.append("<CHOICE>");
            stringBuilder.append(choice);
            stringBuilder.append("</CHOICE>\n");
        }

        Stack<ElementAHP> stack = new Stack<>();
        ElementAHP current = representation.root;
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
                        ElementAHP checking = current.parent;
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
            File file = new File("./"+in+".xml");
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
    private static String getHeadingElement(ElementAHP elementAHP) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int tab = 0; tab < elementAHP.level; tab++) {
            stringBuilder.append("\t");
        }
        stringBuilder.append("\t<CRITERION name=\"");
        stringBuilder.append(elementAHP.name);
        stringBuilder.append("\" m=\"");
        for(LinkedList<Double> row : elementAHP.matrix){
            for(Double value : row){
                stringBuilder.append(value);
                stringBuilder.append(" ");
            }
            stringBuilder.setLength(stringBuilder.length() - 1);
            stringBuilder.append(";");
        }
        stringBuilder.setLength(stringBuilder.length() - 1);
        stringBuilder.append("\"");
        if(elementAHP.children.size()==0){
            stringBuilder.append("/>\n");
        } else {
            stringBuilder.append(">\n");
        }

        return stringBuilder.toString();
    }
}
