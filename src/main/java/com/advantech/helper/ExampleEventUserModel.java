/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import java.io.File;
import java.io.InputStream;
import java.util.Iterator;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Wei.Cheng
 */
public class ExampleEventUserModel {

    public void processOneSheet(String filename) throws Exception {

        OPCPackage pkg = OPCPackage.open(new File(filename));
        XSSFReader r = new XSSFReader(pkg);
        SharedStringsTable sst = r.getSharedStringsTable();
        XMLReader parser = fetchSheetParser(sst);

        // To look up the Sheet Name / Sheet Order / rID,
        //  you need to process the core Workbook stream.
        // Normally it's of the form rId# or rSheet#
        try (InputStream sheet2 = r.getSheet("rId2")) {
            InputSource sheetSource = new InputSource(sheet2);
            parser.parse(sheetSource);
        }
    }

    public void processAllSheets(String filename) throws Exception {

        OPCPackage pkg = OPCPackage.open(new File(filename));
        XSSFReader r = new XSSFReader(pkg);
        SharedStringsTable sst = r.getSharedStringsTable();
        XMLReader parser = fetchSheetParser(sst);
        Iterator<InputStream> sheets = r.getSheetsData();

        while (sheets.hasNext()) {

            System.out.println("Processing new sheet:\n");

            try (InputStream sheet = sheets.next()) {
                InputSource sheetSource = new InputSource(sheet);
                parser.parse(sheetSource);
            }
            System.out.println("");
        }

    }

    public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException, ParserConfigurationException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        SAXParser parser = factory.newSAXParser();
        XMLReader reader = parser.getXMLReader();
        ContentHandler handler = new SheetHandler(sst);
        reader.setContentHandler(handler);
        return reader;
    }

    /**
     *
     *
     * See org.xml.sax.helpers.DefaultHandler javadocs
     *
     *
     */
    private static class SheetHandler extends DefaultHandler {

        private final SharedStringsTable sst;

        private String lastContents;

        private boolean nextIsString;

        private SheetHandler(SharedStringsTable sst) {
            this.sst = sst;
        }

        @Override
        public void startElement(String uri, String localName, String name,
                Attributes attributes) throws SAXException {

            // c => cell
            if (name.equals("c")) {

                // Print the cell reference
                System.out.print(attributes.getValue("r") + " - ");

                // Figure out if the value is an index in the SST
                String cellType = attributes.getValue("t");
                nextIsString = cellType != null && cellType.equals("s");

            }

            // Clear contents cache
            lastContents = "";

        }

        @Override
        public void endElement(String uri, String localName, String name)
                throws SAXException {

            // Process the last contents as required.
            // Do now, as characters() may be called more than once
            if (nextIsString) {
                int idx = Integer.parseInt(lastContents);
                lastContents = sst.getItemAt(idx).getString();
                nextIsString = false;
            }

            // v => contents of a cell
            // Output after we've seen the string contents
            if (name.equals("v")) {
                System.out.println(lastContents);
            }

        }

        @Override
        public void characters(char[] ch, int start, int length) {
            lastContents += new String(ch, start, length);
        }

    }

    public static void main(String[] args) throws Exception {
        ExampleEventUserModel example = new ExampleEventUserModel();

//        String filePath = "C:\\Users\\wei.cheng\\Desktop\\abc.xlsx";
        String filePath = "C:\\Users\\wei.cheng\\Desktop\\excel_test\\活頁簿1.xlsx";

        example.processOneSheet(filePath);
        example.processAllSheets(filePath);

    }
}
