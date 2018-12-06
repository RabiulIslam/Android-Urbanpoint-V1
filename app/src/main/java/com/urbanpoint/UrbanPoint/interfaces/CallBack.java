package com.urbanpoint.UrbanPoint.interfaces;

/**
 * Created by Anurag Sethi
 * The interface handles the callback from the execution of webservices 
 */
public interface CallBack {

    /**
     * The interface method implemented in the java files
     * @param data the result returned by the web service
     * @param tasksID the ID to differential multiple webservice calls
     * @return none
     */
    public void onResult(String data, int tasksID,String responseEmptyErrorMessage);
}
