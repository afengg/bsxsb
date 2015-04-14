<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Enter Courses</title>
        <link href="resources/css/bootstrap.min.css" rel="stylesheet">
        <link href="resources/css/style.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
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
                            <li class="">
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
                            <li class="active">
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
                            <li class="dropdown">
                                <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                                    Manage Desired Courses<span class="caret"></span>
                                </a>
                                <ul class="dropdown-menu">
                                    <li><a href="studentgeneratecourses.html">Generate Desired Courses</a></li>
                                    <li><a href="studentviewgenerated.html">View Desired Courses</a></li>
                                    <li><a href="studentviewgenerated.html">Export Desired Courses</a></li>
                                </ul>
                            </li>

                        </ul>


                        </ul>
                    </div>
                    <div class="text-center col-sm-10">
                        <h1>Enter Course</h1>
                    </div>
                    <form action="submitassigned.html" method="POST" id="register" novalidate>
                        <div class="text-center">
                            <div class="col-sm-10  registerform">
                                <input placeholder="Course Identifier" name="courseidentifier" id="courseidentifier" style="width:395px;"> </input>
                            </div>
                            <div class="col-sm-10  registerform">
                                <input placeholder="Course Name" name="coursename" id="coursename" style="width:395px;"> </input>
                            </div>
                            <div class="col-sm-7  registerform">
                                <div>
                                <p>Semesters</p>
                            </div>
                            <div>
                                <c:forEach begin="1" end="${numSemesters}" var = "val">
                                    <input type="checkbox" name="semesters" value=${val}>${val}</input>
                                </c:forEach>
                            </div>
                            </div>
                            <div class="col-sm-10 registerform">
                                <div><p>Period<p></div>
                                <select placeholder="Period" name="period" id="period" style="width:395px;">
                                <c:forEach begin="1" end ="${numPeriods}" var = "val2">
                                <option value="${val2}">${val2}</option>
                                </c:forEach>
                                </select>
                            </div>
                            <div class="col-sm-7 registerform">
                                <div><p>Days</p></div>
                                <div>
                                <c:forEach begin="1" end="${numDays}" var = "val3">
                                <input type="checkbox" name="days" id="days" value=${val3}>${val3}</input>
                                </c:forEach>
                                </div>
                            </div>
                            <div class="col-sm-10 registerform">
                                <input placeholder="Instructor" name="instructor" id="instructor"  style="width:395px;"> </input>
                            </div>
                            <div class="col-sm-10 registerform" >
                                <button type="submit" class="btn btn-danger dropdown-toggle btn-sm" style="width:180px; margin-left:170px;">
                                    Add Course
                                </button>
                            </div>
                        </div>
                    </form>
                    <c:if test="${not empty sbinvalid}">
                        <div class="sbinvalid">${sbinvalid}</div>
                    </c:if>
                    <c:if test="${not empty halfsuccess}">
                        <div class="halfsuccess">${halfsuccess}</div>
                    </c:if>
                    <c:if test="${not empty success}">
                        <div class="success">${success}</div>
                    </c:if>




                </div>

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