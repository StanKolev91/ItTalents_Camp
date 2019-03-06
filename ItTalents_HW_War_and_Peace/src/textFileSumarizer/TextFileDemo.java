package textFileSumarizer;

import textFileSumarizer.textProcessing.TextFileSumarizer;

import java.io.*;

public class TextFileDemo {

    public static void main(String[] args) {

        TextFileSumarizer textFileSumarizer = new TextFileSumarizer();
        textFileSumarizer.processTextFile(".."+File.separator+"Lev_Tolstoj_-_Vojna_i_mir-24967.txt",22);
    }
}
