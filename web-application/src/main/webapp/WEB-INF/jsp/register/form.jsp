<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Registration page</title>
</head>
<body>
<h2>Registration form</h2>
<form:form method="post" modelAttribute="loginInfoDTO">
    <form:input path="loginInfo.login"></form:input>
    <form:input path="loginInfo.password"></form:input>
</form:form>
<script src="webjars/Semantic-UI/2.4.1/semantic.js"></script>
</body>
</html>