<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../header1.jsp" />

<style type="text/css">
   #map-page-wrapper { 
       width: min(1160px, calc(100% - 48px)); 
       margin: 40px auto; 
       font-family: 'Inter', 'Pretendard', sans-serif; 
       min-height: 800px;
   }

   #map-page-wrapper #title { 
       font-size: 24px; 
       padding: 20px 0 30px 0; 
       text-align: left; 
       font-weight: 800; 
       color: #0f0f10; 
       letter-spacing: -0.5px;
   }
   
   .map-flex-box { 
       display: flex; 
       gap: 30px; 
       align-items: flex-start; /* 늘어남 방지 */
   }
   
   /* 지도와 카드의 높이를 동일하게 680px로 고정 */
   #map { 
       flex: 1.8; 
       min-width: 400px; 
       height: 680px; 
       border: 1px solid rgba(0,0,0,0.08); 
       border-radius: 24px; 
       box-shadow: 0 10px 40px rgba(0,0,0,0.04);
       z-index: 1; 
   }
   
   #info-side-panel { 
       flex: 1; 
       min-width: 320px; 
       height: 680px; /* 고정 높이 */
       background-color: #fff; 
       padding: 35px; 
       border: 1px solid rgba(0,0,0,0.08); 
       border-radius: 24px; 
       box-shadow: 0 10px 40px rgba(0,0,0,0.04);
       display: flex; 
       flex-direction: column;
       box-sizing: border-box;
   }

   #info-side-panel h2 { 
       color: #d95050; 
       margin: 0 0 25px 0; 
       font-size: 26px; 
       font-weight: 800; 
   }

   .info-list { 
       margin-bottom: 20px; 
       border-bottom: 1px solid #f2f2f2; 
       padding-bottom: 20px; 
   }

   .info-list strong { 
       display: block; 
       font-size: 12px; 
       color: #888; 
       margin-bottom: 8px; 
       text-transform: uppercase;
   }

   .info-list p { 
       margin: 0; 
       font-size: 15px; 
       color: #0f0f10; 
       line-height: 1.6; 
       font-weight: 500;
   }

   /* 약도 전송 영역: 이미 공간이 확보되어 있어도 깔끔하게 */
   #sms-mini-form { 
       display: none; 
       margin-top: 15px; 
       padding: 20px; 
       background-color: #f9f9f9; 
       border-radius: 16px; 
   }

   #sms-mini-form input { 
       width: 100%; 
       padding: 12px 15px; 
       margin-bottom: 10px; 
       border: 1px solid #ddd; 
       border-radius: 10px; 
       box-sizing: border-box;
   }
   
   .btn-black { 
       background-color: #121212; 
       color: #fff; 
       border: none; 
       padding: 16px; 
       width: 100%; 
       cursor: pointer; 
       border-radius: 14px; 
       font-weight: 600; 
       font-size: 15px;
   }

   .btn-red { 
       background-color: #d95050; 
       color: #fff; 
       border: none; 
       padding: 12px; 
       width: 100%; 
       cursor: pointer; 
       border-radius: 10px; 
       font-weight: 600; 
   }

   .custom-iw { padding: 15px; font-size: 13px; }

   @media (max-width: 992px) {
       .map-flex-box { flex-direction: column; }
       #map, #info-side-panel { width: 100%; height: 500px; }
   }
</style>

<div id="map-page-wrapper">
    <div id="title">매장위치</div>

    <div class="map-flex-box">
        <div id="map"></div>

        <div id="info-side-panel">
            <h2>VINYST</h2>
            
            <div class="info-list">
                <strong>ADDRESS</strong>
                <p>서울 강남구 테헤란로70길 12<br>H타워 9층 바이니스트</p>
            </div>

            <div class="info-list">
                <strong>TIME</strong>
                <p>평일 11:00 - 19:00<br>주말 10:00 - 20:00</p>
            </div>

            <div class="info-list" style="border:none; padding-bottom:0;">
                <strong>PARKING</strong>
                <p>기계식 주차 (1시간 무료)</p>
            </div>

            <div style="margin-top: auto;">
                <button type="button" id="btn-show-mini-sms" class="btn-black">
                    <i class="fa-regular fa-paper-plane" style="margin-right:8px;"></i>약도 문자 전송
                </button>
                
                <div id="sms-mini-form">
                    <input type="text" id="mobile" placeholder="휴대폰 번호 (- 제외)" maxlength="11" />
                    <button type="button" id="btn-send-sms-final" class="btn-red">전송하기</button>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=1e7dbbe62b92abfc6be74e727bc2ecf3"></script>

<script type="text/javascript">
$(function(){
    // 1. 지도 설정
    var storePos = new kakao.maps.LatLng(37.504631, 127.053198);
    var mapContainer = document.getElementById("map");
    var mapOption = { center: storePos, level: 3 };
    var mapobj = new kakao.maps.Map(mapContainer, mapOption);
    
    // 2. 마커 생성
    var marker = new kakao.maps.Marker({ position: storePos, map: mapobj });

    // 3. 인포윈도우 생성
    var iwContent = '<div class="custom-iw"><strong>VINYST LP SHOP</strong><br/>H타워 9층</div>';
    var infowindow = new kakao.maps.InfoWindow({ content: iwContent });
    infowindow.open(mapobj, marker);

    // 4. 약도 폼 토글
    $("#btn-show-mini-sms").click(function(){
        $("#sms-mini-form").stop().slideToggle('fast');
        $("#mobile").focus();
    });

    // 5. SMS 전송 AJAX
    $("#btn-send-sms-final").click(function(){
        const mobile = $("#mobile").val().trim();
        const regPhone = /^01([0|1|6|7|8|9])([0-9]{3,4})([0-9]{4})$/;

        if(!regPhone.test(mobile)) {
            alert("휴대폰 번호 형식이 올바르지 않습니다.");
            return;
        }

        
        let storeInfo ="*오시는 길*\n";
        storeInfo += "서울 강남구 테헤란로70길 12\nH타워 9층 쌍용교육센터\n\n";
        storeInfo +="선릉역 1번 출구에서 직진 후 던킨도너츠에서 우회전 후 직진하면됩니다.\n\n";
        storeInfo +="*영업시간*\n";
        storeInfo +="평일:11시~19시/주말:10~20시\n\n";
        storeInfo += "TEL: 02-123-4567\n";
        storeInfo += "주차: 1시간 무료";

        $.ajax({
            url: "${pageContext.request.contextPath}/map/sendMapSms.lp",
            type: "POST",
            data: { "mobile": mobile, "smsContent": storeInfo ,"imageName": "map.jpg"},
            dataType: "json",
            success: function(json){
                if(json.result == 1) {
                    alert("성공적으로 발송되었습니다.");
                    $("#sms-mini-form").slideUp();
                    $("#mobile").val("");
                } else {
                    alert("발송 실패: 관리자에게 문의하세요.");
                }
            },
            error: function() {
                alert("서버 통신 오류가 발생했습니다.");
            }
        });
    });

    // 브라우저 리사이즈 시 지도 중심 재설정
    $(window).resize(function() {
        mapobj.relayout();
        mapobj.setCenter(storePos);
    });
});
</script>

<jsp:include page="../footer1.jsp" />