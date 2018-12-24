package ua.training.controller.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.controller.util.managers.PathManager;
import ua.training.model.dao.factory.JdbcDaoFactory;
import ua.training.model.entity.Invoice;
import ua.training.model.entity.User;
import ua.training.model.service.AccountService;
import ua.training.model.service.UserService;

import javax.servlet.http.HttpServletRequest;

public class InfoInvoiceCommand implements Command {
    private static Logger logger = LogManager.getLogger(InfoInvoiceCommand.class);

    @Override
    public String execute(HttpServletRequest request) {
        Long invoiceId = Long.valueOf(request.getParameter("invoiceId"));
        Long accountId = Long.valueOf(request.getParameter("masterAccount"));

        User user = new UserService(JdbcDaoFactory.getInstance()).get((Long) request.getSession().getAttribute("id"));
        Invoice invoice = new AccountService(JdbcDaoFactory.getInstance()).getInvoice(invoiceId);

        if (!user.getAccounts().contains(accountId) || !(accountId.equals(invoice.getPayer()) || accountId.equals(invoice.getRequester()))) {
            logger.warn("User " + user.getId() + "try to access account " + accountId + " without permissions");

            return "redirect:" + PathManager.getPath("path.error");
        }

        request.setAttribute("invoice", invoice);
        request.setAttribute("masterAccount", accountId);

        return PathManager.getPath("path.invoice");
    }
}
