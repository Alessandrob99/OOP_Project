package ProgettoPO.ProgettoProgrammazione.handlers;


import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;

@Controller
public class errorHandler implements ErrorController {

    @RequestMapping("/error")
    @ResponseBody
    public String handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String message = "-Errore-";
        if(statusCode == 404) {
        	message = "-Errore- \nRotta non valida";
        }
        if(statusCode == 500) {
        	message = "-Errore- \nSi è verificato un problema all'interno del server";
        }
        if(statusCode==400) {
        	message = "-Errore- \nLa richiesta inviata risulta incompleta\nSi prega di ricontrollare i dati forniti";
        }
        
        return message;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}