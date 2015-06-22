	var App = angular.module('datatableApp', ['schemaForm','mgcrea.ngStrap',
	                                          'schemaForm-datepicker', 'schemaForm-timepicker', 'schemaForm-datetimepicker'/*'datatables','datatables.bootstrap'*/]);

	App.controller('datatableCtrl', function ($scope, $http) {
		$scope.finishedHeader = false;
		$scope.dtColumns = [];
		$scope.expanded = false;
		function registerEvent() {
			$('#rpt_table').on('click'," tbody a",function(e){
				aPos = window.oTable.fnGetPosition($(this).parents('td')[0])
				rowData = window.oTable.fnGetData(aPos[0]);
				var link = window.oTable.DataTable.settings[0].aoColumns[aPos[1]].link;
				linkUrl = Mustache.render(link.url, rowData);
				console.log(linkUrl);
				if(link.target == '_blank') {
					
				}
				
				if(link.target == '_modal') {
					
				}
				
				// This is important
				$scope.$apply(function(){
					
				});
				
				
			});
			
		}
		
		registerEvent();
		/*$http.get('/template/reports/file/report1.json').success(function(data){
			$scope.dtColumns = data.columns;
			
			
		});
		
		$http.get('/template/reports/file/report1_data.json').success(function(data){
			$scope.reportData = data.data;
			
			
		});*/
		
		$scope.reportMDs = [];
		
		$scope.loadReportsMd = function () {
			$http.get('/webapp/reports/md').success(function(resp){
				console.log(resp);
				$scope.reportMDs = resp;
				$('#saved-report-tab a').click();
			});
		}
		
		$scope.loadReportsMdDummy = function () {
			$http.get('/template/reports/file/md.json').success(function(resp){
				console.log(resp);
				$scope.reportMDs = resp;
			});
		}
		
		$scope.loadReportsMd();
		
		$scope.reportTemplate = {
				jsonForm:{},
				jsonSchema:[],
				model:{}
				
		};
		
		$scope.showExcButton = false;
		$scope.isAnyActiveReport= false;
		$scope.showReport = function (report) {
			$scope.reportTemplate.jsonForm = report.jsonForm;
			$scope.reportTemplate.jsonSchema = report.jsonSchema;
			$scope.reportTemplate.model.id = report.id;
			$scope.showExcButton = true;
			$scope.currenttab = 'search';
			$('#custom-search-tab a').click();
		}
		
		var dateFormat =       $.pivotUtilities.derivers.dateFormat;
		var sortAs =           $.pivotUtilities.sortAs;
		var renderers = $.extend(
                 $.pivotUtilities.renderers, 
                 $.pivotUtilities.d3_renderers
                 );
		for(var k in $.pivotUtilities.c3_renderers)
         renderers["C3 "+k] = $.pivotUtilities.c3_renderers[k];
             
		$scope.runReport = function () {
			$scope.isAnyActiveReport = false;
			$http.get('/webapp/report/run',{params:{filter:$scope.reportTemplate.model}}).success(function(data){
				console.log(data);
				$scope.reportData = data.data;
				$scope.dtColumns = data.columns;
				
				$("#pivot-table-output").pivotUI(data.data, {
					renderers: renderers,
				});
				$scope.isAnyActiveReport = true;
			});
		}
		
		
	  
	});
	
	App.directive('myDatatable', function() {
		  function link(scope, element, attrs) {
			  
			decorateColumns = function (cols) {
				var columns = scope.$eval(cols);
				$.each(columns, function(i,e){
					if(e.link) {
						e.render = function(cellData, type, rowData) {
							return "<a href='#'>" + cellData + "</a>";
						}
					} 
				});
				return columns;
			};
			
		    scope.$watch('finishedHeader', function(val) {
		      if (val) {
		    	if(window.oTable) {  
		    		window.oTable.fnDestroy();
		    		window.oTable = null;
		    	}
		    	
		    	existingHead = $(element).find('thead');
		    	existingHead.remove();
		    	$(element).append($("#tableHeaderTmp").find('thead').clone());  
		        
		    	window.oTable = $(element).dataTable({
		          sDom: '<"clear">TlfCrtip',
		          pageLength: 200,
		          //sScrollY: "500px",
		          tableTools: {
		        	  "sSwfPath": "/template/madmin/app/vendors/DataTables/extensions/TableTools/swf/copy_csv_xls_pdf.swf",
			            
		              "aButtons": [
		                  "copy",
		                  "print",
		                  {
		                      "sExtends":    "collection",
		                      "sButtonText": "Save",
		                      "aButtons":    [ "csv", "xls", "pdf" ]
		                  }
		              ]
		          },
		          
		          columns: decorateColumns(attrs.aaColumns) ,
		          data:scope.$eval(attrs.aaData)
		        });
		      }
		    });
		  }
		  return {
		    link: link
		  }
		})
		  .directive('generateDatatable', function() {
		    function link(scope, element, attrs) {
		      if (scope.$last) {
		          scope.$parent.finishedHeader = Math.random();
		      }
		    }
		    return {
		      link: link
		    }
		  });
	
	
	