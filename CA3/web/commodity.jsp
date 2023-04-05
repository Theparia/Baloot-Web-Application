<%@ page import="Domain.Commodity" %>
<%@ page import="Service.Baloot" %>
<%@ page import="Domain.Comment" %>

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
<span>username: <%=Baloot.getInstance().getLoggedInUser().getUsername()%></span>
<br>
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
  <input type="text" name="comment" value="" />
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
    <td><%=Baloot.getInstance().getUsernameByEmail(comment.getUserEmail())%>></td>
    <td><%=comment.getText()%>></td>
    <td><%=comment.getDate()%></td>
    <td>
      <form action="" method="POST">
        <label><%=comment.getLikeCount()%></label>
        <input type="hidden" name="voteLike" value="1"/>
        <input type="hidden" name="commentId" value=<%=comment.getId()%>/>
        <button type="submit" name="action" value="likeComment">like</button>
      </form>
    </td>
    <td>
      <form action="" method="POST">
        <label><%=comment.getDisLikeCount()%></label>
        <input type="hidden" name="voteDislike" value="-1"/>
        <input type="hidden" name="commentId" value=<%=comment.getId()%>/>
        <button type="submit" name="action" value="dislikeComment">dislike</button>
      </form>
    </td>
  </tr>
  <% } %>

<%--  <tr>--%>
<%--    <td>user1</td>--%>
<%--    <td>Good</td>--%>
<%--    <td>2022-07-25</td>--%>
<%--    <td>--%>
<%--      <form action="" method="POST">--%>
<%--        <label for="">2</label>--%>
<%--        <input--%>
<%--                id="form_comment_id"--%>
<%--                type="hidden"--%>
<%--                name="comment_id"--%>
<%--                value="1"--%>
<%--        />--%>
<%--        <button type="submit" name="action" value="likeComment">like</button>--%>
<%--      </form>--%>
<%--    </td>--%>
<%--    <td>--%>
<%--      <form action="" method="POST">--%>
<%--        <label for="">1</label>--%>
<%--        <input--%>
<%--                id="form_comment_id"--%>
<%--                type="hidden"--%>
<%--                name="comment_id"--%>
<%--                value="-1"--%>
<%--        />--%>
<%--        <button type="submit" name="action" value="dislikeComment">dislike</button>--%>
<%--      </form>--%>
<%--    </td>--%>
<%--  </tr>--%>
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
  <tr>
    <td>2341</td>
    <td>Galaxy S22</td>
    <td>Phone Provider No.1</td>
    <td>34000000</td>
    <td>Technology, Phone</td>
    <td>8.3</td>
    <td>17</td>
    <td><a href="/commodities/2341">Link</a></td>
  </tr>
  <tr>
    <td>4231</td>
    <td>Galaxy S22 Plus</td>
    <td>Phone Provider No.1</td>
    <td>43000000</td>
    <td>Technology, Phone</td>
    <td>8.7</td>
    <td>12</td>
    <td><a href="/commodities/4231">Link</a></td>
  </tr>
  <tr>
    <td>1234</td>
    <td>Galaxy S22 Ultra</td>
    <td>Phone Provider No.2</td>
    <td>50000000</td>
    <td>Technology, Phone</td>
    <td>8.9</td>
    <td>5</td>
    <td><a href="/commodities/1234">Link</a></td>
  </tr>
  <tr>
    <td>4321</td>
    <td>Galaxy A53</td>
    <td>Phone Provider No.2</td>
    <td>16000000</td>
    <td>Technology, Phone</td>
    <td>8.7</td>
    <td>11</td>
    <td><a href="/commodities/4321">Link</a></td>
  </tr>
</table>
</body>
</html>

