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
                        <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                    Manage School<span class="caret"></span>
                  </a>
                  <ul class="dropdown-menu">
                    <li><a href="addschool.html">Add School</a></li>
                    <li><a href="admin.html">View Schools</a></li>
                  </ul>
                </li>
                <li>
                    <a href="managerequests.html">Manage Account Requests</a>
                </li>
                <li>
                    <a href="manageaccounts.html">Display Student Accounts</a>
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
<tr>
<td>
Stony Brook University
</td>
<td>
2015
</td>
<td>
2
</td>
<td>
5
</td>
<td>
20
</td>
<td>
1-20
</td>
<td>
{1;1,2} {2;3,4,5} {1;1,6,2}
</td>
<td>
<button class="btn btn-danger btn-xs">Delete School </button>
</td>
</tr>

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