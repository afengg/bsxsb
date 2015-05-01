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
                    <div class="col-sm-6 ">
                        <h1 align="center">${school.getSchoolname()}'s Schedule Blocks</h1>
                        <div >
                            <table class="table "style=" ">
                                <thead>
                                    <tr>
                                        <th>
                                            Period
                                        </th>
                                        <th>
                                            Days
                                        </th>
                                    </tr>
                                </thead>
                                <tbody style="text-align:left;">
                                    <c:forEach items="${scheduleblocks}" var="sb">
                                        <tr>
                                            <td>
                                                ${sb.getPeriod()}
                                            </td>
                                            <td>
                                                ${sb.getDays()}
                                            </td>
                                            <td>
                                                <form action="deletescheduleblock.html" method="POST">
                                                    <input type="hidden" name="schoolid" id="schoolid" value="${school.getSchoolid()}"</input>
                                                <button class="btn btn-danger btn-xs" type="submit" id="scheduleblockID" name="scheduleblockID" value="${sb.getScheduleblockid()}">Remove</button>
                                                </form>
                                            </td>
</div>
                                        </tr>
                                    </c:forEach>
                                    
                                </tbody>
                            </table>
                        </div>	
                    </div>
                        <div class="col-sm-4" style="padding-top:20px;">
                            <div class="panel panel-primary">
                                <div class="panel-heading">
                                    <h3 class="panel-title text-center">New Schedule Block</h3>
                                </div>
                                <div class="panel-body">
                                    <form action="addscheduleblock.html" method="POST">
                                    <div>
                                    <span class="label label-primary">Period</span>
                                <c:forEach begin="1" end ="${school.getNumperiods()}" var = "val2">
                                    <span style="padding:2px;">
                                <input type="radio" name="period" id="period" value=${val2}>${val2}</input>
                                    </span>
                                </c:forEach>
                                    </div>
                                    <div>
                                    <span class="label label-warning">Days</span>
                                    <c:forEach begin="1" end ="${school.getNumdays()}" var = "val3">
                                    <span style="padding-left:2px;">
                                <input type="checkbox" name="days" id="days" value=${val3}>${val3}</input>
                                    </span>
                                </c:forEach>
                                    </div>
                                    <div style="padding-top:5px;">
                                    <input type="hidden" name="schoolid" id="schoolid" value="${school.getSchoolid()}"</input>
                                    <button class="btn btn-success" type="submit" name="addSB">Add</button>
                                    </div>
                                    </form>
                                    <div>
                                <c:if test="${not empty sbexists}">
                                <span class="label label-danger">${sbexists}</span>
                                </c:if>
                                <c:if test="${not empty sbempty}">
                                    <span class="label label-danger">${sbempty}</span>
                                </c:if>
                                    </div>
                                </div>
                        </div>
                        <div class="alert alert-dismissible alert-warning">
                            <button type="button" class="close" data-dismiss="alert">×</button>
                                <p>Removing a schedule block will delete all existing courses that are associated with it!</p>
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