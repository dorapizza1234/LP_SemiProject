<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String ctxPath = request.getContextPath();
	// /LP_SemiProject
%>
 <link rel="stylesheet" href="<%= ctxPath%>/css/common/footer.css">

</div>

 <footer class="footer">
  <div class="footer-inner">

   
   <div style="margin-bottom: 20px;">
      <a href="${pageContext.request.contextPath}/map/storeLocation.lp" class="btn-location-link">찾아오시는길</a>
    </div>
    <p class="footer-line">
      
      상호: 주식회사 바이니스트(VINYST) | 
      대표: 정민정 |
      개인정보관리책임자: 이시형 |
      전화: 02-123-4567 |
      이메일: vinyst@hanmail.net
    </p>

    <p class="footer-line">
      주소: 서울 강남구 테헤란로70길 12 9층 쌍용교육센터 |
      사업자등록번호: 123-45-6789 |
    </p>

    <p class="footer-copy">
      © 2026 VINYST. All rights reserved.
      
    </p>
        <a href="<%= ctxPath%>/admin/admin_login.lp" style="text-decoration: none; color: #666;">관리자로그인</a>
  </div>
</footer>


</body>
</html>