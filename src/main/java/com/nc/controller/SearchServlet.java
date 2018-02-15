package com.nc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nc.entity.UserGroupEntity;
import com.nc.model.JDBC;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/search")
public class SearchServlet extends HttpServlet{

    private JDBC jdbc = new JDBC();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        String groupName = req.getParameter("group_name");
        String firstName = req.getParameter("first_name");
        String lastName = req.getParameter("last_name");

        resp.setContentType("application/json");

        if (groupName.equals("") && firstName.equals("") && lastName.equals("")) {
            mapper.writeValue(resp.getOutputStream(), jdbc.findAllGroups());
        }
        else if (firstName.equals("") && lastName.equals("")) {
            List<UserGroupEntity> allGroups = jdbc.findAllGroups();
            List<UserGroupEntity> findGroups = new ArrayList<>();
            allGroups.forEach(group -> {
                if (groupName.equals(group.getName())) {
                    findGroups.add(group);
                }
            });
            mapper.writeValue(resp.getOutputStream(), findGroups);
        }
        else {
            mapper.writeValue(resp.getOutputStream(), jdbc.findByUserAndGroup(groupName, firstName, lastName));
        }
    }
}
