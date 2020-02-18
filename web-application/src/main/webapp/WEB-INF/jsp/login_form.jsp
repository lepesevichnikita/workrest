<%--
  Created by IntelliJ IDEA.
  User: nikital
  Date: 2/18/20
  Time: 8:12 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
</head>
<body>
<h2>Login form</h2>
<form:form action="/register" method="post" modelAttribute="loginInfoDTO">
    <form:label path="login">Login</form:label>
    <form:input path="login"></form:input>
    <form:label path="password">Password</form:label>
    <form:input path="password"></form:input>
</form:form>
<script src="webjars/Semantic-UI/2.4.1/semantic.js"></script>
</body>
</html>
