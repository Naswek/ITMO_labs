package org.example.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.exceptions.EmptyRequestException;
import org.example.storage.AppStorage;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.io.Serial;

public class ControllerServlet extends HttpServlet {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(ControllerServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");

        AppStorage storage = (AppStorage) getServletContext().getAttribute("appStorage");
        if (storage == null) {
            storage = new AppStorage();
            getServletContext().setAttribute("appStorage", storage);
        }

        String responseType = req.getParameter("format");
        if (responseType == null) responseType = "json";

        try {
            String query = req.getQueryString();

            AreaCheckServlet checker = new AreaCheckServlet();
            checker.init(this.getServletConfig());

            String resultJson = checker.parseParams(query, req.getParameter("clear"));

            if (responseType.equalsIgnoreCase("json")) {
                resp.getWriter().write(resultJson);
            } else {
                ITemplateEngine engine = (ITemplateEngine)
                        getServletContext().getAttribute(ThymeleafConfig.TEMPLATE_ENGINE_ATTR);

                var webApp = JakartaServletWebApplication.buildApplication(getServletContext());
                var webExchange = webApp.buildExchange(req, resp);
                var context = new WebContext(webExchange);

                context.setVariable("data", resultJson);
                context.setVariable("storage", storage);

                engine.process("results", context, resp.getWriter());
            }

        } catch (EmptyRequestException | ServletException e) {
            log.error("Ошибка при обработке запроса", e);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json;charset=UTF-8");
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
