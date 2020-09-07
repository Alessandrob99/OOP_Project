package OOP_Project.application.handlers;


import java.nio.file.InvalidPathException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;




@Controller
public class errorHandler implements ErrorController {

    @RequestMapping("/error")
    @ResponseBody
    public String handleError(HttpServletRequest request){
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String message = "-Errore-";
        if(statusCode == 404) {
        	message = "-Error- \nThe given path is not valid \nUse the /help rout to consult the guide";
        }
        if(statusCode == 500) {
        	message = "-Error- \n An internal server error has occurred";
        }
        if(statusCode==400) {
        	message = "-Error- \nThe request form is not correct \nUse the /help rout to consult the guide";
        }
        
        return message;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}