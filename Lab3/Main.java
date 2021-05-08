import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {
        try {
            if (args == null || args.length != 1 || args[0] == null)
                throw new MatrixReadFileException("Incorrect arguments start program.");
            process(args[0]);
        } catch (MatrixReadFileException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void process(String arg) throws MatrixReadFileException {
        try (BufferedReader bufferedReader = Files.newBufferedReader(Path.of(arg))) {
            String currentLine;
            while (true) {
                try {
                    currentLine = bufferedReader.readLine();
                } catch (IOException e) {
                    throw new MatrixReadFileException("Error while reading file. " + e.getMessage());
                }
                if (currentLine == null)
                    break;
                System.out.println(currentLine);
            }
        } catch (IOException e) {
            throw new MatrixReadFileException("Input file error. " + e.getMessage());
        }
    }

}
