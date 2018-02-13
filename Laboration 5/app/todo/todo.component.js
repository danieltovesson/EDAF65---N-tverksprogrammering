(function(app) {
  app.TodoComponent = TodoComponent;
  TodoComponent.annotations = [
    new ng.core.Component({
      selector: "todo",
      templateUrl: "app/todo/todo.component.html"
    })
  ];
  TodoComponent.parameters = [ng.router.ActivatedRoute, app.DataService];

  function TodoComponent(route, dataService) {
    this.todo = { text: "", done: false };
    this.dataService = dataService;
    route.params.subscribe(params => {
      tmp = dataService.getTODOs()[params["id"]];
      if (tmp) {
        tmp.id = params["id"];
        this.todo = tmp;
      }
    });
    this.newTodo = function() {
      dataService.addTodo({ text: this.todo.text, done: this.todo.done });
    };
    this.updateTodo = function() {
      dataService.updateTodo(this.todo.id, {
        text: this.todo.text,
        done: this.todo.done
      });
    };
  }
})((window.app = window.app || {}));
