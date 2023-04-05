<%@ page import="Domain.User" %>
<%@ page import="Service.Baloot" %>
<%@ page import="java.util.List" %>
<%@ page import="Domain.Commodity" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en"><head>
    <meta charset="UTF-8">
    <title>User</title>
    <style>
        li {
            padding: 5px
        }
        table{
            width: 100%;
            text-align: center;
        }
    </style>
</head>
<body>
<%
    User loggedInUser = Baloot.getInstance().getLoggedInUser();
%>
<ul>
    <li id="username">Username: <%=loggedInUser.getUsername()%></li>
    <li id="email">Email: <%=loggedInUser.getEmail()%></li>
    <li id="birthDate">Birth Date: <%=loggedInUser.getBirthDate()%></li>
    <li id="address"><%=loggedInUser.getAddress()%></li>
    <li id="credit">Credit: <%=loggedInUser.getCredit()%></li>
    <li>Current Buy List Price: <%=loggedInUser.getCurrentBuyListPrice()%></li>
    <li>
        <a href="/credit">Add Credit</a>
    </li>
    <li>
        <form action="" method="POST">
            <label>Submit & Pay</label>
            <input id="form_payment" type="hidden" name="userId" value="">
            <button type="submit" name="action" value="payment">Payment</button>
        </form>
    </li>
</ul>
<table>
    <caption>
        <h2>Buy List</h2>
    </caption>
    <tbody><tr>
        <th>Id</th>
        <th>Name</th>
        <th>Provider Name</th>
        <th>Price</th>
        <th>Categories</th>
        <th>Rating</th>
        <th>In Stock</th>
        <th></th>
        <th></th>
    </tr>
    <%
        List<Commodity> buyList = loggedInUser.getBuyList();
        for (Commodity commodity: buyList){
    %>
    <tr>
        <td><%=commodity.getId()%></td>
        <td><%=commodity.getName()%></td>
        <td><%=Baloot.getInstance().findProviderById(commodity.getProviderId()).getName()%></td>
        <td><%=commodity.getPrice()%></td>
        <td><%=commodity.getCategories()%></td>
        <td><%=commodity.getRating()%></td>
        <td><%=commodity.getInStock()%></td>
        <td><a href="<%= "/commodities/" + commodity.getId()%>">Link</a></td>
        <td>
            <form action="" method="POST">
                <input id="form_commodity_id" type="hidden" name="removeCommodityId" value="<%= String.valueOf(commodity.getId()) %>">
                <button type="submit" name="action" value="removeCommodityFromBuyList">Remove</button>
            </form>
        </td>
    </tr>
    <%
        }
    %>
    </tbody></table>

<table>
    <caption>
        <h2>Purchased List</h2>
    </caption>
    <tbody><tr>
        <th>Id</th>
        <th>Name</th>
        <th>Provider Name</th>
        <th>Price</th>
        <th>Categories</th>
        <th>Rating</th>
        <th>In Stock</th>
        <th></th>
        <th></th>
    </tr>
    <%
        List<Commodity> purchasedList = loggedInUser.getPurchasedList();
        for (Commodity commodity: purchasedList){
    %>
    <tr>
        <td><%=commodity.getId()%></td>
        <td><%=commodity.getName()%></td>
        <td><%=Baloot.getInstance().findProviderById(commodity.getProviderId()).getName()%></td>
        <td><%=commodity.getPrice()%></td>
        <td><%=commodity.getCategories()%></td>
        <td><%=commodity.getRating()%></td>
        <td><%=commodity.getInStock()%></td>
        <td><a href="<%= "/commodities/" + commodity.getId()%>">Link</a></td>
    </tr>
    <%
        }
    %>
    </tbody></table>

    <form action="" method="POST">
        <label for="discountCode">Discount Code</label>
        <input type="text" id="discountCode" name="discountCode">
        <button type="submit" name="action" value="applyDiscountCode">Apply Discount Code</button>
    </form>

</body></html>
