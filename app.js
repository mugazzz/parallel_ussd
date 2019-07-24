/**
* Module dependencies.
*/
const express = require('express');
const routes = require('./routes');
const user = require('./routes/user');
const home = require('./routes/home');
const reports = require('./routes/reports');
//let http = require('http');
const path = require('path');
const port = parseInt(process.env.PORT, 10) || 5000;

/*const multer = require('multer');
var storage = multer.diskStorage({
  destination: function (req, file, cb) {
    cb(null, 'uploads/');
  },
  filename: function (req, file, cb) {
    cb(null, file.originalname);
  }
})

var upload = multer({ storage: storage });*/

//var methodOverride = require('method-override');
var app = express();
var bodyParser = require("body-parser");
var cookieParser = require("cookie-parser");

let session = require('express-session');
app.use(session({
  key:'user_sid',
  secret: 'duAutom',
  resave: false,
  saveUninitialized: true,
  cookie: { maxAge: 6000000 }
}));


// configure middleware
app.set('port', process.env.PORT || 5000);
app.set('views', __dirname + '/views');
app.set('view engine', 'ejs');
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
app.use(cookieParser());

app.use((req, res, next) => {
  if (req.cookies.user_sid && !req.session.user) {
      res.clearCookie('user_sid');        
  }
  next();
});

// middleware function to check for logged-in users
var sessionChecker = (req, res, next) => {
  if (req.session.user && req.cookies.user_sid) {
      res.redirect('/dashboard');
  } else {
      next();
  }    
};

// all environments
app.use(express.static(path.join(__dirname, 'public')));
app.get('/', sessionChecker,routes.index);//call for main index page
app.get('/login', sessionChecker, routes.index);//call for login page
app.get('/signup', sessionChecker, user.signup);//call for signup page
app.get('/home', home.home);//call for homepage page after login
app.get('/logout', routes.logout);//call for login page
app.get('/dashboard',home.dashboard);//call for dashboard page

app.get('/testcases',home.getTestCases);

app.post('/login', user.login);//call for login post
app.post('/signup', user.signup);//call for signup post
app.post('/save', home.save);
app.post('/delete',home.delete);
app.post('/execute',home.execute);

app.get('/reports',reports.getReport);
app.get('/reports/*',reports.sendReport);

//var serveIndex = require('serve-index');
//app.use('/reports', express.static(path.join(__dirname, 'reports')),serveIndex(path.join(__dirname, 'reports'),{'icons': true,'view':'details'}));

//Middleware
app.listen(port,function(){
  console.log("node server listening at port:",port);
});