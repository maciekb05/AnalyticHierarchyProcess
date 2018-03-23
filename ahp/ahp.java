import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ahp {
    private static Representation representation;
    private static InputStreamReader rd;
    private static BufferedReader bfr;

    public static void main(String[] args) {
        rd = new InputStreamReader(System.in);
        bfr = new BufferedReader(rd);

        System.out.println("Witaj");
        Boolean end = false;
        while(!end){

            InputStreamReader rd = new InputStreamReader(System.in);
            BufferedReader bfr = new BufferedReader(rd);

            System.out.println("Co chcialbys zrobic? ");
            System.out.println("1. Wprowadz dane");
            System.out.println("2. Odczytaj dane");
            System.out.println("3. Zapisz wprowadzone lub odczytane dane");
            System.out.println("4. Wyjdz");

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
                        //Questions.prepareRepresentation(representation,bfr);
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
