package com.nc.controller;

import com.nc.entity.UserEntity;
import com.nc.entity.UserGroupEntity;
import com.nc.model.JDBC;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/createuser", "/creategroup"})
public class CreateServlet extends HttpServlet {

    private JDBC jdbc = new JDBC();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);

        RequestDispatcher dispatcher = null;
        String userPath = request.getServletPath();
        if ("/createuser".equals(userPath)){
            request.setAttribute("groups", jdbc.findAllGroups());
            dispatcher = request.getRequestDispatcher("/views/createUser.jsp");
        } else if ("/creategroup".equals(userPath)){
            dispatcher = request.getRequestDispatcher("/views/createGroup.jsp");

        }
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);

        String userPath = request.getServletPath();
        if ("/createuser".equals(userPath)){
            UserEntity user = new UserEntity();
            user.setFirstName(request.getParameter("first_name"));
            user.setLastName(request.getParameter("last_name"));
            user.setUserGroupId(Integer.parseInt(request.getParameter("group_user")));
            jdbc.addUser(user);
        } else if ("/creategroup".equals(userPath)){
            UserGroupEntity group = new UserGroupEntity();
            group.setName(request.getParameter("group"));
            jdbc.addGroup(group);
        }

        response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/index"));
    }
}
