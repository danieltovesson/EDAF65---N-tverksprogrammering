(function(app) {
  app.TodoListComponent = TodoListComponent;
  TodoListComponent.annotations = [
    new ng.core.Component({
      selector: "todo-list",
      templateUrl: "app/todo.list/todo.list.component.html"
    })
  ];
  TodoListComponent.parameters = [app.DataService];

  function TodoListComponent(dataService) {
    this.myList = dataService.getTODOs();
  }
})((window.app = window.app || {}));
