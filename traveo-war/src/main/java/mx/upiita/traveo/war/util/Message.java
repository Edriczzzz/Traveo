package mx.upiita.traveo.war.util;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.application.FacesMessage.Severity;


public class Message {

    private Message(){

    }

    public static FacesContext getContext(){
        return FacesContext.getCurrentInstance();
    }

    public static void messageInfo(String message){
        addMessage(message,FacesMessage.SEVERITY_INFO);
    }

    public static void messageError(String message){
        addMessage(message,FacesMessage.SEVERITY_ERROR);
    }

    private static void addMessage(String message, Severity severity){
        FacesMessage menJSF = new FacesMessage();
        menJSF.setSeverity(severity);
        menJSF.setSummary(message);
        getContext().addMessage(null, menJSF);
    }


}
