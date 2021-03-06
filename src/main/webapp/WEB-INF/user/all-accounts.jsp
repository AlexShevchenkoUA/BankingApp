<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="ctg" uri="custom" %>

<html>

<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no" />
    <title>Opening Request</title>

    <link rel="stylesheet" href="<c:url value="/css/bootstrap.css" />">
    <link rel="stylesheet" href="<c:url value="/css/bootstrap-grid.css" />">
    <link rel="stylesheet" href="<c:url value="/css/bootstrap-reboot.css" />">
    <link rel="stylesheet" href="<c:url value="/css/custom.css" />">

    <script src="<c:url value="/js/jquery-3.2.1.slim.min.js" />"></script>
    <script src="<c:url value="/js/popper.min.js" />"></script>
    <script src="<c:url value="/js/bootstrap.js" />"></script>
    <script src="<c:url value="/js/bootstrap.bundle.js" />"></script>
</head>

<body>
<fmt:setLocale value="${sessionScope.lang}" scope="session" />
<fmt:setBundle basename="content" />

<jsp:include page="../components/navbar.jsp" />

<div class="container my-lg-4">
    <div class="row justify-content-start my-lg-2">
        <p class="h2"><fmt:message key="content.accounts.welcome" /></p>
    </div>
    <c:choose>
        <c:when test="${not empty requestScope.accounts}">
            <div class="row justify-content-center">
                <div class="col-3">
                    <p class="font-weight-bold"><fmt:message key="content.accounts.id" /></p>
                </div>
                <div class="col-3">
                    <p class="font-weight-bold"><fmt:message key="content.accounts.balance" /></p>
                </div>
                <div class="col-3">
                    <p class="font-weight-bold"><fmt:message key="content.accounts.status" /></p>
                </div>
                <div class="col-3">
                    <p class="font-weight-bold"><fmt:message key="content.accounts.info" /></p>
                </div>
            </div>
            <c:forEach items="${requestScope.accounts}" var="accounts">
                <div class="row justify-content-center my-2">
                    <div class="col-3">
                        <p>${accounts.id}</p>
                    </div>
                    <div class="col-3">
                        <p><ctg:balance balance="${accounts.balance}" currency="${accounts.currency}" /></p>
                    </div>
                    <div class="col-3">
                        <ctg:status account="${accounts}" localeTag="${sessionScope.lang}" />
                    </div>
                    <div class="col-3">
                        <form method="post" action="${pageContext.request.contextPath}/api/infoAccount">
                            <input type="hidden" name="accountId" value="${accounts.id}" />
                            <button class="btn btn-lg btn-primary btn-block" type="submit"><fmt:message key="content.accounts.info" /></button>
                        </form>
                    </div>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <div class="row justify-content-center my-2">
                <div class="h2 text-muted"><fmt:message key="content.accounts.id.empty" /></div>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="../components/footer.jsp" />
</body>
</html>
