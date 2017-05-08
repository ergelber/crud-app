<%-- 
    Document   : edit
    Created on : Apr 22, 2011, 3:04:46 PM
    Author     : FMilens
--%>

<%@taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions'%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Edit Person</title>
    	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
        <!-- Latest compiled and minified CSS -->
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
		<!-- Latest compiled and minified JavaScript -->
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
    </head>
    <body style="padding-left:5%">
        <h1>Edit Person</h1>
        <c:if test="${fn:length(errors) gt 0}">
            <p>Please correct the following errors in your submission:</p>
            <ul>
                <c:forEach items="${errors}" var="error">
                    <li>${error}</li>
                </c:forEach>
            </ul>
        </c:if>
        <form style="width:30%" class="form-group" action="${pageContext.request.contextPath}/person/edit" method="POST">
            <input class="form-control" type="hidden" name="personId" value="${person.personId}"/>
            <br/>
            <label for="firstName">First Name:</label>
            <input class="form-control" type="text" name="firstName" value="${person.firstName}"/>
            <br/>
            <label for="lastName">Last Name:</label>
            <input class="form-control" type="text" name="lastName" value="${person.lastName}"/>
            <br/>
            <label for="emailAddress">Email Address:</label>
            <input class="form-control" type="text" name="emailAddress" value="${person.emailAddress}"/>
            <br/>
            <label for="streetAddress">Street Address:</label>
            <input class="form-control" type="text" name="streetAddress" value="${person.streetAddress}"/>
            <br/>
            <label for="city">City:</label>
            <input class="form-control" type="text" name="city" value="${person.city}"/>
            <br/>
            <label for="state">State:</label>
            <input class="form-control" type="text" name="state" value="${person.state}"/>
            <br/>
            <label for="zipCode">Zip Code:</label>
            <input class="form-control" type="text" name="zipCode" value="${person.zipCode}"/>
            <br/>
            
            <c:forEach items="${clients}" var="client">
           		<div class="checkbox">
                	<input type="checkbox" name="clientIds" value="${client.clientId}"/>${client.company}
            	</div>
            </c:forEach>
            
            
            <input class="btn ntw-default" type="submit" name="Submit" value="Submit"/>
        </form>
    </body>
</html>
