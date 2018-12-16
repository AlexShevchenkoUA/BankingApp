package ua.training.controller.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.controller.util.PathManager;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * This filter implements authorization mechanism. If user has permissions to make such request, than the request will
 * be pass further, else the response will be sent to error page. If there is even no such commend, response will be
 * sent to error page to.
 */
public class AuthorizationFilter implements Filter {
    private static Logger logger = LogManager.getLogger(AuthorizationFilter.class);

    private static Map<String, List<String>> permissions;

    @Override
    public void init(FilterConfig filterConfig) {
        permissions = new ConcurrentHashMap<>();

        permissions.put("GUEST", List.of("signIn",
                "signUp",
                "changeLanguage",
                "currencyRate"));

        permissions.put("USER", List.of("signOut",
                "workspace",
                "changeLanguage",
                "currencyRate",
                "request",
                "showAccounts",
                "infoAccount",
                "infoTransaction",
                "updateUser",
                "infoUser",
                "createInvoice",
                "infoInvoice",
                "completeInvoice"));

        permissions.put("ADMIN", List.of("signOut",
                "workspace",
                "changeLanguage",
                "currencyRate",
                "openAccount",
                "closeAccount",
                "showAccounts",
                "infoAccount",
                "infoTransaction",
                "infoUser",
                "showRequests",
                "processRequest",
                "replenishAccount"));
    }

    @Override
    public void destroy() {}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String command = extractCommand(request);

        String role = (String) request.getSession().getAttribute("role");

        if (permissions.getOrDefault(role, List.of()).contains(command)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            logger.warn("User with role " + role + "tries to access command " + command);

            response.sendRedirect(request.getContextPath() + PathManager.getPath("path.error"));
        }
    }

    private String extractCommand(HttpServletRequest request) {
        return request.getRequestURI().replaceAll(".*/api/", "");
    }
}
