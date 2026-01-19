package admin.controller;

import java.util.*;
import org.json.JSONObject;
import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession; 
import admin.model.AdminDAO;
import admin.model.InterAdminDAO;
import admin.model.AdminVO; 

public class Admin_order extends AbstractController {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        // =============================================================
        // 관리자 로그인 여부 확인
        // =============================================================
        HttpSession session = request.getSession();
        AdminVO loginAdmin = (AdminVO) session.getAttribute("loginAdmin"); 

        if (loginAdmin == null) {
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

        // [기능 2] 배송 시작 처리 (Ajax)
        if("POST".equalsIgnoreCase(method) && "updateDeliveryStart".equals(mode)) {
            String orderno = request.getParameter("orderno");
            String invoice_no = request.getParameter("invoice_no");
            String delivery_company = request.getParameter("delivery_company");
            
            Map<String, String> paraMap = new HashMap<>();
            paraMap.put("orderno", orderno);
            paraMap.put("invoice_no", invoice_no);
            paraMap.put("delivery_company", delivery_company);
            
            // DAO 호출 (배송정보 Insert or Update)
            int n = adao.updateDeliveryStart(paraMap); 
            
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("result", n); 
            
            request.setAttribute("json", jsonObj.toString());
            
            super.setRedirect(false);
            super.setViewPage("/WEB-INF/jsonview.jsp");
        }
        
        // [기능 3] 배송 주소 수정 (Ajax)
        else if("POST".equalsIgnoreCase(method) && "updateOrderAddress".equals(mode)) {
            String orderno = request.getParameter("orderno");
            String postcode = request.getParameter("postcode");
            String address = request.getParameter("address");
            String detailaddress = request.getParameter("detailaddress");
            String extraaddress = request.getParameter("extraaddress");
            
            Map<String, String> paraMap = new HashMap<>();
            paraMap.put("orderno", orderno);
            paraMap.put("postcode", postcode);
            paraMap.put("address", address);
            paraMap.put("detailaddress", detailaddress);
            paraMap.put("extraaddress", extraaddress);
            
            int n = adao.updateOrderAddress(paraMap); 
            
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("result", n); 
            
            request.setAttribute("json", jsonObj.toString());
            
            super.setRedirect(false);
            super.setViewPage("/WEB-INF/jsonview.jsp");
        }
        
        // [기능 4] 배송 완료 처리 (Ajax)
        else if("POST".equalsIgnoreCase(method) && "updateDeliveryEnd".equals(mode)) {
            String orderno = request.getParameter("orderno");
            
            // DAO 호출 (배송완료 처리)
            int n = adao.updateDeliveryEnd(orderno); 
            
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("result", n); 
            
            request.setAttribute("json", jsonObj.toString());
            
            super.setRedirect(false);
            super.setViewPage("/WEB-INF/jsonview.jsp");
        }

        // [기능 1] 주문 전체 목록 조회 + 필터링 + 페이징 (수정됨)
        else {
            String status = request.getParameter("status");
            String str_currentShowPageNo = request.getParameter("currentShowPageNo");
            
            int currentShowPageNo = 0;
            try {
                if(str_currentShowPageNo == null) currentShowPageNo = 1;
                else currentShowPageNo = Integer.parseInt(str_currentShowPageNo);
            } catch(NumberFormatException e) {
                currentShowPageNo = 1;
            }
            
            int sizePerPage = 10; // 한 페이지당 10개씩
            
            Map<String, String> paraMap = new HashMap<>();
            if(status != null && !status.trim().isEmpty()) {
                paraMap.put("status", status);
            }
            
            // 1. 총 주문 건수 구하기
            int totalCount = adao.getTotalOrderCount(paraMap);
            int totalPage = (int) Math.ceil((double)totalCount/sizePerPage);
            
            if(currentShowPageNo < 1) currentShowPageNo = 1;
            if(currentShowPageNo > totalPage) currentShowPageNo = totalPage;
            
            int startRno = ((currentShowPageNo - 1) * sizePerPage) + 1;
            int endRno = startRno + sizePerPage - 1;
            
            paraMap.put("startRno", String.valueOf(startRno));
            paraMap.put("endRno", String.valueOf(endRno));
            
            // 2. 페이징된 주문 목록 가져오기
            List<Map<String, String>> orderList = adao.getOrderListWithPaging(paraMap);
            
            // 3. 페이징 바 생성 (회원/상품 관리와 동일한 로직)
            int blockSize = 10;
            int startPage = ((currentShowPageNo - 1) / blockSize) * blockSize + 1;
            int endPage = startPage + blockSize - 1;
            
            if(endPage > totalPage) {
                endPage = totalPage;
            }
            
            String url = "admin_order.lp";
            String searchParam = "";
            if(status != null && !status.trim().isEmpty()) {
                searchParam = "&status=" + status;
            }
            
            String pageBar = "";
            
            // [맨처음]
            if(currentShowPageNo > 1) {
                 pageBar += "<a href='"+url+"?currentShowPageNo=1"+searchParam+"' class='page-first'>맨처음</a>";
            } else {
                 pageBar += "<span class='page-first disabled'>맨처음</span>";
            }

            // [<] 이전 (1페이지씩 이동)
            if(currentShowPageNo > 1) {
                pageBar += "<a href='"+url+"?currentShowPageNo="+(currentShowPageNo-1)+searchParam+"' class='page-prev'>&lt;</a>";
            } else {
                pageBar += "<span class='page-prev disabled'>&lt;</span>";
            }
            
            // [페이지 번호]
            for(int i = startPage; i <= endPage; i++) {
                if(i == currentShowPageNo) {
                    pageBar += "<span class='active'>"+i+"</span>";
                } else {
                    pageBar += "<a href='"+url+"?currentShowPageNo="+i+searchParam+"'>"+i+"</a>";
                }
            }
            
            // [>] 다음 (1페이지씩 이동)
            if(currentShowPageNo < totalPage) {
                 pageBar += "<a href='"+url+"?currentShowPageNo="+(currentShowPageNo+1)+searchParam+"' class='page-next'>&gt;</a>";
            } else {
                 pageBar += "<span class='page-next disabled'>&gt;</span>";
            }
            
            // [맨마지막]
            if(currentShowPageNo < totalPage) {
                pageBar += "<a href='"+url+"?currentShowPageNo="+totalPage+searchParam+"' class='page-last'>맨마지막</a>";
            } else {
                 pageBar += "<span class='page-last disabled'>맨마지막</span>";
            }
            
            request.setAttribute("orderList", orderList);
            request.setAttribute("pageBar", pageBar);
            request.setAttribute("status", status); 
            
            super.setRedirect(false);
            super.setViewPage("/WEB-INF/admin/admin_order.jsp");
        }
    }
}