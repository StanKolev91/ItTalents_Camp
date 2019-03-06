package filePrint;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

 abstract class PrintFilesInDirectory {
    private static StringBuilder result = new StringBuilder();

     static void printFiles(String pathToFile, String resultFileName) {
        result.append("Files in ").append("\"").append(pathToFile).append("\"").append("\n\r");
        System.out.println(result);
        extractPathsInResultString(pathToFile);
        writeResultToFile(resultFileName);
    }

    private static void writeResultToFile(String fileName) {
        try (PrintStream a = new PrintStream(fileName)) {
            a.append(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void extractPathsInResultString(String pathFile) {
        File file = new File(pathFile);
        if (!file.exists()) {
            System.out.println("File " + file.getName() + " not found!");
            return;
        }

        if (file.isFile()) {
            try {
                System.out.println("\t" + file.getCanonicalFile());
                result.append("\t").append(file.getCanonicalPath()).append("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        File[] files = file.listFiles();

        for (File fileFromFiles : files) {
            extractPathsInResultString(fileFromFiles.getPath());
        }
    }
}
