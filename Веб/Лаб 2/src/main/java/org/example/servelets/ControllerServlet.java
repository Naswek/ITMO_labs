package org.example.servelets;

import jakarta.servlet.ServletException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.exceptions.EmptyRequestException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.storage.AppStorage;
import java.io.IOException;
import java.io.Serial;

//@WebServlet("/controller")
public class ControllerServlet extends HttpServlet {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(ControllerServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        AppStorage storage = (AppStorage) getServletContext().getAttribute("appStorage");
        if (storage == null) {
            storage = new AppStorage();
            getServletContext().setAttribute("appStorage", storage);
        }

        try {
            String query = req.getQueryString();

            AreaCheckServlet checker = new AreaCheckServlet();
            checker.init(this.getServletConfig());

            String resultJson = checker.parseParams(query, req.getParameter("clear"));
            resp.getWriter().write(resultJson);
        } catch (EmptyRequestException | ServletException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}



