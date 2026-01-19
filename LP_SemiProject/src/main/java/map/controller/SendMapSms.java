package map.controller;

import java.util.HashMap;

import org.json.simple.JSONObject;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.nurigo.java_sdk.api.Message;





//... (기존 import 생략)

public class SendMapSms extends AbstractController {

 @Override
 public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
	 if("POST".equalsIgnoreCase(request.getMethod())) {
	        String mobile = request.getParameter("mobile");
	        String smsContent = request.getParameter("smsContent");
	        String imageName = request.getParameter("imageName");

	        // 1. 이미지 경로 생성 및 검증
	        String rootPath = request.getServletContext().getRealPath("/");
	        String imagePath = rootPath + "images" + java.io.File.separator + imageName;
	        java.io.File file = new java.io.File(imagePath);

	        System.out.println("--- [발송 전 데이터 체크] ---");
	        System.out.println("수신번호: " + mobile);
	        System.out.println("이미지경로: " + imagePath);
	        System.out.println("파일존재여부: " + file.exists());
	        System.out.println("--------------------------");

	        // 2. CoolSMS 설정
	        String api_key = "NCSIGYCBOJDBFDGX";
	        String api_secret = "VIIHS01C4X0JJNPYCT00YTMP023D3SIM";
	        Message coolsms = new Message(api_key, api_secret);
	        
	        HashMap<String, String> params = new HashMap<String, String>();
	        params.put("to", mobile);
	        params.put("from", "01042842838");
	        params.put("type", "MMS"); // 이미지가 있으면 반드시 MMS
	        params.put("text", smsContent);
	        // 1. 제목을 별도로 짧게 지정합니다 (줄바꿈 방지)
	        params.put("subject", "[VINYST] 약도 안내");
	        // [중요] 파일이 실제 존재할 때만 경로를 넣습니다.
	        if(file.exists()) {
	            params.put("image", imagePath); 
	        } else {
	            // 파일이 없으면 MMS가 실패하므로 타입을 LMS로 낮추거나 경고를 띄워야 함
	            params.put("type", "LMS"); 
	            System.out.println("경고: 이미지가 없어 LMS로 전환 발송 시도");
	        }

	        boolean isSuccess = false;
	        try {
	            // [수정] 형변환을 org.json.simple.JSONObject로 합니다.
	            JSONObject result = (JSONObject) coolsms.send(params); 
	            System.out.println("DEBUG: CoolSMS 응답 상세 -> " + result.toString());

	            // org.json.simple은 get("key") 방식을 사용합니다.
	            // 성공 건수가 있는지 확인 (Long 타입으로 반환될 수 있음)
	            Object successCount = result.get("success_count");
	            if (successCount != null && Integer.parseInt(successCount.toString()) > 0) {
	                isSuccess = true;
	            } else {
	                System.out.println("전송 실패 원인: " + result.get("message"));
	            }
	        } catch (Exception e) {
	            System.out.println("전송 중 예외 발생!");
	            e.printStackTrace();
	        }

	        // 최종 리턴용 JSON (이건 기존 프로젝트 방식에 맞게 org.json.JSONObject를 써도 되지만 혼선을 피하기 위해 직접 문자열을 만듭니다)
	        String jsonResponse = "{\"result\":" + (isSuccess ? 1 : 0) + "}";
	        request.setAttribute("json", jsonResponse);
	        
	        super.setRedirect(false);
	        super.setViewPage("/WEB-INF/jsonview.jsp");
	}
}
    
}