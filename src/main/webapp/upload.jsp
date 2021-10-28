<%@ page language="java" pageEncoding="UTF-8" session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import="ru.job4j.dream.model.Candidate" %>
<%@ page import="ru.job4j.dream.store.PsqlStore" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Upload</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
    <script
            src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script
            src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
</head>
<%
    String id = request.getParameter("id");
    Candidate candidate = new Candidate(0, "");
    if (id != null) {
        candidate = PsqlStore.instOf().findCandidateById(Integer.valueOf(id));
    }
%>
<body>
<center>
<div class="container">
    <jsp:include page="menu.jsp" />
    </table>
    <h2>Загрузка фото для <b><%=candidate.getName()%></b> </h2>
    <form action="<%=request.getContextPath()%>/photo?id=<%=request.getParameter("id")%>"
          method="post" enctype="multipart/form-data">
        <div class="checkbox"><input type="file" name="file"></div>
        <button type="submit" class="btn btn-default">Загрузить</button>
    </form>
</div>
</body>
</center>
</html>