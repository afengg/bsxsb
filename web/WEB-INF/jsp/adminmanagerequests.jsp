<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Manage Requests</title>
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
                            <li>
                                <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                                    Manage School<span class="caret"></span>
                                </a>
                                <ul class="dropdown-menu">
                                    <li><a href="adminaddschool.html">Add School</a></li>
                                    <li><a href="admin.html">View Schools</a></li>
                                </ul>
                            </li>
                            <li class="active">
                                <a href="adminmanagerequests.html">Manage Account Requests</a>
                            </li>
                            <li>
                                <a href="adminmanageaccounts.html">Display Student Accounts</a>
                            </li>


                        </ul>


                        </ul>
                    </div>


                    <div class="col-sm-7 ">
                        <h1 class="col-sm-offset-1">Account Requests</h1>
                        <div class="col-sm-offset-1">
                            <table class="table  "style=" width:600px;">
                                <thead>
                                    <tr>
                                          <th>
                                            Email
                                        </th>
                                        <th>
                                            First Name
                                        </th>
                                        <th>
                                            Last Name
                                        </th>
                                    </tr>
                                </thead>
                                <tbody style="text-align:left;">
                                    <c:forEach items="${accountrequests}" var="accountrequests">
                                    <tr>
                                        <td>
                                            ${accountrequests.getEmail()}
                                        </td>
                                         <td>
                                          ${accountrequests.getFirstname()}
                                        </td>
                                        <td>
                                          ${accountrequests.getLastname()}
                                        </td>
                                        <td>
                                            <button class="btn btn-info btn-xs">Accept</button>
                                        </td>
                                        <td>
                                            <button class="btn btn-danger btn-xs">Reject</button>
                                        </td>
                                    </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>	
                    </div>
                    <div class="col-sm-3 ">

                        <button class="btn btn-success btn-xs" style="margin-top:15px" >Accept All </button>
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