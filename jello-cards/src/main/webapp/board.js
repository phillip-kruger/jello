/* 
 * Semantic-UI
 * Javascript file for board.html
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
var webSocket;

$('document').ready(function(){
    openSocket();
    writeMessage("");
});

window.onbeforeunload = function() {
    closeSocket();
};

function openSocket(){
    // Ensures only one connection is open at a time
    if(webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED){
        writeMessage("Already connected...");
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
    new_uri += "/card";
    webSocket = new WebSocket(new_uri);

    /**
     * Binds functions to the listeners for the websocket.
     */
    webSocket.onopen = function(event){
        if(event.data === undefined)
            return;
        
        writeMessage(event.data);
        
    };

    webSocket.onmessage = function(event){
        
        var json = JSON.parse(event.data);
        var type = json.type;
        var card = json.card;
        
        if(type === "update"){ 
            removeCard(card.id);
            writeCard(card);
        }else if( type === "delete"){
            removeCard(card.id);
        }else {
            writeCard(card);
        }
        
    };

    webSocket.onclose = function(){
        writeMessage("<div class='ui floating message'>"
                        + "<p>Connection closed</p>"
                     + "</div>");
    };

}

function closeSocket(){
    webSocket.close();
}
function removeCard(id){
    $("#card_" + id).remove();
}

function writeCard(card){
    sessionStorage[card.id] = JSON.stringify(card);
    writeCommentModal(card);
    paintCard("<div class='ui raised card' id='card_" + card.id +"'>"
                    + "<div class='content'>"
                        + "<div class='header'>" + card.title + "</div>"
                        + "<div class='meta'>"
                            + "<span class='date'>" 
                                + new Date(card.created).toDateString() 
                            + "</span>"
                        + "</div>" 
                        + "<div class='description'>" 
                            + card.description + "<br/>"
                        + "</div>"
                        + "<p><i class='comment icon' onclick='showComments(" + card.id +");'></i>" + getNumberOfComments(card) + " comments</p>"
                    + "</div>"
                    + "<div class='extra content'>"
                        + "<div class='right floated author'>"
                            + "<img class='ui avatar image' src='/images/avatars/" + card.createdBy + ".jpg'> " + card.createdBy
                        + "</div>"
                    + "</div>"
                + "</div>",card.swimlane);
}

function showComments(id){
    var card = JSON.parse(sessionStorage[id]);
    
    if(getNumberOfComments(card) > 0){
        $("#comment_" + id)
            .modal('show')
        ;
    }
}

function getNumberOfComments(card){
    var commentsSize = 0;
    if(card.hasOwnProperty('comments')){
        commentsSize = card.comments.length;
    }
    return commentsSize;
    
}

function writeCommentModal(card){
    paintCommentModel("<div class='ui modal' id='comment_" + card.id + "'>"
                        + "<i class='close icon'></i>"
                        + "<div class='header'>"
                            + "<i class='comment icon'></i>" + card.title
                        + "</div>"
                        + "<div class='content'>"
                            + "<div class='description'>"
                                + "<div class='ui comments'>"
                                + getCommentsHTML(card.comments)
                                + "</div>"
                            + "</div>"
                        + "</div>"
                
                    + "</div>");
}

function getCommentsHTML(comments){
    var h = "";
    var arrayLength = comments.length;
    for (var i = 0; i < arrayLength; i++) {
        var c = comments[i];
        
        h += "<div class='comment'>"
            + "<div class='content'>"
                + "<a class='author'>" + c.madeBy + "</a>"
                + "<div class='metadata'>"
                    + "<span class='date'>" + new Date(c.madeOn).toDateString() + "</span>"
                + "</div>"
                + "<div class='text'>"
                    + c.comment
                + "</div>  "
            + "</div>"
        + "</div>"
        
        
    }
    return h;
}

function paintCard(text,swimlane){
    if(swimlane === 'pipeline'){
        pipeline.innerHTML += text + "<br/>";
    }else if (swimlane === 'development'){
        development.innerHTML += text + "<br/>";
    }else if (swimlane === 'testing'){
        testing.innerHTML += text + "<br/>";
    }else if (swimlane === 'release'){
        release.innerHTML += text + "<br/>";
    }
}

function paintCommentModel(text){
    comments.innerHTML += text;
}


function writeMessage(text){
    messages.innerHTML = text + "<br/>";
}