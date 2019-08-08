const mongoose = require("mongoose");
//var timeinms = Date.now();
const all_list_schema = new mongoose.Schema({
  animal_id: {
    type: String,
    required: "Animal ID is Required"
  },
  image_src: {
    type: String,
    required: "Image src is Required"
  },
  geo_location: [{
    type: Number,
    default: null
  }],

  heart_beat:{
    type: Number,
    default: 0
  },
  heart_beat_range:[{
    type: Number,
    required: "Heartbeat range is required"
  }],
 
  body_tempr:{
    type: Number,
    default: 0
  },
  body_tempr_range:[{
    type: Number,
    required: "Body Temperature range is required"
  }],
  surr_tempr:{
    type: Number,
    default: 0
  },
  humidity:{
    type: Number,
    default:0
  },
  timestramp:{
    type: Number,
    default:Date.now
  },
  heart_beat_all: [{
    type: mongoose.Schema.Types.ObjectId,
    ref: "HeartBeat",
    required: "HeartBeat is Required"
  }],
  body_tempr_all: [{
    type: mongoose.Schema.Types.ObjectId,
    ref: "BodyTempr",
    required: "BodyTempr is Required"
  }],
  surr_tempr_all: [{
    type: mongoose.Schema.Types.ObjectId,
    ref: "SurrTempr",
    required: "SurrTempr is Required"
  }],
  geo_location_all: [{
    type: mongoose.Schema.Types.ObjectId,
    ref: "GeoLocation",
    required: "GeoLocation is Required"
  }]

});

const heart_beat_all_schema = new mongoose.Schema({
  heart_beat: {
    type: Number,
    required: "Heart beat is required"
  },
  timestramp: {
    type: Number,
    required: "Timestramp is required"
  },
  animal: {
    type: mongoose.Schema.Types.ObjectId,
    ref: "AllList",
    required: "Animal is Required Field"
  }
});

const body_tempr_all_schema = new mongoose.Schema({
  body_tempr: {
    type: Number,
    required: "Body Tempr is required"
  },
  timestramp: {
    type: Number,
    required: "Timestramp is required"
  },
  animal: {
    type: mongoose.Schema.Types.ObjectId,
    ref: "AllList",
    required: "Animal is Required Field"
  }
});

const surr_tempr_all_schema = new mongoose.Schema({
  surr_tempr: {
    type: Number,
    required: "Surr Temp is required"
  },
  humidity:{
    type: Number,
    required: "Humidity is required"
  },
  timestramp: {
    type: Number,
    required: "Timestramp is required"
  },
  animal: {
    type: mongoose.Schema.Types.ObjectId,
    ref: "AllList",
    required: "Animal is Required Field"
  }
});

const geo_location_all_schema = new mongoose.Schema({
  geo_location: [{
    type: Number,
    required: "Geo location is required"
  }],
  timestramp: {
    type: Number,
    required: "Timestramp is required"
  },
  animal: {
    type: mongoose.Schema.Types.ObjectId,
    ref: "AllList",
    required: "Animal is Required Field"
  }
});

module.exports = mongoose.model("HeartBeat", heart_beat_all_schema);
module.exports = mongoose.model("BodyTempr", body_tempr_all_schema);
module.exports = mongoose.model("SurrTempr", surr_tempr_all_schema);
module.exports = mongoose.model("GeoLocation", geo_location_all_schema);
module.exports = mongoose.model("AllList", all_list_schema);