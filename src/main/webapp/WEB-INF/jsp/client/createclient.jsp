<%-- 
    Document   : create
    Created on : Apr 22, 2011, 3:24:13 PM
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
        <title>Create Client</title>
    	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
        <!-- Latest compiled and minified CSS -->
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
		<!-- Latest compiled and minified JavaScript -->
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
    </head>
    <body style="padding-left:5%">
        <h1>Create Client</h1>
        <c:if test="${fn:length(errors) gt 0}">
            <p>Please correct the following errors in your submission:</p>
            <ul>
                <c:forEach items="${errors}" var="error">
                    <li>${error}</li>
                </c:forEach>
            </ul>
        </c:if>
        <form style="width:30%" class="form-group" action="${pageContext.request.contextPath}/client/create" method="POST">
            <br/>
            <label for="company">Company:</label>
            <input class="form-control" type="text" name="company" value="${client.company}"/>
            <br/>
            <label for="website">Website:</label>
            <input class="form-control" type="text" name="website" value="${client.website}"/>
            <br/>
            <label for="mailing">Mailing Address:</label>
            <input class="form-control" type="text" name="mailing" value="${client.mailing}"/>
            <br/>
            <label for="phone">Phone:</label>
            <input class="form-control" type="text" name="phone" value="${client.phone}"/>
            <br/>
            <h2>Associated Persons:</h2>
            
            <c:forEach items="${persons}" var="person">
           		<div class="checkbox">
                	<input type="checkbox" name="personIds" value="${person.personId}"/>${person.lastName}, ${person.firstName} 
            	</div>
            </c:forEach>
            <input class="btn btn-default" type="submit" name="Submit" value="Submit"/>
        </form>
    </body>
</html>
