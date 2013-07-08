define([
  'app/model/BigEat'
], function (BigEat) {
  'use strict';
  return Backbone.Collection.extend({
    model: BigEat,

    initialize: function (opts) {
    },

    // sort by item name
    comparator: function (model) {
      var name = model.getItemName() || '';
      return name.toLowerCase();
    }

  });
});