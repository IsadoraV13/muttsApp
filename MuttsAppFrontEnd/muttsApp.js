let baseUrl = 'http://demo.codingnomads.co:8082/muttsapp';
//right now we do not have a login function, therefore we will hardcode which userId is accessing the app
let userId = 3;
//********************************
// fetch info for left sidebar
//********************************
// this will run asynchronously as soon as the page is loaded 
// it takes time to convert the readable stream to Json
// in the meantime, other things like submitting a new message can happen
(function getChatPreviews() {   
    fetch(`${baseUrl}/users/${userId}/chats`)
        .then(res => res.json())  // fetching the url returns a response, which is then converted to json
        .then(resObj => { // that json response has a body object and =>
            resObj.data.forEach(chatPreviewObj => { // this object has a data property and under that, an array of 'chatPreview' objects 
                // for each chatPreview obj of the chat array, create a preview box with the chatPreview data using the createChatPreview function (further down) that uses the img, message and date
                createChatPreview(chatPreviewObj);
            })
        })
})();

//************************************************************
// click listener that listens for a click on each chatPreview
//************************************************************
// and then creates each chatBubble for the chat_id clicked
function chatPreviewClick(event) {
    console.log()
    let chatId = event.target.dataset.chat_id;
    let senderId = event.target.dataset.sender_id;
    fetch(`${baseUrl}/users/${userId}/chats/` + senderId)
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
    
    // change chat Pic to match chat clicked on
    document.querySelector("#recipient-image > img").setAttribute("src", event.target.closest('.individual-msg-preview-box').querySelector('img').src);
}

//************************************************************
// submit listener that waits for a user to enter a newMessage
//************************************************************
//prevents the default behaviour and prevents the page re-loading 20.27
let messageForm = document.getElementById("send-message");

messageForm.addEventListener('submit', function (event) {
    console.log(event.target)
    event.preventDefault();
    let newMessage = document.getElementById('new-message').value;
    let newMsgObj = { // our API documentation says we need a sender_id, a chat_id, and a message 
        sender_id: userId,
        chat_id: event.target.dataset.chat_id, //how do we get the chat-id?
        message: newMessage
    }
    createChatBubble(newMsgObj);
    sendNewMessage(newMsgObj);
    document.getElementById('new-message').value = "";
});

//**************************************
// a POST request to post/send a new msg
//**************************************
function sendNewMessage(newMsg) {
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
    fetch(`${baseUrl}/users/${userId}/chat`, postParams)
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
    fetch(`${baseUrl}/users/`)
    //then extract their user ids
    .then(res => res.json())
    .then(resObj => {
        resObj.data.forEach(user => allUserIdArr.push(user.id));
        console.log(resObj.data);
        console.log(allUserIdArr); // this is incorrect? because all Ids are 0    
    }) 
    // Step 2: extract users we already have chats with
    fetch(`${baseUrl}/users/${userId}/chats/`)
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
    if(newMessage.sender_id === userId) {
        inOut = "out";
    } else {
        inOut = "in"
    }
    chatBubble.classList.add("chat-bubble", inOut);
    let p = document.createElement('p');
    p.innerHTML = newMessage.message;
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
    chatPreviewBox.setAttribute('data-chat_id', chatPreviewObj.chat_id);
    chatPreviewBox.setAttribute('data-sender_id', chatPreviewObj.sender_id);
    chatPreviewBox.addEventListener('click', chatPreviewClick);

    // create 3 children for image, message and date
    // 1) senderPic div and image
    let senderPic = document.createElement('div');
    senderPic.classList.add("img-wrap");
    senderPic.setAttribute('data-chat_id', chatPreviewObj.chat_id);
    senderPic.setAttribute('data-sender_id', chatPreviewObj.sender_id);
    let img = document.createElement('img');
    img.setAttribute('data-chat_id', chatPreviewObj.chat_id);
    img.setAttribute('data-sender_id', chatPreviewObj.sender_id);
    img.setAttribute("src", chatPreviewObj.photo_url);
    img.setAttribute("alt", "user's profile pic icon");
    senderPic.appendChild(img);

    // 2) message div
    let message = document.createElement('div');
    message.setAttribute('data-chat_id', chatPreviewObj.chat_id);
    message.setAttribute('data-sender_id', chatPreviewObj.sender_id);
    message.classList.add("message-text-wrap");
    // add 2 paragraphs for name of sender and sender message preview
    let senderName = document.createElement('p');
    senderName.setAttribute('data-chat_id', chatPreviewObj.chat_id);
    senderName.setAttribute('data-sender_id', chatPreviewObj.sender_id);
    senderName.innerHTML = chatPreviewObj.chat_name; // 'chat_name' is a data element from the API
    message.appendChild(senderName);

    let senderMsgPreview = document.createElement('p');
    senderMsgPreview.setAttribute('data-chat_id', chatPreviewObj.chat_id);
    senderMsgPreview.setAttribute('data-sender_id', chatPreviewObj.sender_id);
    senderMsgPreview.innerHTML = chatPreviewObj.last_message;
    message.appendChild(senderMsgPreview);

    let date = document.createElement('div');
    date.classList.add("date-wrap"); 
    date.setAttribute('data-chat_id', chatPreviewObj.chat_id);
    date.setAttribute('data-sender_id', chatPreviewObj.sender_id);
    let msgDate = document.createElement('p');
    msgDate.setAttribute('data-chat_id', chatPreviewObj.chat_id);
    msgDate.setAttribute('data-sender_id', chatPreviewObj.sender_id);
    msgDate.innerHTML = new Date(chatPreviewObj.date_sent).toLocaleDateString();
    date.appendChild(msgDate);

    //append all children to parent div (chatPreviewBox)
    chatPreviewBox.appendChild(senderPic);
    chatPreviewBox.appendChild(message);
    chatPreviewBox.appendChild(date);

    // add back to body
    let messageWrap= document.getElementById('msg-wrap');
    messageWrap.appendChild(chatPreviewBox);

}



