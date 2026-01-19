let b_emailcheck_click = false;
let originalEmail = "";

$(function () {

    // 기존 이메일 저장 (페이지 로딩 시)
    originalEmail = $("#email").val();

    /* =========================
       이메일 중복확인
    ========================= */
    $("#btnEmailCheck").on("click", function () {

        const email = $("#email").val().trim();
        const regEmail =
            /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@naver\.com$/i;

        if (email === "") {
            alert("이메일을 입력하세요.");
            $("#email").focus();
            return;
        }

        if (!regEmail.test(email)) {
            alert("이메일 형식이 올바르지 않습니다.");
            $("#email").focus();
            return;
        }

        // 기존 이메일이면 중복확인 필요 없음
        if (email === originalEmail) {
            $("#emailCheckResult")
                .text("기존 이메일입니다.")
                .css("color", "green");
            b_emailcheck_click = true;
            return;
        }

        $.ajax({
            url: ctxPath + "/member/emailDuplicateCheck.lp",
            type: "post",
            data: { email: email },
            dataType: "json",
            success: function (json) {
                if (json.isExists) {
                    $("#emailCheckResult")
                        .text("이미 사용중인 이메일입니다.")
                        .css("color", "red");
                    b_emailcheck_click = false;
                } else {
                    $("#emailCheckResult")
                        .text("사용 가능한 이메일입니다.")
                        .css("color", "green");
                    b_emailcheck_click = true;
                }
            }
        });
    });

    /* =========================
       이메일 수정 시 중복확인 초기화
    ========================= */
    $("#email").on("input", function () {
        b_emailcheck_click = false;
        $("#emailCheckResult")
            .text("이메일 중복확인을 해주세요.")
            .css("color", "red");
    });

    /* =========================
       휴대폰 숫자만 입력
    ========================= */
	$("#hp2, #hp3").on("input", function () {
	        // 숫자만 남기기
	        this.value = this.value.replace(/[^0-9]/g, "");
	        
	        // 4자리가 넘어가면 자르기
	        if (this.value.length > 4) {
	            this.value = this.value.slice(0, 4);
	        }
	        
	        // hp2 다 채우면 hp3로 자동 포커스
	        if (this.id === "hp2" && this.value.length === 4) {
	            $("#hp3").focus();
	        }
	    });
	});

/* ======================================================
   회원정보 수정 전송
====================================================== */
function goEdit() {

    // 1️ 성명 검사
    const name = $("#name").val().trim();
    const regName = /^([가-힣]{2,10}|[a-zA-Z]{2,20})$/;

	if (!regName.test(name)) {
	      alert("성명은 한글 2~10자 또는 영문 2~20자만 가능합니다.");
	      $("#name").focus();
	      return;
	  }

	
	
    if (name === "") {
        alert("성명을 입력하세요.");
        $("#name").focus();
        return;
    }

  
    // 2️ 이메일 검사
    const email = $("#email").val().trim();

    if (email === "") {
        alert("이메일을 입력하세요.");
        $("#email").focus();
        return;
    }

    // 이메일이 변경된 경우만 중복확인 강제
    if (email !== originalEmail && !b_emailcheck_click) {
        alert("이메일 중복확인을 해주세요.");
        return;
    }

	// 3️. 휴대폰 검사 (정확히 4자리인지 다시 확인)
	    const hp2 = $("#hp2").val().trim();
	    const hp3 = $("#hp3").val().trim();
	    const regExp_hp = /^\d{4}$/; // 정확히 숫자 4자리

	    if (!regExp_hp.test(hp2)) {
	        alert("휴대폰 번호 중간 자리를 숫자 4자리로 입력하세요.");
	        $("#hp2").focus();
	        return;
	    }

	    if (!regExp_hp.test(hp3)) {
	        alert("휴대폰 번호 끝 자리를 숫자 4자리로 입력하세요.");
	        $("#hp3").focus();
	        return;
	    }
		
		//주소
		const detailAddress=$("#detailAddress").val().trim();
		if(detailAddress===""){
			alert("상세 주소를 입력해주세요.");
			$("#detailAddress").focus(); // 입력창으로 커서 이동
			    return; // 함수 종료 (전송 중단)
		}

    // 4️ 폼 전송
    const frm = document.forms["editFrm"];
    frm.method = "post";
    frm.action = ctxPath + "/my_info/my_info.lp";
    frm.submit();
}
