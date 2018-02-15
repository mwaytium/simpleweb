package com.nc.controller;

import com.nc.model.JDBC;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@WebServlet(urlPatterns = {"/deleteuser", "/deletegroup"})
public class DeleteServlet extends HttpServlet {

    private JDBC jdbc = new JDBC();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = null;
        String userPath = request.getServletPath();
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            id = "id".equals(param) ? request.getParameter(param) : id;
        }

        if ("/deleteuser".equals(userPath)){
            jdbc.removeUser(Integer.parseInt(id));
        } else if ("/deletegroup".equals(userPath)){
            jdbc.removeGroup(Integer.parseInt(id));
        }

        response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/index"));
    }
}
