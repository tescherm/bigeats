define(['app/view/BigEatsPageView'], function (BigEatsPageView) {
  'use strict';

  return {

    start: function () {

      var container = $('.container');
      var pageView = new BigEatsPageView({});

      container.html(pageView.render().el);

      pageView.loadBigEats();
    }

  };

});