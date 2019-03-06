package textFileSumarizer.textProcessing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TextFileSumarizer {
    private static int threadIdCounter = 0;
    private HashSet<MyThread> threads;
    private AtomicInteger counterComas = new AtomicInteger(0);
    private ConcurrentHashMap<String, AtomicInteger> counterSrchdWrds;
    private ConcurrentHashMap<String, AtomicInteger> wordOccurances;
    private AtomicInteger warWordCounter = new AtomicInteger(0);
    private AtomicInteger peaceWordCounter = new AtomicInteger(0);

    public class MyThread extends Thread {
        private String textForProcessing;


        private MyThread() {
            setName(Integer.toString(++threadIdCounter));
        }

        private MyThread(String textForProcessing) {
            this();
            this.textForProcessing = textForProcessing;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MyThread myThread = (MyThread) o;
            return Objects.equals(myThread.getName(), ((MyThread) o).getName());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getName());
        }

        @Override
        public void run() {
            if (textForProcessing != null) {
                HashMap<String, Integer> wordOcc = getListOfWords(textForProcessing);
                srchWrd(textForProcessing,"война");
                srchWrd(textForProcessing,"мир");
                for (String word : wordOcc.keySet()) {
                    wordOccurances.putIfAbsent(word, new AtomicInteger(0));
                    wordOccurances.get(word).set(wordOcc.get(word) + wordOccurances.get(word).get());
                }
            }
        }

        void setTextForProcessing(String textForProcessing) {
            this.textForProcessing = textForProcessing;
        }

        HashMap<String, Integer> getListOfWords(String text) {
            String[] words = text.trim()
                    .replaceAll("(?iu)[,.\\(\\)*&^%$#!_+(0-9)\"!?<>„\\[\\]\\t(a-z)]", " ")
                    .replaceAll("(?i)(\n)"," ")
                    .split(" ");
            HashMap<String, Integer> wordsInText = null;

            wordsInText = new HashMap<>();
            for (String word : words) {
                if (!wordsInText.containsKey(word)) {
                    wordsInText.put(word, 0);
                }
                if (word.matches("((?i)\\w*война\\w*)|((?i)\\W*война\\W*)|((?i)\\w*война\\W*)|((?i)\\W*война\\w*)|((?i) *война *)")) {
                    warWordCounter.incrementAndGet();
                }
                if (word.matches("((?i)\\w*мир\\w*)|((?i)\\W*мир\\W*)|((?i)\\w*мир\\W*)|((?i)\\W*мир\\w*)")) {
                    peaceWordCounter.incrementAndGet();
                }

                wordsInText.put(word, wordsInText.get(word) + 1);
            }
            return wordsInText;
        }
    }

    public TextFileSumarizer() {
        wordOccurances = new ConcurrentHashMap<>();
        counterSrchdWrds = new ConcurrentHashMap<>();
    }

    public void processTextFile(String pathFile, int threadCountForProcessing) {
        long start = System.currentTimeMillis();
        Thread helper = new Thread() {
            @Override
            public void run() {
                threads = new HashSet<>();
                for (int i = 0; i < threadCountForProcessing; i++) {
                    threads.add(new MyThread());
                }
            }
        };
        helper.start();

        File file = new File(pathFile);
        StringBuilder text = new StringBuilder();
        if (file.exists()) {
            System.out.println("Started with " + threadCountForProcessing + " threads.\nProcessing...");

            try (Scanner sc = new Scanner(new FileInputStream(file))) {
                while (sc.hasNextLine()) {
                    text.append(sc.nextLine());
                }
            } catch (IOException e) {
                System.out.println("sorry " + e.getMessage());
            }
        } else {
            System.out.println("Sorry, " + pathFile.substring(pathFile.lastIndexOf(File.separator) + 1) + " file not found!");
            return;
        }

        int startIndx = 0;
        int endIndx = (text.length()-1)/ threadCountForProcessing;

        int iteration = 1;
        int threadsUsed = 0;
        int increase = (text.length()-1) / threadCountForProcessing;
        if (helper.isAlive()) {
            try {
                helper.join();
            } catch (InterruptedException e) {
                System.out.println("Interrupted thread!");
            }
        }
        for (MyThread thread : threads) {

            while (Character.toString(text.charAt(endIndx)).matches("[a-zA-Z]") && endIndx <= text.length() - 1) {
                if (endIndx < text.length() - 1) {
                    endIndx++;
                } else {
                    endIndx += 1;
                    break;
                }
            }
            String substring = text.substring(startIndx, endIndx);
            thread.setTextForProcessing(substring);
            threadsUsed++;
            thread.start();
            startIndx = endIndx;

            while (endIndx <= startIndx) {
                endIndx = increase*iteration++;
            }

            if (endIndx > text.length() - 1||threadsUsed==threadCountForProcessing-1) endIndx = text.length() - 1;
            if (startIndx >= endIndx) break;
        }

        for (int i = 0; i < text.length() - 1; i++) {
            if (text.charAt(i) == ',') {
                counterComas.incrementAndGet();
            }
        }
        for (MyThread thread : threads) {
            if (thread.isAlive()) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    System.out.println("Interrupted thread!");
                }
            }
        }
        long end = System.currentTimeMillis();
        long elapsedTime = end - start;
        System.out.println("Elapsed time: " + elapsedTime / 1000 + "." + elapsedTime % 1000 + " sec\nAdditional threads used: " + threadsUsed + "/" + threadCountForProcessing);
        showStatistic();
    }

    private void showStatistic() {
        System.out.println("\"War\" was encountered: " + warWordCounter);
        System.out.println("\"Peace\" was encountered: " + peaceWordCounter);
        System.out.println("Comma \",\" was encaontered: "+counterComas);
        System.out.println("Word occurrences: ");
        System.out.println("Searched words in text: ");
        for (Map.Entry<String, AtomicInteger> entry : counterSrchdWrds.entrySet() ){
            System.out.println("\t"+entry.getKey()+ " - "+entry.getValue()+ " occurances");
        }
        System.out.println("Word occurances in text: ");
        for (Map.Entry<String, AtomicInteger> entry2 : wordOccurances.entrySet()) {
            System.out.println("\t"+entry2.getKey() + " - " + entry2.getValue());
        }
    }
    private void srchWrd(String text, String word) {
        aa:
        for (int i = 0; i < text.length(); i++) {
            if (Character.toString(text.charAt(i)).equalsIgnoreCase(Character.toString(word.charAt(0)))) {
                for (int z = 1; z < word.length(); z++) {
                    if ((i + word.length() - 1) > text.length() - 1) continue aa;
                    if (!Character.toString(text.charAt(i + z)).equalsIgnoreCase(Character.toString(word.charAt(z)))) {
                        continue aa;
                    }
                }
                counterSrchdWrds.putIfAbsent(word, new AtomicInteger(0));
                counterSrchdWrds.get(word).incrementAndGet();
            }
        }
    }
}
