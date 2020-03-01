var initTracerFromEnv = require('jaeger-client').initTracerFromEnv

// See schema https://github.com/jaegertracing/jaeger-client-node/blob/master/src/configuration.js#L37
var config = {
  serviceName: 'microserviceC',
}
var options = {
  logger: {
    info: function logInfo(msg) {
      console.log("INFO ", msg)
    },
    error: function logInfo(msg) {
      // Suppress errors
    }
  }
}
var tracer = initTracerFromEnv(config, options);

module.exports = tracer