let messageForm = document.getElementById("login-form");

messageForm.addEventListener('submit', function (event) {
    event.preventDefault();

    let userName = document.getElementById('username').value;
    let passWord = document.getElementById('password').value;
    console.log(userName);
    console.log(passWord);

})
