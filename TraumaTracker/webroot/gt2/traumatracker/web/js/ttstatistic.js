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

function addValueData() { // aggiunta del prossimo elemento	
	var lastInd = (lastIndex + 1);
	dataLabel.push(dataX[lastInd]);
	dataValue.push(dataY[lastInd]);
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
	var firstInd = (firstIndex - 1);
	dataLabel.unshift(dataX[firstInd]);
	dataValue.unshift(dataY[firstInd]);
}
function numElemFunct() {
	var numElem = dataValue.length;
	var numElemTo = (Math.floor((numElem / 10)) + 1);
	return numElemTo;
}

function forChartData() {
	if (dataY[lastIndex + 1] != null) { // il prossimo elemento != null
		var numTo = numElemFunct();
		
		for (var i = 0; i < numTo && dataY[lastIndex + 1] != null; i++) {
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
		var numTo = numElemFunct();
		
		for (var i = 0; i < numTo && firstIndex > 0; i++) {
			remValueData();
			unshiftValueData();
					  
			lastIndex--;
			firstIndex--;
		}
		
		myChart.update();
	}
}

function addDatasets(labelValue, strNum, backColor, borderColor) {
	var valueDatasets;
	
	switch (strNum) {
		case 2:
			datasetsData2 = dataX2;
			valueDatasets = datasetsData2;
			break;
		case 3:
			datasetsData3 = dataX3;
			valueDatasets = datasetsData3;
			break;
	}
	
	  newDataset = {
			  	label: labelValue,
			  	backgroundColor: backColor,
			  	borderColor: borderColor,
              data: valueDatasets
          };
	  myChart.data.datasets.push(newDataset);
	  myChart.update();
	  
	  return true;
}

function addHorizontalData() {
	var newDataset;
	var aggiunto = false;
	
	if (datasetsData2 == null) {
		aggiunto = addDatasets(datasetsData1[0], 2, "#6d9eeb", "white");
	}
	if (datasetsData3 == null && !aggiunto) {
		aggiunto = addDatasets(labelDataSets3, 3, "blue", "white");
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

function addChartData() {	
	if (dataY[lastIndex+1] != null) { // il prossimo elemento != null
		var numAdd = numElemFunct();
		
		for (var i = 0; i < numAdd && dataY[lastIndex+1] != null; i++) {
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
	  
	  lastIndex--; //ultimo indice = dimensione elementi - 1 (conta lo 0)
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