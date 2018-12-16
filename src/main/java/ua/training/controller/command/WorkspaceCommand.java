package ua.training.controller.command;

import ua.training.controller.util.PathManager;

import javax.servlet.http.HttpServletRequest;

public class WorkspaceCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        return PathManager.getPath("path.workspace");
    }
}
