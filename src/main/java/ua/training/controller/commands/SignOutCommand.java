package ua.training.controller.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ua.training.controller.util.managers.PathManager;

import javax.servlet.http.HttpServletRequest;

@Controller("signOut")
public class SignOutCommand implements Command {
    private static Logger logger = LogManager.getLogger(SignOutCommand.class);

    private PathManager pathManager;

    @Override
    public String execute(HttpServletRequest request) {
        logger.info("User " + request.getSession().getAttribute("login") + " is sign out.");

        request.getServletContext().removeAttribute((String) request.getSession().getAttribute("login"));
        request.getSession().invalidate();

        return "redirect:" + pathManager.getPath("path.index");
    }

    @Autowired
    public void setPathManager(PathManager pathManager) {
        this.pathManager = pathManager;
    }
}
