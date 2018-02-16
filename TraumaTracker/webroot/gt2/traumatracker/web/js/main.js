var app = angular.module('ttApp', ['ttService','ui-notification'],
    function(NotificationProvider) {
        NotificationProvider.setOptions({
            delay: 10000,
            startTop: 20,
            startRight: 10,
            verticalSpacing: 20,
            horizontalSpacing: 20,
            positionX: 'right',
            positionY: 'top'
        });
    });
app
  .filter('cut',function(){
    return function(input, len){
      return input.length < len ? input : input.substring(0,len)+"...";
    };
  })
  .filter('filterDate',function(){
    /* YYYYMMDD" */
    return function(timestamp){
      if (timestamp != undefined){
        return timestamp.substring(0,4)+"-"+timestamp.substring(4,6)+"-"+timestamp.substring(6,8);
      } else {
        return "undefined";
      }
    };
  })
  .filter('filterTime',function(){
    /* HH:MM:SS" */
    return function(timestamp){
      if (timestamp != undefined){
        return timestamp.substring(0,5);
      } else {
        return "undefined";
      }
    };
  })
  .filter('filterTeamMembers',function(){
	  /* array of strings  */
    return function(team){
      if (team != undefined){
	      if (team.length > 0){
		      list = "";
		      for (i = 0; i < team.length - 1; i++) {
		          list += team[i]+", ";
		      }    	
		      list += team[team.length-1];
		      return list;
	      } else {
	    	  return "";
	      }
      } else {
    	  return "";
      }
    }
  })
  .filter('filterOperatorName',function(){
    return function(report){
    	if (report != undefined){
	      if (report.startOperatorDescription != undefined && report.startOperatorDescription != ""){
		      return report.startOperatorDescription;
		  } else {
	    	  return report.startOperatorId;
	      }
    	} else {
    		return "";
    	}
    };
  })
  .filter('filterEventContent',function(){
    return function(eventContent,eventType){
      var msg = "";
      var isok = function(value){
    	  return value != undefined && value != "";
      }
      switch(eventType){
        case "drug": 
          if (eventContent.administrationType == "one-shot"){
        	  return "Farmaco: "+eventContent.drugDescription+" "+eventContent.qty+eventContent.unit;    
          } else {
        	  if (eventContent.event == "start"){
        		  return "Inizio Farmaco ad infusione: "+eventContent.drugDescription+" "+eventContent.qty+eventContent.unit;    
        	  } else if (eventContent.event == "variation"){
        		  return "Variazione Farmaco ad infusione: "+eventContent.drugDescription+" "+eventContent.qty+eventContent.unit;    
        	  } else if (eventContent.event == "stop"){
        		  return "Fine Farmaco ad infusione: "+eventContent.drugDescription+" - durata: "+eventContent.duration+" s";    
        	  }
          }
        case "procedure":
        	if (eventContent.procedureType == "one-shot"){
        		msg =  "Manovra "+eventContent.procedureDescription;      
        	} else if (eventContent.procedureType == "time-dependent"){
        		if (eventContent.event == "start"){
            		msg =  "Inizio Manovra "+eventContent.procedureDescription;      
        		} else if (eventContent.event == "end"){
            		msg =  "Fine Manovra "+eventContent.procedureDescription;      
        		} else {
            		msg =  "Manovra "+eventContent.procedureDescription;      
        		}
        	} 
            if (eventContent.procedureId == "intubation"){
            	if (isok(eventContent.difficultAirway)){
            		msg = msg + " - vie aeree difficili: "+eventContent.difficultAirway;
            	}
            	if (isok(eventContent.inhalation)){
            		msg = msg + " - inalazione: "+eventContent.inhalation;
            	}
            	if (isok(eventContent.videolaringo)){
            		msg = msg + " - videolaringo: "+eventContent.videolaringo;
            	}

            	if (isok(eventContent.frova)){
            		msg = msg + " - frova: "+eventContent.frova;
            	}
            	
            	return msg;
            	
            } else if (eventContent.procedureId == "drainage"){
            	if (isok(eventContent.right)){
            		msg = msg + " - destro: "+eventContent.right;
            	}
            	if (isok(eventContent.left)){
            		msg = msg + " - sinistro: "+eventContent.left;
            	}
            	return msg;	
            }  else if (eventContent.procedureId == "chest-tube"){
            	if (eventContent.right !=""){
            		msg = msg + " - destro: "+eventContent.right;
            	}
            	if (isok(eventContent.left)){
            		msg = msg + " - sinistro: "+eventContent.left;
            	}
            	return msg;	
            }  else if (eventContent.procedureId == "hemostasis"){
            	if (isok(eventContent.epistat)){
            		msg = msg + " - epistat: "+eventContent.epistat;
            	}
            	if (isok(eventContent.suture)){
            		msg = msg + " - sutura: "+eventContent.suture;
            	}
            	if (isok(eventContent.compression)){
            		msg = msg + " - compressione: "+eventContent.compression;
            	}
            	return msg;	
            }  else {
            	return msg;
            }
        case "diagnostic":
            msg = "Esame clinico "+eventContent.diagnosticDescription;   
            if (eventContent.diagnosticId == "skeleton-rx"){
            	if (isok(eventContent.topLeft)){
            		msg = msg + " - arto superiore sinistro: "+eventContent.topLeft;
            	}
            	if (isok(eventContent.topRight)){
            		msg = msg + " - arto superiore destro: "+eventContent.topRight;
            	}
            	if (isok(eventContent.bottomLeft)){
            		msg = msg + " - arto inferiore sinistro: "+eventContent.bottomLeft;
            	}
            	if (isok(eventContent.bottomRight)){
            		msg = msg + " - arto inferiore destro: "+eventContent.bottomRight;
            	}
            	return msg;
            } else if (eventContent.diagnosticId == "ega"){
            	if (isok(eventContent.lactates)){
            		msg = msg + " - lattati: " + eventContent.lactates+ " mmoli/l";
            	}
            	if (isok(eventContent.be)){
            		msg = msg + " - be: " + eventContent.be;
            	}
            	if (isok(eventContent.ph)){
            		msg = msg + " - ph: " + eventContent.ph;
            	}
            	if (isok(eventContent.hb)){
            		msg = msg + " - hb: " + eventContent.hb;
            	}
            	if (isok(eventContent.glycemia)){
            		msg = msg + " - glycemia: " + eventContent.glycemia;
            	}
            	return msg;
            }  else if (eventContent.diagnosticId == "rotem"){
               	if (isok(eventContent.fibtem)){
            		msg = msg + " - fibtem: " + eventContent.fibtem;
            	}
               	if (isok(eventContent.extem)){
            		msg = msg + " - extem: " + eventContent.extem;
            	}
               	if (isok(eventContent.hyperfibrinolysis)){
            		msg = msg + " - hyperfibrinolysis: " + eventContent.hyperfibrinolysis;
            	}
            	return msg;
            }  else {
            	return msg;
            }
            
        case "vital-signs-mon":        
          return  "Parametri vitali monitor: \r\n"+
                  "TEMP "+eventContent.Temp+
                  "; HR "+eventContent.HR+
                  "; DIA "+eventContent.DIA+
                  "; SYS "+eventContent.SYS+
                  "; SpO2 "+eventContent.SpO2+
                  "; etCO2 "+eventContent.EtCO2;   
        case "vital-sign":        
          return  "Variazione parametro vitale: "+eventContent.name+
              " - nuovo valore: "+eventContent.value;
        case "trauma-leader":
          return "Cambio trauma-leader: "+eventContent.name+" "+eventContent.surname;      
        case "photo":
          return "Foto";       
        case "video":
          return "Video";       
        case "vocal-note":
            return "Registrazione vocale";       
        case "text-note":
            return "Nota testuale: "+eventContent.text;       
        case "als-start":
            return "Inizio ALS";       
        case "als-stop":
            return "Fine ALS - durata: "+eventContent.duration+" s";       
        case "room-in":
            return "Ingresso luogo: " + eventContent.place;       
        case "room-out":
            return "Uscita luogo: " + eventContent.place;   
        case "report-reactivation":
        	return "Ripristino report.";
        default:
          return eventContent; 
      } 
    };
  }) 
 .filter('filterEventDateAndContent',function(){
    return function(event,eventType){
      if (event != undefined){
	      eventContent = event.content;
	      var msg = "";
	      switch(eventType){
	        case "drug":  
	          return "Data: "+event.date+" Ora: "+event.time +" -- "+"Farmaco: "+eventContent.drugDescription+" "+eventContent.qty+eventContent.unit;    
	        case "procedure":
	            msg =  "Data: "+event.date+" Ora: "+event.time +" -- "+"Manovra "+eventContent.procedureDescription;      
	            if (eventContent.procedureId == "intubation"){
	            	if (eventContent.difficultAirway !=""){
	            		msg = msg + " - vie aeree difficili: "+eventContent.difficultAirway;
	            	}
	            	if (eventContent.inhalation !=""){
	            		msg = msg + " - inalazione: "+eventContent.inhalation;
	            	}
	            	if (eventContent.videolaringo !=""){
	            		msg = msg + " - videolaringo: "+eventContent.videolaringo;
	            	}
	
	            	if (eventContent.frova !=""){
	            		msg = msg + " - frova: "+eventContent.frova;
	            	}
	            	
	            	return msg;
	            	
	            } else if (eventContent.procedureId == "drainage"){
	            	if (eventContent.right !=""){
	            		msg = msg + " - destro: "+eventContent.right;
	            	}
	            	if (eventContent.left !=""){
	            		msg = msg + " - sinistro: "+eventContent.left;
	            	}
	            	return msg;	
	            }  else if (eventContent.procedureId == "chest-tube"){
	            	if (eventContent.right !=""){
	            		msg = msg + " - destro: "+eventContent.right;
	            	}
	            	if (eventContent.left !=""){
	            		msg = msg + " - sinistro: "+eventContent.left;
	            	}
	            	return msg;	
	            }  else if (eventContent.procedureId == "hemostasis"){
	            	if (eventContent.epistat !=""){
	            		msg = msg + " - epistat: "+eventContent.epistat;
	            	}
	            	if (eventContent.suture !=""){
	            		msg = msg + " - sutura: "+eventContent.suture;
	            	}
	            	if (eventContent.compression !=""){
	            		msg = msg + " - compressione: "+eventContent.compression;
	            	}
	            	return msg;	
	            }  else {
	            	return msg;
	            }
	        case "diagnostic":
	            msg = "Data: "+event.date+" Ora: "+event.time +" -- "+"Esame clinico "+eventContent.diagnosticDescription;   
	            if (eventContent.diagnosticId == "skeleton-rx"){
	            	if (eventContent.topLeft !=""){
	            		msg = msg + " - arto superiore sinistro: "+eventContent.topLeft;
	            	}
	            	if (eventContent.topRight !=""){
	            		msg = msg + " - arto superiore destro: "+eventContent.topRight;
	            	}
	            	if (eventContent.bottomLeft !=""){
	            		msg = msg + " - arto inferiore sinistro: "+eventContent.bottomLeft;
	            	}
	            	if (eventContent.bottomRight !=""){
	            		msg = msg + " - arto inferiore destro: "+eventContent.bottomRight;
	            	}
	            	return msg;
	            } else if (eventContent.diagnosticId == "ega"){
	            	if (eventContent.lactates != ""){
	            		msg = msg + " - lattati: " + eventContent.lactates+ " mmoli/l";
	            	}
	            	if (eventContent.be != ""){
	            		msg = msg + " - be: " + eventContent.be;
	            	}
	            	return msg;
	            }  else if (eventContent.diagnosticId == "rotem"){
	               	if (eventContent.fibtem != ""){
	            		msg = msg + " - fibtem: " + eventContent.fibtem;
	            	}
	            	return msg;
	            }  else {
	            	return msg;
	            }
	        case "vital-signs-mon":        
	          return  "Data: "+event.date+" Ora: "+event.time +" -- "+"Parametri vitali monitor: \r\n"+
	                  "TEMP "+eventContent.Temp+
	                  "; HR "+eventContent.HR+
	                  "; DIA "+eventContent.DIA+
	                  "; SYS "+eventContent.SYS+
	                  "; SpO2 "+eventContent.SpO2+
	                  "; etCO2 "+eventContent.EtCO2;   
	        case "vital-sign":        
	          return  "Data: "+event.date+" Ora: "+event.time +" -- "+"Variazione parametro vitale: "+eventContent.name+
	              " - nuovo valore: "+eventContent.value;
	        case "trauma-leader":
	          return "Data: "+event.date+" Ora: "+event.time +" -- "+"Cambio trauma-leader: "+eventContent.name+" "+eventContent.surname;      
	        case "photo":
	          return "Data: "+event.date+" Ora: "+event.time +" -- "+"Foto";       
	        case "video":
	          return "Data: "+event.date+" Ora: "+event.time +" -- "+"Video";       
	        case "vocal-note":
	            return "Data: "+event.date+" Ora: "+event.time +" -- "+"Registrazione vocale";       
	        case "text-note":
	            return "Data: "+event.date+" Ora: "+event.time +" -- "+"Nota testuale: "+eventContent.text;       
	        case "als-start":
	            return "Data: "+event.date+" Ora: "+event.time +" -- "+"Inizio ALS";       
	        case "als-stop":
	            return "Data: "+event.date+" Ora: "+event.time +" -- "+"Fine ALS - durata: "+eventContent.duration+" s";       
	        case "room-in":
	            return "Data: "+event.date+" Ora: "+event.time +" -- "+"Ingresso luogo: " + eventContent.place;       
	        case "room-out":
	            return "Data: "+event.date+" Ora: "+event.time +" -- "+"Uscita luogo: " + eventContent.place;       
	        case "report-reactivation":
	        	return "Data: "+event.date+" Ora: "+event.time +" -- "+"Ripristino report";
	        default:
	          return "Data: "+event.date+" Ora: "+event.time +" -- "+eventContent; 
	      } 
	    }
    };
  }) 
  .filter('getFirstAvailable',function(){
	  return function(first,second){
	      return first == "" || first == undefined  ? second : first;
	  };
  })
  .filter('isEventPhoto',function(){
    return function(eventType){
      return eventType == "photo" ? "show" : "hidden";
    };
  }) 
  .filter('isEventAudio',function(){
    return function(eventType){
      return eventType == "vocal-note" ? "show" : "hidden";
    };
  }) 
  .filter('isEventVideo',function(){
    return function(eventType){
      return eventType == "video" ? "show" : "hidden";
    };
  }) 
  .filter('isAvailable',function(){
    return function(content){
      return content == "" || content == undefined  ? "hidden" : "show";
    };
  }) 
  .filter('eventPhotoVisibilityFlag',function(){
    return function(eventID,visibilityMap){
      return visibilityMap["photo"+eventID] == "visible" ? "show" : "hidden";
    };
  }) 
 .filter('getAudioSrcAAC', function(){
	 return function(reportId,eventId){
		 //alert("stringa getAudioSrcAAC = " + "/gt2/traumatracker/audio/"+reportId+"-"+eventId+".aac");
		 return "/gt2/traumatracker/audio/"+reportId+"-"+eventId+".aac"
	 }
 })
 .filter('getVideoSrc', function(){
	 return function(reportId,eventId){
		 // return "/gt2/traumatracker/video/rep-20170101-230008-4.mp4"
		 return "/gt2/traumatracker/video/"+reportId+"-"+eventId+".mp4"
	 }
 })
 .filter("getVideo", ['$sce', function ($sce) {
        return function (recordingUrl) {
            return $sce.trustAsResourceUrl(recordingUrl);
        };
    }]);

