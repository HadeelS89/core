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

    public static String ReadData(String par) {
        File file = new File( "src/main/resources/config.properties" );

        FileInputStream fileInput = null;
        try {
            fileInput = new FileInputStream( file );
        } catch (Throwable e) {
            e.printStackTrace( System.out );
            Assert.fail( "\nPlease check config file if exist\n" );
        }
        Properties prop = new Properties();

        // load properties file
        try {
            prop.load( fileInput );
        } catch (IOException e) {
            e.printStackTrace( System.out );
            Assert.fail( "\nPlease check config file Inputs\n" );
        }

        return prop.getProperty( par );
    }

    public static String[][] readCSVFile(String fileName, int linesToRead, int columnsToRead) {
        //Possible future implementation: Make separators as input. However this looks better for reusability
        String line = "";
        String csvSplitBy = ",";
        String[] currentLine = null;
        String[][] finalResult = new String[linesToRead][columnsToRead];
        try (BufferedReader br = new BufferedReader( new FileReader( System.getProperty( "user.dir" ) +
                "/src/main/resources/DataProvider/"+fileName+".csv" ) )) {
            int j = 0;
            while ((line = br.readLine()) != null) {
                // use comma as separator
                 currentLine = line.split( csvSplitBy );
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

    public static String readCredentialsXMLFile(String credentialsType, String tag) {

        String value = "";

        try {
            //creating a constructor of file class and parsing an XML file
            File file = new File( System.getProperty( "user.dir" ) +
                    "/src/main/resources/DataProvider/credentialsData.xml" );
            //an instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //an instance of builder to parse the specified xml file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse( file );
            doc.getDocumentElement().normalize();
            //System.out.println( "Root element: " + doc.getDocumentElement().getNodeName() );
            NodeList nodeList = doc.getElementsByTagName( credentialsType );
            // nodeList is not iterable, so we are using for loop
            for (int itr = 0; itr < nodeList.getLength(); itr++) {
                Node node = nodeList.item( itr );
                //System.out.println( "\nNode Name :" + node.getNodeName() );
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    switch (tag){
                        case "username":
                            value = eElement.getElementsByTagName( "username" ).item( 0 ).getTextContent();
                            break;
                        case "password":
                            value = eElement.getElementsByTagName( "password" ).item( 0 ).getTextContent();
                            break;
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    public static String readProgramsXMLFile(String programNumber, String tag) {

        String value = "";

        try {
            //creating a constructor of file class and parsing an XML file
            File file = new File( System.getProperty( "user.dir" ) +
                    "/src/main/resources/DataProvider/programsData.xml" );
            //an instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //an instance of builder to parse the specified xml file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse( file );
            doc.getDocumentElement().normalize();
            //System.out.println( "Root element: " + doc.getDocumentElement().getNodeName() );
            NodeList nodeList = doc.getElementsByTagName( programNumber );
            // nodeList is not iterable, so we are using for loop
            for (int itr = 0; itr < nodeList.getLength(); itr++) {
                Node node = nodeList.item( itr );
                //System.out.println( "\nNode Name :" + node.getNodeName() );
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    if ("title".equals( tag )) {
                        value = eElement.getElementsByTagName( "title" ).item( 0 ).getTextContent();
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    public static void writeCSVFirstCell(String content){
        try(PrintWriter writer = new PrintWriter(new File("src/main/resources/DataProvider/ActiveProgram.csv"))){
            writer.write(content);
        }
        catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String readApplicationsXMLFile(String applicationId, String tag) {

        String value = "";

        try {
            //creating a constructor of file class and parsing an XML file
            File file = new File( System.getProperty( "user.dir" ) +
                    "/src/main/resources/DataProvider/applicationsData.xml" );
            //an instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //an instance of builder to parse the specified xml file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse( file );
            doc.getDocumentElement().normalize();
            //System.out.println( "Root element: " + doc.getDocumentElement().getNodeName() );
            NodeList nodeList = doc.getElementsByTagName( applicationId );
            // nodeList is not iterable, so we are using for loop
            for (int itr = 0; itr < nodeList.getLength(); itr++) {
                Node node = nodeList.item( itr );
                //System.out.println( "\nNode Name :" + node.getNodeName() );
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    if ("title".equals( tag )) {
                        value = eElement.getElementsByTagName( "title" ).item( 0 ).getTextContent();
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    public static void writeIntoXMLFile(String programTitle){
        String xmlFilePath = System.getProperty( "user.dir" ) +
                "/src/main/resources/DataProvider/createdProgram.xml";


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
            Element employee = doc.createElement("createdProgram");
            root.appendChild(employee);

            // firstname element
            Element firstName = doc.createElement("title");
            firstName.appendChild(doc.createTextNode(programTitle));
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
    public static void writeIntoXMLFileInterview(String programTitle){
        String xmlFilePath = System.getProperty( "user.dir" ) +
                "/src/main/resources/DataProvider/interviewProgram.xml";


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
            Element employee = doc.createElement("createdProgram");
            root.appendChild(employee);

            // firstname element
            Element firstName = doc.createElement("title");
            firstName.appendChild(doc.createTextNode(programTitle));
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

    public static String getCreatedProgram() {

        String value = "";

        try {
            //creating a constructor of file class and parsing an XML file
            File file = new File( System.getProperty( "user.dir" ) +
                    "/src/main/resources/DataProvider/createdProgram.xml" );
            //an instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //an instance of builder to parse the specified xml file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse( file );
            doc.getDocumentElement().normalize();
            //System.out.println( "Root element: " + doc.getDocumentElement().getNodeName() );
            NodeList nodeList = doc.getElementsByTagName( "createdProgram" );
            // nodeList is not iterable, so we are using for loop
            for (int itr = 0; itr < nodeList.getLength(); itr++) {
                Node node = nodeList.item( itr );
                //System.out.println( "\nNode Name :" + node.getNodeName() );
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                            value = eElement.getElementsByTagName( "title" ).item( 0 ).getTextContent();
                            break;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    public static String getScholarshipProgram() {

        String value = "";

        try {
            //creating a constructor of file class and parsing an XML file
            File file = new File( System.getProperty( "user.dir" ) +
                    "/src/main/resources/DataProvider/scholarshipProgram.xml" );
            //an instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //an instance of builder to parse the specified xml file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse( file );
            doc.getDocumentElement().normalize();
            //System.out.println( "Root element: " + doc.getDocumentElement().getNodeName() );
            NodeList nodeList = doc.getElementsByTagName( "createdProgram" );
            // nodeList is not iterable, so we are using for loop
            for (int itr = 0; itr < nodeList.getLength(); itr++) {
                Node node = nodeList.item( itr );
                //System.out.println( "\nNode Name :" + node.getNodeName() );
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    value = eElement.getElementsByTagName( "title" ).item( 0 ).getTextContent();
                    break;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    public static String getInterviewProgram() {

        String value = "";

        try {
            //creating a constructor of file class and parsing an XML file
            File file = new File( System.getProperty( "user.dir" ) +
                    "/src/main/resources/DataProvider/interviewProgram.xml" );
            //an instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //an instance of builder to parse the specified xml file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse( file );
            doc.getDocumentElement().normalize();
            //System.out.println( "Root element: " + doc.getDocumentElement().getNodeName() );
            NodeList nodeList = doc.getElementsByTagName( "createdProgram" );
            // nodeList is not iterable, so we are using for loop
            for (int itr = 0; itr < nodeList.getLength(); itr++) {
                Node node = nodeList.item( itr );
                //System.out.println( "\nNode Name :" + node.getNodeName() );
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    value = eElement.getElementsByTagName( "title" ).item( 0 ).getTextContent();
                    break;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }


    public static String readProgramStatusXMLFile(String applicationId, String tag) {

        String value = "";

        try {
            //creating a constructor of file class and parsing an XML file
            File file = new File( System.getProperty( "user.dir" ) +
                    "/src/main/resources/DataProvider/applicationStatus.xml" );
            //an instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //an instance of builder to parse the specified xml file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse( file );
            doc.getDocumentElement().normalize();
            //System.out.println( "Root element: " + doc.getDocumentElement().getNodeName() );
            NodeList nodeList = doc.getElementsByTagName( applicationId );
            // nodeList is not iterable, so we are using for loop
            for (int itr = 0; itr < nodeList.getLength(); itr++) {
                Node node = nodeList.item( itr );
                //System.out.println( "\nNode Name :" + node.getNodeName() );
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    if ("status".equals( tag )) {
                        value = eElement.getElementsByTagName( "status" ).
                                item( 0 ).getTextContent();
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

}
