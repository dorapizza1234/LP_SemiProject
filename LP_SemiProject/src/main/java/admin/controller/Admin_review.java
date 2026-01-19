package admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject; 

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import admin.model.AdminDAO;
import admin.model.AdminVO;
import admin.model.InterAdminDAO;

public class Admin_review extends AbstractController {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        HttpSession session = request.getSession();
        AdminVO loginadmin = (AdminVO) session.getAttribute("loginAdmin"); 

        if (loginadmin == null) {
            String message = "관리자만 접근 가능합니다.";
            String loc = request.getContextPath() + "/admin/admin_login.lp"; 
            
            request.setAttribute("message", message);
            request.setAttribute("loc", loc);
            
            super.setRedirect(false);
            super.setViewPage("/WEB-INF/msg.jsp");
            return;
        }
        
        String method = request.getMethod();
        String mode = request.getParameter("mode"); 
        
        InterAdminDAO adao = new AdminDAO();
        
        // [기능 1] 리뷰 선택 삭제 (AJAX 요청)
        if("POST".equalsIgnoreCase(method) && "delete".equals(mode)) {
            deleteReview(request, response, adao);
        }
        // [기능 2] 리뷰 목록 조회 (기본 GET 방식)
        else {
            listReview(request, response, adao);
        }
    }
    
    // 리뷰 목록 조회 메소드
    private void listReview(HttpServletRequest request, HttpServletResponse response, InterAdminDAO adao) throws Exception {
        
        Map<String, String> paraMap = new HashMap<>();
        
        String str_currentShowPageNo = request.getParameter("currentShowPageNo");
        int currentShowPageNo = 0;
        
        try {
            if(str_currentShowPageNo == null) currentShowPageNo = 1;
            else currentShowPageNo = Integer.parseInt(str_currentShowPageNo);
        } catch(NumberFormatException e) {
            currentShowPageNo = 1;
        }
        
        int sizePerPage = 10; 
        
        int totalCount = adao.getTotalReviewCount(paraMap);
        int totalPage = (int) Math.ceil((double)totalCount/sizePerPage);
        
        if(currentShowPageNo < 1) currentShowPageNo = 1;
        if(currentShowPageNo > totalPage) currentShowPageNo = totalPage;
        
        int startRno = ((currentShowPageNo - 1) * sizePerPage) + 1;
        int endRno = startRno + sizePerPage - 1;
        
        paraMap.put("startRno", String.valueOf(startRno));
        paraMap.put("endRno", String.valueOf(endRno));
        
        List<Map<String, String>> reviewList = adao.getReviewListWithPaging(paraMap);
        
        int startIter = ((currentShowPageNo - 1) * sizePerPage) + 1;
        
        // ==================================================================
        // [수정 포인트] 공통 페이징 로직 적용
        // ==================================================================
        int blockSize = 10;
        int startPage = ((currentShowPageNo - 1) / blockSize) * blockSize + 1;
        int endPage = startPage + blockSize - 1;
        
        if(endPage > totalPage) {
            endPage = totalPage;
        }
        
        String url = "admin_review.lp";
        String pageBar = "";
        
        // 1. [맨처음]
        if(currentShowPageNo > 1) {
             pageBar += "<a href='"+url+"?currentShowPageNo=1' class='page-first'>맨처음</a>";
        } else {
             pageBar += "<span class='page-first disabled'>맨처음</span>";
        }

        // 2. [<] 이전
        if(currentShowPageNo > 1) {
            pageBar += "<a href='"+url+"?currentShowPageNo="+(currentShowPageNo-1)+"' class='page-prev'>&lt;</a>";
        } else {
            pageBar += "<span class='page-prev disabled'>&lt;</span>";
        }
        
        // 3. [페이지 번호]
        for(int i = startPage; i <= endPage; i++) {
            if(i == currentShowPageNo) {
                pageBar += "<span class='active'>"+i+"</span>";
            } else {
                pageBar += "<a href='"+url+"?currentShowPageNo="+i+"'>"+i+"</a>";
            }
        }
        
        // 4. [>] 다음
        if(currentShowPageNo < totalPage) {
             pageBar += "<a href='"+url+"?currentShowPageNo="+(currentShowPageNo+1)+"' class='page-next'>&gt;</a>";
        } else {
             pageBar += "<span class='page-next disabled'>&gt;</span>";
        }
        
        // 5. [맨마지막]
        if(currentShowPageNo < totalPage) {
            pageBar += "<a href='"+url+"?currentShowPageNo="+totalPage+"' class='page-last'>맨마지막</a>";
        } else {
             pageBar += "<span class='page-last disabled'>맨마지막</span>";
        }
        
        request.setAttribute("reviewList", reviewList);
        request.setAttribute("pageBar", pageBar);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("startIter", startIter);
        
        super.setRedirect(false);
        super.setViewPage("/WEB-INF/admin/admin_review.jsp");
    }
    
    // 리뷰 삭제 메소드 (AJAX)
    private void deleteReview(HttpServletRequest request, HttpServletResponse response, InterAdminDAO adao) throws Exception {
        
        String reviewnos = request.getParameter("reviewnos");
        Map<String, Object> map = new HashMap<>();
        
        try {
            if(reviewnos != null && !reviewnos.trim().isEmpty()) {
                String[] reviewIdArr = reviewnos.split(",");
                
                int n = adao.deleteMultiReviews(reviewIdArr); 
                
                if(n == reviewIdArr.length) { 
                    map.put("result", 1); 
                } else {
                    map.put("result", 0); 
                    map.put("message", "일부 리뷰 삭제에 실패했습니다.");
                }
            } else {
                map.put("result", 0);
                map.put("message", "선택된 리뷰가 없습니다.");
            }
        } catch(Exception e) {
            e.printStackTrace();
            map.put("result", 0);
            map.put("message", "에러 발생: " + e.getMessage());
        }
        
        JSONObject jsonObj = new JSONObject(map);
        request.setAttribute("json", jsonObj.toString());
        
        super.setRedirect(false); 
        super.setViewPage("/WEB-INF/jsonview.jsp"); 
    }
}