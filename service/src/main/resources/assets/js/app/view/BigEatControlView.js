define([
  'text!../template/big_eat_controls.html',

  'app/event/Event'
],
function (template, Event) {
  'use strict';
  return Backbone.View.extend({

    className: 'big-eat-controls',

    initialize: function (opts) {
    },

    render: function () {
      var content = Handlebars.compile(template)({
      });
      this.$el.html(content);

      return this;
    },

    events: {
      'click .number-sort': '_onNumberSort',
      'click .name-sort': '_onNameSort'
    },

    _onNumberSort: function (e) {
      var btn = $(e.currentTarget);
      if (!btn.hasClass('active')) {
        this._toggle(btn);
        this.trigger(Event.SORT_BY_ITEM_NUMBER);
      }
    },

    _onNameSort: function (e) {
      var btn = $(e.currentTarget);
      if (!btn.hasClass('active')) {
        this._toggle(btn);
        this.trigger(Event.SORT_BY_ITEM_NAME);
      }
    },

    _toggle: function (btn) {
      this.$el.find('.btn').removeClass('active');
      btn.addClass('active');
    }
  });
});