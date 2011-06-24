net = require('net');
 
var sockets = [];

var s = net.Server(function(socket) {
	sockets.push(socket);

	socket.on('data', function(d) {
		for (var i = 0; i< sockets.length; i++){
			sockets[i].write(d);
		}
		//b = new Buffer(d);
		console.log(d.toString('UTF-8'));
		console.log(d);
	});

	socket.on('end', function() {
		var i = sockets.indexOf(socket);
		sockets.splice(i, 1);
	});


});

s.listen(2525);
console.log('Listening port 2525');
