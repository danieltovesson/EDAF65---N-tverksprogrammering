(function(app) {
  app.TodoListComponent = TodoListComponent;
  TodoListComponent.annotations = [
    new ng.core.Component({
      selector: "todo-list",
      templateUrl: "app/todo.list/todo.list.component.html"
    })
  ];
  TodoListComponent.parameters = [ng.router.Router, app.DataService];

  function TodoListComponent(router, dataService) {
    this.router = router;
    this.dataService = dataService;
    this.myList = dataService.getTODOs();
    this.viewTodo = function(id) {
      router.navigate(["/todo", id]);
    };
  }
})((window.app = window.app || {}));
