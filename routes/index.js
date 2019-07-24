/*
* GET home page.
*/
 
exports.index = function(req, res){
  var message = '';
  res.render('index.ejs',{message: message})
};

exports.logout = function(req,res){
  if (req.session.user && req.cookies.user_sid) {
    res.clearCookie('user_sid');
    res.redirect('/');
    } else {
        res.redirect('/login');
    }
}