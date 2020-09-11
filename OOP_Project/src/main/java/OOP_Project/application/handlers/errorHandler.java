package OOP_Project.application.handlers;


import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 
 * 
 * @author Bedetta Alessandro
 *
 *<p>
 *This class manages to detect all unhandled errors in the program
 *As the ones that may occur during the route choice.
 *</p>
 *
 *
 */

@Controller
public class errorHandler implements ErrorController {
	/**
	 * 
	 * This method allows to check if the http request returns a error status code
	 * Is so the method returns an error message
	 * @param request Represents the server-side http request to check.
	 * @return A String containing a description of the error.
	 */
    @RequestMapping("/error")
    @ResponseBody
    public String handleError(HttpServletRequest request){
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String message = "-Error-";			//The error message
        if(statusCode == 404) {			
        	message = "-Error- \nThe given path is not valid \nUse the /help route to consult the guide";
        }
        if(statusCode == 500) {
        	message = "-Error- \n An internal server error has occurred";
        }
        if(statusCode==400) {
        	message = "-Error- \nThe request form is not correct \nUse the /help route to consult the guide";
        }
        
        return message;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}