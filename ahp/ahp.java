import java.io.*;
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
            System.out.println("5. Oblicz ranking metoda EVM");
            System.out.println("6. Wyjdz");

            System.out.println("Wybor: ");
            String in = "6"; //Exit by default
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
                            System.out.println(e.getMessage());
                            continue;
                        }
                    }
                    break;
                case "4":
                    try {
                        RankAHP.evaluateRanks(representation, 1);
                        ranking = RankAHP.ranking(representation);
                        for(int i = 0; i < ranking.size(); i++){
                            System.out.print("Wybor: " + representation.choices.get(i) + ": ");
                            System.out.println(ranking.get(i));
                        }
                    }catch(Exception e){
                        System.out.println("ERROR!!!");
                        e.printStackTrace();
                        System.out.println("Sprobuj jeszcze raz");
                        continue;
                    }
                    break;
                case "5":
                    try{
                        RankAHP.evaluateRanks(representation,2);
                        ranking = RankAHP.ranking(representation);
                        for(int i = 0; i < ranking.size(); i++){
                            System.out.print("Wybor: " + representation.choices.get(i) + ": ");
                            System.out.println(ranking.get(i));
                        }
                    }catch (Exception e){
                        System.out.println("ERROR!!!");
                        e.printStackTrace();
                        System.out.println("Sprobuj jeszcze raz");
                        continue;
                    }

                    break;
                case "6":
                    try {
                        File file = new File("err.txt");
                        FileOutputStream fos = new FileOutputStream(file);
                        PrintStream ps = new PrintStream(fos);
                        System.setErr(ps);
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
