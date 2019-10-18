<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<body>
    <h1>New Loan Application</h1>
        
    <form:form modelAttribute="form">
        <form:errors path="" element="div" />
        <div>
            <form:label path="name">Name</form:label>
            <form:input path="name" />
            <form:errors path="name" />
        </div>
        
        <div>
            <form:label path="principal">Principal</form:label>
            <form:input path="principal" />
            <form:errors path="principal" />
        </div>

        <div>
            <form:label path="termInMonths">Term</form:label>
            <form:input path="termInMonths" />
            <form:errors path="termInMonths" />
        </div>
        
        
        <div>
            <input type="submit" />
        </div>
    </form:form>
</body>
</html>