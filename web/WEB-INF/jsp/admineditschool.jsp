<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Edit School</title>
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
                            
            <div class="col-sm-7 text-center">
			<h1>Edit School</h1>
			<form class="form-horizontal" action="editschool.html" method="POST">
                            <fieldset>
                            <input type="hidden" id="schoolID" name="schoolID" value="${school.getSchoolid()}"></input>
			<div class="form-group">
                            <label for="schoolname" class="col-lg-3 control-label">School Name</label>
                            <div class="col-lg-8">
                            <input class="form-control" name="schoolname" id="schoolname" value="${school.getSchoolname()}"></input>
                            </div>
			</div>
			<div class="form-group">
                            <label for="academicyear" class="col-lg-3 control-label">Academic Year</label>
                            <div class="col-lg-8">
                            <input class="form-control" name="academicyear" id="academicyear" value="${school.getAcademicyear()}"></input>
                            </div>
			</div>
		
			<div class="form-group">
			
      <label for="select" class="col-lg-3 control-label">Semesters</label>
      <div class="col-lg-8">
        <select class="form-control" name="numsemesters" id="select">
            <option ${currentsemesters == "1" ? 'selected="selected"' : ' '}> 1</option>
          <option ${currentsemesters == "2" ? 'selected="selected"' : ' '}>2</option>
          <option ${currentsemesters == "3" ? 'selected="selected"' : ' '}>3</option>
          <option ${currentsemesters == "4" ? 'selected="selected"' : ' '}>4</option>
        </select>
			</div>
		
			</div>
        <div class="form-group">
      <label for="select" class="col-lg-3 control-label" >Days</label>
      <div class="col-lg-8">
        <select class="form-control" name="numdays" id="select">
          <option ${currentdays == "1" ? 'selected="selected"' : ' '}>1</option>
          <option ${currentdays == "2" ? 'selected="selected"' : ' '}>2</option>
          <option ${currentdays == "3" ? 'selected="selected"' : ' '}>3</option>
          <option ${currentdays == "4" ? 'selected="selected"' : ' '}>4</option>
	  <option ${currentdays == "5" ? 'selected="selected"' : ' '}>5</option>
          <option ${currentdays == "6" ? 'selected="selected"' : ' '}>6</option>
          <option ${currentdays == "7" ? 'selected="selected"' : ' '}>7</option>
        </select>
      </div>
	</div>
        <div class="form-group" >
		<label class="col-lg-3 control-label" for="select" class="col-sm- control-label">Periods</label>
                <div class="col-lg-8">
        <select class=" form-control" name="numperiods" id="select">
          <option ${currentperiods == "6" ? 'selected="selected"' : ' '}>6</option>
          <option ${currentperiods == "7" ? 'selected="selected"' : ' '}>7</option>
          <option ${currentperiods == "8" ? 'selected="selected"' : ' '}>8</option>
          <option ${currentperiods == "9" ? 'selected="selected"' : ' '}>9</option>
          <option ${currentperiods == "10" ? 'selected="selected"' : ' '}>10</option>
          <option ${currentperiods == "11" ? 'selected="selected"' : ' '}>11</option>
          <option ${currentperiods == "12" ? 'selected="selected"' : ' '}>12</option>
        </select>
                </div>
        </div>	
        <div class="form-group">
            <label for="lunchrange" class="col-lg-3 control-label">Lunch Range</label>
            <div class="col-lg-8">
            <input class="form-control" name="lunchrange" id="lunchrange" value="${school.getLunchrange()}"></input>
            </div>
        </div>
	<div class="col-lg-12 registerform" >
   <button type="submit" class="btn btn-success dropdown-toggle btn-sm" style="width:180px;">
     Save Changes 
   </button>
            <div>
            <c:if test = "${not empty fillout}">
                <div class="label label-danger">${fillout}</div><br>
            </c:if>
            <c:if test="${not empty taken}">
                <div class="label label-danger">${taken}</div><br>
            </c:if>
            <c:if test="${not empty ayregex}">
                <div class="label label-danger">${ayregex}</div><br>
            </c:if>
            <c:if test="${not empty lrregex}">
                <div class="label label-danger">${lrregex}</div><br>
            </c:if>
            <c:if test="${not empty lbregex}">
                <div class="label label-danger">${lbregex}</div><br>
            </c:if>
            <c:if test="${not empty added}">
                <div class="label label-danger">${added}</div>
            </c:if>
	</div>
            </div>
            <fieldset>
            </form>
            </div>
                        <div class="col-sm-3" style="padding-top:20px;">
                            <div class="panel panel-primary">
                                <div class="panel-heading">
                                    <h3 class="panel-title">Current Information</h3>
                                </div>
                            <div class="panel-body">
                                <div>School Name: ${school.getSchoolname()}</div>
                                <div>Academic Year: ${school.getAcademicyear()}</div>
                                <div>Semesters: ${currentsemesters}</div>
                                <div>Periods: ${currentperiods}</div>
                                <div>Days: ${currentdays}</div>
                                <div>Lunch Range: ${school.getLunchrange()}</div>
                                <div>Legal Blocks: ${school.getScheduleblocks()}</div>
                            </div>
                        </div>
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