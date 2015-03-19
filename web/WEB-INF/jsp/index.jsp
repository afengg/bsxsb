<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>BSxSB</title>
        <link href="resources/css/bootstrap.min.css" rel="stylesheet">
        <link href="resources/css/style.css" rel="stylesheet">
        <link href="resources/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    </head>

    <body>

        <div class="navbar navbar-default navbar-fixed-top">
            <div class="container">
                <div class="navbar-header">
                    <a class="navbar-brand nav-header" href="index.html">BSxSB</a>


                </div>
                <button class="navbar-toggle " data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>

                <div class="collapse navbar-collapse nav navbar-nav navbar-right">
                    <form action="j_spring_security_check" method="POST">
                        <input class="navbarinput" type="text" name="username" placeholder="Email">
                        <input class="navbarinput" type="text" name="password" placeholder="Password">
                        <button type="submit" class="btn btn-sm btn-primary">Login</button>
                    </form>
                </div>	


            </div>
        </div>
        <div class="jumbotron" style="margin-top:45px;">

            <div class="container" >
                <div class="row">
                    <div class="col-sm-6" >
                        <h1>High School Schedule Planner</h1>
                        <h2 style="margin-top:45px;"> Make the schedule that fits you best</h2>
                        <h2  style="margin-top:45px;">Choose your own courses</h2>
                        <h2  style="margin-top:45px;">Collaborate with your friends</h2>
                    </div>
                    <div class="col-sm-6" >

                        <div class="row text-center">
                            <div class="col-sm-12" >
                                <h1>Register Now</h1>

                            </div>

                            <form action="register.html" method="POST">
                                <div class="col-sm-12">
                                    <input placeholder="First Name" name="firstName" style="width:360px;"> 
                                </div>
                                <div class="col-sm-12 registerform">
                                    <input placeholder="Last Name" name="lastName" style="width:360px;"> 
                                </div>
                                <div class="col-sm-12 registerform">
                                    <input placeholder="Email Address" name="email" style="width:360px;" >
                                </div>
                                <div class="col-sm-12 registerform">
                                    <input placeholder="Password" name="password" style="width:360px;"> 
                                </div>
                                <div class="col-sm-2 registerform" >
                                    <label for="select" class="control-label">School</label>
                                </div>
                                <div class="col-sm-10 registerform" >            
                                    <select class="form-control" name="school" style="width:360px;" >
                                        <c:forEach items="${school}" var="school">
                                            <option>  ${school.getSchoolname()}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-sm-12 registerform" >
                                    <button type="submit" class="btn btn-danger dropdown-toggle btn-sm" style="width:180px;">
                                        Register 
                                    </button>
                                </div>
                            </form>
                            <c:if test="${not empty taken}">
                                <div class="taken">${taken}</div>
                            </c:if>
                                 <c:if test="${not empty fillout}">
                                <div class="fillout">${fillout}</div>
                            </c:if>
                            <c:if test="${not empty register}">
                                <div class="register">${register}</div>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="container" >

        </div>


        <script src="resources/js/jquery.js"></script>
        <script src="resources/js/bootstrap.min.js"></script>
    </body>

    <footer class="footer-inverse text-center">
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
</html>