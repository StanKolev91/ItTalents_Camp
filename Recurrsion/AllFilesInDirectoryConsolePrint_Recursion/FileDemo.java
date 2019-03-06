package filePrint;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FileDemo {

    public static void main(String[] args) {
        DateTimeFormatter formatter =DateTimeFormatter.ofPattern("dd/MMMM/yyyy");

//        PrintFilesInDirectory.printFiles(".."+File.separator+"Subtitles","Result");
        LocalDate date = LocalDate.parse("2007-12-03");
        formatter.format(date);
        System.out.println(formatter.format(date));
    }
}
