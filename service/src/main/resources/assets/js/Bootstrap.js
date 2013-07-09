requirejs.config({
  baseUrl: 'js/lib',
  paths: {
    app: '../app'
  },
  shim: {
    'isotope': {
      deps: ['jquery']
    },
    'backbone':{
      deps: ['jquery', 'underscore'],
      exports: 'Backbone'
    },
    'bootstrap': {
      deps: ['jquery']
    }
  }
});

requirejs([
  'jquery',
  'underscore',
  'backbone',
  'handlebars',
  'isotope',
  'bootstrap',
  'text',
  'app/App'
],
function ($, _, Backbone, handlebars, isotope, bootstrap, text, App) {
  'use strict';

  App.start();

});
