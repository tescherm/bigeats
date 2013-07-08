define([
  'app/model/Image',
  'app/model/Venue'
  ], function (Image, Venue) {
  'use strict';
  return Backbone.Model.extend({

    defaults: {

      id: null,

      itemNum: 0,
      item: null,

      images: {
        small: null,
        large: null
      },
      venue: {
        name: null,
        address: null,
        website: null,
        phoneNumber: null
      }

    },

    initialize: function (opts) {

    },

    getId: function () {
      return this.get('id');
    },

    getItemName: function () {
      return this.get('item');
    },

    getItemNum: function () {
      return this.get('itemNum');
    },

    getImage: function (type) {
      var images = this.get('images');
      var img  = images[type];

      return new Image(img);
    },

    getVenue: function () {
      return new Venue(this.get('venue'));
    }

  });
});