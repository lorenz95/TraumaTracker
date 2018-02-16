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

// ---- data horizontal ----
var dataY;
var dataLabelY;

var dataX1;
var datasetsData1;

var dataX2;
var datasetsData2;

var dataX3;
var datasetsData3;

var labelDataSets3 = "seconds"; // label aggiunta x mancanza dati label

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

function addHorizontalData() {
	var newDataset;
	var aggiunto = false;
	
	if (datasetsData2 == null) {
		datasetsData2 = dataX2;
		aggiunto = true;
		
		  newDataset = {
				  	label: datasetsData1[0],
				  	backgroundColor: "#6d9eeb",
				  	borderColor: "white",
	              data: datasetsData2
	          };
		  myChart.data.datasets.push(newDataset);
		  myChart.update();
	}
	
	if (datasetsData3 == null && !aggiunto) {
		datasetsData3 = dataX3;
		aggiunto = true;
		
		  newDataset = {
				  	label: labelDataSets3,
				  	backgroundColor: "blue",
				  	borderColor: "white",
	              data: datasetsData3
	          };
		  myChart.data.datasets.push(newDataset);
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

function remHorizontalData() {
	var cancellato = false;
	
	if (datasetsData2 != null && datasetsData3 == null && !cancellato) {
		datasetsData2 = null;
		cancellato = true;
		
		myChart.data.datasets.pop();
		myChart.update();
	}
	
	if (datasetsData3 != null && !cancellato) {
		datasetsData3 = null;
		cancellato = true;
		
		myChart.data.datasets.pop();
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

function getHorizontalCanvas(response){	
	dataY = response.y;
	dataLabelY = dataY;

	dataX1 = response.x1;
	datasetsData1 = dataX1;

	dataX2 = response.x2;
	datasetsData2 = dataX2;
	
	dataX3 = response.x3;
	
	var ctx = document.getElementById("statCanvas").getContext('2d');
	return new Chart(ctx, {
		    type: 'horizontalBar',
		    data: {
		        labels: dataLabelY,
		        datasets: [{
		            label: datasetsData1[0],
		            data: datasetsData2,			            
		            backgroundColor: '#6d9eeb', // 'rgba(0, 0, 255, 1)',
		            borderColor: '#6d9eeb', //'rgba(0, 0, 255, 1)',
		            borderWidth: 1
		        }]
		    },
		    
		    options: {
		    	responsive: true,
		        scales: {
		            xAxes: [{
		                ticks: {
		                    beginAtZero:true,
		                    stepSize: 50
		                }
		            }]
		        }
		    }
		});
}

function getPieCanvas(response) {
	var valuesKey = response.keyArray;
  	var valuesInt = response.intArray;
  	
  	var colorArray = ["blue", "lime", "yellow", "red", "pink"];
  	var colors = [];
  	for (var i = 0; i < valuesKey.length; i++) {
  		colors[i] = colorArray[i % colorArray.length];
  	}
  	
  	var ctx = document.getElementById("statCanvas").getContext('2d');
	
  	return new Chart(ctx, {
		    type: 'pie',
		    data: {
		    	    datasets: [{
		    	    	data: valuesInt,
		    	    	backgroundColor: colors,
		                borderColor: "black",
		                borderWidth: 2
		    	    }],
		    	    labels: valuesKey
		    	},
		    options: {
		    	responsive: true
		    }
		});
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

function initTypeDrug(response) {
	myChart = getHorizontalCanvas(response);
}

function initPieCanvas(response) {
	myChart = getPieCanvas(response);
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