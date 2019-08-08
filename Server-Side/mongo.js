const mongoose = require("mongoose");
require("dotenv").config();
const mongoDBErrors = require("mongoose-mongodb-errors");

mongoose.Promise = global.Promise;
mongoose.plugin(mongoDBErrors);
mongoose.connect('mongodb://ams123:ams123@ds149947.mlab.com:49947/animalmonitoringsystem',{ useNewUrlParser: true });
