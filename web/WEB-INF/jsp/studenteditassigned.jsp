<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Edit Courses</title>
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
                                    <li><a href="studentviewcourses.html">View Desired Courses</a></li>
                                    <li><a href="studentviewcourses.html">Export Desired Courses</a></li>
                                </ul>
                            </li>
                        </ul>
                        </ul>
                    </div>
                    <div class="col-sm-8 ">
                        <h1 align="center">Edit Courses</h1>
                        <div class="col-sm-offset-1">
                            <table class="table  "style=" width:700px;">
                                <thead>
                                    <tr>
                                        <th>Course Name</th>
                                        <th>Course Identifier</th>
                                        <th>Number of Students</th>
                                        <th>Semester</th>
                                        <th>Period</th>
                                        <th>Days</th>
                                        <th>Instructor</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:set var="index" value="0" />
                                    <c:forEach items="${courses}" var="courses">
                                        <tr>
                                            <td>${courses.getCoursename()}</td>
                                            <td>${courses.getCourseidentifier()}</td>
                                            <td>${courses.getNumstudents()}</td>
                                            <td> ${courses.getSemester()}</td>
                                            <td> ${scheduleblocks[index].getPeriod()}</td>
                                            <td> ${scheduleblocks[index].getDays()}</td>
                                            <td>${courses.getInstructor()}</td>
                                            <c:set var="index" value="${index + 1}" />
                                    <form action="removeassign.html" method="POST">
                                        <td><button class="btn btn-xs btn-danger" type="submit" name="id" value="${courses.getCourseid()}">Remove Course</button></td>
                                    </form>      
                                    </tr>
                                </c:forEach>

                                </tbody>
                            </table>
                        </div>	
                    </div>		
                </div>

            </div>
        </div>


        <footer class="text-center">

            <div class="container">
                <div class="row">
                    <div class="col-lg-12">
                        &copy; BSxSB
                    </div>
                </div>
            </div>


            <div class="container">
                <div class="row">
                    <div class="col-lg-12">
                        Lun Zhang, Alvin Feng, Chris Mak
                    </div>
                </div>
            </div>
        </footer>

        <script src="resources/js/jquery.js"></script>
        <script src="resources/js/bootstrap.min.js"></script>
    </body>


</html>