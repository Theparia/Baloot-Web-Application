<%@ page import="Domain.Commodity" %>
<%@ page import="Service.Baloot" %>
<%@ page import="Domain.Comment" %>
<%@ page import="java.util.List" %>

<% Commodity commodity = (Commodity) request.getAttribute("commodity"); %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Commodity</title>
  <style>
    li {
      padding: 5px;
    }
    table {
      width: 100%;
      text-align: center;
    }
  </style>
</head>
<body>
<a href="/">Home</a>
<p id="username">username: <%= Baloot.getInstance().getLoggedInUser().getUsername()%></p>
<ul>
  <li id="id">Id: <%=commodity.getId()%></li>
  <li id="name">Name: <%=commodity.getName()%></li>
  <li id="providerName">Provider Name: <%=Baloot.getInstance().findProviderById(commodity.getProviderId()).getName()%></li>
  <li id="price">Price: <%=commodity.getPrice()%></li>
  <li id="categories">Categories: <%=String.join(", ", commodity.getCategories())%></li>
  <li id="rating">Rating: <%=commodity.getRating()%></li>
  <li id="inStock">In Stock: <%=commodity.getInStock()%></li>
</ul>

<label>Add Your Comment:</label>
<form action="" method="post">
  <input type="text" name="comment" value="" >
  <button type="submit" name="action" value="addComment">submit</button>
</form>
<br>
<form action="" method="POST">
  <label>Rate(between 1 and 10):</label>
  <input type="number" id="rate" name="rate" min="1" max="10">
  <button type="submit" name="action" value="addRating">Rate</button>
</form>
<br>
<form action="" method="POST">
  <button type="submit" name="action" value="addToBuyList">Add to BuyList</button>
</form>
<br />
<table>
  <caption><h2>Comments</h2></caption>
  <tr>
    <th>username</th>
    <th>comment</th>
    <th>date</th>
    <th>likes</th>
    <th>dislikes</th>
  </tr>
  <% for(Comment comment: Baloot.getInstance().getCommodityComments(commodity.getId())) {%>
  <tr>
    <td><%=Baloot.getInstance().getUsernameByEmail(comment.getUserEmail())%></td>
    <td><%=comment.getText()%></td>
    <td><%=comment.getDate()%></td>
    <td>
      <form action="" method="POST">
        <label><%=comment.getLikeCount()%></label>
<%--        <input type="hidden" name="voteLike" value="1"/>--%>
        <input type="hidden" name="commentIdLike" value=<%=comment.getId()%> >
        <button type="submit" name="action" value="likeComment">like</button>
      </form>
    </td>
    <td>
      <form action="" method="POST">
        <label><%=comment.getDisLikeCount()%></label>
<%--        <input type="hidden" name="voteDislike" value="-1"/>--%>
        <input type="hidden" name="commentIdDisLike" value=<%=comment.getId()%> >
        <button type="submit" name="action" value="dislikeComment">dislike</button>
      </form>
    </td>
  </tr>
  <% } %>
</table>
<br><br>
<table>
  <caption><h2>Suggested Commodities</h2></caption>
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
  <%
    List<Commodity> suggestedCommodities = Baloot.getInstance().getSuggestedCommodities(commodity.getId());
    for (Commodity suggestedCommodity: suggestedCommodities){
  %>
  <tr>
    <td><%=suggestedCommodity.getId()%></td>
    <td><%=suggestedCommodity.getName()%></td>
    <td><%=Baloot.getInstance().findProviderById(suggestedCommodity.getProviderId()).getName()%></td>
    <td><%=suggestedCommodity.getPrice()%></td>
    <td><%=String.join(", ", suggestedCommodity.getCategories())%></td>
    <td><%=suggestedCommodity.getRating()%></td>
    <td><%=suggestedCommodity.getInStock()%></td>
    <td><a href=<%="/commodities/" + suggestedCommodity.getId()%>>Link</a></td>
  </tr>
  <% } %>
</table>
</body>
</html>

