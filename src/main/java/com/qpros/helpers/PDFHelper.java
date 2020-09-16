package com.qpros.helpers;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.*;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Pattern;

public class PDFHelper {


    public static String readAndParsePDF(String uri) throws IOException {


        PDFTextStripper pdfStripper = null;
        PDDocument pdDoc = null;
        COSDocument cosDoc = null;
        String parsedText = null;
        PDFParser parser;


        URL url = new URL(uri);
        BufferedInputStream file = new BufferedInputStream(url.openStream());
        parser = new PDFParser((RandomAccessRead) file);


        parser.parse();
        cosDoc = parser.getDocument();
        pdfStripper = new PDFTextStripper();
        pdfStripper.setStartPage(1);
        pdfStripper.setEndPage(6);

        pdDoc = new PDDocument(cosDoc);
        parsedText = pdfStripper.getText(pdDoc);
        return parsedText;

    }


    public static void main(String[] args) {


        File inputFile = new File("C:\\Users\\KhaledMa'ayeh\\Documents\\QProsCore\\src\\main\\resources\\Report(1).pdf");
        if(inputFile.exists()){
            System.out.println("Input File:"+inputFile.getAbsolutePath());

            File txtFile = convertPDFtoText(inputFile);
            assert(txtFile.exists());
            System.out.println("Text... File:"+txtFile.getAbsolutePath());

            File csvFile = convertTextToCSV(txtFile);
            assert(csvFile.exists());
            System.out.println("CSV... File:"+csvFile.getAbsolutePath());

        }else{
            System.out.println("File does not exist:"+inputFile.getAbsolutePath());
        }
    }

    public static File convertPDFtoText(File inputFile) {
        try {
            String newFileName = replaceSuffix(inputFile.getName(), ".txt");
            String newPath = inputFile.getAbsoluteFile().getParent()+File.separator+newFileName;
            File outFile = new File(newPath);
            PDDocument document = PDDocument.load(inputFile);
            PDFTextStripper stripper = new PDFTextStripper();
            document.save("C:\\Users\\KhaledMa'ayeh\\Documents\\QProsCore\\src\\main\\resources\\Report(1).txt");
            stripper.writeText(document, new FileWriter(outFile));
            return outFile;
        } catch (IOException e) {
            throw new FailedException(e);
        }
    }


    public static File convertTextToCSV(File inputFile) {
        try {
            String newFileName = replaceSuffix(inputFile.getName(), ".csv");
            String newPath = inputFile.getAbsoluteFile().getParent() + File.separator + newFileName;
            Scanner scanner = new Scanner(inputFile);
            File outFile = new File(newPath);
            PrintWriter writer = new PrintWriter(outFile);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String result = convertLineToCSV(line);
                if (result != null)
                    writer.println(result);
            }
            writer.close();
            return outFile;
        } catch (FileNotFoundException e) {
            throw new FailedException(e);
        }
    }

    private static class FailedException extends RuntimeException {
        private static final long serialVersionUID = 2L;

        public FailedException(Exception e) {
            super(e);
        }
    }

    private static String replaceSuffix(String fileName, String suffix) {
        int index = fileName.indexOf('.');
        if (index != -1) {
            int lastIndex = index;
            while (index != -1) {
                index = fileName.indexOf('.', lastIndex + 1);
                if (index != -1)
                    lastIndex = index;
            }
            return fileName.substring(0, lastIndex) + suffix;
        } else {
            return fileName + "suffix";
        }
    }

    private static String convertLineToCSV(String line) {
        String[] fields = line.split("\\s+");
        if (fields.length < 1)
            return null;
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i >= 1; i--) {
            if (i < 10)
                builder.append("|");
            builder.append(fields[fields.length - i].replace(",", ""));
        }
        String result = builder.toString();
        if (isQuoteLine(result))
            return result;
        return null;
    }

    private static boolean isQuoteLine(String line) {
        String pattern = "[A-Z0-9]+" + "\\|(-)?(\\d+(\\.\\d+)?)?"
                + "\\|(-)?(\\d+(\\.\\d+)?)?" + "\\|(-)?(\\d+(\\.\\d+)?)?"
                + "\\|(-)?(\\d+(\\.\\d+)?)?" + "\\|(-)?(\\d+(\\.\\d+)?)?"
                + "\\|(-)?(\\d+(\\.\\d+)?)?" + "\\|(-)?(\\d+(\\.\\d+)?)?"
                + "\\|(-)?(\\d+(\\.\\d+)?)?" + "\\|(-)?(\\(?\\d+(\\.\\d+)?\\)?)?";
        return Pattern.matches(pattern, line);
    }


}
