<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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
    <title>home</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-icons/font/bootstrap-icons.min.css" />

    <script src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/moment.js"></script>
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
        <input type="hidden" id="updater" name="updater"/>
        <div class="form-group row">
            <div class="col-md-6 mb-3">
                <label for="creator" class="col-sm-2 col-form-label">작성자</label><input type="text" class="form-control" id="creator" name="creator" value="" readonly/>
            </div>
            <div class="col-md-6 mb-3">
                <label for="createdAt" class="col-sm-2 col-form-label">등록일시</label><input type="text" class="form-control" id="createdAt" name="createdAt" readonly placeholder="자동입력"/>
            </div>
        </div>

        <div class="form-group row">
            <div class="col mb-3">
                <label for="noticeStartDate" class="col-sm-2 col-form-label">공지시작일시</label>
                <input type="datetime-local" class="form-control" id="noticeStartDate" name="noticeStartDate"/>
            </div>
            <div class="col-auto mb-3">
                <label for="startSec" class="col-sm-2 col-form-label">(초)</label>
                <input type="number" min="00" max="59" class="form-control" id="startSec" name="startSec">
            </div>
            <div class="col mb-3">
                <label for="noticeEndDate" class="col-sm-2 col-form-label">공지종료일시</label>
                <input type="datetime-local" class="form-control" id="noticeEndDate" name="noticeEndDate"/>
            </div>
            <div class="col-auto mb-3">
                <label for="endSec" class="col-sm-2 col-form-label">(초)</label>
                <input type="number" min="00" max="59" class="form-control" id="endSec" name="endSec">
            </div>
        </div>

        <div class="form-group row">
            <div class="col mb-3">
                <label for="title" class="col-sm-2 col-form-label">제목</label><input type="text" class="form-control" id="title" name="title"/>
            </div>
        </div>

        <div class="form-group row">
            <div class="col mb-3">
                <label for="content" class="col-sm-2 col-form-label">내용</label><textarea class="form-control" id="content" name="content" style="resize:none; width:100%; height:500px;"></textarea>
            </div>
        </div>

        <div class="form-group row">
            <div class="col mb-3">
                <input type="file" class="form-control" id="fileInput" name="files" multiple>
            </div>
        </div>

        <div id="fileList"></div>
    </form>

    <div class="d-grid gap-2 d-md-flex justify-content-md-end">
        <button class="btn btn-outline-primary" type="button" onclick="save();">수정</button>
        <button class="btn btn-outline-info" type="button" onclick="location.href='/notice'">목록</button>
    </div>
</div>

<footer class="py-3 my-4 mt-auto">
    <p class="text-center text-body-secondary">© 2024</p>
</footer>

