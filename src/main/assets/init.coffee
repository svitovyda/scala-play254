'use strict'

requirejs ["angular", "angular-resource"], ->
  require ["angular"]
  angular = window.angular
  app = angular.module 'Application', ["ngResource"]

  app.controller 'main', ($scope, $resource)->
    $scope.data =
      manifests: []
      environment: "--"

    $scope.some =
      strategies: []
      someBool: false

    $scope.counter = 0

    data = $resource "/serverInfo"
    data.get().$promise.then (resp)->
      console.log("config: " + resp)
      $scope.data.environment = resp.environment
      $scope.data.manifests = JSON.stringify resp.manifests, null, 2

    some = $resource "/some"
    some.get().$promise.then (resp)->
      console.log("some: " + resp)
      $scope.some.someBool = resp.someBoolean
      $scope.some.strategies = resp.strategies

    counter = $resource "/counter"
    counter.get().$promise.then (resp)->
      console.log("counter: " + resp)
      $scope.counter = resp.views

