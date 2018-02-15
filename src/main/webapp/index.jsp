<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<html>
<head>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <link rel='stylesheet prefetch'>
    <link href="https://fonts.googleapis.com/css?family=Fjalla+One" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="views/css/stylesheet.css">
</head>
<body>
<h1>List of groups</h1>
<input id="group_name" size="12" placeholder="group" type="text" name="first_name">
<input id="first_name" size="12" placeholder="first name" type="text" name="first_name">
<input id="last_name" size="12" placeholder="last name" type="text" name="last_name">
<input id="search_button" type="button" align="center" value="Поиск"/>
<nav class="nav">
    <ul class=list>
        <li id="data">
            <a href="${pageContext.servletContext.contextPath}/creategroup">Create group </a>/
            <a href="${pageContext.servletContext.contextPath}/createuser"> Create user</a>
            <ul id="search">
            <c:forEach var="group" items="${groups}">
                <li>
                    <a href="#">Group</a>
                    <span>'${group.name}'</span>
                    <a href="editgroup?id=${group.id}"><i class="material-icons">edit</i></a>
                    <a href="deletegroup?id=${group.id}"><i class="material-icons ">delete</i></a>
                    <ul>
                    <c:forEach var="user" items="${group.users}">
                        <li>
                            <a href="#">User</a>
                            <span>
                                    ${user.firstName}
                                    ${user.lastName}
                            </span>
                            <a href="edituser?id=${group.id}"><i class="material-icons">edit</i></a>
                            <a href="deleteuser?id=${group.id}"><i class="material-icons ">delete</i></a>
                        </li>
                    </c:forEach>
                    </ul>
                </li>
            </c:forEach>
            </ul>
        </li>
    </ul>

</nav>
<script>
    $(document).ready(function() {
        $("#search_button").click(function(){
            var data = 'first_name='+$("#first_name").val()+'&last_name='+$("#last_name").val()+'&group_name='+$("#group_name").val();
            $.ajax({
                url: "search",
                type: 'POST',
                data: data,

                success: function (data) {
                    $("#search").remove();

                    var $main_ul = $("<ul id='search'>").appendTo($("#data"));
                    var $main_li , $li, $ul, $a;
                    $.each(data, function(index, group) {
                        $main_li = $("<li>").appendTo($main_ul);
                        $("<a>").appendTo($main_li).attr("href", "#").text("Group ");
                        $("<span>").appendTo($main_li).text("'" + group.name + "'");
                        $a = $("<a>").appendTo($main_li).attr("href", "editgroup?id="+group.id);
                        $("<i class='material-icons'>").appendTo($a).attr("class", "material-icons").text("edit");
                        $a = $("<a>").appendTo($main_li).attr("href", "deletegroup?id="+group.id);
                        $("<i class='material-icons'>").appendTo($a).attr("class", "material-icons").text("delete");
                        $ul = $("<ul>").appendTo($main_li);
                        $.each(group.users, function(i, user) {
                            $li = $("<li>").appendTo($ul);
                            $("<a>").appendTo($li).attr("href", "#").text("User ");
                            $("<span>").appendTo($li).text(user.firstName + " " + user.lastName);
                            $a = $("<a>").appendTo($li).attr("href", "edituser?id="+user.id);
                            $("<i class='material-icons'>").appendTo($a).attr("class", "material-icons").text("edit");
                            $a = $("<a>").appendTo($li).attr("href", "deleteuser?id="+user.id);
                            $("<i class='material-icons'>").appendTo($a).attr("class", "material-icons").text("delete");
                        });
                    });
                },
                error:function(data,status,er) {
                    alert("error: "+data+" status: "+status+" er:"+er);
                }
            });
        });
    });
</script>
</body>
</html>
