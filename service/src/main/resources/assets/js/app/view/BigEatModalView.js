define([
  'text!../template/big_eat_modal.html',

  'app/model/ImageSize'
],
function (template, ImageSize) {
  'use strict';
  return Backbone.View.extend({

    className: 'big-eat-modal modal hide fade',

    // Members

    initialize: function (opts) {

      // Bind Scope
      _.bindAll(this);
    },

    render: function (bigEat) {

      this.$el.empty();
      var content = this._template(bigEat);
      this.$el.html(content);

      return this;
    },

    _template:function(bigEat){

      var context = {};

      // item
      _.extend(context, this._itemInfo(bigEat));

      // venue
      _.extend(context, this._venueInfo(bigEat));

      // contact
      _.extend(context, this._contactInfo(bigEat));

      // control what gets displayed
      _.extend(context, this._controlInfo(bigEat));

      // image
      _.extend(context, this._imageInfo(bigEat));

      return Handlebars.compile(template)(context);
    },

    _itemInfo:function(bigEat){
      return {
        itemName: bigEat.getItemName(),
        itemNum: bigEat.getItemNum()
      };
    },

    _venueInfo:function(bigEat){
      var venue = bigEat.getVenue();
      var location = venue.getLocation();

      return {
        venueName: venue.getName(),
        website: venue.getWebsite(),
        address: location.getAddress()
      };
    },

    _contactInfo:function(bigEat){
      var venue = bigEat.getVenue();
      var contact = venue.getContact();

      return {
        phoneNumber: contact.getPhoneNumber()
      };
    },

    _controlInfo:function(bigEat){

      var venue = bigEat.getVenue();
      var contact = venue.getContact();

      var hasPhoneNumber = contact.getPhoneNumber() ? true : false;
      var hasWebsite = venue.getWebsite() ? true : false;

      var showDot = hasWebsite && hasPhoneNumber;

      return {
        showDot: showDot,
        hasWebsite: hasWebsite
      };
    },

    _imageInfo:function(bigEat){
      return {
        imageUrl: this._imageUrl(bigEat)
      };
    },

    events: {
    },

    hide: function () {
      this.$el.modal('hide');
    },

    show: function (bigEat) {
      this.render(bigEat);

      this.$el.modal({
        keyboard: true,
        show: true,
        backdrop: true
      });
    },

    _imageUrl:function(bigEat){
      var large = bigEat.getImage(ImageSize.large);
      // TODO seems cheezy
      return document.location.origin + large.getEndpoint();
    }
  });
});