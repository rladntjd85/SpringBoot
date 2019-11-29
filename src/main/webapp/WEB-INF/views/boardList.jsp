<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="layoutTag" tagdir="/WEB-INF/tags"%>    
<layoutTag:layout>    

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>List</title>
</head>
<body>
 
<div class="container">
    <div class="col-xs-12" style="margin:15px auto;">
        <label style="font-size:20px;"><span class="glyphicon glyphicon-list-alt"></span>게시글 목록</label>
        <button class="btn btn-primary btn-sm" style="float:right;" onclick="location.href='/insert'">글쓰기</button>
    </div>
    
    <div class="col-xs-12">
        <table class="table table-hover">
            <tr>
                <th>No</th>
                <th>Subject</th>
                <th>Writer</th>
                <th>Date</th>
            </tr>
              <c:forEach var="l" items="${boardList}">
                  <tr onclick="location.href='/detail/${l.bno}'" id="mouse">
                      <td>${l.bno}</td>
                      <td>${l.subject}</td>
                      <td>${l.writer}</td>
                      <td>
                        <fmt:formatDate value="${l.reg_date}" pattern="yyyy.MM.dd HH:mm:ss"/>
                    </td>
                  </tr>
              </c:forEach>
        </table>
     </div>
    <br/>
    <div class="text-center">
	    <span><a href="/list?currentPage=${currentPage=1}">처음</a></span>
	    <!-- 현재 페이지가 1보다 클 경우 이전 href, 1보다 작은 경우 이전 text -->
	   <%-- 	<c:choose>
			 <c:when test="${currentPage != startPageNum}"><a href="/list?currentPage=${currentPage-1}">이전페이지</a></c:when>
			 <c:otherwise>이전</c:otherwise>
		</c:choose> --%>
	    <!-- #number.sequence 인수로 지정한 2개의 수 범위에서 배열을 생성 -->
	    <c:forEach var="num" begin="${startPageNum}" end="${lastPageNum}">
	    	<c:choose>
	    	<c:when test="${currentPage == num}">${num}</c:when>
			<c:otherwise><a href="/list?currentPage=${num}">${num }</a></c:otherwise>
			</c:choose>
	    </c:forEach>
	    <!-- 현재 페이지가 마지막페이지와 같지 않을 경우 다음 href, 같을 경우 다음 text -->
	   <%--  <c:choose>
			 <c:when test="${currentPage != lastPage}"><a href="/list?currentPage=${currentPage+1}">다음</a></c:when>
			 <c:otherwise>다음</c:otherwise>
		</c:choose> --%>
		<span><a href="/list?currentPage=${lastPage}">마지막</a></span>
	</div>
    <br/>
</div>
</body>
</html>
 
</layoutTag:layout>