/* 
// ------------------------------------------------
// STRUTTURE X GRAFICI
// ------------------------------------------------
*/
var myChart;

var dataX;
var dataY;
var media;

var dataLabel;
var dataValue;
var dataAvg;

var lastIndex;
var firstIndex;

function addValueData() {	
  dataLabel.push(dataX[lastIndex]);
  dataValue.push(dataY[lastIndex]);
}
function remValueData() {
  dataLabel.pop();
  dataValue.pop();
}
function sliceValueData() {
  dataLabel.splice(0, 1);
  dataValue.splice(0, 1);
}
function unshiftValueData() {
	var firstLabel = dataX[firstIndex];
	var firstData = dataY[firstIndex];
  if (firstLabel != null && firstData != null) {
	 dataLabel.unshift(firstLabel);
	 dataValue.unshift(firstData);
  }
}
function numElemFunct() {
	var numElem = dataValue.length;
	var numAdd = (Math.floor((numElem / 10)) + 1);
	return numAdd;
}

function forChartData() {
	if (dataY[lastIndex] != null) {
		var numAdd = numElemFunct();
						  
		for (var i = 0; i < numAdd && dataY[lastIndex] != null; i++) {
			addValueData();
			sliceValueData();
									  
			firstIndex++;
			lastIndex++;
		}
					   
		myChart.update();
	}
}

function backChartData() {
	if (firstIndex > 0) {
		var numCanc = numElemFunct();
			  
		for (var i = 0; i < numCanc && lastIndex > 0; i++) {
			remValueData();
			unshiftValueData();
					  
			lastIndex--;
			firstIndex--;
		}
			  			  
		myChart.update();
	}
}

function addChartData() {
	if (dataY[lastIndex] != null) {
		var numAdd = numElemFunct();
		
		for (var i = 0; i < numAdd && dataY[lastIndex] != null; i++) {
			addValueData();
			lastIndex++;  
		}
		
		myChart.update();
	}
}

function remChartData() {
	if (lastIndex > 0) {
		var numCanc = numElemFunct();
		
		for (var i = 0; i < numCanc && lastIndex > 0; i++) {
			remValueData();
			lastIndex--;
		}
	  
		myChart.update();
	}
}

function loadChartData(response) {
	dataX = response.x;
	dataY = response.y;
	media = response.media;
	  
	dataLabel = [];
	dataValue = [];
	dataAvg = [];
	  
	firstIndex = 0;
	lastIndex = dataY.length;
	  
	// load data
	  for (var i = 0; i < lastIndex; i++) {
		  dataLabel[i] = dataX[i];
		  dataValue[i] = dataY[i];
		  dataAvg[i] = media;
	  }
} 

function getChartCanvas(dataTitle) {
	var dim = (media / 10);
	
	if (dim <= 0) dim++;
	
	var ctx = document.getElementById("statCanvas").getContext('2d');
	  var chartData = {
			  labels: dataLabel,
			  datasets: [
				  {
					  label: 'Media',
					  type: 'line',
					  borderColor: "#c45850",
					  data: dataAvg,
					  borderWidth: 2,
					  fill: false
				  },
				  {
					 label: dataTitle,
					 type: 'bar',
		  			 backgroundColor: '#6d9eeb',
		  			 data: dataValue,
		  			 borderWidth: 1
				  }
			  ]
	  };
	  
	  return new Chart(ctx, {
		  type: 'bar',
		  data: chartData,		  
	  	  options: {
		      scales: {
		    	  yAxes: [{
		    		  ticks: {
		    			  beginAtZero:true,
		    			  stepSize: dim
		                }
		          }]
		      },
		      tooltips: {
              mode: 'index',
              intersect: true
          }
		  }
	  });
}

function initChartCanvas(response, label) {	
	loadChartData(response);
	myChart = getChartCanvas(label);
}

var commonPATH = '/gt2/traumatracker/api/stats/';

var numTraumiMonth = commonPATH+'numTraumiMonth';
var numTraumiWeek = commonPATH+'numTraumiWeek';
var numTraumiDay = commonPATH+'numTraumaDay';
var numAvarageTraumaTime = commonPATH+'numAvarageTraumaTime';
var numAvarageTimeRoom = commonPATH+'numAvarageTimeRoom';
var numFinalDestination = commonPATH+'numFinalDestination';
var consumptionTypeDrug = commonPATH+'consumptionTypeDrug';
var numMaxProcedure = commonPATH+'numMaxProcedure';

// ------------------------------------------------