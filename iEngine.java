import java.io.BufferedReader;
import java.io.FileReader;

public class iEngine {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: iEngine [FC|BC|TT] [input_file]");
            System.exit(1);
        }

        String method = args[0].trim().toUpperCase();
        String inputFile = args[1];

        PropositionalLogic exp = new PropositionalLogic();
        ForwardChaining fc = new ForwardChaining();
        BackwardChaining bc = new BackwardChaining();

        try (BufferedReader inputStream = new BufferedReader(new FileReader(inputFile))) {
            String buffer;
            String nextLine = "";

            while ((buffer = inputStream.readLine()) != null) {
                if (buffer.startsWith("TELL")) {
                    nextLine = "TELL";
                } else if (buffer.equals("ASK")) {
                    nextLine = "ASK";
                } else {
                    if (nextLine.equals("TELL")) {
                        switch (method) {
                            case "FC" -> fc.readTell(buffer);
                            case "BC" -> bc.readTell(buffer);
                            case "TT" -> exp.readTell(buffer);
                            default -> {
                                System.out.println("Method not recognized: " + method);
                                System.exit(1);
                            }
                        }
                    } else if (nextLine.equals("ASK")) {
                        switch (method) {
                            case "FC" -> fc.readAsk(buffer);
                            case "BC" -> bc.readAsk(buffer);
                            case "TT" -> exp.readAsk(buffer);
                            default -> {
                                System.out.println("Method not recognized: " + method);
                                System.exit(1);
                            }
                        }
                    }
                    nextLine = "";
                }
            }
        } catch (Exception err) {
            System.out.println("Failed to open or process " + inputFile);
            err.printStackTrace();
            System.exit(1);
        }
    }
}
