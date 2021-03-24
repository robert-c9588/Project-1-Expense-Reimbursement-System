/**
 * 
 */
let reimbursements;
var filter = "all";
let updateButton = document.getElementById('updateButton');
let filterButton = document.getElementById('filter');
let approve = [];
let deny = [];
window.onload = function() {
	updateButton.disabled = true;
	filterButton.disabled = true;

	console.log("Window has loaded, script mhome.js is being run");
	document.getElementById('drop-all').addEventListener('click', getAll);
	document.getElementById('drop-pending').addEventListener('click', getPending);
	document.getElementById('drop-approved').addEventListener('click', getApproved);
	document.getElementById('drop-denied').addEventListener('click', getDenied);
	document.getElementById('logout').addEventListener('click',logout);
	getAllReimbAJAX();

};

function updateReimb() {
	console.log(`update ${filter}`)
	if (filter == "pending") {
		console.log('changing list')
		//need a list of all the select nodes
		let slist = document.getElementsByTagName('select');
		let body = document.getElementById('body');

		for (i = 0; i < reimbursements.length; i++) {
			for (j = 0; j < slist.length; j++) {
				if (reimbursements[i].id == slist[j].id) {
					if (slist[j].value == 1) {
						approve.push(slist[j].id);
						reimbursements[i].statusid = "APPROVED";
						body.removeChild(body.childNodes[j]);
					} else if (slist[j].value == 2) {
						deny.push(slist[j].id);
						reimbursements[i].statusid = "DENIED";
						body.removeChild(body.childNodes[j]);
					}
				}

			}
		}

		console.log(approve)
		console.log(deny)

		while (approve.length > 0) {
			approveId(approve.pop());
		}
		while (deny.length > 0) {
			denyId(deny.pop());
		}


	}
}

function getAll() {
	if (filter != "all") {
		updateButton.disabled = true;
		filterButton.disabled = true;


		filter = "all";

		console.log('buttonpushed')
		createTable(reimbursements);
	}
}
function getPending() {
	if (filter != "pending") {
		updateButton.disabled = false;
		filterButton.disabled = true;

		updateButton.addEventListener('click', updateReimb);

		filter = "pending";

		console.log('buttonpushed')
		let tempList = [];
		for (i = 0; i < reimbursements.length; i++) {
			if (reimbursements[i].statusid == "PENDING") {
				tempList.push(reimbursements[i]);
			}
		}
		createTable(tempList);
	}
}
function getApproved() {
	if (filter != "approved") {
		updateButton.disabled = true;
		filterButton.disabled = true;


		filter = "approved";

		console.log('buttonpushed')
		let tempList = [];
		for (i = 0; i < reimbursements.length; i++) {
			if (reimbursements[i].statusid == "APPROVED") {
				tempList.push(reimbursements[i]);
			}
		}
		createTable(tempList);
	}

}
function getDenied() {
	if (filter != "denied") {
		updateButton.disabled = true;
		filterButton.disabled = true;


		filter = "denied";

		console.log('buttonpushed')
		let tempList = [];
		for (i = 0; i < reimbursements.length; i++) {
			if (reimbursements[i].statusid == "DENIED") {
				tempList.push(reimbursements[i]);
			}
		}
		createTable(tempList);
	}
}

function logout() {
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
			reimbursements = userObj;
			//	createTable(reimbursements);
		}
	}

	//Step 3: create and open the connection to the server 
	xhttp.open("POST", 'http://localhost:8080/Project1--ReimbSys/logout.rsys');

	//Step 4: send the request
	xhttp.send();

	
}

function approveId(id) {
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
			reimbursements = userObj;
			//	createTable(reimbursements);
		}
	}

	//Step 3: create and open the connection to the server 
	xhttp.open("POST", 'http://localhost:8080/Project1--ReimbSys/updateapprove.json');
	xhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

	//Step 4: send the request
	let idnum = encodeURI(id);
	xhttp.send(`id=${idnum}`);

}

function denyId(id) {
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
			reimbursements = userObj;
			createTable(reimbursements);
		}
	}

	//Step 3: create and open the connection to the server 
	//Content-Type: application/x-www-form-urlencoded
	xhttp.open("POST", 'http://localhost:8080/Project1--ReimbSys/udpatedeny.json');
	xhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

	//Step 4: send the request
	let idnum = encodeURI(id);

	xhttp.send(`id=${idnum}`);

}

function getAllReimbAJAX() {
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
			reimbursements = userObj;
			createTable(reimbursements);
		}
	}

	//Step 3: create and open the connection to the server 
	xhttp.open("POST", 'http://localhost:8080/Project1--ReimbSys/getreimbs.json');

	//Step 4: send the request
	xhttp.send();

}

function createTable(list) {
	console.log(list);
	filterButton.disabled = false;

	let body = document.getElementById('body');

	while (body.hasChildNodes()) {
		console.log('removing node');
		body.removeChild(body.childNodes[0]);
	}

	for (i = 0; i < list.length; i++) {
		let id = list[i].id;
		let type = list[i].type;
		let user = list[i].author.username;
		let amount = list[i].amount;
		let submit = list[i].submittedTs;
		let status = list[i].statusid;
		let descrp = list[i].description;

		let row = document.createElement("tr");

		body.appendChild(row);

		let idtd = document.createElement("td");
		let typetd = document.createElement("td");
		let usertd = document.createElement("td");
		let amounttd = document.createElement("td");
		let submittd = document.createElement("td");
		let statustd = document.createElement("td");
		let descrptd = document.createElement("td");

		let textfield = document.createElement("textarea");
		textfield.className = "form-control"
		textfield.disabled = true;
		descrptd.appendChild(textfield);

		let select = document.createElement('select');
		select.className = "form-select form-select sm";
		select.id = `${id}`;

		let optiond = document.createElement('option');
		let option1 = document.createElement('option');
		let option2 = document.createElement('option');

		optiond.value = "";
		option1.value = "1";
		option2.value = "2";
		optiond.innerHTML = "CHOOSE";
		option1.innerHTML = "APPROVE";
		option2.innerHTML = "DENY";

		select.appendChild(optiond);
		select.appendChild(option1);
		select.appendChild(option2);


		idtd.innerHTML = id;
		typetd.innerHTML = type;
		usertd.innerHTML = user;
		amounttd.innerHTML = amount;
		submittd.innerHTML = `${submit.monthValue}/${submit.dayOfMonth}/${submit.year}`;
		statustd.innerHTML = status;
		textfield.innerHTML = descrp;

		row.appendChild(idtd);
		row.appendChild(typetd);
		row.appendChild(usertd);
		row.appendChild(amounttd);
		row.appendChild(submittd);
		row.appendChild(statustd);
		row.appendChild(descrptd);
		console.log('helllooooooo');
		console.log(`filter is ${filter}`);
		if (filter == 'pending') {
			console.log('adding select');
			row.appendChild(select);
		}
		
	}

}


