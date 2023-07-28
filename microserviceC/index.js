const instrumentation = require("./instrumentation");
const express = require("express");
const app = express();
const port = 3000;

app.get("/hello/:name", (req, res) => {
  console.log(req.headers);
  res.send("Microservice C: Hello " + req.params.name + "!");
});

app.listen(port, () => {
  console.log(`Microservice C listening on port ${port}!`);
});
