define(function () {
  'use strict';
  return Backbone.Model.extend({

    defaults: {

      name: null,
      address: null,
      website: null,
      phoneNumber: null

    },

    initialize: function (opts) {

    },

    getName: function () {
      return this.get('name');
    },

    getAddress: function () {
      return this.get('address');
    },

    getWebsite: function () {
      return this.get('website');
    },

    getPhoneNumber: function () {
      return this.get('phoneNumber');
    }

  });
});