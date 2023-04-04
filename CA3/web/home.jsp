<%@ page import="Service.Baloot" %><%--
  Created by IntelliJ IDEA.
  User: paria
  Date: 4/4/23
  Time: 6:48 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en"><head>
  <meta charset="UTF-8">
  <title>Home</title>
</head>
<body>
<ul>
  <li id="username">username: <%= Baloot.getInstance().getLoggedInUser().getUsername() %></li>
  <li>
    <a href="/commodities">Commodities</a>
  </li>
  <li>
    <a href="/buyList">Buy List</a>
  </li>
  <li>
    <a href="/credit">Add Credit</a>
  </li>
  <li>
    <a href="/logout">Log Out</a>
  </li>
</ul>

</body></html>
