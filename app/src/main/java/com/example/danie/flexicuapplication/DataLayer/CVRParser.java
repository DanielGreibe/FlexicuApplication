package com.example.danie.flexicuapplication.DataLayer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class CVRParser {
    private URL getString;
    private File xmlFile;
    private DocumentBuilderFactory dbFactory;
    private DocumentBuilder db;

    public CVRParser() {
        try {
            dbFactory = DocumentBuilderFactory.newInstance();
            db = dbFactory.newDocumentBuilder();
            xmlFile = new File(System.getProperty("java.io.tmpdir"), "cvrapitemp.xml");

        } catch(ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> getResult(String text) {
        try {
            System.setProperty("http.agent", "mit projekt");
            getString = new URL("http://cvrapi.dk/api?search="+text+"&country=dk&format=xml");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        FileOutputStream fos = null;
        ReadableByteChannel rbc;
        try {
            fos = new FileOutputStream(xmlFile);
            rbc = Channels.newChannel(getString.openStream());
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return parseXML();
    }

    private Map<String,String> parseXML() {
        Map<String, String> map = new HashMap<>();
        Document d = null;
        try {
            d = db.parse(xmlFile);
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }
        d.getDocumentElement().normalize();

        Node n = d.getFirstChild();

        if(n.getNodeType() == Node.ELEMENT_NODE) {

            Element eElement = (Element) n;
            System.out.println("ELEMENT " + n.toString());
            try {
                map.put("cvr", eElement.getElementsByTagName("vat").item(0).getTextContent());
                map.put("navn", eElement.getElementsByTagName("name").item(0).getTextContent());
                map.put("adresse", eElement.getElementsByTagName("address").item(0).getTextContent());
                map.put("postnr", eElement.getElementsByTagName("zipcode").item(0).getTextContent());
                map.put("city", eElement.getElementsByTagName("city").item(0).getTextContent());
                map.put("beskytet", eElement.getElementsByTagName("protected").item(0).getTextContent());
                map.put("startdato", eElement.getElementsByTagName("startdate").item(0).getTextContent());
                map.put("bkode", eElement.getElementsByTagName("industrycode").item(0).getTextContent());
                map.put("vkode", eElement.getElementsByTagName("companycode").item(0).getTextContent());
                map.put("vtekst", eElement.getElementsByTagName("companydesc").item(0).getTextContent());
                map.put("apiversion", eElement.getElementsByTagName("version").item(0).getTextContent());
            } catch(NullPointerException e) {
                map.put("error", "not found");
            }
        }

        return map;
    }
}