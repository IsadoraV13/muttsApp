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
    let chatId = event.target.dataset.chat_id; // assign 'chatId' to the data attribute 'chat_id' that we assigned (in createMsgPreview)
    // to the various elements of the preview box (these are the targets of the click event) - each of which holds the corresponding
    // chat ids for each preview
    fetch(`/chats/${chatId}`)
    .then(res => res.json())
    .then(resObj => {
        // to only get the chat messages relevant to this chat in the chatBubble wrapper, reset the innerHTML to null every time
        document.getElementById('chat-container-wrapper').innerHTML = "";
        document.getElementById("send-message").dataset.chat_id = chatId;
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
    let userId = document.getElementById('user_id').value;
    let newMsgObj = { // our API documentation says we need a senderId, a chatId, and a message content (& timestamp??)
        senderId: parseInt(userId),
        chatId: event.target.dataset.chat_id,
        content: newMessage
    }
    createChatBubble(newMsgObj);
    sendNewMessage(newMsgObj);
    document.getElementById('new-message').value = ""; //resets form value
});

//**************************************
// a POST request to post/send a new msg
//**************************************
function sendNewMessage(newMsg) { // this message object contains attributes stated in lines 59-61
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
        body: JSON.stringify(newMsg) // converts it to String to post
    }
    let chatId = newMsg.chatId;
    fetch(`/chats/${chatId}/message`, postParams)
         .then(res => res.json())
}


//************************************************************
// click listener to create a new chat
//************************************************************
let newChatBtn = document.getElementById("new-chat-btn");
let newChatModalBody = document.getElementById("new-chat-modal-body");
newChatModalBody.innerHTML = "now loading...";
newChatBtn.addEventListener('click', findUsersToChat)


//************************************************************
// function to determine who we can start a new duo chat with >>improvement: start new group chat
//************************************************************
function findUsersToChat() {
    let remainingUsers =[];
    const p1 = fetch(`${baseUrl}`); //fetch all users
    const p2 = fetch(`${baseUrl}/${userId}/duoUsers`); //fetch all users we already have duo chats with

    Promise.all([p1, p2])
        .then(results => Promise.all(results.map(res => res.json())))
        .then(resp => {
            let allUsers = resp[0].data;
            let existingDuoUserIds = resp[1].data

            // compare the 2 arrays
            for(var i = 0; i < allUsers.length; i ++) {
                if(existingDuoUserIds.indexOf(allUsers[i].userId) === -1){
                    remainingUsers.push(allUsers[i]);
                }
            }
            console.log(remainingUsers)
            populateNewChatDropDown(remainingUsers)
        })
}


//************************************************************
// populate list of users we can start a new chat with
//************************************************************
function populateNewChatDropDown(remainingUsers) {
// we create a form element because we want to add a Submit event listener and it's harder to do with a number of Strings??
    let form = document.createElement('form');
    form.id = `new-chat-form`;

    let formString = ``;
    formString += `<input id="new-chat-user" type="text" list="users-list" class="form-control">`;
    formString += `<datalist id="users-list">`
//    formString += `select name="user"`
    remainingUsers.forEach(user => { //below data-value is what gets passed, but the users only see firstName, lastName
        formString += `<option data-value="${user.userId}" value="${user.firstName} ${user.lastName}"></option>`
    })
    formString += `</datalist>`
    formString += `<input type="submit" class="btn btn-success">`
    form.innerHTML = formString;
    form.addEventListener('submit', newChatSubmit)
    newChatModalBody.innerHTML = "";
    newChatModalBody.appendChild(form);
}

function newChatSubmit(e){
    e.preventDefault()
    let options = document.getElementById('users-list').options;
    console.log(document.getElementById('users-list').options)
    console.log(e.target.elements)
    let val = e.target.elements["new-chat-user"].value
    console.log(val)
    let chatName;
    let duoUserId;
    Array.from(options).forEach(option => {
        if (option.value === val) { //what does this line do?
            chatName = option.getAttribute('value');
            duoUserId = option.getAttribute('data-value');
        }
    })
    console.log(chatName)
    console.log(userId)
    console.log(duoUserId)

    let postData = {
        "chatName": chatName,
        "chatPhotoUrl": 'https://cdn.pixabay.com/photo/2019/08/11/18/48/icon-4399681_960_720.png'
    };

    let postParams = {
            method: 'POST', //*GET, POST, PUT, DELETE, etc
            headers: {
                'Content-Type': 'application/json; charset=UTF-8',
                'Access-Control-Allow-Headers': "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With",
                'Access-Control-Allow-Origin': "*"
            },
            body: JSON.stringify(postData)
        }

        fetch(`/chats/${userId}/${duoUserId}`, postParams)
             .then(res => res.json())
}



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
    let userId = document.getElementById('user_id').value;
    let chatBubble = document.createElement('div');
    let inOut;
    if(newMessage.senderId === parseInt(userId)) {
        inOut = "out";
    } else {
        inOut = "in"
    }
    chatBubble.classList.add("chat-bubble", inOut);
    let p = document.createElement('p');
    p.innerHTML = newMessage.content;
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
    chatPreviewBox.addEventListener('click', chatPreviewClick);

    // create 3 children for image, message and date
    // 1) senderPic div and image
    let chatPhoto = document.createElement('div');
    chatPhoto.classList.add("img-wrap");
    chatPhoto.setAttribute('data-chat_id', chatPreviewObj.chatId);
    let img = document.createElement('img');
    img.setAttribute('data-chat_id', chatPreviewObj.chatId);
    img.setAttribute("src", chatPreviewObj.chatPhotoUrl);
    img.setAttribute("alt", "chat photo icon");
    chatPhoto.appendChild(img);

    // 2) message div
    let message = document.createElement('div');
    message.setAttribute('data-chat_id', chatPreviewObj.chatId);
    message.setAttribute("class", "message-text-wrap");
    // add 2 paragraphs for name of sender and sender message preview
    let chatName = document.createElement('p');
    chatName.setAttribute("class", "chat-name");
    chatName.innerHTML = chatPreviewObj.chatName; // 'chatName' is a data element from the API
    message.appendChild(chatName);

    let lastMsgPreview = document.createElement('p');
    lastMsgPreview.setAttribute('data-chat_id', chatPreviewObj.chatId);
    lastMsgPreview.innerHTML = chatPreviewObj.lastMsgContent;
    message.appendChild(lastMsgPreview);

    let date = document.createElement('div');
    date.classList.add("date-wrap");
    date.setAttribute('data-chat_id', chatPreviewObj.chatId);
    let msgDate = document.createElement('p');
    msgDate.setAttribute('data-chat_id', chatPreviewObj.chatId);
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



