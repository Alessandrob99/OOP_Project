package ProgettoPO.ProgettoProgrammazione.myErrors;


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
        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
        String message = "-Errore-";
        if(statusCode == 404) {
        	message = "-Errore- \nRotta non valida";
        }
        return message;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}