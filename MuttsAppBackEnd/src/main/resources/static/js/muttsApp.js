let baseUrl = '/users';
// this is a hidden element that passes the userId into the URL
let userId = document.getElementById('user_id').value;
//********************************
// fetch info for left sidebar
//********************************
// this will run asynchronously as soon as the page is loaded
// it takes time to convert the readable stream to Json
// in the meantime, other things like submitting a new message can happen
(function getChatPreviews() {
    fetch(`${baseUrl}/${userId}/msgpreviews`)
        .then(res => res.json())  // fetching the url returns a response, which is then converted to json
        .then(resObj => { // that json response has a body object and =>
            resObj.data.forEach(msgPreviewObj => { // this object has a data property and under that, an array of 'chatPreview' objects
                // for each chatPreview obj of the chat array, create a preview box with the chatPreview data using the createChatPreview function (further down) that uses the img, message and date
                createChatPreview(msgPreviewObj);
            })
        })
})();

//************************************************************
// click listener that listens for a click on each chatPreview
//************************************************************
// and then creates each chatBubble for the chat_id clicked
function chatPreviewClick(event) {
    console.log()
    let chatId = event.target.dataset.chat_id; // assign 'chatId' to the data attribute 'chat_id' that we assigned (in createMsgPreview)
    // to the various elements of the preview box (these are the targets of the click event) - and which hold the corresponding
    // chat ids for each preview
//    let senderId = event.target.dataset.sender_id;
    fetch(`/chats/${chatId}`)
    .then(res => res.json())
    .then(resObj => {
        // to only get the chat messages relevant to this chat in the chatBubble wrapper, reset the innerHTML to null every time
        document.getElementById('chat-container-wrapper').innerHTML = "";
        document.getElementById("send-message").dataset.chat_id = chatId;
        console.log(resObj)
        resObj.data.forEach(chatBubbleObj => {
            createChatBubble(chatBubbleObj)
        })
    })
    // only allow form input after clicking on a chatPreview
    document.getElementById("new-message").removeAttribute("disabled");

    // change chat Pic to match chat clicked on: find the closest 'individual-msg-preview-box' element to the event target
    // and set the src attribute of chat-photo>img to the src of that closest element
    document.querySelector("#chat-photo > img").setAttribute("src", event.target.closest('.individual-msg-preview-box').querySelector('img').src);
}

//************************************************************
// submit listener that waits for a user to enter a newMessage
//************************************************************
//prevents the default behaviour and prevents the page re-loading 20.27
let messageForm = document.getElementById("send-message");

messageForm.addEventListener('submit', function (event) {
    event.preventDefault();
    let newMessage = document.getElementById('new-message').value;
    let newMsgObj = { // our API documentation says we need a senderId, a chatId, and a message content
        senderId: userId,
        chatId: event.target.dataset.chat_id, //how do we get the chat-id?
        content: newMessage
    }
    createChatBubble(newMsgObj);
    sendNewMessage(newMsgObj);
    document.getElementById('new-message').value = ""; //resets form value
});

//**************************************
// a POST request to post/send a new msg
//**************************************
function sendNewMessage(newMsg) { // this message object contains attributes stated in lines 61-63
    // let postData = {
    //     "sender_id": '',
    //     "chat_id": '',
    //     "message": ''
    // };
    let postParams = {
        method: 'POST', //*GET, POST, PUT, DELETE, etc
        headers: {
            'Content-Type': 'application/json; charset=UTF-8',
            'Access-Control-Allow-Headers': "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With",
            'Access-Control-Allow-Origin': "*"
        },
        body: JSON.stringify(newMsg)
    }
    let chatId = newMsg.chatId;
    fetch(`/chats/${chatId}/message`, postParams) //`${baseUrl}/${userId}/chat`
         .then(res => res.json())
         .then(res => console.log(res))
}

//************************************************************
// click listener to create a new (group) chat
//************************************************************
let newChatBubble = document.getElementById("new-chat");
let allUserIdArr = [];
let existingChats_senderId = [];
let remainingUserIds =[];
newChatBubble.addEventListener('click', function(event) {
    // Step 1: fetch all possible users
    fetch(`${baseUrl}/`)
    //then extract their user ids
    .then(res => res.json())
    .then(resObj => {
        resObj.data.forEach(user => allUserIdArr.push(user.id));
        console.log(resObj.data);
        console.log(allUserIdArr); // this is incorrect? because all Ids are 0
    })
    // Step 2: extract users we already have chats with
    fetch(`${baseUrl}/${userId}/chats/`)
    .then(res => res.json())
    .then(resObj => {
        resObj.data.forEach(chat => existingChats_senderId.push(chat.sender_id));
        console.log(resObj.data);
        console.log(existingChats_senderId);
    })
    // compare the 2 arrays
    allUserIdArr.sort();
    existingChats_senderId.sort();
    for(var i = 0; i < allUserIdArr.length; i ++) {
        if(existingChats_senderId.indexOf(allUserIdArr[i]) > -1){
            remainingUserIds.push(allUserIdArr[i]);
        }
    }
    console.log(remainingUserIds)
    return remainingUserIds;

})

