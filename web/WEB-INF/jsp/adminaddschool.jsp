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
			<div class="col-sm-10 text-center">
			<h1>Add School</h1>
			<form id="adminaddschool" novalidate>
			<div class="col-sm-12">
			<input placeholder="School Name" id="firstName" style="width:395px;"> </input>
			</div>
			<div class="col-sm-12 registerform">
			<input placeholder="Academic Year" id="lastName" style="width:395px;"> </input>
			</div>
		
			<div class="col-sm-12 registerform" style="margin-left:93px;">
			
      <label for="select" class="col-sm-2 control-label">Semesters</label>
      <div class="col-lg-10" style="width:425px;">
        <select class="form-control" id="select">
          <option>1</option>
          <option>2</option>
          <option>3</option>
          <option>4</option>
        </select>
			</div>
		
			</div>
		<div class="col-sm-3 registerform">	
			    <table class="table" style="">
	  <thead>
	  <tr>
	  <th>Semester 1</th>
	  </tr>
	  </thead>
	  <tbody>
	  <tr>
	  <td><div>
      <label for="select" >Days</label>  
        <select class="form-control" id="select" style="height:40px;">
          <option>1</option>
          <option>2</option>
          <option>3</option>
          <option>4</option>
		  <option>5</option>
          <option>6</option>
          <option>7</option>
        </select>
			</div></td></tr>
			
		<tr>
		<td>
		<label for="select">Periods</label>
        <select class="form-control" id="select">
          <option>6</option>
          <option>7</option>
          <option>8</option>
          <option>9</option>
		  <option>10</option>
          <option>11</option>
          <option>12</option>
        </select>
      </td>
	  </tr>
	  </tbody>
	  </table>
	  </div>
	<div class="col-sm-3 registerform">	
			    <table class="table" style="">
	  <thead>
	  <tr>
	  <th>Semester 2</th>
	  </tr>
	  </thead>
	  <tbody>
	  <tr>
	  <td><div>
      <label for="select" >Days</label>  
        <select class="form-control" id="select" style="height:40px;">
          <option>1</option>
          <option>2</option>
          <option>3</option>
          <option>4</option>
		  <option>5</option>
          <option>6</option>
          <option>7</option>
        </select>
			</div></td></tr>
			
		<tr>
		<td>
		<label for="select">Periods</label>
        <select class="form-control" id="select">
          <option>6</option>
          <option>7</option>
          <option>8</option>
          <option>9</option>
		  <option>10</option>
          <option>11</option>
          <option>12</option>
        </select>
      </td>
	  </tr>
	  </tbody>
	  </table>
	  </div>	
		<div class="col-sm-3 registerform">	
			    <table class="table" style="">
	  <thead>
	  <tr>
	  <th>Semester 3</th>
	  </tr>
	  </thead>
	  <tbody>
	  <tr>
	  <td><div>
      <label for="select" >Days</label>  
        <select class="form-control" id="select" style="height:40px;">
          <option>1</option>
          <option>2</option>
          <option>3</option>
          <option>4</option>
		  <option>5</option>
          <option>6</option>
          <option>7</option>
        </select>
			</div></td></tr>
			
		<tr>
		<td>
		<label for="select">Periods</label>
        <select class="form-control" id="select">
          <option>6</option>
          <option>7</option>
          <option>8</option>
          <option>9</option>
		  <option>10</option>
          <option>11</option>
          <option>12</option>
        </select>
      </td>
	  </tr>
	  </tbody>
	  </table>
	  </div>			
				<div class="col-sm-12 registerform">
			<input placeholder='Lunch Range(exe."3-5")' id="firstName" style="width:395px;"> </input>
			</div>
			<div class="col-sm-12 registerform">
			<input placeholder='Legal Blocks(exe."{1;1,2,3,5")' id="firstName" style="width:395px;"> </input>
			</div>
	<div class="col-sm-12 registerform" >
   <button type="button" class="btn btn-danger dropdown-toggle btn-sm" style="width:180px; margin-right:215px;">
     Add School 
   </button>
			</div>
			</form>
             
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