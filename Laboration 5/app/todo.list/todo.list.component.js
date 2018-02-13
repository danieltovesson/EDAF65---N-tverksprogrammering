(function(app) {
  app.TodoListComponent = TodoListComponent;
  TodoListComponent.annotations = [
    new ng.core.Component({
      selector: "todo-list",
      templateUrl: "app/todo.list/todo.list.component.html"
    })
  ];

  function TodoListComponent() {
    this.myList = [
      { text: "make a list", done: false },
      { text: "print the list", done: false },
      { text: "add more functionality...", done: false }
    ];
  }
})((window.app = window.app || {}));
