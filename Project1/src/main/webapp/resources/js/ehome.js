/**
 * This script will read in the user's information and display their current reimbursments.
 * Also it will take the form from browser and create new reimbursement for them. 
 */

window.onload = function() {
	console.log("Window has loaded, script ehome.js is being run");
	//document.getElementById('swSubmit').addEventListener('click',getSW);
	//getUser();
	getUserAJAX();
	getUserReimbAJAX();

};



async function getUser() {
	fetch('http://localhost:8080/Project1--ReimbSys/user.json').then(
		function(response) {
			console.log(response);

			return response.json();//response consumed
		}, function() { //rejection behavior
			console.log('Panic!!!!');
		}
	).then(function(userJSON) {
		console.log(userJSON);
		userDOMManipulation(userJSON);
	})
};

function getUserAJAX() {
	//step 1: create the xmlhttprequest object
	let xhttp = new XMLHttpRequest();

	//Step 2: create a callback function for the ready state changes.
	xhttp.onreadystatechange = function() {
		console.log("the ready state changed");
		if (xhttp.readyState == 4 && xhttp.status == 200) {
			let rtext = xhttp.responseText;
			//rtext = rtext.slice(0, rtext.length - 2);
			let userObj = JSON.parse(rtext);
			console.log(userObj);
			userDOMManipulation(userObj);
		}
	}

	//Step 3: create and open the connection to the server 
	xhttp.open("GET", 'http://localhost:8080/Project1--ReimbSys/user.json');

	//Step 4: send the request
	xhttp.send();

}

async function getUserReimbAJAX() {
	let xhr = new XMLHttpRequest();

	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4 && xhr.status == 200) {
			let rtext = xhr.responseText;
			let userReimbObj = JSON.parse(rtext);
			if (userReimbObj.length > 0) {
				uReimbDOMManipulation(userReimbObj);
			} else {
				uReimbDOMManipulation(null);
			}
		}
	}

	xhr.open("GET", 'http://localhost:8080/Project1--ReimbSys/ureimb.json');
	xhr.send();
}

function userDOMManipulation(userJSON) {

	let welcometag = document.getElementById('welcomeh1');
	welcometag.innerHTML = `Welcome, ${userJSON.username}`
}

function uReimbDOMManipulation(urJSON) {
	console.log(urJSON);
	let table = document.getElementById('table');
	if (urJSON) {
		for (i = 0; i < urJSON.length; i++) {
			let id = urJSON[i].id;
			let type = urJSON[i].type;
			let amount = urJSON[i].amount;
			let submit = urJSON[i].submittedTs;
			let status = urJSON[i].statusid;

			let row = document.createElement("tr");
			
			table.appendChild(row);

			let idtd = document.createElement("td");
			let typetd = document.createElement("td");
			let amounttd = document.createElement("td");
			let submittd = document.createElement("td");
			let statustd = document.createElement("td");


			idtd.innerHTML = id;
			typetd.innerHTML =  type;
			amounttd.innerHTML = amount;
			submittd.innerHTML = `${submit.monthValue}/${submit.dayOfMonth}/${submit.year}`;
			statustd.innerHTML = status;
			
			row.appendChild(idtd);
			row.appendChild(typetd);
			row.appendChild(amounttd);
			row.appendChild(submittd);
			row.appendChild(statustd);
			
			if (status == "APPROVED") {
				row.className = "table-success";
				idtd.className = "table-success";
				typetd.className = "table-success";
				amounttd.className = "table-success";
				submittd.className =  "table-success";
				statustd.className =  "table-success";
			} else if (status == "DENIED") {
				row.className = "table-danger";
				idtd.className = "table-danger";
				typetd.className = "table-danger";
				amounttd.className = "table-danger";
				submittd.className =  "table-danger";
				statustd.className =  "table-danger";
			} else {
				row.className = "table-info";
				idtd.className = "table-info";
				typetd.className = "table-info";
				amounttd.className = "table-info";
				submittd.className =  "table-info";
				statustd.className =  "table-info";
			}

		}
	} else {
		document.getElementById('tableh1').innerHTML = "You currently have no reimbursements. Please click the link above to start";
	}
}
