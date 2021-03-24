/**
 * Creates the reimbursment for the user. 
 */
window.onload = function() {
	document.getElementById("submit").addEventListener('click', addReimb);
};

async function addReimb() {
	
var formValid = form.checkValidity();
	if (formValid) {
		let xhr = new XMLHttpRequest();

		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4 && xhr.status == 200) {
				console.log(xhr.responseText);
				let rjson = JSON.parse(xhr.responseText);
			}
		}
		xhr.open("POST", "http://localhost:8080/Project1--ReimbSys/createreimb.json");
		//xhr.setRequestHeader('Content-Type', 'application/json');
		xhr.send();
	} else {
		alert("Please try again! Some fields are set to invalid values.");
	}
};
