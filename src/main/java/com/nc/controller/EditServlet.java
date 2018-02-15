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
import java.util.Enumeration;

@WebServlet(urlPatterns = {"/edituser", "/editgroup"})
public class EditServlet extends HttpServlet {

    private JDBC jdbc = new JDBC();

    private String id = null;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);

        String userPath = request.getServletPath();
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            id = "id".equals(param) ? request.getParameter(param) : id;
        }

        RequestDispatcher dispatcher = null;
        if ("/edituser".equals(userPath)){
            request.setAttribute("user", jdbc.findUserById(Integer.parseInt(id)));
            request.setAttribute("groups", jdbc.findAllGroups());

            dispatcher = request.getRequestDispatcher("/views/editUser.jsp");
        } else if ("/editgroup".equals(userPath)){
            request.setAttribute("group", jdbc.findGroupById(Integer.parseInt(id)));
            dispatcher = request.getRequestDispatcher("/views/editGroup.jsp");
        }
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);

        String userPath = request.getServletPath();
        if ("/edituser".equals(userPath)){
            UserEntity user = jdbc.findUserById(Integer.parseInt(id));
            user.setFirstName(request.getParameter("first_name"));
            user.setLastName(request.getParameter("last_name"));
            user.setUserGroupId(Integer.parseInt(request.getParameter("group_user")));
            jdbc.updateUser(user);
        } else if ("/editgroup".equals(userPath)){
            UserGroupEntity group = jdbc.findGroupById(Integer.parseInt(id));
            group.setName(request.getParameter("group"));
            jdbc.updateGroup(group);
        }

        response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/index"));
    }

}
