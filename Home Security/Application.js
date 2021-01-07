 switchData = 0

    var socket = io.connect('http://' + document.domain + ':' + location.port);

    socket.on('connect', function(msg){
        console.log("connection established");
    });
    
    socket.on('switchData', function(msg) {
        console.log("Button was pressed server side")
    });
    
    function switchFunction(){
        switchData += 1
        console.log(switchData);
        socket.emit('switch', switchData);
    }