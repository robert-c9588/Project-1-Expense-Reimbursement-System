/**
 * This script will read in the user's information and display their current reimbursments.
 * Also it will take the form from browser and create new reimbursement for them. 
 */

//window.onload = function(){
	//document.getElementById('swSubmit').addEventListener('click',getSW);
	
//};

async function getUser() {
	fetch('http://localhost:8080/Project1--ReimbSys/resources/html/index.html').then(
		function(response) {
			console.log(response.json());
			return response.json();
		}, function() {
			console.log('Panic!!!!');
		}
	).then(function(userJSON){
		console.log(userJSON);
	})
};

function getReimbursements() {
	//let swId =  document.getElementById('swId').value;
	
	fetch('http://localhost:8080/Project1--ReimbSys/resources/html/index.html').then(
		function(response) {
			console.log(response.json());
			return response.json();
		}, function() {
			console.log('Panic!!!!!')
		}
	).then(function(myJSON){
		
		console.log(myJSON);
		//swDOMManipulation(myJSON);
	})
};


//function swDOMManipulation(swJSON) {
	//document.getElementById('swName').innerText = `Name: ${swJSON.name}`;
	//document.getElementById('swBirthYear').innerText = `Birth Year: ${swJSON.birth_year}`;
//};

/*
let hello = async function() {
	return 'Hello';
};

hello().then((value)=>console.log(value));

async function aa() {
	
	return mess = await fetch(`https://swapi.dev/api/people/1/`);
}

aa().then((response)=>console.log(response));/**
 * 
 */