<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Admin</title>
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
                            <li class="active">
                                <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                                    Manage School<span class="caret"></span>
                                </a>
                                <ul class="dropdown-menu">
                                    <li><a href="adminaddschool.html">Add School</a></li>
                                    <li><a href="admin.html">View Schools</a></li>
                                </ul>
                            </li>
                            <li>
                                <a href="adminmanagerequests.html">Manage Account Requests</a>
                            </li>
                            <li>
                                <a href="adminmanageaccounts.html">Display Student Accounts</a>
                            </li>


                        </ul>


                        </ul>
                    </div>
                    <div class="col-sm-10 ">
                        <h1 align="center">Schools</h1>
                        <div >
                            <table class="table "style=" ">
                                <thead>
                                    <tr>
                                        <th>
                                            School Name
                                        </th>
                                        <th>
                                            Academic Year
                                        </th>
                                        <th>
                                            Semesters
                                        </th>
                                        <th>
                                            Days
                                        </th>
                                        <th>
                                            Periods
                                        </th>	
                                        <th>
                                            Lunch Range
                                        </th>
                                        <th>
                                            Schedule Blocks
                                        </th>
                                    </tr>
                                </thead>
                                <tbody style="text-align:left;">
                                    <c:forEach items="${school}" var="school">
                                        <tr>
                                            <td>
                                                ${school.getSchoolname()}
                                            </td>
                                            <td>
                                                ${school.getAcademicyear()}
                                            </td>
                                            <td>
                                                ${school.getNumsemesters()}
                                            </td>
                                            <td>
                                                ${school.getNumdays()}
                                            </td>
                                            <td>
                                                ${school.getNumperiods()}
                                            </td>
                                            <td>
                                                ${school.getLunchrange()}
                                            </td>
                                            <td>
                                                ${school.getScheduleblocks()}
                                            </td>
                                            <td>
                                                <form action="deleteschool.html" method="POST">
                                                <button class="btn btn-danger btn-sm" type="submit" name="schoolID" value="${school.getSchoolid()}">Delete School </button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                        Hi there
                                            ${schoolid}
                                </tbody>
                            </table>
                        </div>	
                    </div>

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