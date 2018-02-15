<%@ page language="java" pageEncoding="UTF-8" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title></title>
</head>
<body>
<form action="createuser" method="POST">
    <table>
        <tr>
            <td align="right" >Имя : </td>
            <td>
                <input type="text" name="first_name">
            </td>
        </tr>
        <tr>
            <td align="right" >Фамилия : </td>
            <td>
                <input type="text" name="last_name">
            </td>
        </tr>
        <tr>
            <td align="right" >Группа : </td>
            <td>
                <p>
                    <select name="group_user">
                    <option disabled>Выберите группу</option>
                    <c:forEach var="group" items="${groups}">
                        <option value="${group.id}">${group.name}</option>
                    </c:forEach>
                    </select>
                </p>
                <p>
            </td>
        </tr>
        <tr>
            <td><input type="submit" align="center" value="Submit"/></td>
        </tr>
    </table>
</form>
</body>
</html>
