import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class ahp {
    private static Representation representation;

    public static void main(String[] args) {

        System.out.println("Witaj");
        Boolean end = false;
        while(!end){
            LinkedList<Double> ranking;

            InputStreamReader rd = new InputStreamReader(System.in);
            BufferedReader bfr = new BufferedReader(rd);

            System.out.println("Co chcialbys zrobic? ");
            System.out.println("1. Wprowadz dane");
            System.out.println("2. Odczytaj dane");
            System.out.println("3. Zapisz wprowadzone lub odczytane dane");
            System.out.println("4. Oblicz ranking metoda GMM");
            System.out.println("5. Oblicz ranking metoda OTHER");
            System.out.println("6. Wyjdz");

            System.out.println("Wybor: ");
            String in = null;
            try {
                in = bfr.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            switch(in){
                case "1":
                    try {
                        representation = new Representation();
                        Questions.askChoices(representation,bfr);
                        Questions.askStructure(representation,bfr);
                        Questions.askMatrixes(representation,bfr);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "2":
                    System.out.print("Podaj nazwe pliku z jakiego czytac: ");
                    try {
                        representation = new Representation();
                        in = bfr.readLine();
                        FileReader.parseFile(representation,in+".xml");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "3":
                    if (representation!=null) {
                        try {
                            Questions.prepareRepresentation(representation,bfr);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "4":
                    RankAHP.evaluateRanks(representation,1);
                    ranking = RankAHP.ranking(representation);
                    for(Double value : representation.root.rank){
                        System.out.println(value);
                    }
                    for(Double value : representation.root.children.get(0).rank){
                        System.out.println(value);
                    }
                    for(Double value : representation.root.children.get(1).rank){
                        System.out.println(value);
                    }
                    for(Double value : representation.root.children.get(2).rank){
                        System.out.println(value);
                    }
                    for(Double value : ranking){
                        System.out.println(value);
                    }
                    break;
                case "5":
                    RankAHP.evaluateRanks(representation,2);
                    ranking = RankAHP.ranking(representation);
                    for(Double value : representation.root.rank){
                        System.out.println(value);
                    }
                    for(Double value : ranking){
                        System.out.println(value);
                    }
                    break;
                case "6":
                    try {
                        bfr.close();
                        rd.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    end = true;
                    break;
                default:

            }
        }
    }
}
