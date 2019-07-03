
'use strict';

/* Directives */

ClbApp
  .directive('loading', function ($parse, $q) {
    return {
      restrict: 'E',
      scope: {},
      templateUrl: 'pages/directives/loading.html',
      link: function (scope, element, attrs) {
        scope.text = (!!!attrs.text) ? 'Loading...' : attrs.text;
      }
    };
  })
  .directive('noResult', function ($parse, $q) {
    return {
      scope: {},
      templateUrl: 'pages/directives/noResult.html',
      link: function (scope, element, attrs) {
        scope.text = (!!!attrs.text) ? 'No Result.' : attrs.text;
      }
    };
  }).directive('showonhoverparent',
  function() {
    return {
      link : function(scope, element, attrs) {
        element.parent().bind('mouseenter', function(e) {
          e.stopPropagation();
          element.show();
        });
        element.parent().bind('mouseleave', function(e) {
          e.stopPropagation();
          element.hide();
        });
      }
    };
  })
  .directive('typeahead', function ($timeout, $filter) {
    return {
      restrict: 'AEC',
      scope: {
        items: '=',
        prompt: '@',
        title: '@',
        model: '=',
        required: '@'
      },
      templateUrl: 'pages/directives/typeahead.html',
      link: function (scope, elem, attrs) {
        scope.current = null;
        scope.selected = true; // hides the list initially

        scope.handleSelection = function () {
          scope.model = scope.current[scope.title];
          scope.current = null;
          scope.selected = true;
        };
        scope.isCurrent = function (item) {
          return scope.current == item;
        };
        scope.setCurrent = function (item) {
          scope.current = item;
        };
        scope.keyListener = function (event) {
          var list, idx;
          switch (event.keyCode) {
            case 13:
              scope.handleSelection();
              break;
            case 38:
              list = $filter('filter')(scope.items, {name: scope.model});
              scope.candidates = $filter('orderBy')(list, 'name');
              idx = scope.candidates.indexOf(scope.current);
              if (idx > 0) {
                scope.setCurrent(scope.candidates[idx - 1]);
              } else if (idx == 0) {
                scope.setCurrent(scope.candidates[scope.candidates.length - 1]);
              }
              break;
            case 40:
              list = $filter('filter')(scope.items, {name: scope.model});
              scope.candidates = $filter('orderBy')(list, 'name');
              idx = scope.candidates.indexOf(scope.current);
              if (idx < scope.candidates.length - 1) {
                scope.setCurrent(scope.candidates[idx + 1]);
              } else if (idx == scope.candidates.length - 1) {
                scope.setCurrent(scope.candidates[0]);
              }
              break;
            default:
              break;
          }
        };

      }
    };
  })
  .directive('autoFillSync', function ($timeout) {
    return {
      require: 'ngModel',
      link: function (scope, elem, attrs, ngModel) {
        var origVal = elem.val();
        $timeout(function () {
          var newVal = elem.val();
          if (ngModel.$pristine && origVal !== newVal) {
            ngModel.$setViewValue(newVal);
          }
        }, 500);
      }
    }
  }).directive('retentionFormat', function() {
    return {
      require: 'ngModel',
      link: function(scope, element, attrs, ngModelController) {
        ngModelController.$parsers.push(function(data) {
          //convert data from view format to model format
          return data*86400000; //converted
        });

        ngModelController.$formatters.push(function(data) {
          //convert data from model format to view format
          return data/86400000; //converted
        });
      }
    }
  }).directive('datepickerTimezone', function () {
    // this directive workaround to convert GMT0 timestamp to GMT date for datepicker
    return {
      restrict: 'A',
      priority: 1,
      require: 'ngModel',
      link: function (scope, element, attrs, ctrl) {
        ctrl.$formatters.push(function (value) {

          //set null for 0
          if(value===0){
            return null;
          }

          //return value;
          var date = new Date(value + (60000 * new Date().getTimezoneOffset()));
          return date;
        });

        ctrl.$parsers.push(function (value) {
          if (isNaN(value)||value==null) {
            return value;
          }
          value = new Date(value.getFullYear(), value.getMonth(), value.getDate(), 0, 0, 0, 0);
          return value.getTime()-(60000 * value.getTimezoneOffset());
        });
      }
    };
  }).directive('dateTimepickerTimezone', function () {
    return {
      restrict: 'A',
      priority: 1,
      require: 'ngModel',
      link: function (scope, element, attrs, ctrl) {
        ctrl.$formatters.push(function (value) {

          //set null for 0
          if(value===0){
            return '';
          }

          //return value;
          var newDate = new Date(value);
          var year = newDate.getFullYear();
          var month = (newDate.getMonth()+1)<10?'0'+(newDate.getMonth()+1):(newDate.getMonth()+1);
          var date = newDate.getDate()<10?'0'+newDate.getDate():newDate.getDate();
          var hour = newDate.getHours()<10?'0'+newDate.getHours():newDate.getHours();
          var mins = newDate.getMinutes()<10?'0'+newDate.getMinutes():newDate.getMinutes();
          var seconds = newDate.getSeconds()<10?'0'+newDate.getSeconds():newDate.getSeconds();
          var viewVal = year+"-"+month+"-"+date+" "+hour+":"+mins+":"+seconds;
          return viewVal;
        });

        ctrl.$parsers.push(function (value) {
          var date;
          if(/^\d{4}-\d{1,2}-\d{1,2}(\s+\d{1,2}:\d{1,2}:\d{1,2})?$/.test(value)) {
            date=new Date(value);
           return date.getTime();
          }else{
            return value;
          }
        });
      }
    };
}).directive("parametertree", function($compile) {
    return {
      restrict: "E",
      transclude: true,
      scope: {
        nextpara: '='
      },
      template:
      '<li class="parent_li">Value:<b>{{nextpara.value}}</b>, Type:<b>{{ nextpara.type }}</b></li>' +
      '<parametertree ng-if="nextpara.next_parameter!=null" nextpara="nextpara.next_parameter"></parameterTree>',
      compile: function(tElement, tAttr, transclude) {
        var contents = tElement.contents().remove();
        var compiledContents;
        return function(scope, iElement, iAttr) {
          if(!compiledContents) {
            compiledContents = $compile(contents, transclude);
          }
          compiledContents(scope, function(clone, scope) {
            iElement.append(clone);
          });
        };
      }
    };
  }).directive("groupbytree", function($compile) {
    return {
      restrict: "E",
      transclude: true,
      scope: {
        nextpara: '=',
      },
      template:
      '<b>{{nextpara.value}}<b ng-if="nextpara.next_parameter!=null">,</b></b>'+
      '<groupbytree ng-if="nextpara.next_parameter!=null" nextpara="nextpara.next_parameter"></groupbytree>',
      compile: function(tElement, tAttr, transclude) {
        var contents = tElement.contents().remove();
        var compiledContents;
        return function(scope, iElement, iAttr) {
          if(!compiledContents) {
            compiledContents = $compile(contents, transclude);
          }
          compiledContents(scope, function(clone, scope) {
            iElement.append(clone);
          });
        };
      }
    };
  }).directive("topntree", function($compile) {
  return {
    restrict: "E",
    transclude: true,
    scope: {
      nextpara: '='
    },
    template:
    '<li class="parent_li">SUM|ORDER BY:<b>{{nextpara.value}}</b></b></li>' +
    '<li class="parent_li">Group By:'+
    '<groupbytree nextpara="nextpara.next_parameter"></groupbytree>'+
    '</li>',
    compile: function(tElement, tAttr, transclude) {
      var contents = tElement.contents().remove();
      var compiledContents;
      return function(scope, iElement, iAttr) {
        if(!compiledContents) {
          compiledContents = $compile(contents, transclude);
        }
        compiledContents(scope, function(clone, scope) {
          iElement.append(clone);
        });
      };
    }
  };
}).directive("extendedcolumntree", function($compile) {
  return {
    restrict: "E",
    transclude: true,
    scope: {
      nextpara: '='
    },
    template:
    '<li class="parent_li">Host Column:<b>{{nextpara.value}}</b></b></li>' +
    '<li class="parent_li">Extended Column:<b>{{nextpara.next_parameter.value}}</b></li>',
    compile: function(tElement, tAttr, transclude) {
      var contents = tElement.contents().remove();
      var compiledContents;
      return function(scope, iElement, iAttr) {
        if(!compiledContents) {
          compiledContents = $compile(contents, transclude);
        }
        compiledContents(scope, function(clone, scope) {
          iElement.append(clone);
        });
      };
    }
  };
}).directive('kylinpopover', function ($compile,$templateCache) {
  return {
    restrict: "A",
    link: function (scope, element, attrs) {
      var popOverContent;
      var dOptions = {
        placement : 'right'
      }
      popOverContent = $templateCache.get(attrs.template);

      var placement = attrs.placement? attrs.placement : dOptions.placement;
      var title = attrs.title;

      var options = {
        content: popOverContent,
        placement: placement,
        trigger: "hover",
        title: title,
        html: true
      };
      $(element).popover(options);
    }
  };
}).directive('extendedColumnReturn', function() {
  return {
    require: 'ngModel',
    link: function(scope, element, attrs, ngModelController) {

      var prefix = "extendedcolumn(";
      var suffix = ")";
      ngModelController.$parsers.push(function(data) {
        //convert data from view format to model format
        return prefix +data+suffix; //converted
      });

      ngModelController.$formatters.push(function(data) {
        //convert data from model format to view format
        var prefixIndex = data.indexOf("(")+1;
        var suffixIndex = data.indexOf(")");
        return data.substring(prefixIndex,suffixIndex); //converted
      });
    }
  }
}).directive('mydate', function(){
    return {
        restrict: 'AE',
        scope: {
            range: '=',
            multiple:"=",
            onSelected: '&'
        },
        link: function($scope, element, attrs){
            $scope.array = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17,
                18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31,32,33,34,35,36,37,38,
                39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60];
            $scope.update = function(val){
                $scope.onSelected({selected: val});
            };
        },
        template: '<select style="width: 120px;" chosen {{multiple}} ng-options="day as day for day in array| limitTo: range" ng-model="select" ng-change="update(select)"></select>'
    };
}).directive('autoHeight', function(){
    return {
        restrict: 'A',
        link: function($scope, element, attrs){
            $scope.h = $scope.h || $("#container").height();

            element.css("min-height", $scope.h + "px");

        }
    };
}).directive('resizeable', function(){
    return {
        restrict: 'A',
        link: function($scope, element, attrs){
            var myDiagramContainerDiv = $("#myDiagramContainerDiv");
            var height = myDiagramContainerDiv.height();
            var width = myDiagramContainerDiv.width();
            var moduleInfoDivWidth = $("#moduleInfoDiv").width();
            myDiagramContainerDiv.resizable({
                containment: "#container",
                minHeight: height,
                maxHeight: height,
                minWidth: width / 2,
                maxWidth: width,
                resize: function (event, ui) {
                    $("#moduleInfoDiv").width(moduleInfoDivWidth + (ui.originalSize.width - ui.size.width));
                },
                stop: function (event, ui) {
                    moduleInfoDivWidth = $("#moduleInfoDiv").width();
                }
            });
        }
    };
}).directive("icheck",function(){
    return {
        restrict: 'A',
        link: function(scope, element, attrs){
            $(element).iCheck({
                checkboxClass: 'icheckbox_minimal-blue',
                radioClass   : 'iradio_minimal-blue'
            });
            $(element).on('ifToggled', function(event){
                scope.$emit('iChecked',{value:$(event.target).val(),isChecked:event.target.checked,data:event.target.dataset});
            });
            /*$(element).on('ifChecked', function(event){
                scope.$emit('iChecked',{value:$(event.target).val(),isChecked:true});
            });
            $(element).on('ifUnchecked', function(event){
                scope.$emit('iChecked',{value:$(event.target).val(),isChecked:false});
            });*/
        }
    };
});
