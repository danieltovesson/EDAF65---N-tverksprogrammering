(function(app) {

  app.DataService = DataService;
  function DataService() {
    this.myList = [{text: "make a list", done: true},
                   {text: "print the list", done: true},
                   {text: "create a service", done: true},
                   {text: "add more functionality...", done: false}];
  }

  DataService.prototype.getTODOs = function() {
    return this.myList;
  };

  DataService.prototype.addTodo = function(todo) {
    this.myList.push(todo);
  };

})(window.app = window.app || {});
