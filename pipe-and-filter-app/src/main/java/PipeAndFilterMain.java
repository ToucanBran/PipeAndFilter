import filters.*;
import pipes.Pipe;
import services.PipeAndFilter;

import javax.annotation.processing.FilerException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PipeAndFilterMain {
    private static PipeAndFilter pipeAndFilter;
    private static Pipe startPipe;
    public static void main(String[] args) {
        pipeAndFilter = setupPipeAndFilter();
        System.out.println("Pipe and Filter Main Demo");
        System.out.println("--------------------------");
        Scanner scanner = new Scanner(System.in);
        String input = "";
        while (!input.toLowerCase().equals("q"))
        {
            System.out.printf("Choose a book to see the top 10 most frequent words.\n" +
                    "1.\tAlice in Wonderland\n"+
                    "2.\tKing James Bible\n" +
                    "3.\tUS Declaration of Independence\n" +
                    "(press 'q' to quit)\n\n"
            );
            System.out.print("Your choice: ");
            input = scanner.next();
            if (input.equals("1") || input.equals("2") || input.equals("3"))
            {
                startPipeline(input);
            }
            else if (input.equals("q")) {
                pipeAndFilter.stopPipeline();
                System.exit(0);
            }
        }
    }

    private static PipeAndFilter setupPipeAndFilter() {
        InputStream stopWordsStream = PipeAndFilterMain.class.getResourceAsStream("/stopwords.txt");
        List<String> stopWords = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(stopWordsStream);
            while (scanner.hasNext()) {
                stopWords.add(scanner.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Filter[] filters = new Filter[]{
                new RemoveWords(null, stopWords),
                new RemoveNonAlphabetical(),
                new Stem(),
                new Frequency(),
                new MostFrequent()};

        return new PipeAndFilter(filters);
    }

    private static void startPipeline(String option)
    {
        ClassLoader classLoader = PipeAndFilterMain.class.getClassLoader();
        if (startPipe == null) {
            startPipe = pipeAndFilter.startPipeline();
        }
        InputStream book = null;
        final String ALICE = "1", KJV = "2", US_DECLARATION = "3";
        switch (option) {
            case ALICE:
                book = PipeAndFilterMain.class.getResourceAsStream("/alice30.txt");
                break;
            case KJV:
                book = PipeAndFilterMain.class.getResourceAsStream("/kjbible.txt");
                break;
            case US_DECLARATION:
                book = PipeAndFilterMain.class.getResourceAsStream("/usdeclar.txt");
                break;
        }
        if (book != null) {
            startPipe.write(getWords(book));
            List<String> output = null;
            while (output == null) {
                output = (List<String>) pipeAndFilter.getFinalOutput().read();
            }
            output.forEach(word -> {
                System.out.println(word);
            });
        }
    }
    // Given a file, all strings are added to a list and returned to the caller
    private static List<String> getWords(InputStream file) {
        if (file == null) {
            return null;
        }
        List<String> output = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                output.add(scanner.next().toLowerCase());
            }
        }
        catch (Exception e) {
            System.out.println("Something went wrong when searching for the book. Rerun the program to try again.");
            System.exit(1);
        }
        return output;
    }
}
