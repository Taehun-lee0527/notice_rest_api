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
    <title>notice</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/tui-pagination/dist/tui-pagination.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/tui-grid/dist/tui-grid.css" />

    <script src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/tui-pagination/dist/tui-pagination.js"></script>
    <script src="${pageContext.request.contextPath}/tui-grid/dist/tui-grid.js"></script>
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
    <div class="row">
        <div class="col-4">
            <select class="form-select" id="searchType" name="searchType">
                <option value="01">제목</option>
                <option value="02">내용</option>
                <option value="03">제목+내용</option>
                <option value="04">등록자</option>
            </select>
        </div>
        <div class="col-8">
            <div class="input-group">
                <input type="text" class="form-control" id="searchText" name="searchText"/>
                <button class="btn btn-outline-secondary" type="button" id="searchButton">조회</button>
            </div>
        </div>
    </div>

    <div id="grid" style="margin-top:5%;"><span class="totalCount"></span></div>

    <div class="d-grid gap-2 d-md-flex justify-content-md-end">
        <button class="btn btn-outline-primary" type="button" onclick="location.href='/notice/createNotice'">등록</button>
        <button class="btn btn-outline-danger" type="button" id="deleteNoticeBtn">삭제</button>
    </div>
</div>

<footer class="py-3 my-4 mt-auto">
    <p class="text-center text-body-secondary">© 2024</p>
</footer>

<script>
    $(function(){
        const Grid = tui.Grid;
        Grid.applyTheme('striped');

        const dataSource = {
            api: {
                readData: { url: '/api/notices', method: 'get', initParams: {searchType:$('#searchType').val(), searchText:$('#searchText').val()}}
            }
        };

        const grid = new Grid({
            el: document.getElementById('grid'), // Container element
            rowHeaders: ['checkbox'],
            pageOptions: {
                perPage: 20
            },
            data: dataSource,
            minBodyHeight: 800,
            columns: [
                { header: '제목', name: 'title', escapeHTML: true},
                { header: '내용', name: 'content', escapeHTML: true },
                { header: '등록일시', name: 'createdAt', align: "center", width: 180 },
                { header: '조회수', name: 'viewCount', align: "center", width: 100 },
                { header: '작성자', name: 'creator', width: 200 },
                { header: '상세', name: 'detailButton', align: "center", width: 80,  formatter: function(data){
                        let link = '/notice/detailNotice/' + data.row.noticeNo;
                        return '<a href="'+link+'">상세</a>';
                    }
                }
            ]
        });

        $('#searchButton').on('click', function(){
            $.ajax({
                url: `/api/notices`, // 실제 API URL
                method: 'GET',
                data: { page: 1, perPage:20, searchType:$('#searchType').val(), searchText:$('#searchText').val() }, // 검색어를 쿼리 파라미터로 전달
                dataType: 'json',
                success: function (response) {

                    const pageState = { page: 1, totalCount: response.data.pagination.totalCount, perPage: 20 };

                    const data = response.data.contents || [];

                    grid.resetData(data, {pageState});
                },
                error: function (xhr, status, error) {
                    console.error('Error fetching data:', error);
                }
            });
        })

        $('#deleteNoticeBtn').on('click', function(){
            let checkedRows = grid.getCheckedRows();
            if(checkedRows.length === 0){
                alert('삭제할 공지사항을 선택해주세요.');
                return false;
            }

            $.ajax({
                url: `/api/notices`, // 실제 API URL
                method: 'DELETE',
                data: { page: 1, perPage:20, searchType:$('#searchType').val(), searchText:$('#searchText').val() }, // 검색어를 쿼리 파라미터로 전달
                dataType: 'json',
                success: function (response) {

                    const pageState = { page: 1, totalCount: response.data.pagination.totalCount, perPage: 20 };

                    const data = response.data.contents || [];
                    grid.resetData(data, {pageState});
                },
                error: function (xhr, status, error) {
                    console.error('Error fetching data:', error);
                }
            });
        })
    })
</script>

</body>
</html>