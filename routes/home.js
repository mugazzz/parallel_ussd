exports.home = function(req, res, next){
	let userId = req.session.userId;
 console.log(req.session);
 if(userId == null){
	 res.redirect("/login");
	 return;
 }
	
	var sql="SELECT * FROM mav_user WHERE username='"+userId+"'";
	
		db.query(sql, function(err, results){
			//console.log(results);
			res.render('home.ejs', {user:userId,ipaddress:req.session.user.ip_address});	  
		 
});
}

exports.dashboard = function(req, res){

  let userId = req.session.userId;
	//console.log(req.session.userId);
	let message  = "";

	if (req.session.user && req.cookies.user_sid) {

		 let tsQuery = "SELECT * FROM  mav_test_case WHERE created_by='"+userId+"' ORDER BY created DESC";
	 
		 db.query(tsQuery, (err,results) =>{
			res.render('dashboard.ejs', {user:userId,ipaddress:req.session.user.ip_address,testCases:results,data:results,message:message});
		 });

		} 
		else {
		//	console.log("session invalid");
			res.redirect("/");
		} 
	 
	

};

exports.getTestCases = function(req, res){

	let userId = req.session.userId;
	  //console.log(req.session.userId);
	  let message  = "";
  
	  if (req.session.user && req.cookies.user_sid) {
		  //console.log("session valid");
		   //var sql="SELECT * FROM mav_auto.mav_user WHERE username='"+userId+"'";
		   let tsQuery = "SELECT * FROM  mav_test_case WHERE created_by='"+userId+"' ORDER BY created DESC";
	   
		   db.query(tsQuery, (err,results) =>{
					res.send({user:userId,
					"draw": 1,
					"recordsTotal": 57,
					"recordsFiltered": 57,
					data:results,
					message:message
					});
		   });
  
		  } 
		  else {
		  //	console.log("session invalid");
			  res.send({message:"invalid Login",result:"error"});
		  } 
	   
	  
  
  };

exports.save = function(req, res){
	let userId = req.session.userId;
  if(req.method == "POST"){
 
	let data ={	created_by:userId,
				Test_Case_ID:req.body.testCaseNo,
				Test_Scenario:req.body.testScenario,
				Test_Case:req.body.testCase,
				/*Test_Case_Desc:req.body.testcaseDesc,*/
				Test_Device:req.body.testDevice,
				MSISDN:req.body["MSISDN"],
				Product_Name:req.body["Product_Name"],
				Recharge_Coupon:req.body["Recharge_Coupon"],
				Call_TO_MSISDN:req.body["Call_TO_MSISDN"],
				CALL_DURATION:req.body["CALL_DURATION"],
				RECEIVER_MSISDN:req.body["RECEIVER_MSISDN"],
				Message_To_Send:req.body["Message_To_Send"],
				SMS_COUNT:req.body["SMS_COUNT"],
				TRANSFER_AMOUNT:req.body["TRANSFER_AMOUNT"],
				TRANSFER_TO_MSISDN:req.body["TRANSFER_TO_MSISDN"]
			};
		
		let sql = "INSERT INTO mav_test_case SET ?";
		db.query(sql, data,(err, results) => {
			if(err)
			{
				res.send({message:'error',result:err});
			} 
			else
				res.send({message:'successful',result:results});
			/*let message = "test case created successfully";
			let tsQuery = "SELECT * FROM  mav_auto.mav_test_case WHERE created_by='"+userId+"' ORDER BY row_id ASC";
	 
		 db.query(tsQuery, (err,results) =>{
			res.render('dashboard.ejs', {user:userId,testCases:results,message:message});
		 });*/
		//	res.redirect("/dashboard");
		});
    
  }

};

exports.delete = function(req, res){

  if(req.method == "POST"){	
		let data = req.body.delete_test_row_id;
		console.log(data);

		let sql = `DELETE FROM mav_test_case where row_id in (${data})`;
		db.query(sql, (err, results) => {
			if(err) throw err;
			res.redirect("/dashboard");
		});
    
  }

};

exports.execute = function(req, res, next){
	//const reports = require('../routes/reports');
	//const path = require('path');
	//const fs = require('fs');
	let userId = req.session.userId;
 //console.log(req.session);
 if(userId == null){
	 res.redirect("/login");
	 return;
 }

 if(req.method == "POST"){	

	let tc_id_list = req.body.execute_test_row_id.split(",");
	let datalist  = [];
	//let sql = `UPDATE mav_test_case SET Execution='1' where row_id in (${data})`;
	tc_id_list.forEach(element => {
		let data = [];
		data[0] = element;
		data[1] = userId;
		data[2] = "In Progress"
		datalist.push(data);
	});
	let sql = `INSERT INTO mav_tc_execute (test_case_id,created_by,execution_status) VALUES ?`;
	db.query(sql, [datalist], (err, results) => {
		if(err) throw err;

		let message;
		const mvn = require('maven').create({});
		mvn.execute(['clean', 'test'], { '-DsuiteXmlFile': 'testng1.xml' })
			.then((data) => {
				console.log('data from command:',data); 
				message=data;
				res.send(message);
			})
			.catch((error) =>{
				console.log('error executing mvn', error);
				message=error;
				res.send(message);
			});

		/*const util = require('util');
		const execFile = util.promisify(require('child_process').execFile);
		async function runFile() {
		const { stdout } = await execFile('mvn', ['clean test -DsuiteXmlFile=testng1.xml']);
		console.log(stdout);

		//const { stdout } = await execFile('notepad', ['sathish']);
  		//console.log(stdout);
		}*/

		//runFile();
		//console.log("Message:",message);
		
		//res.redirect("/dashboard");
		//res.render('dashboard.ejs', {user:userId});

	});
	
}
else{
	res.redirect("/dashboard");
	//res.render('dashboard.ejs', {user:userId});
}


}