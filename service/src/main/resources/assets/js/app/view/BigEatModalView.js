define([
  'text!../template/big_eat_modal.html',

  'app/model/ImageType'
],
function (template, ImageType) {
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
      var content = this._template();
      this.$el.html(content);

      return this;
    },

    _template:function(bigEat){

      var venue = bigEat.getVenue();

      var hasPhoneNumber = venue.getPhoneNumber() ? true : false;
      var hasWebsite = venue.getWebsite() ? true : false;

      var showDot = hasWebsite && hasPhoneNumber;

      return Handlebars.compile(template)({

        // item
        itemName: bigEat.getItemName(),
        itemNum: bigEat.getItemNum(),

        // venue
        venueName: venue.getName(),
        website: venue.getWebsite(),
        address: venue.getAddress(),

        // control what gets displayed
        showDot: showDot,
        hasWebsite: hasWebsite,

        phoneNumber: venue.getPhoneNumber(),

        // image
        imageUrl: this._imageUrl(bigEat)

      });
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
      var large = bigEat.getImage(ImageType.large);
      // TODO seems cheezy
      return document.location.origin + large.getEndpoint();
    }
  });
});