<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Student</title>
		 <link href="resources/css/bootstrap.min.css" rel="stylesheet">
		  <link href="resources/css/style.css" rel="stylesheet">
		  <link href="resources/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
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
                            <a href="resume.html">Sign Out</a>
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
                    <a href="student.html">
                        Home
                    </a>
                </li>
                <li>
                    <a href="managefriends.html">Manage Friends</a>
                </li>
                <li>
                   <a href="displayfriends.html">Display Friends</a>
                </li>
                <li>
                   <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                    Manage Assigned Courses<span class="caret"></span>
                  </a>
                  <ul class="dropdown-menu">
                    <li><a href="entercourses.html">Enter Assigned Courses</a></li>
                    <li><a href="assignedcourses.html">View Assigned Courses</a></li>
					 <li><a href="editassigned.html">Edit Assigned Courses</a></li>
                  </ul>
                </li>
              <li class="dropdown">
                  <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                    Course Offerings<span class="caret"></span>
                  </a>
                  <ul class="dropdown-menu">
                    <li><a href="courseofferings.html">2014</a></li>
                    <li><a href="courseofferings.html">2015</a></li>
                  </ul>
                </li>
      <li class="dropdown">
                  <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                    Manage Desired Courses<span class="caret"></span>
                  </a>
                  <ul class="dropdown-menu">
       <li><a href="generatecourses.html">Generate Desired Courses</a></li>
                    <li><a href="viewgenerated.html">View Desired Courses</a></li>
					<li><a href="viewgenerated.html">Export Desired Courses</a></li>
                  </ul>
                </li>
                
            </ul>
			
			
			</ul>
				</div>
			<div class="col-sm-8 text-center" style="margin-top:30px;">
			<img src="resources/img/profile.png">
			
			
			</div>
			<div class="col-sm-8  text-center">
			<h3></h3>
			</div>
                        <div class="col-sm-8  text-center">
			<h3>    ${student.getPassword()}${student.getEmail()}${student.getLastname()}</h3>

                        ${msg}
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