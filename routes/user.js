exports.login = function(req, res){
    let message = '';
    let sess = req.session; 
    let mysql = require('mysql');

    let connection = mysql.createConnection({
      host     : 'localhost',
      user     : 'root',
      password : '',
      database : 'mav_auto'
    });

   connection.connect();

   global.db = connection;
 
    if(req.method == "POST"){
       let post  = req.body;
       let name= post.username;
       let pass= post.password;
      
       let sql="SELECT username,email,password FROM mav_user WHERE username='"+name+"' and password = '"+pass+"'";                           
      // console.log(req.session);
       db.query(sql, function(err, results){ 
         if(err){
           // message = err;
           // res.render('index.ejs',{message: message});
            return res.json({'error':true,'message':err});
         }
         if(results.length){
           // console.log(results[0]);
            sess.userId = results[0].username;
            sess.user = results[0];
            res.redirect('/dashboard');
          }
          else{
             message = 'Wrong Credentials.';
             res.render('index.ejs',{message: message});
          }
                  
       });
    } 
    else {
       res.render('index.ejs',{message: message});
    }         
 };

 exports.signup = function(req, res){
    let message = '';
    let mysql = require('mysql');

    let connection = mysql.createConnection({
      host     : 'localhost',
      user     : 'root',
      password : '',
      database : 'mav_auto'
    });

   connection.connect();

   global.db = connection;

    if(req.method == "POST"){
       let post  = req.body;
       console.log(post);
       let data ={username:post.user_name,
                  password:post.password,
                  firstname:post.first_name,
                  lastname:post.last_name,
                  email:post.email
                  };
 
       let sql = "INSERT INTO mav_user SET ?";
 
       db.query(sql,data, function(err, result) {
          message = "Succesfully! Your account has been created.";
          res.render('signup.ejs',{message: message});
       });
 
    } else {
       res.render('signup.ejs',{message: message});
    }
 };