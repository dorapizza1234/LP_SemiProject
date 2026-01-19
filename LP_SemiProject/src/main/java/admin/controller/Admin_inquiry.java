package admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import admin.model.AdminDAO;
import admin.model.AdminVO;
import admin.model.InterAdminDAO;
import admin.model.InquiryVO;

public class Admin_inquiry extends AbstractController {

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
        InterAdminDAO adao = new AdminDAO();
        
        if("POST".equalsIgnoreCase(method)) {
            replyInquiry(request, response, adao);
        } else {
            listInquiry(request, response, adao);
        }
    }
    
    // [기능 1] 문의 목록 조회
    private void listInquiry(HttpServletRequest request, HttpServletResponse response, InterAdminDAO adao) throws Exception {
        
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
        
        int totalCount = adao.getTotalInquiryCount(paraMap);
        int totalPage = (int) Math.ceil((double)totalCount/sizePerPage);
        
        if(currentShowPageNo < 1) currentShowPageNo = 1;
        if(currentShowPageNo > totalPage) currentShowPageNo = totalPage;
        
        int startRno = ((currentShowPageNo - 1) * sizePerPage) + 1;
        int endRno = startRno + sizePerPage - 1;
        
        paraMap.put("startRno", String.valueOf(startRno));
        paraMap.put("endRno", String.valueOf(endRno));
        
        List<InquiryVO> inquiryList = adao.getInquiryListWithPaging(paraMap);
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
        
        String url = "admin_inquiry.lp";
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
        
        request.setAttribute("inquiryList", inquiryList);
        request.setAttribute("pageBar", pageBar);
        request.setAttribute("startIter", startIter);
        request.setAttribute("totalCount", totalCount);
        
        super.setRedirect(false);
        super.setViewPage("/WEB-INF/admin/admin_inquiry.jsp");
    }
    
    // [기능 2] 답변 등록
    private void replyInquiry(HttpServletRequest request, HttpServletResponse response, InterAdminDAO adao) throws Exception {
        
        String inquiryno = request.getParameter("inquiryno");
        String adminreply = request.getParameter("adminreply");
        
        int n = adao.replyInquiry(inquiryno, adminreply);
        
        String message = "";
        String loc = "";
        
        if(n == 1) {
            message = "답변이 등록되었습니다.";
            loc = request.getContextPath() + "/admin/admin_inquiry.lp"; 
        } else {
            message = "답변 등록 실패";
            loc = "javascript:history.back()";
        }
        
        request.setAttribute("message", message);
        request.setAttribute("loc", loc);
        
        super.setRedirect(false);
        super.setViewPage("/WEB-INF/msg.jsp");
    }
}