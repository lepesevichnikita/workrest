<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!--
Created by IntelliJ IDEA.
User:User
Date: 18.02.2020
Time: 16:08
-->
<html>
<head><title>Create new administrator</title></head>
<body>
<form:form action="/system-administrator/" method="post" modelAttribute="loginInfoDTO">
    <form:label path="login">Login</form:label>
    <form:input path="login"></form:input>
    <form:label path="password">Password</form:label>
    <form:input path="password"></form:input>
    <input type="submit">
</form:form>
</body>
</html>
