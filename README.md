2013 7x7 Big Eat SF - A Better Visualization 
================

Overview
-----------

The visualization for the [2013 7x7 Big Eat - 100 Things to eat before you die list ](http://www.7x7.com/big-eat-2013#/0) leaves something to be desired. It uses a carousel widget, which works great if you have five or so entries to cycle through, however it becomes a bit unwieldy if you have more than that. There are 100 items on the list, which means that you'll be clicking on that that carousel arrow quite a few times to view every item...

I wrote a scraper, service, and UI that provides a better visualization of this year's list. 

Scraper
-----------

The scraper parses the [list web site](http://www.7x7.com/big-eat-2013), reads interesting information (the venue location, website, item name, item number, etc.) for each item, and submits a json representation of eath big eat to a web service that maintains them.

Service & REST API
-----------

Detailed documentation for the service's REST resources and API model can be found here:

http://docs.tescherm.apiary.io/

The cliffsnotes version:

To create a big eat, POST a json request similar to the following to service/v1/bigeats:

```json
{
   "itemNum":36,
   "item":"Pasta tasting menu",
   "images":{
      "small":{
         "type":"url",
         "image":"http://d1e3evqzwzcve9.cloudfront.net/sites/all/files/imagecache/mobile_imagegallery_main/37pastaSPQR.jpg"
      },
      "large":{
         "type":"url",
         "image":"http://d1e3evqzwzcve9.cloudfront.net/sites/all/files/imagecache/blog_imagegallery_main/37pastaSPQR.jpg"
      }
   },
   "venue":{
      "name":"SPQR",
      "website":"http://bit.ly/ryeNP",
      "location":{
         "address":"1911 Fillmore Street"
      },
      "contact":{
         "phoneNumber":"(415) 771-7779"
      }
   }
}
```

The response for the request above will look like:

```json
{
  "id":"be_6631c3ebf1b0",
  "itemNum":36,
  "item":"Pasta tasting menu",
  "venue":{
     "name":"SPQR",
     "website":"http://bit.ly/ryeNP",
     "location":{
         "address":"1911 Fillmore Street"
      },
      "contact":{
         "phoneNumber":"(415) 771-7779"
      }
  },
  "images":{
     "small":{
        "id":"im_6c47b0248351",
        "endpoint":"/service/v1/bigeats/be_6631c3ebf1b0/image/im_6c47b0248351"
     },
     "large":{
        "id":"im_2639c0b1618f",
        "endpoint":"/service/v1/bigeats/be_6631c3ebf1b0/image/im_2639c0b1618f"
     }
  },
  "created":1373259062062,
  "modified":1373259062062
}
```

Big eats can be listed by performing a GET on the service/v1/bigeats endpoint

Big Eats UI
-----------

The Big Eats UI uses [isotope](http://isotope.metafizzy.co/) to present a grid layout of each big eat, and gains inspiration from [Pinterest](http://pinterest.com) and [Foodspotting](http://www.foodspotting.com). Some screenshots:

Grid View:

http://tescherm.com/blog/?attachment_id=312

Detail View:

http://tescherm.com/blog/?attachment_id=311

To Run
-----------

#####Service

The start the service, run the following gradle target:

```
cd service
gradle clean run
```

Alternatively you can build the service, then run startservice.sh from the dist dir:
```
cd service
gradle clean dist
cd dist
./startservice.sh
```

#####Scraper

In a new terminal, run the scraper with the following gradle target:

```
cd scraper
gradle clean run
```

You should be able to hit [http://localhost:8080/service/v1/bigeats](http://localhost:8080/service/v1/bigeats) in your browser to view the big eats that were created.

#####Big Eats UI

The web UI runs at [http://localhost:8080](http://localhost:8080)
