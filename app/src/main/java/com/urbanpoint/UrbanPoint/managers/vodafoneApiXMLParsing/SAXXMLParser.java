package com.urbanpoint.UrbanPoint.managers.vodafoneApiXMLParsing;


import com.urbanpoint.UrbanPoint.dataobject.offerPackagesSubscriptions.MSISDNVerificationStatusInfo;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

public class SAXXMLParser {
    public static List<MSISDNVerificationStatusInfo> parse(InputStream is) {
        List<MSISDNVerificationStatusInfo> statusInfo = null;
        try {
            // create a XMLReader from SAXParser
            XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser()
                    .getXMLReader();
            // create a SAXXMLHandler
            SAXXMLHandler saxHandler = new SAXXMLHandler();
            // store handler in XMLReader
            xmlReader.setContentHandler(saxHandler);
            // the process starts
            xmlReader.parse(new InputSource(is));
            // get the `Employee list`
            statusInfo = saxHandler.getVerificationStatusList();

        } catch (Exception ex) {
            MyApplication.getInstance().printLogs("XML", "SAXXMLParser: parse() failed");
        }

        // return Employee list
        return statusInfo;
    }
}
