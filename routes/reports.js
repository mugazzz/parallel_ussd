const path = require('path');
const fs = require('fs');


exports.readReport = function (req,res){
    fs.open(path.join(__dirname, '..','\\reports\\11-Jun-2019\\11-Jun-2019_11-28-41\\Master.html'), 'r', function(err, fileToRead){
        if (!err){
            fs.readFile(fileToRead, {encoding: 'utf-8'}, function(err,data){
                if (!err){
               // console.log('received data: ' + data);
                res.writeHead(200, {'Content-Type': 'text/html'});
                res.write(data);
                res.end();
                }else{
                    console.log(err);
                }
            });
        }else{
            console.log(err);
        }
    });
}

exports.sendReport = function (req,res){

    var options = {
        root: path.join('./reports'),
        dotfiles: 'deny',
        headers: {
          'x-timestamp': Date.now(),
          'x-sent': true
        }
      }
    
      var fileName = req.params['0'];
      //console.log("request params",req.params);
      res.sendFile(fileName, options, function (err) {
        if (err) {
            console.log(err);
        } else {
          console.log('Sent:', fileName)
        }
      })
}

exports.getReport = function (req,res){

    if(req.method == "GET"){	
        let userId = req.session.userId;
        
        if(userId == null){
            res.redirect("/login");
            return;
        }

        //requiring path and fs modules
        const path = require('path');
        //joining path of directory 
        const directoryPath = path.join('./reports',userId); //,'16-Jul-2019','16-Jul-2019_11-28-41');//, '11-Jun-2019_11-28-41'
        //passsing directoryPath and callback function
        
        var filter = require('filter-files');

        var files = filter.sync(directoryPath);
        let fileListArr = [];
        //console.log('files', files);
        fileListArr = files.filter((file)=>{
            return (file.search("Master.html") > -1)
            
        })
        res.render('reports.ejs', {user:userId,ipaddress:req.session.user.ip_address,fileList:fileListArr});
    }
    

}