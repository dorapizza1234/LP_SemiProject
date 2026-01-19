<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% String ctxPath = request.getContextPath(); %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width,initial-scale=1" />
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="stylesheet" href="<%= ctxPath%>/css/common/header.css">
  <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.1/css/all.min.css">
</head>
<body>
  <div class="bg-art" aria-hidden="true"></div>
  <div class="wrap">
   <header class="topbar">
     <div class="logo">
       <a href="<%= ctxPath%>/index.lp"><img src="<%= ctxPath%>/images/logo.png" alt="HOME"></a>
     </div>

     <nav class="navlinks">
       <a href="<%= ctxPath%>/index.lp">HOME</a> |
       
       <c:choose>
           <c:when test="${empty sessionScope.loginuser}">
               <a href="<%= ctxPath %>/login/login.lp">LOGIN</a> |
           </c:when>
           <c:otherwise>
               <a href="<%= ctxPath %>/login/logout.lp">LOGOUT</a> |
           </c:otherwise>
       </c:choose>

       <a href="<%= ctxPath%>/order/cart.lp">CART</a> |
       <a href="<%= ctxPath%>/my_info/my_info.lp">MYPAGE</a>
     </nav>
     
     <button class="nav-toggle" type="button"><i class="fa-solid fa-bars"></i></button>
     
     <nav class="navlinks-mobile">
        <a href="<%= ctxPath%>/index.lp">HOME</a>
        <c:choose>
           <c:when test="${empty sessionScope.loginuser}">
               <a href="<%= ctxPath %>/login/login.lp">LOGIN</a>
           </c:when>
           <c:otherwise>
               <a href="<%= ctxPath %>/login/logout.lp">LOGOUT</a>
           </c:otherwise>
        </c:choose>
        <a href="<%= ctxPath%>/order/cart.lp">CART</a>
        <a href="<%= ctxPath%>/my_info/my_info.lp">MY PROFILE</a>
     </nav>
   </header>
   
   <!-- ✅ 스크립트는 CSS 텍스트 뒤에 붙이지 말고, body 끝쪽에 두는 게 안전 -->
<script>
  $(function () {
    const $btn = $(".nav-toggle");
    const $menu = $(".navlinks-mobile");

    function closeMenu(){
      $menu.removeClass("is-open");
      $btn.attr("aria-expanded", "false");
      $btn.find("i").removeClass("fa-xmark").addClass("fa-bars");
    }

    function openMenu(){
      $menu.addClass("is-open");
      $btn.attr("aria-expanded", "true");
      $btn.find("i").removeClass("fa-bars").addClass("fa-xmark");
    }

    $btn.on("click", function (e) {
      e.stopPropagation();
      if ($menu.hasClass("is-open")) closeMenu();
      else openMenu();
    });

    $(document).on("click", function () {
      if ($menu.hasClass("is-open")) closeMenu();
    });

    $menu.on("click", function (e) {
      e.stopPropagation();
    });

    $(document).on("keydown", function(e){
      if(e.key === "Escape") closeMenu();
    });

    $(window).on("resize", function(){
      if(window.innerWidth > 768) closeMenu();
    });
  });
</script>