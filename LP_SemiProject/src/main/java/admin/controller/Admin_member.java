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
import admin.model.MemberVO;

public class Admin_member extends AbstractController {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        // =============================================================
        // 관리자 로그인 여부 확인
        // =============================================================
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
        
        // POST 방식이고 모드가 delete라면 회원 삭제 실행
        if("POST".equalsIgnoreCase(method) && "delete".equals(mode)) {
            deleteMember(request, response, adao);
        }
        else {
            // 그 외에는 회원 목록 조회 실행
            listMember(request, response, adao);
        }
    }
    
    // [기능 1] 회원 목록 조회
    private void listMember(HttpServletRequest request, HttpServletResponse response, InterAdminDAO adao) throws Exception {
        
        String searchType = request.getParameter("searchType");
        String searchWord = request.getParameter("searchWord");
        
        if(searchWord == null || searchWord.trim().isEmpty()) {
            searchWord = "";
            searchType = "name"; 
        }
        
        String str_currentShowPageNo = request.getParameter("currentShowPageNo");
        int currentShowPageNo = 0;
        
        try {
            if(str_currentShowPageNo == null) currentShowPageNo = 1;
            else currentShowPageNo = Integer.parseInt(str_currentShowPageNo);
        } catch(NumberFormatException e) {
            currentShowPageNo = 1;
        }
        
        int sizePerPage = 10; // 한 페이지당 보여줄 회원 수
        
        Map<String, String> paraMap = new HashMap<>();
        paraMap.put("searchType", searchType);
        paraMap.put("searchWord", searchWord);
        
        // 총 회원수(또는 검색된 회원수) 알아오기
        int totalCount = adao.getTotalMemberCount(paraMap); 
        int totalPage = (int) Math.ceil((double)totalCount/sizePerPage);
        
        if(currentShowPageNo < 1) currentShowPageNo = 1;
        if(currentShowPageNo > totalPage) currentShowPageNo = totalPage;
        
        int startRno = ((currentShowPageNo - 1) * sizePerPage) + 1;
        int endRno = startRno + sizePerPage - 1;
        
        paraMap.put("startRno", String.valueOf(startRno));
        paraMap.put("endRno", String.valueOf(endRno));
        
        // 페이징 처리된 회원 목록 가져오기
        List<MemberVO> memberList = adao.getMemberListWithPaging(paraMap);
        
        // 오름차순 번호 출력을 위한 시작값 계산
        int startIter = ((currentShowPageNo - 1) * sizePerPage) + 1;
        
        // ==================================================================
        // [수정 포인트] 페이징 바 생성 (1페이지씩 이동 + 블럭 단위 숫자 표시)
        // ==================================================================
        
        int blockSize = 10; // 페이지 번호 10개씩 묶음 (1~10, 11~20 ...)
        
        // 블럭의 시작페이지 번호 계산
        int startPage = ((currentShowPageNo - 1) / blockSize) * blockSize + 1;
        
        // 블럭의 마지막페이지 번호 계산
        int endPage = startPage + blockSize - 1;
        
        if(endPage > totalPage) {
            endPage = totalPage;
        }
        
        String url = request.getContextPath() + "/admin/admin_member.lp";
        String searchParam = "&searchType=" + searchType + "&searchWord=" + searchWord;
        String pageBar = "";
        
        // 1. [맨처음] 버튼
        if(currentShowPageNo > 1) {
             pageBar += "<a href='"+url+"?currentShowPageNo=1"+searchParam+"' class='page-first'>맨처음</a>";
        } else {
             pageBar += "<span class='page-first disabled'>맨처음</span>";
        }

        // 2. [<] 이전 버튼 (1페이지씩 앞으로 이동)
        if(currentShowPageNo > 1) {
            pageBar += "<a href='"+url+"?currentShowPageNo="+(currentShowPageNo-1)+searchParam+"' class='page-prev'>&lt;</a>";
        } else {
            pageBar += "<span class='page-prev disabled'>&lt;</span>";
        }
        
        // 3. [페이지 번호] 반복 출력
        for(int i = startPage; i <= endPage; i++) {
            if(i == currentShowPageNo) {
                // 현재 페이지: 비활성 span (검정 배경)
                pageBar += "<span class='active'>"+i+"</span>";
            } else {
                // 다른 페이지: 이동 링크
                pageBar += "<a href='"+url+"?currentShowPageNo="+i+searchParam+"'>"+i+"</a>";
            }
        }
        
        // 4. [>] 다음 버튼 (1페이지씩 뒤로 이동)
        if(currentShowPageNo < totalPage) {
             pageBar += "<a href='"+url+"?currentShowPageNo="+(currentShowPageNo+1)+searchParam+"' class='page-next'>&gt;</a>";
        } else {
             pageBar += "<span class='page-next disabled'>&gt;</span>";
        }
        
        // 5. [맨마지막] 버튼
        if(currentShowPageNo < totalPage) {
            pageBar += "<a href='"+url+"?currentShowPageNo="+totalPage+searchParam+"' class='page-last'>맨마지막</a>";
        } else {
             pageBar += "<span class='page-last disabled'>맨마지막</span>";
        }
        
        request.setAttribute("memberList", memberList);
        request.setAttribute("searchType", searchType);
        request.setAttribute("searchWord", searchWord);
        request.setAttribute("pageBar", pageBar);
        request.setAttribute("totalCount", totalCount);
        
        request.setAttribute("startIter", startIter); 
        
        super.setRedirect(false);
        super.setViewPage("/WEB-INF/admin/admin_member.jsp");
    }
    
    // [기능 2] 회원 탈퇴 처리
    private void deleteMember(HttpServletRequest request, HttpServletResponse response, InterAdminDAO adao) throws Exception {
        
        String[] userids = request.getParameterValues("userid");
        String message = "";
        String loc = "";
        
        if(userids != null && userids.length > 0) {
            int n = adao.deleteMember(userids);
            
            if(n > 0) {
                message = "선택한 회원이 탈퇴 처리되었습니다.";
                loc = request.getContextPath() + "/admin/admin_member.lp"; 
            } else {
                message = "탈퇴 처리에 실패했습니다.";
                loc = "javascript:history.back()";
            }
        } else {
            message = "선택된 회원이 없습니다.";
            loc = "javascript:history.back()";
        }
        
        request.setAttribute("message", message);
        request.setAttribute("loc", loc);
        
        super.setRedirect(false);
        super.setViewPage("/WEB-INF/msg.jsp");
    }
}