<!DOCTYPE html>
<html lang="en">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Display Friends</title>
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
                 <li >
                    <a href="student.html">
                        Home
                    </a>
                </li>
				<li>
                    <a href="studentmanagefriends.html">Manage Friends</a>
                </li>
                <li  class="active">
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
                    <li><a href="studentcourseofferings.html">2014</a></li>
                    <li><a href="studentcourseofferings.html">2015</a></li>
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
			<div class="col-sm-8 ">
			<h1 align="center">Friends</h1>
			<div class="col-sm-offset-1">
<table class="table  "style=" width:700px;">
<thead>
<tr>
<th>
First Name
</th>
<th>
Last Name
</th>
<th>
School
</th>
</tr>
</thead>
<tbody style="text-align:left;">
<tr>
<td>
Alvin
</td>
<td>
Feng
</td>
<td>
Stony Brook University
</td>
<td>
<div class="btn-group">
   <button type="button" class="btn btn-info dropdown-toggle btn-xs" data-toggle="dropdown">
      Assigned<span class="caret"></span>
   </button>
   <ul class="dropdown-menu" style="background-color:#31b0d5; ">
      <li><a href="#">2014</a></li>
      <li><a href="#">2015</a></li>
      <li><a href="#">2016</a></li>
   </ul>
</div>	
</td>
<td>
<div class="btn-group">
   <button type="button" class="btn btn-success dropdown-toggle btn-xs" data-toggle="dropdown">
      Desired<span class="caret"></span>
   </button>
   <ul class="dropdown-menu" style="background-color:#449d44; ">
      <li><a href="#">2014</a></li>
      <li><a href="#">2015</a></li>
      <li><a href="#">2016</a></li>
   </ul>
</div></td>
<td>
<button class="btn btn-danger btn-xs">Unfriend</button>

</td>
</tr>

</tbody>
</table>
		</div>	
			</div>

			
<div class="col-sm-3 ">
			
			
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