<script>
    let filesToUpload = [];
    let deleteAttachmentList = [];

    $(function(){

        $.get('/api/users/profile', function(response) {
            $('#updater').val(response);
        }).fail(function(jqXHR, textStatus, errorThrown) {
            console.error('Error:', textStatus, errorThrown);
        });

        $.get('/api/notices/${noticeNo}', function(response) {
            $('#creator').val(response.notice.creator);
            $('#createdAt').val(response.notice.createdAt);

            const noticeStartDate = moment(response.notice.noticeStartDate, "YYYY-MM-DD HH:mm:ss");
            const formattedStartDateTime = noticeStartDate.format("YYYY-MM-DD HH:mm");
            const startSec = noticeStartDate.format("ss");
            $('#noticeStartDate').val(formattedStartDateTime);
            $('#startSec').val(startSec);
            const noticeEndDate = moment(response.notice.noticeStartDate, "YYYY-MM-DD HH:mm:ss");
            const formattedEndDateTime = noticeEndDate.format("YYYY-MM-DD HH:mm");
            const endSec = noticeEndDate.format("ss");
            $('#noticeEndDate').val(formattedEndDateTime);
            $('#endSec').val(endSec);
            $('#viewCount').val(response.notice.viewCount);
            $('#title').val(response.notice.title);
            $('#content').val(response.notice.content);

            if(response.attachmentList){
                for (let i = 0; i < response.attachmentList.length; i++) {

                    let fileItem = $('<div>').addClass('file-item');
                    let fileName = $('<span>').addClass('ms-2').text(response.attachmentList[i].fileName);
                    let btnCount = $('.delete-button').length;

                    let deleteButton = $('<button>').addClass('btn btn-outline-danger btn-sm delete-button')
                        .data('noticeAttachmentNo', response.attachmentList[i].noticeAttachmentNo)
                        .data('filePath', response.attachmentList[i].filePath)
                        .text("delete");

                    deleteButton.on('click', function(){
                        deleteAttachmentList.push({noticeAttachmentNo : $(this).data('noticeAttachmentNo'), filePath : $(this).data('filePath')});
                    })

                    fileItem.append(deleteButton).append(fileName);
                    $('#fileList').append(fileItem);
                }
            }
        }).fail(function(jqXHR, textStatus, errorThrown) {
            console.error('Error:', textStatus, errorThrown);
        });

        // 파일 선택 시 파일 목록 업데이트
        $('#fileInput').on('change', function(event) {
            let files = $('#fileInput')[0].files;
            if (files.length === 0) {
                alert('No files selected.');
                return;
            }

            $.each(files, function(index, file) {
                if (!filesToUpload.some(f => f.name === file.name)) {
                    filesToUpload.push(file);

                    let listItem = $('<div>').addClass('file-item');
                    let fileName = $('<span></span>').addClass('ms-2').text(file.name);
                    let deleteButton = $('<button></button>').text('Delete').addClass('btn btn-outline-danger btn-sm delete-button');

                    listItem.append(deleteButton);
                    listItem.append(fileName);

                    $('#fileList').append(listItem);
                }
            });

            $('#fileInput').val('');
        });

        // 파일 삭제 버튼 클릭 시
        $('#fileList').on('click', '.delete-button', function() {
            let fileName = $(this).siblings('span').text();

            filesToUpload = filesToUpload.filter(file => file.name !== fileName);
            deleteAttachmentList

            $(this).parent().remove();
        });
    })

    function save(){
        if(!validation()) return false;

        var formData = new FormData();
        formData.append("updater", $("#updater").val());
        formData.append("title", $("#title").val());
        formData.append("content", $("#content").val());
        if($('#noticeStartDate').val().length>0){
            formData.append("noticeStartDate", $('#noticeStartDate').val().concat(':'+($('#startSec').val().length == 1 ? "0".concat($('#startSec').val()) : $('#startSec').val())));
        }
        if($('#noticeEndDate').val().length>0){
            formData.append("noticeEndDate", $('#noticeEndDate').val().concat(':'+($('#endSec').val().length == 1 ? "0".concat($('#endSec').val()) : $('#endSec').val())));
        }

        if (filesToUpload && filesToUpload.length > 0) {
            filesToUpload.forEach(function(file, index) {
                formData.append("attachmentList", file);
            });
        }
        if (deleteAttachmentList && deleteAttachmentList.length > 0) {
            formData.append("deleteAttachmentList", JSON.stringify(deleteAttachmentList));
        }

        $.ajax({
            url : '/api/notices/${noticeNo}',
            type : "put",
            data : formData,
            processData: false, // Prevent jQuery from automatically transforming the data into a query string
            contentType: false, // Prevent jQuery from setting content-type header
        })
        .done(function(data, textStatus, xhr) {
            if(textStatus === 'success'){
                alert('수정 성공!');
                location.href = '/notice/detailNotice/${noticeNo}';
            }
        })
        .fail(function(xhr, textStatus, errorThrown) {
            console.log(xhr);
            console.log(textStatus);
            console.log(errorThrown);
            alert('에러 발생!!!!' + errorThrown);
        });
    }

    function validation(){
        let startSec = $('#startSec').val();
        let endSec = $('#endSec').val();
        let noticeStartDate = $('#noticeStartDate').val();
        let noticeEndDate = $('#noticeEndDate').val();

        if(Number(startSec)>59 || Number(startSec)<0){
            alert('초는 00 ~ 59 사이로 입력해주세요.');
            return false;
        }
        if(Number(endSec)>59 || Number(endSec)<0){
            alert('초는 00 ~ 59 사이로 입력해주세요.');
            return false;
        }
        if((noticeStartDate.length>0 && startSec.length<=0) || (noticeStartDate.length<=0 && startSec.length>0)){
            alert('공지시작일시와 초를 모두 입력하세요.');
            return false;
        }
        if((noticeEndDate.length>0 && endSec.length<=0) || (noticeEndDate.length<=0 && endSec.length>0)){
            alert('공지종료일시와 초를 모두 입력하세요.');
            return false;
        }
        return true;
    }
</script>

</body>
</html>