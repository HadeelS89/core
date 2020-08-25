package com.qpros.helpers;


import org.testng.Assert;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Properties;

public class ReadWriteHelper {


    /**
     * @param par Name of parameter to be read from the config.properties file
     * @return Value of property
     */
    public static String ReadData(String par) {
        File file = new File("src/main/resources/config.properties");

        FileInputStream fileInput = null;
        try {
            fileInput = new FileInputStream(file);
        } catch (Throwable e) {
            e.printStackTrace(System.out);
            Assert.fail("\nPlease check config file if exist\n");
        }
        Properties prop = new Properties();

        // load properties file
        try {
            prop.load(fileInput);
        } catch (IOException e) {
            e.printStackTrace(System.out);
            Assert.fail("\nPlease check config file Inputs\n");
        }

        return prop.getProperty(par);
    }

    /**
     * @param fileName CSV File name
     * @param linesToRead Numbers of lines to read.
     * @param columnsToRead Number of columns to be read.
     * @return
     */
    public static String[][] readCSVFile(String fileName, int linesToRead, int columnsToRead) {
        //Possible future implementation: Make separators as input. However this looks better for reusability
        String line = "";
        String csvSplitBy = ",";
        String[] currentLine = null;
        String[][] finalResult = new String[linesToRead][columnsToRead];
        try (BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") +
                "/src/main/resources/DataProvider/" + fileName + ".csv"))) {
            int j = 0;
            while ((line = br.readLine()) != null) {
                // use comma as separator
                currentLine = line.split(csvSplitBy);
                for (int i = 0; i < currentLine.length; i++) {
                    finalResult[j][i] = currentLine[i];
                }
                j++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return finalResult;
    }


    /**
     * Reads data from an XML file
     * @param filePath The name of the file
     * @param parent Tag parent name
     * @param tag Tag to read
     * @return 2D String array containing the CSV file data
     */
    public static String readXMLFile(String filePath, String parent, String tag) {

        String value = "";
        try {
            //creating a constructor of file class and parsing an XML file
            File file = new File(System.getProperty("user.dir") +
                    "/src/main/resources/DataProvider/" + filePath + ".xml");
            //an instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //an instance of builder to parse the specified xml file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            //System.out.println( "Root element: " + doc.getDocumentElement().getNodeName() );
            NodeList nodeList = doc.getElementsByTagName(parent);
            // nodeList is not iterable, so we are using for loop
            for (int itr = 0; itr < nodeList.getLength(); itr++) {
                Node node = nodeList.item(itr);
                //System.out.println( "\nNode Name :" + node.getNodeName() );
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    value = eElement.getElementsByTagName(tag).item(0).getTextContent();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    public static void writeCSVFirstCell(String filePath, String content) {
        try (PrintWriter writer = new PrintWriter(new File("src/main/resources/DataProvider/" + filePath + ".csv"))) {
            writer.write(content);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * This will erase data on an existing XML file.
     *
     * @param filePath The name of the file you want to write to
     * @param parent   Parent name in the XML file
     * @param tag      Tag name in the XML file
     * @param value    Value to be written inside the tag
     */
    public static void writeIntoXMLFile(String filePath, String parent, String tag, String value) {
        String xmlFilePath = System.getProperty("user.dir") +
                "/src/main/resources/DataProvider/" + filePath + ".xml";


        try {
            //an instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //an instance of builder to parse the specified xml file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();
            //doc.getDocumentElement().normalize();

            // root element
            Element root = doc.createElement("class");
            doc.appendChild(root);

            // employee element
            Element employee = doc.createElement(parent);
            root.appendChild(employee);

            // firstname element
            Element firstName = doc.createElement(tag);
            firstName.appendChild(doc.createTextNode(value));
            employee.appendChild(firstName);

            // create the xml file
            //transform the DOM Object to an XML File
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(new File(xmlFilePath));

            // If you use
            // StreamResult result = new StreamResult(System.out);
            // the output will be pushed to the standard output ...
            // You can use that for debugging

            transformer.transform(domSource, streamResult);

            System.out.println("Done creating XML File");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }

    }
}
