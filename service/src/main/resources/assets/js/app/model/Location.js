define(function () {
  'use strict';
  return Backbone.Model.extend({

    defaults: {

      address: null
    },

    initialize: function (opts) {

    },

    getAddress: function () {
      return this.get('address');
    }

  });
});