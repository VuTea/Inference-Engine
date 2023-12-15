import java.io.BufferedReader;
import java.io.FileReader;

public class iEngine
{
    public static void main(String[] args)
    {
        if (!(args.length == 2))
        {
            System.out.println("Usage: " + args[0] + " [method] [input_file]\n");
            System.exit(0);
        }
        String buffer;
        BufferedReader inputStream;

        PropositionalLogic exp = new PropositionalLogic();
        ForwardChaining fc = new ForwardChaining();
        BackwardChaining bc = new BackwardChaining();

        try
        {
            inputStream = new BufferedReader(new FileReader(args[1]));
            String nextLine ="";
            while ((buffer = inputStream.readLine()) != null)
            {
                if (buffer.startsWith("TELL"))
                {
                    nextLine ="TELL";
                }
                else if (buffer.equals("ASK"))
                {
                    nextLine ="ASK";
                }
                else
                {
                    if (nextLine.equals("TELL"))
                    {
                        switch (args[0]) {
                            case "FC" -> fc.readTell(buffer);
                            case "BC" -> bc.readTell(buffer);
                            case "TT" -> exp.readTell(buffer);
                            default -> {
                                System.out.println("Method not recognized");
                                System.exit(0);
                            }
                        }
                    }
                    else if(nextLine.equals("ASK"))
                    {
                        switch (args[0]) {
                            case "FC" -> fc.readAsk(buffer);
                            case "BC" -> bc.readAsk(buffer);
                            case "TT" -> exp.readAsk(buffer);
                        }
                    }
                    nextLine ="";
                }
            }
            inputStream.close();
        }
        catch (Exception err)
        {
            System.out.println("failed to open " + args[1]);
            err.printStackTrace();
            System.exit(0);
        }

    }
}