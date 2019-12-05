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
			<div class="col-xs-12" style="margin: 15px auto;">
				<label style="font-size: 20px;"><span
					class="glyphicon glyphicon-list-alt"></span>게시글 목록</label>
				<button class="btn btn-primary btn-sm" style="float: right;"
					onclick="location.href='/insert'">Writer</button>
			</div>
			<div class="search_div">
				<form class="form" id="boardSearchVO" name="boardSearchVO">
					<input type="hidden" id="pageIndex" name="pageIndex" value="${boardSearchVO.pageIndex}" /> 
					<input type="hidden" id="pageSize" name="pageSize" value="${boardSearchVO.pageSize}" />
					<input type="hidden" id="bno" name="bno" value="${boardSearchVO.bno}" /> 
					<input type="hidden" id="blt_rsrc_sno" name="blt_rsrc_sno" value="0" />

					<input type="hidden"
				id="searchType2" name="searchType" value="${boardSearchVO.searchType}" /> 
				<input type="hidden"
				id="keyword2" name="keyword" value="${boardSearchVO.keyword}" /> 
				</form>
			</div>

			<h4>total : ${count}</h4>

			<div class="col-xs-12">
				<table class="table table-hover">
					<!-- table-hover -->
					<thead>
						<tr>
							<th class="col-xs-1">번호</th>
							<th class="col-xs-6">제목</th>
							<th class="col-xs-2">글쓴이(ID)</th>
							<th class="col-xs-2">등록일</th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${not empty articleList}">
								<c:forEach items="${articleList}" var="vo" varStatus="idx">

									<tr class="${idx.count % 2 == 1 ? 'trOdd' : 'trEven'}"
										onclick="location.href='/detail/${vo.bno}'" id="mouse">
										<td><c:choose>
												<c:when test="${count > pageSize}">
													<!-- ex) count= 11, pageSize=10 -->
													<c:out
														value="${count - pageSize*(pageIndex-1) - idx.count +1}" />
													<!-- 11,10,9,8.......... -->
												</c:when>
												<c:otherwise>
													<c:out value="${count  - idx.count +1}" />
												</c:otherwise>
											</c:choose>
										</td>
										<td><c:out value="${vo.subject}" /></td>
										<td><c:out value="${vo.writer}" /></td>
										<td><fmt:formatDate value="${vo.reg_date}"
												pattern="yyyy.MM.dd HH:mm:ss" /></td>
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<tr>
									<td colspan="7">조회된 자료가 없습니다.</td>
								</tr>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
			</div>
			<!-- Paging : S -->

			<c:if test="${count > 0}">
				<c:set var="pageCount"
					value="${count / pageSize + ( count % pageSize == 0 ? 0 : 1)}" />
				<c:set var="startPage" value="${pageGroupSize*(nowPageGroup-1)+1}" />
				<c:set var="endPage" value="${startPage + pageGroupSize-1}" />
				<c:if test="${endPage > pageCount}">
					<c:set var="endPage" value="${pageCount}" />
				</c:if>
				<div class="jb-center text-center">
					<ul class="pagination">
						<c:if test="${nowPageGroup > 1}">
							<li><a href="#;"
								onclick='paging_script(${(nowPageGroup-2)*pageGroupSize+1 },${pageSize},"boardSearchVO","/list");'><span
									class="glyphicon glyphicon-chevron-left"></span></a></li>
						</c:if>

						<c:if test="${nowPageGroup == 1}">
							<!-- <li class="disabled"><a href="#"><span class="glyphicon glyphicon-chevron-left"></span></a></li> -->
						</c:if>
						<c:forEach var="i" begin="${startPage}" end="${endPage}">
							<li <c:if test="${pageIndex == i}"> class="active" </c:if>><a
								href="#;"
								onclick='paging_script(${i},${pageSize},"boardSearchVO","/list");'>${i}</a></li>
						</c:forEach>
						<c:if test="${nowPageGroup < pageGroupCount}">
							<li><a href="#;"
								onclick='paging_script(${nowPageGroup*pageGroupSize+1},${pageSize},"boardSearchVO","/list");'><span
									class="glyphicon glyphicon-chevron-right"></span></a></li>
						</c:if>
					</ul>
				</div>
			</c:if>
			<!-- Pageing : E -->
			<!-- search{s} -->

			<div class="search row">
				<div class="col-xs-2 col-sm-2">
					<select class="form-control" name="searchType"
						id="searchType">
						<option value="n"<c:out value="${searchType == null ? 'selected' : ''}"/>>------</option>
						<option value="subject"<c:out value="${searchType eq 'subject' ? 'selected' : ''}"/>>제목</option>
						<option value="content"<c:out value="${searchType eq 'content' ? 'selected' : ''}"/>>내용</option>
						<option value="writer"<c:out value="${searchType eq 'writer' ? 'selected' : ''}"/>>글쓴이</option>
					</select>
				</div>
				<div class="col-xs-10 col-sm-10">
					<div class="input-group">
						<input type="text" class="form-control"
						name="keyword" id="keyword" value="${keyword}">
						<span class="input-group-btn">
							<button class="btn btn-default" name="btnSearch" id="btnSearch">검색</button>
						</span>
					</div>
				</div>
			</div>
			<br/>
			<!-- search{e} -->
		</div>
		<!-- </div> -->
		<c:url var="boardList" value="/list"></c:url>
	</body>
	</html>
	<script type="text/javascript">
		$(document).ready(function() {
	        $("#keyword").keydown(function(key) {
	            if (key.keyCode == 13) {
	                $("#btnSearch").click();
	            }
	        });
	
			$(document).on('click', '#btnSearch', function(e){
				e.preventDefault();
				var url = "${list}";
				url = url + "?searchType=" + $('#searchType').val();
				url = url + "&keyword=" + $('#keyword').val();
				location.href = url;
	
			});
		});

		function paging_script(pageIndex, pageSize, form, url) {
			 var form_id = '#'+form;
			 $(form_id).find("#pageIndex").val(pageIndex);
			 $(form_id).find("#pageSize").val(pageSize);
			 $(form_id).attr('action', url).submit();
			 return false;
		};
	</script>
</layoutTag:layout>
