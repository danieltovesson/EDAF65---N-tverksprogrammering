(function(app) {

app.AppComponent = AppComponent;
function AppComponent() { }

AppComponent.annotations = [
  new ng.core.Component({
    selector: 'my-app',
    templateUrl: 'app/app.component.html',
  })
];

})(window.app = window.app || {});