//*************************************
// create each chat bubble on the right
//*************************************
// assign creating a chatBubble to a const (can also be assigned to a function)
    // 1) create a div (chatBubble)
    // 2) add relevant classes
    // 3) create a paragraph (p) to add the message content
    // 4) append p to the div
    // 5) append the div to the relevant spot in the body (using an existing element id, chat-container in this instance)
const createChatBubble = (newMessage) => {
    let chatBubble = document.createElement('div');
    let inOut;
    if(newMessage.senderId === +userId) {
        inOut = "out";
    } else {
        inOut = "in"
    }
    chatBubble.classList.add("chat-bubble", inOut);
    let p = document.createElement('p');
    p.innerHTML = newMessage.content;
    console.log(newMessage.senderId);
    console.log(newMessage.userId); //undefined
    chatBubble.appendChild(p);
    document.getElementById('chat-container-wrapper').prepend(chatBubble);
}

//*************************************
// create each chat preview on the left
//*************************************
function createChatPreview (chatPreviewObj) {
    // create parent div
    let chatPreviewBox = document.createElement('div');
    chatPreviewBox.classList.add("individual-msg-preview-box");
    // set an attribute (that is then used to identify a specific chat for a click listener later)
    chatPreviewBox.setAttribute('data-chat_id', chatPreviewObj.chatId);
//    chatPreviewBox.setAttribute('data-sender_id', chatPreviewObj.senderId);
    chatPreviewBox.addEventListener('click', chatPreviewClick);

    // create 3 children for image, message and date
    // 1) senderPic div and image
    let chatPhoto = document.createElement('div');
    chatPhoto.classList.add("img-wrap");
    chatPhoto.setAttribute('data-chat_id', chatPreviewObj.chatId);
//    senderPic.setAttribute('data-sender_id', chatPreviewObj.senderId);
    let img = document.createElement('img');
    img.setAttribute('data-chat_id', chatPreviewObj.chatId);
//    img.setAttribute('data-sender_id', chatPreviewObj.senderId);
    img.setAttribute("src", chatPreviewObj.chatPhotoUrl);
    img.setAttribute("alt", "chat photo icon");
    chatPhoto.appendChild(img);

    // 2) message div
    let message = document.createElement('div');
    message.setAttribute('data-chat_id', chatPreviewObj.chatId);
//    message.setAttribute('data-sender_id', chatPreviewObj.senderId);
    message.classList.add("message-text-wrap");
    // add 2 paragraphs for name of sender and sender message preview
    let chatName = document.createElement('p');
    chatName.setAttribute('data-chat_id', chatPreviewObj.chatId);
//    senderName.setAttribute('data-sender_id', chatPreviewObj.senderId);
    chatName.innerHTML = chatPreviewObj.chatName; // 'chat_name' is a data element from the API
    message.appendChild(chatName);

    let lastMsgPreview = document.createElement('p');
    lastMsgPreview.setAttribute('data-chat_id', chatPreviewObj.chatId);
//    senderMsgPreview.setAttribute('data-sender_id', chatPreviewObj.senderId);
    lastMsgPreview.innerHTML = chatPreviewObj.lastMsgContent;
    message.appendChild(lastMsgPreview);

    let date = document.createElement('div');
    date.classList.add("date-wrap");
    date.setAttribute('data-chat_id', chatPreviewObj.chatId);
//    date.setAttribute('data-sender_id', chatPreviewObj.senderId);
    let msgDate = document.createElement('p');
    msgDate.setAttribute('data-chat_id', chatPreviewObj.chatId);
//    msgDate.setAttribute('data-sender_id', chatPreviewObj.senderId);
    msgDate.innerHTML = new Date(chatPreviewObj.lastMsgtimestamp).toLocaleDateString();
    date.appendChild(msgDate);

    //append all children to parent div (chatPreviewBox)
    chatPreviewBox.appendChild(chatPhoto);
    chatPreviewBox.appendChild(message);
    chatPreviewBox.appendChild(date);

    // add back to body
    let messageWrap= document.getElementById('msg-wrap');
    messageWrap.appendChild(chatPreviewBox);

}



