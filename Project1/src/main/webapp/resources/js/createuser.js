/**
 * Sends information to CreateUserControl if returns true from register then it will send user back to login
 * else it will inform user to try to register with a new username.
 */
window.onload = function() {
	document.getElementById("submit").addEventListener('click', getRegistered);
};


//AJAX request to get registered
async function getRegistered() {
	var formValid = form.checkValidity();

	if (formValid) {

		let xhr = new XMLHttpRequest();

		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4 && xhr.status == 200) {
				console.log(xhr.responseText);
				let rjson = JSON.parse(xhr.responseText);
				DOMManipulation(rjson);
			}
		}
		xhr.open("POST", "http://localhost:8080/Project1--ReimbSys/createuser.json");
		//xhr.setRequestHeader('Content-Type', 'application/json');
		xhr.send();
	} else {
		alert("Please try again! Some fields are set to invalid values.");
	}
};

function DOMManipulation(rJSON) {

	if (rJSON) {
		alert('REGISTERED!!');

	} else {
		alert('NOT REGISTERED');
	}
}
