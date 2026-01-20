$(document).ready(function(){

    // 1. [전체 선택] 체크박스 클릭 시
    $("#checkAll").click(function(){
        var bool = $(this).prop("checked");
        $("input[name='reviewno']").prop("checked", bool);
    });
    
    // 2. [개별] 체크박스 클릭 시 전체선택 박스 상태 동기화
    $("input[name='reviewno']").click(function(){
        var total = $("input[name='reviewno']").length;
        var checked = $("input[name='reviewno']:checked").length;
        
        if(total == checked) {
            $("#checkAll").prop("checked", true);
        } else {
            $("#checkAll").prop("checked", false);
        }
    });

});

// =================================================
// 1. 선택 삭제 실행 함수 (체크박스 선택 후 삭제)
// =================================================
function goDelete() {
    
    // 1. 체크된 항목의 값을 배열에 담기
    var checkArr = [];
    $("input[name='reviewno']:checked").each(function(){
        checkArr.push($(this).val());
    });
    
    // 2. 선택된 항목이 없는 경우
    if(checkArr.length == 0) {
        alert("삭제할 리뷰를 하나 이상 선택해주세요.");
        return;
    }
    
    // 3. 삭제 확인
    if(!confirm("선택한 리뷰 " + checkArr.length + "개를 정말 삭제하시겠습니까?")) {
        return;
    }
    
    // 4. 배열을 콤마(,) 구분 문자열로 변환
    var reviewnos = checkArr.join(",");
    
    // 5. AJAX 요청 전송
    $.ajax({
        url: ctxPath + "/admin/admin_review.lp",
        type: "POST",
        data: { 
            "mode": "delete",
            "reviewnos": reviewnos
        },
        dataType: "json",
        success: function(json) {
            if(json.result == 1) {
                alert("성공적으로 삭제되었습니다.");
                location.reload(); 
            } else {
                alert(json.message);
            }
        },
        error: function(request, status, error) {
            alert("서버 통신 오류가 발생했습니다.\ncode:"+request.status+"\n"+"error:"+error);
        }
    });
} //

// =================================================
// 2. 개별 삭제 실행 함수 (버튼 클릭 시 바로 삭제)
// =================================================
function goDeleteOne(reviewno) {
    
    // 1. 삭제 확인
    if(!confirm("해당 리뷰를 정말 삭제하시겠습니까?")) {
        return;
    }
    
    // 2. AJAX 요청 전송
    $.ajax({
        url: ctxPath + "/admin/admin_review.lp",
        type: "POST",
        data: { 
            "mode": "delete",
            "reviewnos": reviewno 
        },
        dataType: "json",
        success: function(json) {
            if(json.result == 1) {
                alert("삭제되었습니다.");
                location.reload(); 
            } else {
                alert(json.message);
            }
        },
        error: function(request, status, error) {
            alert("서버 통신 오류가 발생했습니다.\ncode:"+request.status+"\n"+"error:"+error);
        }
    });
}