define(function () {
  'use strict';
  return Backbone.Model.extend({

    defaults: {

      phoneNumber: null
    },

    initialize: function (opts) {

    },

    getPhoneNumber: function () {
      return this.get('phoneNumber');
    }

  });
});