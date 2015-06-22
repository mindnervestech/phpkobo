var dp;

dp = angular.module('ng-bs3-datepicker', []);

dp.directive('ngBs3Datepicker', function($compile) {
  return {
    restrict: 'E',
    replace: true,
    template: "bootstart3datepickerInline.html",
    link: function($scope, element, attr) {
      
    	element.datetimepicker({
          pickTime: true,
          icons: {
            time: 'fa fa-clock-o',
            date: 'fa fa-calendar',
            up: 'fa fa-arrow-up',
            down: 'fa fa-arrow-down'
          }
        });
      
      
     
      return $compile(element)($scope);
    }
  };
});