/* ------------------ */
app.controller('ttController', ['$scope', 'TTService',
	'$interval','$filter','Notification',
                                function($scope,service,$interval,$filter,Notification) {
  $scope.version = "<unknown version>";
  
  $scope.notAvailCallBack = function(reason){
      Notification.error({message: 'Servizio TraumaTracker non raggiungibile.', delay: 1000});
  };
  
  $scope.pageBlocks = {
    main: "block",
    report: "none",
    users: "block",
    showMain: function(){
      this.main = "block"
      this.report = "none"
      this.users = "block"
    },
    showReport: function(){
      this.main = "none"
      this.report = "block"
      this.users = "none"
    },
    showUsers: function(){
      this.main = "none"
      this.report = "none"
      this.users = "block"
    }
  };
  
  $scope.status = "";
  $scope.reportToSearchDate = "";
  $scope.reportToSearchOperator = "";
  $scope.reportsFound = [];

  $scope.users = [];
  $scope.userToAdd = {
    name: "", surname: "", userId: ""
  }

  $scope.getVersion = function(){    
    service.getVersion().then(function(response){
      $scope.version = response;
    },  $scope.notAvailCallBack);
  };

  $scope.getAllReports = function(){    
    service.getAllReports().then(function(response){
      $scope.reports = response;
      $scope.status = "All Reports Retrieved.";
    },  $scope.notAvailCallBack);
  };
  
  $scope.findReports = function(){    
    service.findReports($scope.reportToSearchDate,$scope.reportToSearchOperator).then(function(response){
      $scope.reportsFound = response;
    },  $scope.notAvailCallBack);
  };
  
  $scope.showReport = function(report){   
    $scope.selectedReport = report
    $scope.pageBlocks.showReport()
	//  $("#viewReport").modal("show");
  };
  
  $scope.exportReport = function(report){  
      var csvStr = '';
      for (var i = 0; i < report.events.length ; i++) {
        var event = report.events[i];
        csvStr += event.eventId + ',' + 
              event.date + ',' + 
              event.time + ',' +
              event.place +',' +
              $filter('filterEventContent')(event.content,event.type)+'\r\n';
      }

      var blob = new Blob([csvStr], { type: 'text/csv;charset=utf-8;' });
      var filename = report._id+".cvs";

      if (navigator.msSaveBlob) { // IE 10+
        navigator.msSaveBlob(blob, filename);
      } else {
        var link = document.createElement("a");
        if (link.download !== undefined) { // feature detection
          // Browsers that support HTML5 download attribute
          var url = URL.createObjectURL(blob);
          link.setAttribute("href", url);
          link.setAttribute("download", filename);
          link.style.visibility = 'hidden';
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link);
        }
      }

      Notification.success({message: 'Report '+report._id+' esportato.', delay: 2000});
  };

  $scope.addPhotosToReport = function (doc, report, i, photoCount){
        while (i < report.events.length && report.events[i].type != "photo"){
          i++;
        } 
        if (i < report.events.length){
          var event = report.events[i];
          var img = new Image();
          img.src = "/gt2/traumatracker/photo/"+report._id+"-"+event.eventId+".jpg"; // event.content.ref;
          img.onload = function() {
              var canvas = document.createElement('canvas');
              scale = 600/img.width;
              canvas.width = img.width*scale;
              canvas.height = img.height*scale;

              var context = canvas.getContext('2d');
              context.drawImage(img, 0,0,img.width,img.height, 0, 0, canvas.width, canvas.height);

              var dataURL = canvas.toDataURL('image/jpeg');              
              doc.addPage();

              doc.setFontType("bold")
              doc.text(20,20, "Foto #"+photoCount+": ")
              doc.setFontType("normal")
              doc.text(40, 20, report._id+"-"+event.eventId+".jpg")
              doc.addImage(dataURL, "JPEG", 20,30); 

              $scope.addPhotosToReport(doc, report, i+1, photoCount+1)  
          };          
        } else {
          doc.save(report._id+'.pdf');
          Notification.success({message: 'PDF del report '+report._id+' creato.', delay: 2000})
        }
  };

  $scope.printReport = function(report,multimediaToBeIncluded){  

	  var docStruct = {
			    y: 25,
			    nPages: 1,
			    date: "xx/xx/xxxx",
			    doc: {},			    	
			    
			    init: function(currdoc, ystart){
			    	this.y = ystart;
			    	this.doc = currdoc;
			    	
			    	var today = new Date();
			    	var dd = today.getDate();
			    	var mm = today.getMonth() + 1;
			    	var yyyy = today.getFullYear();
			    	
			    	var ho = today.getUTCHours() + 2; // +2 Italy Time
			    	var mi = today.getUTCMinutes();
			    	
			    	if (dd < 10) {
			    	    dd='0'+dd;
			    	} 

			    	if (mm < 10) {
			    	    mm='0'+mm;
			    	} 

			    	if (ho < 10){
			    		ho = '0'+ho;
			    	}

			    	if (mi < 10){
			    		mi = '0'+mi;
			    	}
			    	
			    	this.date = mm+'/'+dd+'/'+yyyy+" "+ho+":"+mi;
			    },
			    
			    addMainTitle: function(x, title){
			        this.doc.setFontSize(20);
			        this.doc.text(x, this.y, title);
			        this.doc.setFontSize(10);
			        this.y+=10;
			    },
			    
			    addTitle: function(title) {
			    	this.y+=5;
			        // this.doc.setFontType("bold");
			    	this.doc.setFontSize(12);
			        this.doc.text(20, this.y, title);
			        this.doc.line(20, this.y+4, 190, this.y+4);
			        this.y+=10;
			        this.doc.setFontSize(10);
			        this.checkNewPage();
			    },
			    
			    addSubTitle: function(title) {
			    	this.y+=10;
			        // this.doc.setFontType("bold");
			    	this.doc.setFontSize(12);
			        this.doc.text(20, this.y, title);
			        //this.doc.line(20, this.y+4, 190, this.y+4);
			        this.y+=10;
			        this.doc.setFontSize(10);
			        this.checkNewPage();
			    },
	    			    
			    addSubTitleIfAnyAvailable: function(title,list) {
			    	found = false;
			    	list.forEach(function(el){
			    		if (el != undefined && el != ""){
			    			found = true;
			    		}
			    	});
			    	if (found){
				    	this.y+=10;
				        // this.doc.setFontType("bold");
				    	this.doc.setFontSize(12);
				        this.doc.text(20, this.y, title);
				        //this.doc.line(20, this.y+4, 190, this.y+4);
				        this.y+=10;
				        this.doc.setFontSize(10);
				        this.checkNewPage();
			    	}
			    },

			    addItemIfAvailable: function(x1, x2, what, content) {
			        if (what != undefined && what != "" && content != undefined && content != "" ){
			        	this.addItem(x1, x2, what, content)
			        }
			    },

			    addItem: function(x1, x2, what, content) {
			    	if (what != undefined && what != "" && content != undefined){
			        	this.doc.setFontType("bold");
			        	this.doc.text(x1, this.y, what)
			        	this.doc.setFontType("normal");
			        	this.doc.text(x2, this.y, ""+content)
			        	this.y+=8;
				        this.checkNewPage();
			    	}
			    },
			    
			    addEventTrackPage: function(){
			        this.newPage();
			        this.addTitle("TRACCIA DEGLI EVENTI");			    	
			        this.doc.setFontSize(10)
			        this.doc.setFontType("bold");
			        this.doc.text(20, this.y, "Data")
			        this.doc.text(45, this.y, "Ora")
			        this.doc.text(60, this.y, "Luogo")
			        this.doc.text(110, this.y, "Evento")
			        this.doc.setFontType("normal");
			        this.y+=10;
			    },
			    
			    addEventItem: function(date, time, place, what, deltay){
			        this.doc.text(20, this.y, date);
			        this.doc.text(45, this.y, time);
			        this.doc.text(60, this.y, place);
			        this.doc.text(110, this.y, what)
			        this.y+=8 + deltay
			        this.checkNewPage();
			    	
			    },
			    
			    checkNewPage: function(){
			        if (this.y > 260){
			        	this.printFooter();
		            	this.doc.addPage();
		                this.y = 20;
		                this.nPages++;
			        }
			    },

			    newPage: function(){
			    	if (this.y > 20){
				        this.printFooter();
			            this.doc.addPage();
			            this.y = 20;
			            this.nPages++;
			    	}
			    },
			    
			    printFooter: function(){
		        	this.doc.setFontType("normal");
			        this.doc.text(20,280, "Data: "+this.date);				        
			        this.doc.text(90,280, "Pagina "+this.nPages)
			    }
	  };
	  
      var doc = new jsPDF()
         
      var img = new Image();
      img.src = "/gt2/traumatracker/img/header.jpg"; // event.content.ref;
      img.onload = function() {
          var canvas = document.createElement('canvas');
          scale = 675/img.width;
          canvas.width = img.width*scale;
          canvas.height = img.height*scale;

          var context = canvas.getContext('2d');
          context.drawImage(img, 0,0,img.width,img.height, 0, 0, canvas.width, canvas.height);

          var dataURL = canvas.toDataURL('image/jpeg');              
          doc.addImage(dataURL, "JPEG", 20, 20); 

          docStruct.init(doc,60);
                    
          $scope.printReportContinuation(report, doc, docStruct, multimediaToBeIncluded);
      }          
  }
  
  $scope.printReportContinuation = function(report, doc, docStruct, multimediaToBeIncluded){  

	  var isok = function(value){
    	  return value != undefined && value != "";
      }	  
	  docStruct.addMainTitle(20, 'Trauma Tracker Report '+report._id);

      docStruct.addTitle("DATI GENERALI");
      docStruct.addItem(20, 70, "Operatore",report.startOperatorDescription);
      docStruct.addItem(20, 70, "Seconda attivazione",report.secondaryActivation);
      docStruct.addItem(20, 70, "Trauma team",$filter('filterTeamMembers')(report.traumaTeamMembers));
      docStruct.addItem(20, 70, "Data inizio",report.startDate);
      docStruct.addItem(20, 70, "Ora",report.startTime);
      docStruct.addItem(20, 70, "Data fine",report.endDate);
      docStruct.addItem(20, 70, "Ora",report.endTime);
      docStruct.addItem(20, 70, "Destinazione",report.finalDestination);

      patInfo = report.patientInfo;

      docStruct.addSubTitleIfAnyAvailable("DATI PAZIENTE",[patInfo.code,patInfo.gender,patInfo.type,patInfo.age,patInfo.accidentDate,patInfo.accidentTime,patInfo.vehicle,patInfo.fromOtherEmergency]);
      docStruct.addItemIfAvailable(20, 70, "SDO",patInfo.sdo);
      docStruct.addItemIfAvailable(20, 70, "Codice PS",patInfo.code);
      docStruct.addItemIfAvailable(20, 70, "Genere",patInfo.gender);
      docStruct.addItemIfAvailable(20, 70, "Tipo",patInfo.type);
      docStruct.addItemIfAvailable(20, 70, "Età",patInfo.age);
      docStruct.addItemIfAvailable(20, 70, "Data dell'incidente",patInfo.accidentDate);
      docStruct.addItemIfAvailable(20, 70, "Ora dell'incidente",patInfo.accidentTime);
      docStruct.addItemIfAvailable(20, 70, "Veicolo di arrivo in PS",patInfo.vehicle);
      docStruct.addItemIfAvailable(20, 70, "Provenienza da altro PS",patInfo.fromOtherEmergency);

      docStruct.addSubTitleIfAnyAvailable("ANAMNESI",[report.anamnesi.antiplatelets,report.anamnesi.anticoagulants,report.anamnesi.nao]);
      docStruct.addItemIfAvailable(20, 70, "Antiaggreganti", report.anamnesi.antiplatelets);
      docStruct.addItemIfAvailable(20, 70, "Anticoagulanti", report.anamnesi.anticoagulants);
      docStruct.addItemIfAvailable(20, 70, "Nao", report.anamnesi.nao);

      docStruct.newPage();

      var startVS = report.startVitalSigns;

      docStruct.addSubTitleIfAnyAvailable("STATO INIZIALE PAZIENTE",
    		  [startVS.TempValue,startVS.Temp,startVS.HRValue,startVS.HR,startVS.BPValue,startVS.BP,startVS.SpO2Value,startVS.SpO2,startVS.EtCO2Value,startVS.EtCO2,startVS.ExtBleeding,
    			  startVS.Airway,startVS.Tracheo,startVS.Inhalation,startVS.IntubationFailed,startVS.ChestTube,startVS.OxygenPercentage,
    			  startVS.GCSTotal,startVS.GCSMotor,startVS.GCSVerbal,startVS.GCSEyes, startVS.Sedated,
					startVS.Pupils, startVS.EyesDeviation, startVS.EarsBlood,
					startVS.UpperLimbsRightMotility, startVS.UpperLimbsLeftMotility, startVS.LowerLimbsRightMotility, startVS.LowerLimbsLeftMotility,
					startVS.UpperLimbsRightSensitivity, startVS.UpperLimbsLeftSensitivity, startVS.LowerLimbsRightSensitivity, startVS.LowerLimbsLeftSensitivity,
					startVS.SphincterTone,startVS.Priapism,startVS.UpperLimbsRightPeripheralWrists,startVS.UpperLimbsRightPeripheralWrists,
                    startVS.LowerLimbsRightPeripheralWrists,startVS.LowerLimbsLeftPeripheralWrists,
                    startVS.Peripherals,startVS.Intraosseous,startVS.Cvc,
                    startVS.LimbsFractur,startVS.FractureExpositio,startVS.FractureGustiloDegree,startVS.UnstableBasin,
                    startVS.Burn, startVS.BurnDegree,startVS.BurnPercentage]);

      var exfilter = function(exact,other) {
        	return exact != undefined && exact != "" ? exact : other; 
      }

      var posx = 110;

      docStruct.addSubTitleIfAnyAvailable("Parametri vitali",[startVS.TempValue,startVS.Temp,startVS.HRValue,startVS.HR,startVS.BPValue,startVS.BP,startVS.SpO2Value,startVS.SpO2,startVS.EtCO2Value,startVS.EtCO2,startVS.ExtBleeding]);      
      docStruct.addItemIfAvailable(20, posx, "Temperatura", exfilter(startVS.TempValue,startVS.Temp));
      docStruct.addItemIfAvailable(20, posx, "Frequenza cardiaca",exfilter(startVS.HRValue,startVS.HR));
      docStruct.addItemIfAvailable(20, posx, "Pressione arteriosa",exfilter(startVS.BPValue,startVS.BP));
      docStruct.addItemIfAvailable(20, posx, "Saturazione (SpO2)",exfilter(startVS.SpO2Value,startVS.SpO2));
      docStruct.addItemIfAvailable(20, posx, "EtCO2",exfilter(startVS.EtCO2Value,startVS.EtCO2));
      docStruct.addItemIfAvailable(20, posx, "Emorragie esterne",startVS.ExtBleeding);

      docStruct.addSubTitleIfAnyAvailable("Vie aeree e respirazione",[startVS.Airway,startVS.Tracheo,startVS.Inhalation,startVS.IntubationFailed,startVS.ChestTube,startVS.OxygenPercentage]);
      docStruct.addItemIfAvailable(20, posx, "Vie aeree",startVS.Airway);
      docStruct.addItemIfAvailable(20, posx, "Tracheo",startVS.Tracheo);
      docStruct.addItemIfAvailable(20, posx, "Inalazione",startVS.Inhalation);
      docStruct.addItemIfAvailable(20, posx, "IOT fallita",startVS.IntubationFailed);
      docStruct.addItemIfAvailable(20, posx, "Decompressione pleurica",startVS.ChestTube);
      docStruct.addItemIfAvailable(20, posx, "Ossigeno (%)",startVS.OxygenPercentage);

      docStruct.addSubTitleIfAnyAvailable("Esame neurologico",[startVS.GCSTotal,startVS.GCSMotor,startVS.GCSVerbal,startVS.GCSEyes, startVS.Sedated,
    	  													startVS.Pupils, startVS.EyesDeviation, startVS.EarsBlood,
    	  													startVS.UpperLimbsRightMotility, startVS.UpperLimbsLeftMotility, startVS.LowerLimbsRightMotility, startVS.LowerLimbsLeftMotility,
    	  													startVS.UpperLimbsRightSensitivity, startVS.UpperLimbsLeftSensitivity, startVS.LowerLimbsRightSensitivity, startVS.LowerLimbsLeftSensitivity,
    	  													startVS.SphincterTone,startVS.Priapism]);

      docStruct.addItemIfAvailable(20, posx, "GCS totale",startVS.GCSTotal);
      docStruct.addItemIfAvailable(20, posx, "GCS motoria",startVS.GCSMotor);
      docStruct.addItemIfAvailable(20, posx, "GCS verbale",startVS.GCSVerbal);
      docStruct.addItemIfAvailable(20, posx, "GCS oculare",startVS.GCSEyes);
      docStruct.addItemIfAvailable(20, posx, "Sedato",startVS.Sedated);
      docStruct.addItemIfAvailable(20, posx, "Pupille",startVS.Pupils);
      docStruct.addItemIfAvailable(20, posx, "Deviazione occhi",startVS.EyesDeviation);
      docStruct.addItemIfAvailable(20, posx, "Otorragia",startVS.EarsBlood);
      docStruct.addItemIfAvailable(20, posx, "Motilità arti superiori - destro",startVS.UpperLimbsRightMotility);
      docStruct.addItemIfAvailable(20, posx, "Motilità arti superiori - sinistro",startVS.UpperLimbsLeftMotility);
      docStruct.addItemIfAvailable(20, posx, "Motilità arti inferiori - destro",startVS.LowerLimbsRightMotility);
      docStruct.addItemIfAvailable(20, posx, "Motilità arti inferiori - sinistro",startVS.LowerLimbsLeftMotility);
      docStruct.addItemIfAvailable(20, posx, "Sensibilità arti superiori - destro",startVS.UpperLimbsRightSensitivity);
      docStruct.addItemIfAvailable(20, posx, "Sensibilità arti superiori - sinistro",startVS.UpperLimbsLeftSensitivity);
      docStruct.addItemIfAvailable(20, posx, "Sensibilità arti inferiori - destro",startVS.LowerLimbsRightSensitivity);
      docStruct.addItemIfAvailable(20, posx, "Sensibilità arti inferiori - sinistro",startVS.LowerLimbsLeftSensitivity);
      docStruct.addItemIfAvailable(20, posx, "Tono sfintere",startVS.SphincterTone);
      docStruct.addItemIfAvailable(20, posx, "Priapismo",startVS.Priapism);

      docStruct.addSubTitleIfAnyAvailable("Valutazione polsi periferici",[startVS.UpperLimbsRightPeripheralWrists,startVS.UpperLimbsRightPeripheralWrists,
    	                                                                 startVS.LowerLimbsRightPeripheralWrists,startVS.LowerLimbsLeftPeripheralWrists]);            
      docStruct.addItemIfAvailable(20, posx, "Valutazione polsi periferici - arto superiori destro",startVS.UpperLimbsRightPeripheralWrists);
      docStruct.addItemIfAvailable(20, posx, "Valutazione polsi periferici - arto superiori sinistro",startVS.UpperLimbsLeftPeripheralWrists);
      docStruct.addItemIfAvailable(20, posx, "Valutazione polsi periferici - arto inferiore destro",startVS.LowerLimbsRightPeripheralWrists);
      docStruct.addItemIfAvailable(20, posx, "Valutazione polsi periferici - arto inferiore sinistro",startVS.LowerLimbsLeftPeripheralWrists);

      docStruct.addSubTitleIfAnyAvailable("Accessi vascolari",[startVS.Peripherals,startVS.Intraosseous,startVS.Cvc]);            
      docStruct.addItemIfAvailable(20, posx, "Periferiche",startVS.Peripherals);
      docStruct.addItemIfAvailable(20, posx, "Intraossea",startVS.Intraosseous);
      docStruct.addItemIfAvailable(20, posx, "CVC",startVS.Cvc);

      docStruct.addSubTitleIfAnyAvailable("Lesioni ortopediche",[startVS.LimbsFractur,startVS.FractureExpositio,startVS.FractureGustiloDegree,startVS.UnstableBasin]);            
      docStruct.addItemIfAvailable(20, posx, "Frattura arti",startVS.LimbsFracture);
      docStruct.addItemIfAvailable(20, posx, "Frattura esposta",startVS.FractureExposition);
      docStruct.addItemIfAvailable(20, posx, "Grado di gustilo",startVS.FractureGustiloDegree);
      docStruct.addItemIfAvailable(20, posx, "Bacino instabile",startVS.UnstableBasin);

      docStruct.addSubTitleIfAnyAvailable("Ustioni",[startVS.Burn, startVS.BurnDegree,startVS.BurnPercentage]);            
      docStruct.addItemIfAvailable(20, posx, "Ustione",startVS.Burn);
      docStruct.addItemIfAvailable(20, posx, "Grado di ustione",startVS.BurnDegree);
      docStruct.addItemIfAvailable(20, posx, "Ustione (%)",startVS.BurnPercentage);

      docStruct.addEventTrackPage();

      var nPhoto = 1;
      var nVideo = 1;
      var nAudio = 1;
      var nPages = 1;

      for (i = 0; i < report.events.length; i++){
        var event = report.events[i];               
        var what = "";
        var deltay = 0;
        switch(event.type){
        case "procedure":
            if (event.content.procedureType == "one-shot"){
            	what = "Procedura "+event.content.procedureDescription; 
            } else {
            	if (event.content.event == "start"){
            		what = "Inizio Procedura "+event.content.procedureDescription; 
            	} else {
            		what = "Fine Procedura "+event.content.procedureDescription; 
            	}
            }
            if (event.content.procedureId == "intubation"){
	            deltay = 0;
	        	if (isok(event.content.difficultAirway)){
	        		what = what + "\r\n - vie aeree difficili: "+event.content.difficultAirway;
	        		deltay += 4;
	        	}
	        	if (isok(event.content.inhalation)){
	        		what = what + "\r\n - inalazione: "+event.content.inhalation;
	        		deltay += 4;
	        	}
	        	if (isok(event.content.videolaringo)){
	        		what = what + "\r\n - videolaringo: "+event.content.videolaringo;
	        		deltay += 4;
	        	}
	
	        	if (isok(event.content.frova)){
	        		what = what + "\r\n - frova: "+event.content.frova;
	        		deltay += 4;
	        	}
            } else if (event.content.procedureId == "drainage"){
	            deltay = 0;
	        	if (isok(event.content.right)){
	        		what = what + "\r\n - destro: "+event.content.right;
	        		deltay += 4;
	        	}
	        	if (isok(event.content.left)){
	        		what = what + "\r\n - sinistro: "+event.content.left;
	        		deltay += 4;
	        	}
            } else if (event.content.procedureId == "chest-tube"){
	            deltay = 0;
	        	if (isok(event.content.right)){
	        		what = what + "\r\n - destro: "+event.content.right;
	        		deltay += 4;
	        	}
	        	if (isok(event.content.left)){
	        		what = what + "\r\n - sinistro: "+event.content.left;
	        		deltay += 4;
	        	}
            } else if (event.content.procedureId == "hemostasis"){
	            deltay = 0;
	        	if (isok(event.content.epistat)){
	        		what = what + "\r\n - epistat: "+event.content.epistat;
	        		deltay += 4;
	        	}
	        	if (isok(event.content.suture)){
	        		what = what + "\r\n - sutura: "+event.content.suture;
	        		deltay += 4;
	        	}
	        	if (isok(event.content.compression)){
	        		what = what + "\r\n - compressione: "+event.content.compression;
	        		deltay += 4;
	        	}
            }

            break;     
        case "diagnostic":
            what = "Esame clinico "+event.content.diagnosticDescription; 
        	if (event.content.diagnosticId == "skeleton-rx"){
	            deltay = 0;
	        	if (isok(event.content.topLeft)){
	        		what = what + "\r\n - arto superiore sinisto: "+event.content.topLeft;
	        		deltay += 4;
	        	}
	        	if (isok(event.content.topRight)){
	        		what = what + "\r\n - arto superiore destro: "+event.content.topRight;
	        		deltay += 4;
	        	}
	        	if (isok(event.content.bottomLeft)){
	        		what = what + "\r\n - arto inferiore sinistro: "+event.content.bottomLeft;
	        		deltay += 4;
	        	}
	        	if (isok(event.content.bottomRight)){
	        		what = what + "\r\n - arto inferiore destro: "+event.content.bottomRight;
	        		deltay += 4;
	        	}
            } else if (event.content.diagnosticId == "ega"){
	            deltay = 0;
	        	if (isok(event.content.lactates)){
	        		what = what + "\r\n - lattati: "+event.content.lactates;
	        		deltay += 4;
	        	}
	        	if (isok(event.content.be)){
	        		what = what + "\r\n - be: "+event.content.be;
	        		deltay += 4;
	        	}
	        	if (isok(event.content.ph)){
	        		what = what + "\r\n - ph: "+event.content.ph;
	        		deltay += 4;
	        	}
	        	if (isok(event.content.hb)){
	        		what = what + "\r\n - hb: "+event.content.hb;
	        		deltay += 4;
	        	}
	        	if (isok(event.content.glycemia)){
	        		what = what + "\r\n - glycemia: "+event.content.glycemia;
	        		deltay += 4;
	        	}
            }  else if (event.content.diagnosticId == "rotem"){
	            deltay = 0;
	        	if (isok(event.content.fibtem)){
	        		what = what + "\r\n - fibtem: "+event.content.fibtem;
	        		deltay += 4;
	        	}
	        	if (isok(event.content.extem)){
	        		what = what + "\r\n - extem: "+event.content.extem;
	        		deltay += 4;
	        	}
	        	if (isok(event.content.hyperfibrinolysis)){
	        		what = what + "\r\n - hyperfibrinolysis: "+event.content.hyperfibrinolysis;
	        		deltay += 4;
	        	}
            }
            break;     
        case "drug":       
          if (event.content.administrationType=="one-shot"){
        	  what = "Farmaco "+event.content.drugDescription+" "+event.content.qty+event.content.unit; 
          } else {
              if (event.content.event=="start"){
            	  what = "Inizio infusione farmaco "+event.content.drugDescription+" "+event.content.qty+event.content.unit; 
              } else if (event.content.event=="variation"){
            	  what = "Variazione infusione farmaco "+event.content.drugDescription+" "+event.content.qty+event.content.unit; 
              } else {
            	  what = "Fine infusione farmaco "+event.content.drugDescription +"\r\n - durata: "+event.content.duration; 
              }
          }
          break;   
        case "vital-signs-mon":        
          what = "Parametri vitali monitor: \r\n"+
                  "TEMP "+event.content.Temp+
                  "; HR "+event.content.HR+
                  "; DIA "+event.content.DIA+
                  "; SYS "+event.content.SYS+
                  "; SpO2 "+event.content.SpO2+
                  "; etCO2 "+event.content.EtCO2;  
          deltay = 4;         
          break;         
        case "vital-sign":        
          what = "Variazione parametro vitale: \r\n"+event.content.name+
              " - nuovo valore: "+event.content.value;   
          deltay = 4;         
          break;         
        case "trauma-leader":        
          what = "Cambio trauma leader: "+event.content.name+" "+event.content.surname;   
          break;         
        case "photo":
          if (multimediaToBeIncluded){
	          what = "Foto #"+nPhoto+": "+event.eventId;       
	          nPhoto++;
          };
          break;
        case "video":
          if (multimediaToBeIncluded){
        	  what = "Video #"+nVideo+": "+event.eventId;       
        	  nVideo++;
          };
          break;
        case "vocal-note":
          if (multimediaToBeIncluded){
	          what = "Registrazione vocale #"+nAudio+": "+event.eventId;       
	          nAudio++;
          };
          break;
        case "text-note":         
          var txt = '';
          var len = event.content.text.length;
          var words = event.content.text.split(" ");
          var rlen = 0;
          var index = 0;
          deltay = 4;         
          while (index < words.length){
        	  var wl = words[index].length;
        	  if (rlen + wl > 100){
        		  txt = txt + "\r\n";
        		  rlen = 0;
                  deltay += 4;         
        	  }
        	  txt = txt + ' ' + words[index];
        	  index++;
        	  rlen += wl; 
          }        	
          what = "Nota testuale: \r\n"+txt;       
          break;
        case "als-start":
            what = "Inizio ALS";       
            break;
        case "als-stop":
            what = "Fine ALS - durata: "+event.content.duration+" s";       
            break;
        case "room-in":
            what = "Ingresso luogo: "+event.content.place;       
            break;
        case "room-out":
            what = "Uscita luogo: "+event.content.place;       
            break;
        default:
          what = "unknown: "+event.type; 
          break;
        } 	    
 
        var place = event.place;
        if (event.place == "Sala Operatoria (Chirurgia Generale)"){
        	place = "Chirugia Generale (SO)";
        } else if (event.place == "Sala Operatoria (Neurochirurgia)"){
			place = "Neurochirurgia (SO)";
        } else if (event.place == "Sala Operatoria (Ortopedia)"){
			place = "Ortopedia (SO)";
        } 

        if ((event.type != "photo" && event.type != "video" && event.type != "vocal-note") || multimediaToBeIncluded){
        	docStruct.addEventItem(event.date, ""+$filter('filterTime')(event.time), place, what, deltay);
        }
      }

      docStruct.printFooter();

      if (multimediaToBeIncluded){
    	  $scope.addPhotosToReport(doc, report, 0, 1);
      } else {
          doc.save(report._id+'.pdf');
          Notification.success({message: 'PDF del report '+report._id+' creato.', delay: 2000}) 
      }
  };  

  $scope.deleteReport = function(report){  
    service.deleteReport(report).then(function(response){
      $scope.status = "Report deleted.";
      // trick da sistemare
      $scope.pageBlocks.showMain()
      /* notification */
      Notification.success({message: 'Report '+report._id+' eliminato.', delay: 2000});
    });
  };  

  $scope.deleteEventInReport = function(report,event){  
	  service.deleteEventInReport(report,event).then(function(response){

		  $scope.status = "Event deleted.";
		  $scope.selectedReport = response;
		  $scope.pageBlocks.showReport()

		  /* notification */
		  Notification.success({message: 'Event '+event.eventId+' dal report '+report._id+' eliminato.', delay: 2000});
	  });
  };  

 $scope.resetReportsDB = function(){  
		service.deleteAllReports().then(function(response){
		      Notification.success({message: 'Reports DB reset.', delay: 2000});
		});
	  };

$scope.closeGraph = function(){
	$('#statCanvas').remove(); // this is my <canvas> element
	$('#divStat').append("<canvas id='statCanvas' width='600' height='600'><canvas>");
	
	// null function list button
	$("#forData").off("click");
	$("#backData").off("click");
	$("#addData").off("click");
	$("#remData").off("click");
};
	  
  $scope.openDeleteReportDialog = function(report){  
    $scope.reportToDelete = report;
    $("#deleteReportModal").modal("show");
  };
    
  $scope.openGetNumTraumaMonth = function() {
	  service.getStat(numTraumiMonth).then(function(response){
		$scope.selectedStat = {
			title: 'Numero interventi al mese'
		};

		initChartCanvas(response, 'Numero Interventi');
		
		$("#forData").click(function() {forChartData()});
		$("#backData").click(function() {backChartData()});
		$("#addData").click(function() {addChartData()});
		$("#remData").click(function() {remChartData()});
	  
		$("#showStat").modal("show");
	  
		$("#divDataSet").hide();
		$("#divData").show();
		$("#moveData").show();
	  });
  };
  
  $scope.openGetNumTraumaWeek = function() {
	  service.getStat(numTraumiWeek).then(function(response){
		$scope.selectedStat = {
			title: 'Numero interventi alla settimana'
		};

		initChartCanvas(response, 'Numero Interventi');
		
		$("#forData").click(function() {forChartData()});
		$("#backData").click(function() {backChartData()});
		$("#addData").click(function() {addChartData()});
		$("#remData").click(function() {remChartData()});
	  
		$("#showStat").modal("show");
	  
		$("#divDataSet").hide();
		$("#divData").show();
		$("#moveData").show();
	  });
  };
  
  $scope.openGetNumTraumaDay = function() {
	  service.getStat(numTraumiDay).then(function(response){
		$scope.selectedStat = {
				title: 'Numero interventi al giorno'
			};

		initChartCanvas(response, 'Numero Interventi');
		
		$("#forData").click(function() {forChartData()});
		$("#backData").click(function() {backChartData()});
		$("#addData").click(function() {addChartData()});
		$("#remData").click(function() {remChartData()});
	  
		$("#showStat").modal("show");
	  
		$("#divDataSet").hide();
		$("#divData").show();
		$("#moveData").show();
	});
  };
    
  $scope.openGetAvarageTraumaTime = function(){
	  service.getStat(numAvarageTraumaTime).then(function(response){		
			$scope.selectedStat = {
				title: 'Durata media del Trauma'
			};

			initChartCanvas(response, 'Numero Interventi');
			
			$("#forData").click(function() {forChartData()});
			$("#backData").click(function() {backChartData()});
			$("#addData").click(function() {addChartData()});
			$("#remData").click(function() {remChartData()});
		  
			$("#showStat").modal("show");
		  
			$("#divDataSet").hide();
			$("#divData").show();
			$("#moveData").show();
	  });
  };
  
  $scope.openGetAvarageTimeRoom = function() {
	  service.getStat(numAvarageTimeRoom).then(function(response){		
			$scope.selectedStat = {
				title: 'Tempo medio per stanza'
			};

			initChartCanvas(response, 'Numero Interventi');
			
			$("#forData").click(function() {forChartData()});
			$("#backData").click(function() {backChartData()});
			$("#addData").click(function() {addChartData()});
			$("#remData").click(function() {remChartData()});
		  
			$("#showStat").modal("show");
		  
			$("#divDataSet").hide();
			$("#divData").show();
			$("#moveData").show();
	  });
  };
  
  $scope.openGetNumFinalDestination = function() {
	  service.getStat(numFinalDestination).then(function(response){	  
		  $scope.selectedStat = {
		    title: 'Numero interventi per stanza'
		  };
		  
		  initPieCanvas(response);
	
		  $("#showStat").modal("show");
		  
		  $("#moveData").hide();
		  $("#divData").hide();
		  $("#divDataSet").hide();
	  });
  };
  
  $scope.openGetConsumptionTypeDrug = function() {
	  service.getStat(consumptionTypeDrug).then(function(response){

		  initTypeDrug(response);

		  $scope.selectedStat = {
		    title: 'Quantità e durata'
		  };

		  $("#showStat").modal("show");
		  $("#moveData").hide();
		  $("#divData").hide();
		  $("#divDataSet").show();
		  
		  $("#addDataSet").click(function(){addHorizontalData()});
		  $("#remDataSet").click(function(){remHorizontalData()});
	  });
  };
  
  $scope.openGetMaxProcedure = function() {	  
	  service.getStat(numMaxProcedure).then(function(response){			
			$scope.selectedStat = {
				title: 'Operazioni per stanza'
			};

			initChartCanvas(response, 'Numero Procedure');
			
			$("#forData").click(function() {forChartData()});
			$("#backData").click(function() {backChartData()});
			$("#addData").click(function() {addChartData()});
			$("#remData").click(function() {remChartData()});
		  
			$("#showStat").modal("show");
		  
			$("#divDataSet").hide();
			$("#divData").show();
			$("#moveData").show();
	  });
  };
  
  $scope.backToMain = function(){  
    $scope.pageBlocks.showMain();
  };

  $scope.refreshRecentReports = function(){
    $scope.getAllReports();
  };

  $scope.viewPhoto = function(eventPhoto){
	    $scope.eventVisualisedPhoto = eventPhoto; 
	  };

  $scope.viewVideo = function(report,eventVideo){
	 $scope.eventVisualisedVideo = eventVideo; 
	 $scope.selectedVideoRef = "/gt2/traumatracker/video/"+report._id+"-"+eventVideo.eventId+".mp4"; 
  };

  $scope.removeEvent = function(event){
	$scope.eventToBeRemoved = event; 
  };

  $scope.onAdmin = function(){
    $scope.showUsers();
  };

  $scope.onAbout = function(){
      Notification.success({message: 'About ', delay: 1000});
  };

  $scope.getAllUsers = function(){    
    service.getAllUsers().then(function(response){
      $scope.users = response;
    }, $scope.notAvailCallBack);
  };

  $scope.resetUsersDB = function(){  
		service.deleteAllUsers().then(function(response){
			Notification.success({message: 'Users DB reset.', delay: 2000});
			$scope.getAllUsers();
		});
	  };

  $scope.addUser = function(){
	 service.addUser($scope.userToAdd).then(function (response){
	     Notification.success({message: 'Nuovo utente '+$scope.userToAdd.userId+" inserito.", delay: 2000});					 
		 $scope.getAllUsers();
		 $scope.userToAdd.name = "";
		 $scope.userToAdd.surname = "";
		 $scope.userToAdd.userId = "";
	 },function (res){
	     Notification.success({message: 'Utente '+$scope.userToAdd.userId+" già presente.", delay: 2000});					 
		 $scope.userToAdd.name = "";
		 $scope.userToAdd.surname = "";
		 $scope.userToAdd.userId = "";
	 });
  };

  $scope.getVersion();

  $scope.getAllUsers();

  $scope.refreshRecentReports();

  $interval(function(){
	  $scope.refreshRecentReports(); 
  }, 2000);
}]);
