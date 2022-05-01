//password validation 
  
  
//validate inputs for password
var myInput = document.getElementById("password"); //password input field
var letter = document.getElementById("letter");
var capital = document.getElementById("capital");
var number = document.getElementById("number");
var length = document.getElementById("length");

// When the user clicks on the password field, show the message box
myInput.onfocus = function() {
  document.getElementById("messagePassword").style.display = "block";
}

// When the user clicks outside of the password field, hide the message box
myInput.onblur = function() {
  document.getElementById("messagePassword").style.display = "none";
}

// When the user starts to type something inside the password field
myInput.onkeyup = function() {
  //booleans to track password input validation
  let atLeastOneLowerCase = false;
  let atLeastOneUpperCase = false;
  let atLeastOneNumber = false;
  let atLeastLengthOf8 = false;

  // Validate lowercase letters
  var lowerCaseLetters = /[a-z]/g;
  if(myInput.value.match(lowerCaseLetters)) {  
    letter.classList.remove("text-danger");
    letter.classList.add("text-success");
    atLeastOneLowerCase = true;
  } else {
    letter.classList.remove("text-success");
    letter.classList.add("text-danger");
    atLeastOneLowerCase = false;
  }
  
  // Validate capital letters
  var upperCaseLetters = /[A-Z]/g;
  if(myInput.value.match(upperCaseLetters)) {  
    capital.classList.remove("text-danger");
    capital.classList.add("text-success");
    atLeastOneUpperCase=true;
  } else {
    capital.classList.remove("text-success");
    capital.classList.add("text-danger");
    atLeastOneUpperCase=false;
  }

  // Validate numbers
  var numbers = /[0-9]/g;
  if(myInput.value.match(numbers)) {  
    number.classList.remove("text-danger");
    number.classList.add("text-success");
    atLeastOneNumber=true;
  } else {
    number.classList.remove("text-success");
    number.classList.add("text-danger");
    atLeastOneNumber=false;
  }
  
  // Validate length
  if(myInput.value.length >= 8) {
    length.classList.remove("text-danger");
    length.classList.add("text-success");
    atLeastLengthOf8=true;
  } else {
    length.classList.remove("text-success");
    length.classList.add("text-danger");
    atLeastLengthOf8=false;
  }

  //if all inputs are correct, we enable the submit button
  if(atLeastOneLowerCase && atLeastOneUpperCase && atLeastOneNumber && atLeastLengthOf8){
    //submitButton
    document.getElementById("submitRegistration").disabled = false;
  }else{
    document.getElementById("submitRegistration").disabled = true;
  }
}


//email validation

/**
 * Assuming there are no dots in the personal info part of the email
 * */
function validateEmail(email) {
    if (email == null || email == "") {
      return false;
    }
  
    let atSymbolPos = email.indexOf("@");
    if (atSymbolPos < 1) {
      return false;
    }
  
    let dotPos = email.indexOf(".");
    if (dotPos <= atSymbolPos + 2) {
      return false;
    }
  
    if (dotPos === email.length - 1) {
      return false;
    }
  
    return true;
}
  
function validateEmailWithRegex(email) {
    let emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  
    return !!email && typeof email === "string" && email.match(emailRegex);
}
  
function checkInputEmail(email) {
    let isValidEmail = validateEmail(email);
    if (!isValidEmail) {
      document.getElementById("emailHelp").innerText = "Invalid email!";
      return false;
    }
    document.getElementById("emailHelp").remove();
    return true;
}
  
function escapeHtml(unsafe) {
    return unsafe
      .replace(/&/g, "&amp;")
      .replace(/</g, "&lt;")
      .replace(/>/g, "&gt;")
      .replace(/"/g, "&quot;")
      .replace(/'/g, "&#039;");
}
  

/**
 * 
 * this function is called when user submits the form 
 * since password input was already validated, we just validate the email input
 */

function validateFormInputs() {
    let email = document.getElementById("email").value;
  
    // input sanitization
    email = email.trim();
    email = escapeHtml(email);

    let isEmailValid = checkInputEmail(email);
  
    return (isEmailValid);
}