(function(app) {
  app.AppModule = AppModule;
  function AppModule() {}

  routes = [
    { path: "list", component: app.TodoListComponent },
    { path: "todo", component: app.TodoComponent },
    { path: "todo/:id", component: app.TodoComponent },
    { path: "**", component: app.TodoListComponent }
  ];

  AppModule.annotations = [
    new ng.core.NgModule({
      imports: [
        ng.platformBrowser.BrowserModule,
        ng.forms.FormsModule,
        ng.router.RouterModule.forRoot(routes, { useHash: true })
      ],

      declarations: [
        app.AppComponent,
        app.TodoListComponent,
        app.TodoComponent
      ],
      providers: [app.DataService],
      bootstrap: [app.AppComponent]
    })
  ];
})((window.app = window.app || {}));
