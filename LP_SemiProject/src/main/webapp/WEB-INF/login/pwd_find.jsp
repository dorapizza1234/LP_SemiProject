<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String ctxPath = request.getContextPath();
%>
    
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="<%= ctxPath %>/css/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="<%= ctxPath %>/css/bootstrap-4.6.2-dist/css/bootstrap.min.css">

<style>
/* ===== 공통 폰트 기준 ===== */
:root {
  --form-font-size: 16px;
}
.find-menu {
    width: 100%;
    display: flex;
    justify-content: center;
    gap: 30px;
    margin: 20px 0;
}
.find-menu-item {
    display: inline-flex;
    align-items: center;
    font-weight: bold;
    cursor: pointer;
    white-space: nowrap;
}

/* 폼 컨테이너 */
.form-container {
  list-style-type: none;
  padding: 0;
  margin: 0 auto;
  display: table;
}

.form-container li {
  margin: 12px 0;
}

/* 라벨 */
.form-container label {
  display: inline-block;
  width: 90px;
  font-weight: bold;
  font-size: var(--form-font-size);
}

/* input 공통 */
.form-container input,
#div_confirm input {
  width: 220px;
  height: 30px;
  padding: 4px 6px;
  font-size: var(--form-font-size);
  box-sizing: border-box;
}

/* 인증코드 안내 문구 */
#div_confirm span {
  font-size: 12px;
}

/* 버튼 */
.btn-dark,
.btn-info {
  font-size: 13px;
  padding: 7px 26px;
}

/* 스피너 텍스트 스타일 */
#spinner p {
    font-size: 12px;
    color: #666;
    margin-top: 8px;
}
</style>

<script type="text/javascript">
   $(function(){
       const method = "${method}";
       const find_method = "${find_method}";

       // 1. 초기 UI 세팅
       if(method == "POST") {
           $('input:text[name="userid"]').val("${userid}");
           
           if(find_method == "mobile") {
               setFindMethod('mobile');
               $('input:text[name="mobile"]').val("${mobile}");
           } else {
               setFindMethod('email');
               $('input:text[name="email"]').val("${email}");
           }
           
           if("${isUserExists}" == "true" && "${sendSuccess}" == "true") {
               $('#btnFind').hide();
           }
       } else {
           $('div#div_findResult').hide();
       }
       
       // 탭 클릭 이벤트
       $('.find-menu-item').click(function(){
           const id = $(this).attr('id');
           const mode = (id == 'btn_email') ? 'email' : 'mobile';
           setFindMethod(mode);
       });

       // 엔터키 이벤트
       $('input:text[name="email"], input:text[name="mobile"]').bind('keyup', function(e){
           if(e.keyCode == 13) { goFind(); }
       });
       
       $('#btnFind').click(function(){ goFind(); });
       
       // 인증하기 버튼 클릭
       $(document).on('click', 'button.btn-info', function(){
           const input_confirmCode = $('input:text[name="input_confirmCode"]').val().trim(); 
           if(input_confirmCode == "") {
               alert("인증코드를 입력하세요.");
               return;
           }
           
           const frm = document.verifyCertificationFrm;
           frm.userCertificationCode.value = input_confirmCode;
           frm.userid.value = $('input:text[name="userid"]').val();
           
           frm.action = "<%= ctxPath%>/login/verifyCertification.lp";
           frm.method = "post";
           frm.submit();   
       });
   });

   function setFindMethod(mode) {
       $('.find-menu-item').css({'color': '#888', 'border-bottom': 'none'});
       $('input[name="find_method"]').val(mode);

       if(mode == 'email') {
           $('#btn_email').css({'color': '#000', 'border-bottom': '2px solid #000'});
           $('#li_email').show();
           $('#li_mobile').hide();
       } else {
           $('#btn_mobile').css({'color': '#000', 'border-bottom': '2px solid #000'});
           $('#li_email').hide();
           $('#li_mobile').show();
       }
   }

   function goFind(){
       const userid = $('input:text[name="userid"]').val().trim();
       if(userid == "") { alert("아이디를 입력하세요."); return; }
       
       const mode = $('input[name="find_method"]').val();
       if(mode == 'email') {
           const email = $('input:text[name="email"]').val();
           const regExp_email =  /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@naver\.com$/i; 
           if(!regExp_email.test(email)) { alert("이메일을 올바르게 입력하세요."); return; }
       } else {
           const mobile = $('input:text[name="mobile"]').val().trim();
           if(mobile == "") { alert("휴대폰 번호를 입력하세요."); return; }
       }
       
       // 전송 버튼 숨기고 스피너 표시
       $('#btnFind').hide();
       $('#spinner').show();

       const frm = document.pwdFindFrm;
       frm.method = "post";
       frm.action = "<%= ctxPath%>/login/pwd_find.lp"; 
       frm.submit();
   }
</script>


<div class="find-menu mb-4">
    <div id="btn_email" class="find-menu-item active" style="margin: 0 15px; cursor: pointer; font-weight: bold;">이메일로 인증</div>
    <div id="btn_mobile" class="find-menu-item" style="margin: 0 15px; cursor: pointer; font-weight: bold; color: #888;">휴대폰으로 인증</div>
</div>

<form name="pwdFindFrm">
   <input type="hidden" name="find_method" value="email" /> 
   
   <ul class="form-container">
      <li>
          <label>아이디</label>
          <input type="text" name="userid" autocomplete="off" /> 
      </li>
      
      <li id="li_email">
          <label>이메일</label>
          <input type="text" name="email" autocomplete="off" placeholder="pwd_find@naver.com"/> 
      </li>

      <li id="li_mobile" style="display: none;">
          <label>휴대폰번호</label>
          <input type="text" name="mobile" placeholder="'-' 제외 숫자만" autocomplete="off" /> 
      </li>
   </ul> 

   <div class="my-3 text-center">
       <button type="button" id="btnFind" class="btn btn-dark">인증번호 발송</button>
       
       <div id="spinner" style="display: none;">
           <div class="spinner-border text-dark" role="status">
               <span class="sr-only">Loading...</span>
           </div>
           <p>메일을 발송 중입니다. 잠시만 기다려주세요.</p>
       </div>
   </div>
</form>

<div class="my-3 text-center" id="div_findResult">
   <c:if test="${method == 'POST' && isUserExists == false}">
       <span style="color: red;">사용자 정보가 없습니다</span>
   </c:if>
   
   <c:if test="${isUserExists == true && sendSuccess == true}">
       <div id="div_confirm">
           <span style="font-size: 10pt; color: dark;">
               인증코드가 발송되었습니다.<br>
               인증코드를 입력해주세요.
           </span>
           <br>
           <input type="text" name="input_confirmCode" class="mt-2" />
           <br><br> 
           <button type="button" class="btn btn-info">인증하기</button>
       </div>
       
       <script type="text/javascript">
           $('#btnFind').hide();
           $('#spinner').hide(); // 결과가 오면 스피너도 숨김
       </script>
   </c:if>
</div>

<form name="verifyCertificationFrm">
    <input type="hidden" name="userCertificationCode" />
    <input type="hidden" name="userid" />
</form>