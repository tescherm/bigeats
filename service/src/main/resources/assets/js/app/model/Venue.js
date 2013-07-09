define([
  'app/model/Location',
  'app/model/Contact'
  ], function (Location, Contact) {
  'use strict';
  return Backbone.Model.extend({

    defaults: {

      name: null,
      website: null,

      location:{},
      contact:{}

    },

    initialize: function (opts) {

    },

    getName: function () {
      return this.get('name');
    },


    getWebsite: function () {
      return this.get('website');
    },

    getLocation:function (){
      return new Location(this.get('location'));
    },

    getContact:function (){
      return new Contact(this.get('contact'));
    }

  });
});