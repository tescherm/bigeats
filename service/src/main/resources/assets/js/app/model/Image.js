define(function () {
  'use strict';
  return Backbone.Model.extend({

    defaults: {

      id: null,
      endpoint: null
    },

    initialize: function (opts) {

    },

    getId: function () {
      return this.get('id');
    },

    getEndpoint: function () {
      return this.get('endpoint');
    }

  });
});