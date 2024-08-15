<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
<head>
    <style>
        html,
        body {
            width: 100%;
            height: 100%;
        }
        .container {
            width: 100%;
            height: 85%;
        }
    </style>

    <meta charset="UTF-8">
    <title>notice</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-icons/font/bootstrap-icons.min.css" />


    <script src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
</head>
<body>
<header class="d-flex flex-wrap align-items-center justify-content-center justify-content-md-between py-3 mb-4 border-bottom">
    <a href="/" class="d-flex align-items-center col-md-3 mb-2 mb-md-0 text-dark text-decoration-none">
        <svg class="bi me-2" width="40" height="32" role="img" aria-label="Bootstrap"><use xlink:href="#bootstrap"></use></svg>
    </a>

    <ul class="nav col-12 col-md-auto mb-2 justify-content-center mb-md-0">
        <li><a href="/home" class="nav-link px-2 link-dark">Home</a></li>
        <li><a href="/notice" class="nav-link px-2 link-primary">notice</a></li>
    </ul>

    <div class="col-md-3 text-end">
        <button type="button" class="btn btn-outline-primary me-2" onclick="location.href='/logout'">Logout</button>
    </div>
</header>

<div class="container">
    <form id="noticeForm" name="noticeForm">
        <input type="hidden" id="noticeNo" name="noticeNo" value="${noticeNo}"/>
        <div class="form-group row">
            <div class="col-md mb-3">
                <label for="creator" class="col-sm-2 col-form-label">작성자</label><input type="text" class="form-control" id="creator" name="creator" value="" readonly/>
            </div>
            <div class="col-md mb-3">
                <label for="createdAt" class="col-sm-2 col-form-label">등록일시</label><input type="text" class="form-control" id="createdAt" name="createdAt" value="" readonly/>
            </div>
            <div class="col mb-3">
                <label for="viewCount" class="col-sm-2 col-form-label">조회수</label>
                <input type="text" class="form-control" id="viewCount" name="viewCount" value="" readonly/>
            </div>
        </div>

        <div class="form-group row">
            <div class="col mb-3">
                <label for="noticeStartDate" class="col-sm-2 col-form-label">공지시작일시</label>
                <input type="text" class="form-control" id="noticeStartDate" name="noticeStartDate" value="" readonly/>
            </div>
            <div class="col mb-3">
                <label for="noticeEndDate" class="col-sm-2 col-form-label">공지종료일시</label>
                <input type="text" class="form-control" id="noticeEndDate" name="noticeEndDate" value="" readonly/>
            </div>
        </div>

        <div class="form-group row">
            <div class="col mb-3">
                <label for="title" class="col-sm-2 col-form-label">제목</label><input type="text" class="form-control" id="title" name="title" value="" readonly/>
            </div>
        </div>

        <div class="form-group row">
            <div class="col mb-3">
                <label for="content" class="col-sm-2 col-form-label">내용</label><textarea class="form-control" id="content" name="content" readonly style="resize:none; width:100%; height:500px;"></textarea>
            </div>
        </div>

        <div class="form-group row">
            <div class="col mb-3">
                <label class="col-sm-2 col-form-label">첨부 파일 목록</label>
                <div id="fileListDiv"></div>
            </div>
        </div>

        <div id="fileList"></div>
    </form>

    <div class="d-grid gap-2 d-md-flex justify-content-md-end">
        <button class="btn btn-outline-secondary" type="button" onclick="location.href='/notice/modifyNotice/${noticeNo}'">수정</button>
        <button class="btn btn-outline-info" type="button" onclick="location.href='/notice'">목록</button>
    </div>
</div>

<footer class="py-3 my-4 mt-auto">
    <p class="text-center text-body-secondary">© 2024</p>
</footer>

<script>
    $(function(){
        $.ajax({
            url: `/api/notices/${noticeNo}/increase-view-count`, // 실제 API URL
            method: 'PATCH',
        })
        .done(function(data, textStatus, xhr) {
            if(textStatus === 'success'){
                getNoticeDetail();
            }
        })
        .fail(function(jqXHR, textStatus, errorThrown) {
            alert('Error:'+ textStatus + ',' + errorThrown);
            location.href = '/notice';
        });
    })

    function getNoticeDetail(){
        $.get('/api/notices/${noticeNo}', function(response) {
            $('#creator').val(response.notice.creator);
            $('#createdAt').val(response.notice.createdAt.replace('T', ' '));
            $('#noticeStartDate').val(response.notice.noticeStartDate);
            $('#noticeEndDate').val(response.notice.noticeEndDate);
            $('#viewCount').val(response.notice.viewCount);
            $('#title').val(response.notice.title);
            $('#content').val(response.notice.content);

            if(response.attachmentList){
                for (let i = 0; i < response.attachmentList.length; i++) {

                    let fileItem = $('<div>').addClass('file-item');
                    let fileName = $('<span>').addClass('ms-2').text(response.attachmentList[i].fileName);
                    // let downloadButton = $('<button>').addClass('btn btn-outline-secondary btn-sm').text("download");

                    fileItem.append(fileName);
                    $('#fileList').append(fileItem);
                }
            }
        }).fail(function(jqXHR, textStatus, errorThrown) {
            console.error('Error:', textStatus, errorThrown);
        });
    }
</script>
</body>
</html>