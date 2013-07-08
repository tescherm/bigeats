define([
  'text!../template/big_eat.html',

  'app/event/Event',
  'app/model/ImageType'
],
function (template, Event, ImageType) {
  'use strict';
  return Backbone.View.extend({

    className: 'big-eat thumbnail',

    // Members
    modal: null,

    initialize: function (opts) {

      // Bind Scope
      _.bindAll(this);
    },

    render: function () {

      this.$el.empty();
      var content = this._template();
      this.$el.html(content);

      this.$el.attr('data-id', this.model.getId());
      this.$el.attr('data-name', this.model.getItemName());
      this.$el.attr('data-number', this.model.getItemNum());

      this.$el.find('img').on('load', this._onImageLoaded);

      return this;
    },

    _template:function(){

      var venue = this.model.getVenue();
      return Handlebars.compile(template)({

        // item
        itemName: this.model.getItemName(),
        itemNum: this.model.getItemNum(),

        // venue
        venueName: venue.getName(),
        website: venue.getWebsite(),

        // image
        imageUrl: this._imageUrl()
      });
    },

    events: {
      'click .image': '_onBigEatImageClick'
    },

    _onBigEatImageClick: function (e) {
      this.trigger(Event.BIG_EAT_SELECTED, this.model);
    },

    _onImageLoaded: function (e) {
      var img = $(e.currentTarget);
      var width = img.width();

      this.$el.css({
        width: width + "px"
      });
    },

    _imageUrl:function(){
      var small = this.model.getImage(ImageType.small);
      return document.location.origin + small.getEndpoint();
    }
  });
});