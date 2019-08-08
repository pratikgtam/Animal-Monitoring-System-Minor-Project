const express = require("express");
const app = express();
const jwt    = require('jsonwebtoken');
const config = require('../configurations/config')
const router = require("express").Router();
const mongoose = require("mongoose");

const Animal = mongoose.model("AllList");
const HeartBeat = mongoose.model("HeartBeat");
const BodyTempr = mongoose.model("BodyTempr");
const SurrTempr = mongoose.model("SurrTempr");
const GeoLocation = mongoose.model("GeoLocation");

app.set('Secret', config.secret);

// router.use((req, res, next) =>{


//  // check header for the token
//   var token = req.headers['access-token'];

//   // decode token
//   if (token) {

//     // verifies secret and checks if the token is expired
//     jwt.verify(token, app.get('Secret'), (err, decoded) =>{      
//       if (err) {
//         console.log("Invalid Token");
//         //return res.json({ message: 'invalid token' });    
//       } else {
//         // if everything is good, save to request for use in other routes
//         req.decoded = decoded;    
//         next();
//       }
//     });

//    }
//    else {

//     // if there is no token  

//     console.log("Token is required")
//   }
// });


router.get("/", async (req, res) => {
  const animals = await Animal.find({});
  res.send(animals);
});

router.get("/:animalId", async (req, res) => {
  const animal = await Animal.findOne({ _id: req.params.animalId }).populate(
    "heart_beat_all geo_location_all surr_tempr_all body_tempr_all"
  );
  res.send(animal);
});
router.put("/updateanimal/:animalId", async (req, res) => {
  const animal = await Animal.findByIdAndUpdate(
    {
      _id: req.params.animalId
    },
    req.body,
    {
      new: true,
      runValidators: true
    }
  );

  res.send(animal);
});

router.delete("/deleteanimal/:animalId", async (req, res) => {
  const animal = await Animal.findByIdAndRemove({
    _id: req.params.animalId
  });
  res.send(animal);
});

router.post("/", async (req, res) => {
  const animal = new Animal();
  animal.animal_id = req.body.animal_id;
  animal.image_src = req.body.image_src;
  animal.geo_location = req.body.geo_location;
  animal.heart_beat = req.body.heart_beat;
  animal.heart_beat_range = req.body.heart_beat_range;
  animal.body_tempr = req.body.body_tempr;
  animal.body_tempr_range = req.body.body_tempr_range;
  animal.surr_tempr = req.body.surr_tempr;
  animal.humidity = req.body.humidity;
  animal.timestramp = req.body.timestramp;

  await animal.save();
  res.send(animal);
});

// /heartBeats

// Create a HeartBeat
router.post("/postheartbeat/:animalId", async (req, res) => {
  //Find a animal
  const animal = await Animal.findOne({ _id: req.params.animalId });

  //Create a HeartBeat
  const heartBeat = new HeartBeat();
  heartBeat.heart_beat = req.body.heart_beat;
  heartBeat.timestramp = req.body.timestramp;
  heartBeat.animal = animal._id;
  await heartBeat.save();

  // Associate Animal with heartBeat
  animal.heart_beat_all.push(heartBeat._id);
  await animal.save();

  res.send(heartBeat);
});


//Edit a HeartBeat
router.put("/updateheartBeat/:heartBeatId", async (req, res) => {
  const heartBeat = await HeartBeat.findOneAndUpdate(
    {
      _id: req.params.heartBeatId
    },
    req.body,
    { new: true, runValidators: true }
  );

  res.send(heartBeat);

});

router.delete("/deleteheartBeat/:heartBeatId", async (req, res) => {
  const heartBeat = await HeartBeat.findByIdAndRemove({_id: req.params.heartBeatId});
  res.send(heartBeat);
});


router.post("/postbodytempr/:animalId", async (req, res) => {
  const animal = await Animal.findOne({ _id: req.params.animalId });

  const bodyTempr = new BodyTempr();
  bodyTempr.body_tempr = req.body.body_tempr;
  bodyTempr.timestramp = req.body.timestramp;
  bodyTempr.animal = animal._id;
  await bodyTempr.save();

  animal.body_tempr_all.push(bodyTempr._id);
  await animal.save();

  res.send(bodyTempr);
});


router.put("/updatebodytempr/:bodyTemprId", async (req, res) => {
  const bodyTempr = await BodyTempr.findOneAndUpdate(
    {
      _id: req.params.bodyTemprId
    },
    req.body,
    { new: true, runValidators: true }
  );

  res.send(heartBeat);

});

router.delete("/deletebodytempr/:bodyTemprId", async (req, res) => {
  const bodyTempr = await BodyTempr.findByIdAndRemove({_id: req.params.bodyTemprId});
  res.send(bodyTempr);
});



router.post("/postsurrtempr/:animalId", async (req, res) => {
  const animal = await Animal.findOne({ _id: req.params.animalId });

  const surr_tempr = new SurrTempr();
  surr_tempr.surr_tempr = req.body.surr_tempr;
  surr_tempr.humidity = req.body.humidity

  surr_tempr.timestramp = req.body.timestramp;
  surr_tempr.animal = animal._id;
  await surr_tempr.save();

  animal.surr_tempr_all.push(surr_tempr._id);
  await animal.save();

  res.send(surr_tempr);
});


router.put("/updatesurrtempr/:surrTemprId", async (req, res) => {
  const surr_tempr = await SurrTempr.findOneAndUpdate(
    {
      _id: req.params.surrTemprId
    },
    req.body,
    { new: true, runValidators: true }
  );

  res.send(surr_tempr);

});

router.delete("/deletesurrtempr/:surrTemprId", async (req, res) => {
  const surrTempr = await SurrTempr.findByIdAndRemove({_id: req.params.surrTemprId});
  res.send(surrTempr);
});




router.post("/postgeolocation/:animalId", async (req, res) => {
  const animal = await Animal.findOne({ _id: req.params.animalId });

  const geoLocation = new GeoLocation();
  geoLocation.geo_location = req.body.geo_location;
  geoLocation.timestramp = req.body.timestramp
  geoLocation.animal = animal._id;
  await geoLocation.save();

  animal.geo_location_all.push(geoLocation._id);
  await animal.save();

  res.send(geoLocation);
});


router.put("/updategeolocation/:geolocationId", async (req, res) => {
  const geoLocation = await GeoLocation.findOneAndUpdate(
    {
      _id: req.params.geolocationId
    },
    req.body,
    { new: true, runValidators: true }
  );

  res.send(heartBeat);

});

router.delete("/deletegeolocation/:geolocationId", async (req, res) => {
  const geoLocation = await GeoLocation.findByIdAndRemove({_id: req.params.geolocationId});
  res.send(geoLocation);
});





module.exports = router;
