package ibiz.logtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping(value="/delete")
public class DeleteController {

    final static Logger logger = LoggerFactory.getLogger(DeleteController.class);

    @RequestMapping(value="/{type}/{name}", method = RequestMethod.GET)
    public void delete(HttpServletResponse response, @PathVariable String type, @PathVariable String name) throws IOException {

        switch (type) {
            case "debug": logger.debug("deletion log debug"); break;
            case "info": logger.info("deletion log info"); break;
            case "error": logger.error("deletion log error"); break;
            case "trace": logger.trace("deletion log trace"); break;
            case "warn": logger.warn("deletion log warn"); break;
        }

        response.getWriter().println("DELETE");
        response.getWriter().println("type : " + type);
        response.getWriter().println("name : " + name);
    }

    public static void deleteDefault(String type) {
        switch (type) {
            case "debug": logger.debug("deletion log debug"); break;
            case "info": logger.info("deletion log info"); break;
            case "error": logger.error("deletion log error"); break;
            case "trace": logger.trace("deletion log trace"); break;
            case "warn": logger.warn("deletion log warn"); break;
        }
    }

}
