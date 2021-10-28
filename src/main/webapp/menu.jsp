<%@page pageEncoding="UTF-8"%>
<div class="row">
    <ul class="nav">
        <li class="nav-item">
            <a class="nav-link" href="<%=request.getContextPath()%>/posts.do">Вакансии</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="<%=request.getContextPath()%>/candidates.do">Кандидаты</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="<%=request.getContextPath()%>/post/edit.jsp">Добавить вакансию</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="<%=request.getContextPath()%>/candidate/edit.jsp">Добавить кандидата</a>
            <c:if test="${user != null}">
        <li class="nav-item">
            <a class="nav-link" href="<%=request.getContextPath()%>/logout.do"> <c:out value="${user.name}"/> | Выйти</a>
        </li>
        </c:if>
    </ul>
</div>