<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Registration page</title>
</head>
<body>
<h2>Registration form</h2>
<form:form action="/register" method="post" modelAttribute="loginInfoDTO">
    <form:label path="login">Login</form:label>
    <form:input path="login">${loginInfoDTO.login}</form:input>
    <form:label path="password">Password</form:label>
    <form:input path="password">${loginInfoDTO.password}</form:input>
</form:form>
<script src="webjars/Semantic-UI/2.4.1/semantic.js"></script>
</body>
</html>