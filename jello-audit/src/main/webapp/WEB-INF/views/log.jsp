<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Jello Audit log</title>
        <link rel="stylesheet" type="text/css" href="webjars/fomantic-ui/dist/semantic.min.css">
    </head>
    <body>
        <div class="ui page">
            <div class="ui mini menu">
                <div class="header item">Jello: Audit log</div>
                <a class="item" href="cards" target="_blank">
                    Board
                </a>
                <div class="right menu">
                    
                    <div class="item">
                        <button id="btnConnect" class="ui positive button" type="submit" onclick="connect()"><i class="linkify icon"></i></i> Connect</button>
                        <button id="btnDisconnect" class="ui negative button" type="submit" onclick="disconnect()"><i class="unlink icon"></i> Disconnect</button>  
                    </div>
                    
                </div>
            </div>
            
            <table class="ui compact table" id="audittable">
                <thead>
                    <tr>
                        <th>When</th>
                        <th style="display: none;">Object</th>
                        <th>Action</th>
                        <th>Id</th>
                        <th>Title</th>
                        <th>Description</th>
                        <th>Created</th>
                        <th>Number Of Comments</th>
                        <th>Swimlane</th>
                    </tr>
                </thead>
                <tbody>
                    <tr/>
                </tbody>
            </table>
            
        </div>
  
        <script type="text/javascript" src="webjars/jquery/dist/jquery.min.js"></script>
        <script type="text/javascript" src="webjars/fomantic-ui/dist/semantic.min.js"></script>
        <script>
            var webSocket;
            
            $('document').ready(function(){
                
                if(${status.connected}){
                    connect();
                }else{
                    $("#btnDisconnect").hide();
                }
            });
            
            function connect(){
                openSocket();
                doAction("connect");
                
                $("#btnConnect").hide();
                $("#btnDisconnect").show();
                
                $("#audittable").dimmer('hide');
                toastMessage("Connect","Successfully connected to the Jello Card System");
            }
            
            function disconnect(){
                $("#btnDisconnect").hide();        
                $("#btnConnect").show();
                doAction("disconnect");
                closeSocket();
                
                $("#audittable").dimmer('show');
                
                toastMessage("Disconnect","Successfully disconnected from the Jello Card System");
            }
            
            function openSocket(){
                // Ensures only one connection is open at a time
                if(webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED){
//                    writeMessage("Already connected...");
                    return;
                }
                // Create a new instance of the websocket
                var loc = window.location, new_uri;
                if (loc.protocol === "https:") {
                    new_uri = "wss:";
                } else {
                    new_uri = "ws:";
                }
                new_uri += "//" + loc.host;
                new_uri += "/audit/stream";
                webSocket = new WebSocket(new_uri);

                /**
                 * Binds functions to the listeners for the websocket.
                 */
                webSocket.onopen = function(event){
                    if(event.data === undefined)
                        return;
                };

                webSocket.onmessage = function(event){

                    var json = JSON.parse(event.data);
                    var what = json.object;
                    var action = json.what;
                    var when = json.when;
                    var created = json.metadata.created;
                    var description = json.metadata.description;
                    var id = json.metadata.id;
                    var numberOfComments = json.metadata.numberOfComments;
                    var swimlane = json.metadata.swimlane;
                    var title = json.metadata.title;
//                    console.log(json);
                    addRowAtTop(what,when,action,id,title,description,created,numberOfComments,swimlane);
                };

            }
            
            function closeSocket(){
                webSocket.close();
            }
    
            function doAction(action){
                $.post("", { action:action });
            }
    
            function addRowAtTop(what,when,action,id,title,description,created,numberOfComments,swimlane) { 
                var clazz = "positive";
                if(action === "update"){ 
                    clazz = "warning";
                }else if( action === "delete"){
                    clazz = "error";
                }
                
                $('#audittable > tbody > tr:first')
                        .before(
                        '<tr class=' + clazz + '>' + 
                            '<td data-label="When">' + when + '</td>' + 
                            '<td data-label="Object" style="display: none;">' + what + '</td>' + 
                            '<td data-label="Action">' + action + '</td>' + 
                            '<td data-label="Id">' + id + '</td>' + 
                            '<td data-label="Title">' + title + '</td>' + 
                            '<td data-label="Description">' + description + '</td>' + 
                            '<td data-label="Created">' + created + '</td>' + 
                            '<td data-label="Number Of Comments">' + numberOfComments + '</td>' + 
                            '<td data-label="Swimlane">' + swimlane + '</td>' + 
                        '</tr>');
                
            } 
            
            function toastMessage(title,message){
                $('body').toast({
                    title: title,
                    message: message,
                    showProgress: 'bottom',
                    position: 'bottom right'
                });
            }
        </script>
    </body>
</html>
