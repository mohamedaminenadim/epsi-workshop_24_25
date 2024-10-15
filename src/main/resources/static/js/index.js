const getStartedBtn = document.getElementById("get-started-btn");
const loginForm = document.getElementById("login-form");
const backButton = document.getElementById("back-button");

console.log(getStartedBtn)
console.log(loginForm)
console.log(backButton)

// Initially hide the form
loginForm.style.display = 'none';

// Function to show the form with fade-in effect
function showForm() {
    loginForm.style.display = 'flex';
    setTimeout(() => {
        loginForm.style.opacity = 1;
    }, 10);
}

// Function to hide the form with fade-out effect
function hideForm() {
    loginForm.style.opacity = 0;
    setTimeout(() => {
        loginForm.style.display = 'none';
    }, 750); // Duration matches the transition time
}

// Event listeners for button clicks
getStartedBtn.addEventListener('click', showForm);
backButton.addEventListener('click', hideForm);
