define([
  'app/event/Event',

  'app/view/BigEatControlView',
  'app/view/BigEatsView',
  'app/view/BigEatModalView',

  'app/collection/BigEatsCollection'
],
function (Event, BigEatControlView, BigEatsView, BigEatModalView, BigEatsCollection) {
  'use strict';
  return Backbone.View.extend({

    className: 'big-eats-page',

    // Members

    controlView: null,
    bigEatsView: null,
    bigEatModalView: null,

    bigEatsCollection: null,

    initialize: function (opts) {
      // Bind Scope
      _(this).bindAll(

      );

      this.bigEatModalView = new BigEatModalView({});
      this.bigEatsCollection = new BigEatsCollection();
    },

    render: function () {

      this._renderControlView();
      this._renderBigEatsView();

      return this;
    },

    _renderControlView: function () {
      this.controlView = new BigEatControlView({

      });
      this.controlView.on(Event.SORT_BY_ITEM_NAME, this._onSortedByItemName, this);
      this.controlView.on(Event.SORT_BY_ITEM_NUMBER, this._onSortedByItemNumber, this);

      this.$el.append(this.controlView.render().el);
    },

    _renderBigEatsView: function () {

      this.bigEatsView = new BigEatsView({
        collection: this.bigEatsCollection
      });

      this.bigEatsView.on(Event.BIG_EAT_SELECTED, this._onBigEatSelected, this);
      this.bigEatsView.on(Event.LOAD_BIG_EATS_FAILED, this._onBigEatsLoadFailed, this);

      this.$el.append(this.bigEatsView.render().el);
    },

    events: {

    },

    _onSortedByItemName: function () {
      this.bigEatsView.sortBigEats('name');
    },

    _onSortedByItemNumber: function () {
      this.bigEatsView.sortBigEats('number');
    },

    _onBigEatSelected: function (bigEat) {
      this.bigEatModalView.show(bigEat);
    },

    _onBigEatsLoadFailed: function () {
      this.$el.html('<h4 style="color:red">Loading big eats list failed!</h4>');
    },

    loadBigEats: function () {
      this.bigEatsView.load();
    }

  });

});