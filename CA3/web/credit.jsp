<%@ page import="Service.Baloot" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <title>Credit</title>
</head>

<body>
<a href="/">Home</a>
<p id="username">username: <%= Baloot.getInstance().getLoggedInUser().getUsername()%></p>
<form method="post" action="">
  <label>Credits:</label>
  <input name="credit" type="text" />
  <br>
  <button type="submit">Add credits</button>
</form>
</body>
</html>
