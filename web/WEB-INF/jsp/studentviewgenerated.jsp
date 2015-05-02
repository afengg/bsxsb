<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Generated Courses</title>
        <link href="resources/css/bootstrap.min.css" rel="stylesheet">
        <link href="resources/css/style.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <script>
            function showFriends() {
                var div = document.getElementsByClassName("friends");
                for (var i = 0; i < div.length; i++) {
                    div[i].style.display = 'block';
                }
            }
            function hideFriends() {
                var div = document.getElementsByClassName("friends");
                for (var i = 0; i < div.length; i++) {
                    div[i].style.display = 'none';
                }
            }

            function downloadCurrentDocument() {
                var link = document.createElement('a');
                var event = document.createEvent("HTMLEvents");
                link.download = 'generatedschedule.html';
                link.href = 'data:text/html,' + document.getElementById('generatedschedule').innerHTML;
                event.initEvent('click');
                link.dispatchEvent(event);
            }

        </script>
    </head>
    <body>
        <div class="navbar navbar-default navbar-fixed-top">
            <div class="container">
                <div class="navbar-header">
                    <button class="navbar-toggle " data-toggle="collapse" data-target=".navbar-collapse">
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                </div>
                <div class="collapse navbar-collapse">
                    <ul class="nav navbar-nav navbar-right">
                        <li>
                            <a href="j_spring_security_logout">Sign Out</a>
                        </li>
                    </ul>
                </div>


            </div>
        </div>
        <div class="jumbotron">
            <div class="container" >
                <div class="row">
                    <div class="col-sm-2">
                        <ul class="nav nav-pills nav-stacked" >
                            <li >
                                <a href="student.html">
                                    Home
                                </a>
                            </li>
                            <li>
                                <a href="studentmanagefriends.html">Manage Friends</a>
                            </li>
                            <li>
                                <a href="studentdisplayfriends.html">Display Friends</a>
                            </li>
                            <li>
                                <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                                    Manage Assigned Courses<span class="caret"></span>
                                </a>
                                <ul class="dropdown-menu">
                                    <li><a href="studententercourses.html">Enter Assigned Courses</a></li>
                                    <li><a href="studentassignedcourses.html">View Assigned Courses</a></li>
                                    <li><a href="studenteditassigned.html">Edit Assigned Courses</a></li>
                                </ul>
                            </li>
                            <li class="dropdown">
                                <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                                    Course Offerings<span class="caret"></span>
                                </a>
                                <ul class="dropdown-menu">
                                    <c:forEach items="${schoolyears}" var="schoolyears">
                                        <form action="studentcourseofferings.html" method="get">
                                            <li>
                                                <button class="btn btn-link" type="submit" name="year" value="${schoolyears.getAcademicyear()}">
                                                    ${schoolyears.getAcademicyear()}
                                                </button>
                                            </li>
                                        </form>
                                    </c:forEach>
                                </ul>
                            </li>
                            <li class="dropdown active">
                                <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                                    Manage Desired Courses<span class="caret"></span>
                                </a>
                                <ul class="dropdown-menu">
                                    <li><a href="studentgeneratecourses.html">Generate Desired Courses</a></li>
                                </ul>
                            </li>
                            <li class="dropdown ">
                                <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                                    Export Generated Schedule<span class="caret"></span>
                                </a>
                                <ul class="dropdown-menu">
                                    <li><button class="btn btn-link" onclick="downloadCurrentDocument();">Export Schedule</button></li>
                                </ul>
                            </li>
                        </ul>
                        </ul>
                    </div>
                    <div class="col-sm-9" style="font-size:12px;">
                        <c:if test = "${not empty conflictCourses}">
                            <h5 style="padding-top:25px;">The following pairs have conflicting schedules:</h5>
                            <c:forEach items="${conflictCourses}" var="conflictCourses">
                                <div class="col-sm-8" style="padding-top:10px;">
                                    <c:forEach items="${conflictCourses}" var="courses">
                                        <div class="col-sm-4" align="right"> 
                                            ${courses.getCoursename()}
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:forEach>
                        </c:if>
                        <div id="generatedschedule">
                            <c:if test = "${not empty semester}">

                                <h1 align="center">Generated Courses</h1>
                                <c:set var="sem" value="1" />
                                <c:forEach items="${semester}" var="semester2">
                                    <div style="border-style:solid; border-width: 1.5px; margin-top:10px;">
                                        <h3 align="center">Semester ${sem}</h3>
                                        <table class="table  "style=" width:700px;">
                                            <thead>
                                                <tr>
                                                    <th>Period</th>
                                                    <th>Monday</th>
                                                    <th>Tuesday</th>
                                                    <th>Wednesday</th>
                                                    <th>Thursday</th>
                                                    <th>Friday</th>
                                                    <th>Saturday</th>
                                                    <th>Sunday</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:set var="index" value="1" />
                                                <c:forEach items="${semester2}" var="schedule">
                                                    <tr>
                                                        <td>
                                                            ${index}
                                                        </td>
                                                        <c:forEach items="${schedule}" var="period">
                                                            <td>
                                                                <div class="friends" style=" display: none;">    
                                                                    ${period.getFriends()}
                                                                </div>
                                                                ${period.getCoursename()}
                                                            </td>
                                                        </c:forEach>
                                                        <c:set var="index" value="${index + 1}" /> 
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                    <c:set var="sem" value="${sem + 1}" /> 
                                </c:forEach>
                            </c:if>    
                        </div>

                    </div>
                    <c:if test = "${not empty semester}">
                        <div style="margin-top:30px;">
                            <button onclick="showFriends()" class="btn btn-success btn-xs">With friend</button>
                            <button onclick="hideFriends()"class="btn btn-danger btn-xs" style="margin-top:10px;">Without friend</button>
                        </div>
                    </c:if>
                </div>
            </div>


            <footer class="text-center">
                <div class="footer-above">
                    <div class="container">
                        <div class="row">

                            <div class="col-lg-12">
                                &copy; BSxSB
                            </div>

                        </div>
                    </div>
                </div>
                <div class="footer-below">
                    <div class="container">
                        <div class="row">
                            <div class="col-lg-12">
                                Lun Zhang, Alvin Feng, Chris Mak
                            </div>
                        </div>
                    </div>
                </div>
            </footer>
            <script src="resources/js/jquery.js"></script>
            <script src="resources/js/bootstrap.min.js"></script>
    </body>


</html>