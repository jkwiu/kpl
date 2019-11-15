package ibiz.logtest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class HomeController {
    @RequestMapping("/")
    public void hello(HttpServletResponse response) throws IOException {
        response.getWriter().println("HELLO !!");

        LogController.defaultLog("trace", 10);
        LogController.defaultLog("warn", 10);
        LogController.defaultLog("info", 10);
        LogController.defaultLog("debug", 10);
        LogController.defaultLog("error", 10);

        DeleteController.deleteDefault("trace");
        DeleteController.deleteDefault("warn");
        DeleteController.deleteDefault("info");
        DeleteController.deleteDefault("debug");
        DeleteController.deleteDefault("error");
    }
}
