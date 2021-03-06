package com.urbanpoint.UrbanPoint.managers.vodafoneApiXMLParsing;

import com.urbanpoint.UrbanPoint.dataobject.offerPackagesSubscriptions.MSISDNVerificationStatusInfo;
import com.urbanpoint.UrbanPoint.utils.Constants;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by riteshpandhurkar on 28/3/16.
 */
public class SAXXMLHandler extends DefaultHandler {

    // SAX parser to parse job

    List<MSISDNVerificationStatusInfo> verificationStatusList = null;

    // string builder acts as a buffer
    StringBuilder builder;

    MSISDNVerificationStatusInfo verificationStatusObject = null;


    // Initialize the temp XmlValuesModel object which will hold the parsed info
    // and the string builder that will store the read characters
    // @param uri
    // @param localName ( Parsed Node name will come in localName  )
    // @param qName
    // @param attributes
    // @throws SAXException

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {

        /****  When New XML Node initiating to parse this function called *****/

        // Create StringBuilder object to store xml node value
        builder = new StringBuilder();

        if (localName.equalsIgnoreCase(Constants.Request.RESPONSE)) {

            // Log.i("parse","----Job start----");
            /********** Create Model Object  *********/
            verificationStatusObject = new MSISDNVerificationStatusInfo();
        }
    }


    // Finished reading the login tag, add it to arraylist
    // @param uri
    // @param localName
    // @param qName
    // @throws SAXException

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {


        if (localName.equalsIgnoreCase(Constants.Request.RESPONSE)) {

            /** finished reading a job xml node, add it to the arraylist **/
            verificationStatusList.add(verificationStatusObject);

        } else if (localName.equalsIgnoreCase(Constants.Request.STATUS_CODE)) {
            verificationStatusObject.setStatusCode(builder.toString());
        } else if (localName.equalsIgnoreCase(Constants.Request.TRANSACTION_ID)) {
            verificationStatusObject.setTransactionID(builder.toString());
        }

        // Log.i("parse",localName.toString()+"========="+builder.toString());
    }


    // Read the value of each xml NODE
    // @param ch
    // @param start
    // @param length
    // @throws SAXException

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {

        /******  Read the characters and append them to the buffer  ******/
        String tempString = new String(ch, start, length);
        builder.append(tempString);
    }

    public SAXXMLHandler() {
        verificationStatusList = new ArrayList<MSISDNVerificationStatusInfo>();
    }


    public List<MSISDNVerificationStatusInfo> getVerificationStatusList() {
        return verificationStatusList;
    }

}
