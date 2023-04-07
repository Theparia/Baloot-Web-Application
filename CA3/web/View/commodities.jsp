<%@ page import="Service.Baloot" %>
<%@ page import="Domain.Commodity" %>
<%@ page import="java.util.List" %>

<%
  List<Commodity> commodities = (List<Commodity>)request.getSession().getAttribute("commodities");
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Commodities</title>
  <style>
    table{
      width: 100%;
      text-align: center;
    }
  </style>
</head>
<body>
<a href="/">Home</a>
<p id="username">username: <%= Baloot.getInstance().getLoggedInUser().getUsername()%></p>
<form action="" method="POST">
  <label>Search:</label>
  <input type="text" name="search" value="">
  <button type="submit" name="action" value="searchByCategory">Search By Cagtegory</button>
  <button type="submit" name="action" value="searchByName">Search By Name</button>
  <button type="submit" name="action" value="clear">Clear Search</button>
</form>
<br><br>
<form action="" method="POST">
  <label>Sort By:</label>
  <button type="submit" name="action" value="sortByPrice">Price</button>
</form>
<br><br>
<table>
  <tr>
    <th>Id</th>
    <th>Name</th>
    <th>Provider Name</th>
    <th>Price</th>
    <th>Categories</th>
    <th>Rating</th>
    <th>In Stock</th>
    <th>Links</th>
  </tr>
  <% for(Commodity commodity: commodities){ %>
  <tr>
    <td><%=commodity.getId()%></td>
    <td><%=commodity.getName()%></td>
    <td><%=Baloot.getInstance().findProviderById(commodity.getProviderId()).getName()%></td>
    <td><%=commodity.getPrice()%></td>
    <td><%=String.join(", ", commodity.getCategories())%></td>
    <td><%=commodity.getRating()%></td>
    <td><%=commodity.getInStock()%></td>
    <td><a href="<%= "/commodities/" + commodity.getId()%>">Link</a></td>
  </tr>
  <% } %>
</table>
</body>
</html>
