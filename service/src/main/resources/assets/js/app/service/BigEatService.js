define([
  'app/Endpoint'
],
function (Endpoint) {
  'use strict';
  return {

    getBigEats: function (callbacks) {
      callbacks = callbacks || {};

      return $.ajax({
        type: 'GET',
        url: Endpoint.BIG_EATS,
        dataType: 'json',
        cache: true,
        async: true,
        success: function (data) {
          callbacks.success(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
          callbacks.error(jqXHR, textStatus, errorThrown);
        }
      });
    }
  };
});