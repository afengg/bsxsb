<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Add School</title>
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
                    <div class="col-sm-6 container">
                        <h3>Current Information</h3>
                        <h4><p>School Name: ${school.getSchoolname()}</p>
                            <p>Academic Year: ${school.getAcademicyear()}</p>
                            <p>Semesters: ${currentsemesters}</p>
                            <p>Periods: ${currentperiods}</p>
                            <p>Days: ${currentdays}</p>
                            <p>Lunch Range: ${school.getLunchrange()}</p>
                            <p>Legal Blocks: ${school.getScheduleblocks()}</p>
                        </h4>
                            
                    </div>
			<div class="col-sm-10 text-center">
			<h1>Edit School</h1>
			<form action="editschool.html" method="POST">
                            <input type="hidden" id="schoolID" name="schoolID" value="${school.getSchoolid()}"></input>
			<div class="col-sm-12 registerform">
			<input name="schoolname" id="firstName" style="width:395px;" value="${school.getSchoolname()}"></input>
			</div>
			<div class="col-sm-12 registerform">
			<input name="academicyear" id="lastName" style="width:395px;" value="${school.getAcademicyear()}"></input>
			</div>
		
			<div class="col-sm-12 registerform" style="margin-left:93px;">
			
      <label for="select" class="col-sm-2 control-label">Semesters</label>
      <div class="col-lg-10" style="width:425px;">
        <select class="form-control" name="numsemesters" id="select">
            <option ${currentsemesters == "1" ? 'selected="selected"' : ' '}> 1</option>
          <option ${currentsemesters == "2" ? 'selected="selected"' : ' '}>2</option>
          <option ${currentsemesters == "3" ? 'selected="selected"' : ' '}>3</option>
          <option ${currentsemesters == "4" ? 'selected="selected"' : ' '}>4</option>
        </select>
			</div>
		
			</div>
        <div class="col-sm-12 registerform" style="margin-left:106px;">
      <label for="select" class="col-sm-2 control-label" >Days</label>  
        <select class="form-control" name="numdays" id="select" style="height:40px; width:395px;">
          <option ${currentdays == "1" ? 'selected="selected"' : ' '}>1</option>
          <option ${currentdays == "2" ? 'selected="selected"' : ' '}>2</option>
          <option ${currentdays == "3" ? 'selected="selected"' : ' '}>3</option>
          <option ${currentdays == "4" ? 'selected="selected"' : ' '}>4</option>
	  <option ${currentdays == "5" ? 'selected="selected"' : ' '}>5</option>
          <option ${currentdays == "6" ? 'selected="selected"' : ' '}>6</option>
          <option ${currentdays == "7" ? 'selected="selected"' : ' '}>7</option>
        </select>
	</div>
        <div class="col-sm-12 registerform" style="margin-left:106px;">
		<label for="select" class="col-sm-2 control-label">Periods</label>
        <select class=" form-control" name="numperiods" id="select" style="height:40px; width:395px;">
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
        <div class="col-sm-12 registerform" style="margin-left:50px;">
            <label for="lunchrange" class="col-sm-2 control-label" style="margin-left:60px;">Lunch Range</label>
			<input  name="lunchrange" id="lunchrange" style="width:395px;" value="${school.getLunchrange()}"></input>
        </div>
			<div class="col-sm-12 registerform" style="margin-left:50px;">
                            <label for="legalblocks" class="col-sm-2 control-label" style="margin-left:60px">Legal Blocks</label>
                            <textarea  name="legalblocks" id="legalblocks" style="width:395px;">${school.getScheduleblocks()}</textarea>
			</div>
	<div class="col-sm-12 registerform" >
   <button type="submit" class="btn btn-danger dropdown-toggle btn-sm" style="width:180px; margin-left:300px;">
     Add School 
   </button>
			</div>
			</form>
                        <div>
            <c:if test = "${not empty fillout}">
                <div class="fillout">${fillout}</div>
            </c:if>
            <c:if test="${not empty taken}">
                <div class="taken">${taken}</div>
            </c:if>
            <c:if test="${not empty ayregex}">
                <div class="ayregex">${ayregex}</div>
            </c:if>
            <c:if test="${not empty lrregex}">
                <div class="lrregex">${lrregex}</div>
            </c:if>
            <c:if test="${not empty lbregex}">
                <div class="lbregex">${lbregex}</div>
            </c:if>
			<c:if test="${not empty added}">
                <div class="added">${added}</div>
            </c:if>
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