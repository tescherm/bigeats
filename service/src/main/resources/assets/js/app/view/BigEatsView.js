define([
  'app/event/Event',

  'app/model/BigEat',

  'app/view/BigEatView',
  'app/service/BigEatService'
],
function (Event, BigEat, BigEatView, BigEatsService) {
  'use strict';

  var _itemSelector = '.big-eat';

  return Backbone.View.extend({

    className: 'big-eats',

    // Members
    ajaxRequest: null,

    isotopeOpts: {
      itemSelector: _itemSelector,
      layout: 'masonry',
      getSortData: {
        name: function ($el) {
          return $el.attr('data-name');
        },
        number: function ($el) {
          return parseInt($el.attr('data-number'));
        }
      }
    },

    initialize: function (opts) {

      // Bind Scope
      _.bindAll(this
      );

      this.$el.isotope(this.isotopeOpts);

      // Event Binding
      this.collection.on('add', this._onBigEatAdded, this);
    },

    render: function () {
      return this;
    },

    _renderBigEats: function () {
      var views = [];
      this.collection.each(_.bind(function (bigEat) {
        var bigEatView = this._buildBigEatView(bigEat);

        bigEatView.render();
        views.push(bigEatView);
      }, this));

      var bigEatEls = _.pluck(views, 'el');

      this.$el.isotope('insert', $(bigEatEls));
      this.$el.imagesLoaded(this._onImagesLoaded);
    },

    _buildBigEatView: function (bigEat) {
      var bigEatView = new BigEatView({
        model: bigEat
      });
      bigEatView.on(Event.BIG_EAT_SELECTED, this._onBigEatSelected, this);

      return bigEatView;
    },

    events: {
    },

    _onBigEatAdded: function (model, collection, options) {
    },

    _onBigEatSelected: function (bigEat) {
      this.trigger(Event.BIG_EAT_SELECTED, bigEat);
    },

    _onImagesLoaded: function () {
      this.$el.isotope('reLayout');
    },

    load: function () {
      // big eats not loaded yet? load
      if (this.collection.length === 0) {
        this._doLoad();
        // big eats already loaded, just render
      } else {
        this._renderBigEats();
      }
    },

    _doLoad: function () {
      this.ajaxRequest = BigEatsService.getBigEats({
        success: _.bind(function (bigEats) {
          this.ajaxRequest = null;

          this._doAddBigEats(bigEats);
          this._renderBigEats();
        }, this),
        error: _.bind(function () {
          this.ajaxRequest = null;

          this.trigger(Event.LOAD_BIG_EATS_FAILED);
        }, this)
      });
    },

    _doAddBigEats: function (bigEats) {
      _.each(bigEats, _.bind(function (bigEat) {
        var bigEatModel = new BigEat(bigEat);
        this.collection.add(bigEatModel);
      }, this));
    },

    sortBigEats: function (byField) {
      this.$el.isotope({ sortBy: byField });
    }
  });

});