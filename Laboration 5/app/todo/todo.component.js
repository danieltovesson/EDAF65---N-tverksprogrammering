(function(app) {
  app.TodoComponent = TodoComponent;
  TodoComponent.annotations = [
    new ng.core.Component({
      selector: "todo",
      templateUrl: "app/todo/todo.component.html"
    })
  ];
  TodoComponent.parameters = [app.DataService];

  function TodoComponent(dataService) {
    this.todo = { text: "", done: false };
    this.newTodo = function() {
      dataService.addTodo({ text: this.todo.text, done: this.todo.done });
    };
  }
})((window.app = window.app || {}));
