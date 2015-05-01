<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Enter Courses</title>
        <link href="resources/css/bootstrap.min.css" rel="stylesheet">
        <link href="resources/css/style.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    </head>

    <body>
        <script>
        function courseCheck(){
            var identifier = $('#courseidentifier').val();
            $.ajax({
                type:"POST",
                url:"courseCheck.html",
                data:"identifier=" + identifier,
                success: function(response){
                    $('#coursename').val(response);
                }
            });
        }    
        </script>
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
                    <div class="col-sm-7 text-center">
                        <h1>Enter Course</h1>
                    <form class="form-horizontal" action="submitassigned.html" method="POST" id="register" novalidate>
                        <fieldset>
                            <div class="form-group">
                                <label for="courseidentifier" class="col-lg-3 control-label">Course ID</label>
                                <div class="col-lg-8">
                                <input class="form-control" name="courseidentifier" onblur="courseCheck()" id="courseidentifier" style="width:395px;"> </input>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="coursename" class="col-lg-3 control-label">Course Name</label>
                                <div class="col-lg-8">
                                <input class="form-control" name="coursename" id="coursename" style="width:395px;"> </input>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="semesters" class="col-lg-3 control-label">Semesters</label>
                            <div class="col-lg-8 text-left checkbox">
                                <c:forEach begin="1" end="${numSemesters}" var = "val">
                                    <span style="padding-right:10px;"><label><input type="checkbox" name="semesters" value=${val}>${val}</input></label></span>
                                </c:forEach>
                            </div>
                            </div>
                            <div class="form-group">
                                <label for="period" class="col-lg-3 control-label">Period</label>
                                <div class="col-lg-8">
                                <select class="form-control" placeholder="Period" name="period" id="period" style="width:395px;">
                                <c:forEach begin="1" end ="${numPeriods}" var = "val2">
                                <option value="${val2}">${val2}</option>
                                </c:forEach>
                                </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="days" class="col-lg-3 control-label">Days</label>
                                <div class="col-lg-8 text-left checkbox">
                                <c:forEach begin="1" end="${numDays}" var = "val3">
                                    <span style="padding-right:10px;"><label><input type="checkbox" name="days" id="days" value=${val3}>${val3}</input></label></span>
                                </c:forEach>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="instructor" class="col-lg-3 control-label">Instructor</label>
                                <div class="col-lg-8">
                                <input class="form-control" name="instructor" id="instructor"  style="width:395px;"> </input>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-lg-6">
                                <button type="submit" class="btn btn-danger dropdown-toggle btn-sm" style="width:180px; margin-left:170px;">
                                    Add Course
                                </button>
                                </div>
                            </div>
                         </div>
                        </fieldset>
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
                        <c:if test="${not empty alreadyreg}">
                            <div class="alreadyreg">${alreadyreg}</div>
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